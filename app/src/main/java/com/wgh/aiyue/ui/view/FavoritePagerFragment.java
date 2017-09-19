package com.wgh.aiyue.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.FavoriteListAdapter;
import com.wgh.aiyue.adapter.PagerListAdapter;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.util.IntentUtil;
import com.wgh.aiyue.util.ConstDefine;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by   : WGH.
 */
public class FavoritePagerFragment extends Fragment implements FavoriteListAdapter.OnItemClickListener, PagerListAdapter.OnLoadPageFinishListener {
    private static final String FAVORITE_CATEGORY = "favorite_pager_category";

    Unbinder unbinder;
    @BindView(R.id.recycler_favorite)
    AutoLoadRecyclerView recyclerFavorite;

    private FavoriteListAdapter mFavoritePagerAdapter;

    private static Context mContext;
    private Category mCategory;

    public static Fragment newInstance(Context context, Category category) {
        mContext = context;
        Bundle bundle = new Bundle();
        bundle.putSerializable(FAVORITE_CATEGORY, category);
        return Fragment.instantiate(context, FavoritePagerFragment.class.getName(), bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = (Category) getArguments().getSerializable(FAVORITE_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_pager, viewGroup, false);
        unbinder = ButterKnife.bind(this, view);
        mFavoritePagerAdapter = new FavoriteListAdapter(mContext, mCategory, this);
        mFavoritePagerAdapter.setOnItemClickListener(this);
        recyclerFavorite.setAdapter(mFavoritePagerAdapter);
        recyclerFavorite.setLayoutManager(new GridLayoutManager(mContext, ConstDefine.getViewPagerColumnNum()));
        recyclerFavorite.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                mFavoritePagerAdapter.loadNextPage();
            }
        });
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (!TextUtils.isEmpty(mFavoritePagerAdapter.getClickUrl(position))) {
            IntentUtil.openDetail(mContext, mFavoritePagerAdapter.getClickUrl(position));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadPageFinish() {
        recyclerFavorite.loadFinish();
    }
}
