package com.wgh.aiyue.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.helper.DataCacheHelper;
import com.wgh.aiyue.helper.DatabaseHelper;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.ui.view.AsyncImageView;
import com.wgh.aiyue.util.ConstDefine;
import com.wgh.aiyue.util.UiUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by   : WGH.
 */
public class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.ViewHolder> {

    protected int page = 1;
    protected int pageSize = 0;
    protected int imgHeight;
    protected Context mContext;
    protected Category mCategory;
    protected ArrayList<String> mContentUrls = new ArrayList<>();
    protected ArrayList<String> mContentTitles = new ArrayList<>();
    private ArrayList<String> mFavoriteUrls = new ArrayList<>();
    private ArrayList<Integer> mSortList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener = null;
    private OnLoadPageFinishListener mLoadPageFinishListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnLoadPageFinishListener {
        void onLoadPageFinish();
    }

    public BaseListAdapter(Context context, OnLoadPageFinishListener loadPageFinishListener) {
        this.mContext = context;
        this.mLoadPageFinishListener = loadPageFinishListener;
        imgHeight = (int) (UiUtil.getInstance(context).getWindowWidth()
                * ConstDefine.scalingValueScanActivity / ConstDefine.getViewPagerColumnNum()) - 20;
        initContentData();
        initFavoriteDate();
    }

    public BaseListAdapter(Context context, Category category, OnLoadPageFinishListener loadPageFinishListener) {
        this.mContext = context;
        this.mCategory = category;
        this.mLoadPageFinishListener = loadPageFinishListener;
        imgHeight = (int) (UiUtil.getInstance(context).getWindowWidth()
                * ConstDefine.scalingValueScanActivity / ConstDefine.getViewPagerColumnNum()) - 20;
        initContentData();
        initFavoriteDate();
    }

    protected void initContentData() {
        if (mCategory == null) {
            return;
        }
        mContentUrls = mCategory.getContentUrls();
        mContentTitles = mCategory.getContentTitles();
    }

    private void initFavoriteDate() {
        mFavoriteUrls = DatabaseHelper.getInstance().getFavoriteUrlFromDB(mCategory);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_view_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, (int) view.getTag());
            }
        });
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer position = (int) view.getTag();
                if (!mFavoriteUrls.contains(mContentUrls.get(position))) {
                    mFavoriteUrls.add(mContentUrls.get(position));
                    viewHolder.imageStar.setImageResource(R.drawable.ic_star_checked);
                } else {
                    mFavoriteUrls.remove(mContentUrls.get(position));
                    viewHolder.imageStar.setImageResource(R.drawable.ic_star_unchecked);
                }
                DatabaseHelper.getInstance().insertOrUpdateFavorite(mCategory, mFavoriteUrls);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String contentUrl = mContentUrls.get(position);
        holder.itemView.setTag(position);
        holder.imageStar.setTag(position);
        holder.textTitle.setText(mContentTitles.get(position));
        holder.imageCover.setImageResource(R.mipmap.ic_launcher);

        if (mFavoriteUrls.contains(mContentUrls.get(position))) {
            holder.imageStar.setImageResource(R.drawable.ic_star_checked);
        } else {
            holder.imageStar.setImageResource(R.drawable.ic_star_unchecked);
        }

        holder.imageCover.loadImage(contentUrl, position);

        DataCacheHelper.getInstance().setItemHight(holder.imageCover, holder.textTitle, imgHeight);
        DataCacheHelper.getInstance().setCardParams(holder.cardView, ConstDefine.getViewPagerColumnNum());
    }

    @Override
    public int getItemCount() {
        return pageSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected final CardView cardView;
        protected final TextView textTitle;
        protected final AsyncImageView imageCover;
        protected final ImageView imageStar;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.content_card);
            textTitle = (TextView) itemView.findViewById(R.id.content_text_title);
            imageCover = (AsyncImageView) itemView.findViewById(R.id.content_image_cover);
            imageStar = (ImageView) itemView.findViewById(R.id.content_image_star);
        }
    }

    public void updateData() {
        updateContentData();
        notifyDataSetChanged();
    }

    public void setCategoryData(Category category) {
        this.mCategory = category;
    }

    private void updateContentData() {
        ArrayList<String> tempUrls = new ArrayList<>();
        ArrayList<String> tempTitles = new ArrayList<>();
        Collections.shuffle(mSortList);
        for (int i = 0; i < mSortList.size(); i++) {
            tempUrls.add(mContentUrls.get(mSortList.get(i)));
            tempTitles.add(mContentTitles.get(mSortList.get(i)));
        }
        mContentUrls.clear();
        mContentUrls.addAll(tempUrls);
        mContentTitles.clear();
        mContentTitles.addAll(tempTitles);
    }

    public void loadNextPage() {
        page++;
        // get next page from net...
        notifyDataSetChanged();
        mLoadPageFinishListener.onLoadPageFinish();
    }

    protected void removeItem(int position) {
        if (position < 0 || mContentUrls.size() <= 0 || mContentTitles.size() <= 0) {
            return;
        }
        mContentUrls.remove(position);
        mContentTitles.remove(position);
    }

    public String getClickUrl(int position) {
        return mContentUrls.get(position);
    }
}
