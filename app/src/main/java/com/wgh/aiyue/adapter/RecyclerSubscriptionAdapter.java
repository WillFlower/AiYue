package com.wgh.aiyue.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.helper.MyItemTouchCallback;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.util.AttrsUtil;
import com.wgh.aiyue.util.ConstDefine;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by   : WGH.
 */
public class RecyclerSubscriptionAdapter extends RecyclerView.Adapter<RecyclerSubscriptionAdapter.ViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {

    private Context mContext;
    private Category mCategory;
    private ArrayList<TextView> mTextViews = new ArrayList<>();
    private ArrayList<CardView> mCardViews = new ArrayList<>();
    private ArrayList<String> mNextNames = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = null;

    public RecyclerSubscriptionAdapter(Context context, Category category) {
        mContext = context;
        mCategory = category;
        mNextNames = mCategory.getNextNames();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String nextName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_subscription_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nextName = (String) view.getTag();
                int position = mNextNames.indexOf(nextName);
                updateCardBackgroundColor(position);
                ConstDefine.setLeftDrawerCategoryIndex(position);
                mOnItemClickListener.onItemClick(view, nextName);
            }
        });
        return new ViewHolder(view);
    }

    private void updateCardBackgroundColor(int position) {
        for (int i = 0; i < mCardViews.size(); i++) {
            if (i == position) {
                mCardViews.get(i).setCardBackgroundColor(AttrsUtil.getColor(mContext, R.attr.colorPrimaryLight));
            } else {
                mCardViews.get(i).setCardBackgroundColor(mContext.getResources().getColor(R.color.card_write));
            }
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mNextNames.get(position));
        holder.itemView.setTag(mNextNames.get(position));

        mTextViews.add(holder.textView);
        mCardViews.add(holder.cardView);

        if (ConstDefine.getLeftDrawerCategoryIndex() == position) {
            holder.cardView.setCardBackgroundColor(AttrsUtil.getColor(mContext, R.attr.colorPrimaryLight));
        }
    }

    @Override
    public int getItemCount() {
        return mNextNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_category_item);
            cardView = (CardView) itemView.findViewById(R.id.navigation_left_card);
        }
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition == mCardViews.size() || toPosition == mCardViews.size()) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mCardViews, i, i + 1);
                Collections.swap(mNextNames, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mCardViews, i, i - 1);
                Collections.swap(mNextNames, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        notifyItemRemoved(position);
    }
}
