package com.wgh.aiyue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgh.aiyue.R;
import com.wgh.aiyue.helper.DataCacheHelper;
import com.wgh.aiyue.helper.DatabaseHelper;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.util.ConstDefine;

/**
 * Created by   : WGH.
 */
public class FavoriteListAdapter extends BaseListAdapter {

    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public FavoriteListAdapter(Context context, Category category, OnLoadPageFinishListener mLoadPageFinishListener) {
        super(context, category, mLoadPageFinishListener);
    }

    @Override
    protected void initContentData() {
        mContentUrls = DatabaseHelper.getInstance().getFavoriteUrlFromDB(mCategory);
        mContentTitles = DataCacheHelper.getInstance().getFavoriteTitles(mCategory, mContentUrls);
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
                viewHolder.imageStar.setImageResource(R.drawable.ic_star_unchecked);
                removeItem(position);
                DatabaseHelper.getInstance().insertOrUpdateFavorite(mCategory, mContentUrls);
                notifyDataSetChanged();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.imageStar.setTag(position);
        if (mContentTitles != null) {
            holder.textTitle.setText(mContentTitles.get(position));
        }
        holder.imageCover.setImageResource(R.drawable.willflow);
        holder.imageStar.setImageResource(R.drawable.ic_star_checked);

        DataCacheHelper.getInstance().setItemHight(holder.imageCover, holder.textTitle, imgHeight);
        DataCacheHelper.getInstance().setCardParams(holder.cardView, ConstDefine.getViewPagerColumnNum());
    }
}
