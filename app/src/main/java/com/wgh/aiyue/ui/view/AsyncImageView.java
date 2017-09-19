package com.wgh.aiyue.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wgh.aiyue.R;
import com.wgh.aiyue.helper.DatabaseHelper;
import com.wgh.aiyue.model.ImgCat;
import com.wgh.aiyue.network.RequestCategory;
import com.wgh.aiyue.network.RequestManager;
import com.wgh.aiyue.util.DLog;
import com.wgh.aiyue.util.NetWorkUtil;

import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
@SuppressLint("AppCompatCustomView")
public class AsyncImageView extends ImageView {

    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImage(final String contentUrl, final int position) {
        if (NetWorkUtil.isNetWork()) {
            ImgCat imgCat = DatabaseHelper.getInstance().getImgCatFromDB(contentUrl);
            if (imgCat != null) {
                loadWithGlide(imgCat);
            } else {
                RequestManager.getInstance().addRequest(new RequestCategory(contentUrl,
                        new Response.Listener<ArrayList<String>>() {
                            @Override
                            public void onResponse(ArrayList<String> response) {
                                DLog.i("onResponse : " + response);
                                if (response != null) {
                                    if (response.size() > 1) {
                                        String imgUrl = response.get(0);
                                        String description = response.get(1);
                                        ImgCat ic = new ImgCat(contentUrl, imgUrl);
                                        loadWithGlide(ic);
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DLog.e("onErrorResponse : " + error.toString());
                    }
                }), contentUrl);
            }
        } else {
            setImageResource(R.mipmap.ic_launcher);
        }
    }

    private void loadWithGlide(final ImgCat imgCat) {
        RequestManager.getInstance().getGlideManager().load(imgCat.getImgUrl()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String imgUrl, Target<GlideDrawable> target, boolean isFirstResource) {
                DatabaseHelper.getInstance().deleteImgCatFromDB(imgCat.getContentUrl());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String imgUrl, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                DatabaseHelper.getInstance().insetOrUpdateImgCat(imgCat);
                return false;
            }
        })
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .centerCrop()
                .crossFade(666)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(this);
    }
}
