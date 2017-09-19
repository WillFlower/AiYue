package com.wgh.aiyue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.ViewPagerAdapter;
import com.wgh.aiyue.cache.DataCache;
import com.wgh.aiyue.helper.DataCacheHelper;
import com.wgh.aiyue.message.PagerCategoryMessage;
import com.wgh.aiyue.message.ParseMessage;
import com.wgh.aiyue.message.RightCategoryMessage;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.ui.view.NavigationLeftFragment;
import com.wgh.aiyue.ui.view.NavigationRightFragment;
import com.wgh.aiyue.ui.view.ViewPagerFragment;
import com.wgh.aiyue.util.DateUtil;
import com.wgh.aiyue.util.IntentUtil;
import com.wgh.aiyue.util.MessageUtil;
import com.wgh.aiyue.util.ConstDefine;
import com.wgh.aiyue.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ActionBar.TabListener {

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout_main)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager_main)
    ViewPager mViewpager;
    @BindView(R.id.coordinator_layout_main)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.navigation_left)
    NavigationView mNavigationViewLeft;
    @BindView(R.id.navigation_right)
    NavigationView mNavigationViewRight;
    @BindView(R.id.drawer_layout_main)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.search_main)
    ImageView searchMain;
    @BindView(R.id.more_main)
    ImageView moreMain;

    public static Activity activityMain;
    private ViewPagerAdapter mViewPagerAdapter;
    private NavigationLeftFragment navigationLeftFragment;
    private NavigationRightFragment navigationRightFragment;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        if (ConstDefine.isFirstTime()) {
            // Synchronize information from local...
        } else {
            initView();
            DataCache.getInstance().syncCategoryFromNet("");
        }

        if (DateUtil.isAppUpdate()) {
            // Check for update...
        }

        initToolBar();
        mExitTime = System.currentTimeMillis();
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        // init Left Drawer
        Category leftCategory = DataCacheHelper.getInstance().getLeftCategory();
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_left,
                navigationLeftFragment = (NavigationLeftFragment) new NavigationLeftFragment().
                        newInstance(this, leftCategory, getSupportFragmentManager())).commit();
        // init Right Drawer
        Category rightCategory = DataCacheHelper.getInstance().getRightCategory();
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_right,
                navigationRightFragment = (NavigationRightFragment) new NavigationRightFragment().
                        newInstance(this, rightCategory)).commit();
        // init ViewPager
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        mViewpager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                return true;
            case R.id.action_show_single:
                ConstDefine.setViewPagerColumnNum(1);
                showRestartSnack();
                return true;
            case R.id.action_show_double:
                ConstDefine.setViewPagerColumnNum(2);
                showRestartSnack();
                return true;
            case R.id.action_show_three:
                ConstDefine.setViewPagerColumnNum(3);
                showRestartSnack();
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRestartSnack() {
        Snackbar.make(mViewpager, R.string.snake_restart_hint, Snackbar.LENGTH_LONG)
                .setAction(R.string.snake_restart, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtil.openMain(MainActivity.this);
                        finish();
                    }
                }).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ParseMessage parseMessage) {
        switch (parseMessage.what) {
            case ConstDefine.ParseSuccess:
                initView();
                break;
            case ConstDefine.ParseFail:
                ToastUtil.ToastShort(getString(R.string.error_parse_data));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PagerCategoryMessage message) {
        switch (message.what) {
            case ConstDefine.PagerCategoryChange:
                String pagerKey = message.key;
                if (pagerKey != null) {
                    updateView(pagerKey);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RightCategoryMessage message) {
        switch (message.what) {
            case ConstDefine.DrawerCategoryChange:
                String drawerKey = message.drawerKey;
                Category rightCategory = DataCacheHelper.getInstance().getCategory(drawerKey);
                getSupportFragmentManager().beginTransaction().replace(R.id.navigation_right,
                        navigationRightFragment = (NavigationRightFragment) new NavigationRightFragment().
                                newInstance(this, rightCategory)).commit();
                String pagerKey = DataCacheHelper.getInstance().getDefaultPagerCategoryKey(rightCategory);
                ConstDefine.setViewPagerKey(pagerKey);
                MessageUtil.postPagerCategoryChangeMsg(pagerKey);
                break;
        }
    }

    public void updateView(String pagerKey) {
        for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
            Fragment fragment = getSupportFragmentManager()
                    .findFragmentByTag(mViewPagerAdapter.getFragmentTag(R.id.viewpager_main, i));
            if (null != fragment) {
                String categoryKey = DataCache.getInstance().getCategory(pagerKey).getNextKey(i);
                if (categoryKey != null) {
                    Category viewPagerCategory = DataCache.getInstance().getCategory(categoryKey);
                    ViewPagerFragment viewPagerFragment = (ViewPagerFragment) fragment;
                    viewPagerFragment.setCategoryData(viewPagerCategory);
                }
            }
        }
        mViewPagerAdapter.onCategorysChange(pagerKey);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            if (navigationRightFragment != null) {
                navigationRightFragment.onBackPress();
            }
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 1666) {
                ToastUtil.ToastShort(R.string.exit_hint);
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.more_main, R.id.search_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_main:
                break;
            case R.id.search_main:
                IntentUtil.openSearch(this);
                break;
        }
    }
}