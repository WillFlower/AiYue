package com.wgh.aiyue.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.wgh.aiyue.network.RequestManager;

/**
 * Created by   : WGH.
 */
public class AutoLoadRecyclerView extends RecyclerView{

    private LoadMoreListener loadMoreListener;
    private boolean isLoadingMore;
    private int ySpeed;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isLoadingMore = false;
        addOnScrollListener(new AutoLoadScrollListener());
    }

    public void loadFinish() {
        isLoadingMore = false;
    }

    public interface LoadMoreListener {
        void loadMore();
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * Slide auto load monitor.
     */
    private class AutoLoadScrollListener extends OnScrollListener {
        public AutoLoadScrollListener() {
            super();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            ySpeed = dy;
            // GridLayoutManager is the LinearLayoutManager subclass.
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    isLoadingMore = true;
                    loadMoreListener.loadMore();
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case SCROLL_STATE_IDLE:
                    RequestManager.getInstance().setGlideResume();
                    break;
                case SCROLL_STATE_DRAGGING:
                    if (ySpeed > 20) {
                        RequestManager.getInstance().setRequsetEnable(false);
                        RequestManager.getInstance().setGlidePause();
                    } else {
                        RequestManager.getInstance().setRequsetEnable(true);
                        RequestManager.getInstance().setGlideResume();
                    }
                    break;
                case SCROLL_STATE_SETTLING:
                    if (ySpeed > 20) {
                        RequestManager.getInstance().setRequsetEnable(false);
                        RequestManager.getInstance().setGlidePause();
                    } else {
                        RequestManager.getInstance().setRequsetEnable(true);
                        RequestManager.getInstance().setGlideResume();
                    }
                    break;
            }
        }
    }
}
