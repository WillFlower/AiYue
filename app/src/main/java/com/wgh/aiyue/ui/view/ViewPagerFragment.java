package com.wgh.aiyue.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.PagerListAdapter;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.util.AttrsUtil;
import com.wgh.aiyue.util.IntentUtil;
import com.wgh.aiyue.util.ConstDefine;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by   : WGH.
 */
public class ViewPagerFragment extends Fragment implements PagerListAdapter.OnItemClickListener, PagerListAdapter.OnLoadPageFinishListener {
    private static final String EXTRA_CATEGORY = "extra_pager_category";

    Unbinder mUnbinder;
    @BindView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;
    @BindView(R.id.detail_frame_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static Context mContext;
    private Category mCategory;
    private PagerListAdapter mPagerListAdapter;

    public static Fragment newInstance(Context context, Category category) {
        mContext = context;
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CATEGORY, category);
        return Fragment.instantiate(context, ViewPagerFragment.class.getName(), bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = (Category) getArguments().getSerializable(EXTRA_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, viewGroup, false);
        mUnbinder = ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setColorSchemeColors(AttrsUtil.getColor(mContext, R.attr.colorPrimaryDark),
                AttrsUtil.getColor(mContext, R.attr.colorPrimary), AttrsUtil.getColor(mContext, R.attr.colorPrimaryLight));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1666);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                                mPagerListAdapter.updateData();
                            }
                        });
                    }
                }).start();
            }
        });

        mPagerListAdapter = new PagerListAdapter(mContext, mCategory, this);
        mPagerListAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mPagerListAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, ConstDefine.getViewPagerColumnNum()));
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                mPagerListAdapter.loadNextPage();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (!TextUtils.isEmpty(mPagerListAdapter.getClickUrl(position))) {
            IntentUtil.openDetail(mContext, mPagerListAdapter.getClickUrl(position));
        }
    }

    @Override
    public void onLoadPageFinish() {
        mRecyclerView.loadFinish();
    }

    public void setCategoryData(Category category) {
        mCategory = category;
        mPagerListAdapter.setCategoryData(category);
        mPagerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
