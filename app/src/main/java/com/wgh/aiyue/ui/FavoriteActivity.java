package com.wgh.aiyue.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.FavoritePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseActivity  implements ActionBar.TabListener {


    @BindView(R.id.tab_layout_favorite)
    TabLayout tabLayoutFavorite;
    @BindView(R.id.viewpager_favorite)
    ViewPager pagerFavorite;

    private FavoritePagerAdapter mFavoritePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorate);
        ButterKnife.bind(this);

        mFavoritePagerAdapter = new FavoritePagerAdapter(getSupportFragmentManager(), this);
        pagerFavorite.setAdapter(mFavoritePagerAdapter);
        tabLayoutFavorite.setupWithViewPager(pagerFavorite);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        pagerFavorite.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
