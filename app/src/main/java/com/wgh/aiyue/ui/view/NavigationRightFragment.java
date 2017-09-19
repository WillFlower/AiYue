package com.wgh.aiyue.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.helper.DataCacheHelper;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.util.MessageUtil;
import com.wgh.aiyue.util.ConstDefine;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by   : WGH.
 */
public class NavigationRightFragment extends Fragment {
    private static final String TAG = NavigationRightFragment.class.getSimpleName();
    private static final String EXTRA_CATEGORY = "extra_drawerRight_category";

    Unbinder unbinder;
    @BindView(R.id.head_navigation_right)
    TextView textNavigationRight;
    @BindView(R.id.layout_navigation_right)
    LinearLayout layoutNavigationRight;
    @BindView(R.id.channel_view)
    ChannelView channelView;


    private static Context mContext;
    private Category mCategory;
    private ArrayList<Category> mDragCategories = new ArrayList<>();
    private ArrayList<Category> mBelowCategories = new ArrayList<>();

    public NavigationRightFragment() {
    }

    public static Fragment newInstance(Context context, Category category) {
        mContext = context;
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CATEGORY, category);
        return Fragment.instantiate(context, NavigationRightFragment.class.getName(), bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = (Category) getArguments().getSerializable(EXTRA_CATEGORY);
        mDragCategories = DataCacheHelper.getInstance().getRightDragCategoryList();
        mBelowCategories= DataCacheHelper.getInstance().getRightBelowCategoryList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_right, viewGroup, false);
        unbinder = ButterKnife.bind(this, view);

        channelView.initData(mDragCategories, mBelowCategories);
        channelView.setOnChannelViewActionListener(new OnChannelViewActionListener() {

            @Override
            public void onEditMode(boolean isEditMode) {
            }

            @Override
            public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onItemDragPosition(int startPosition, int endPosition) {
            }

            @Override
            public void onItemDragFinish() {
                saveData();
            }

            @Override
            public void onDragItemClick(int position) {
                Category pagerCategory  = mDragCategories.get(position);
                String pagerCategoryKey = DataCacheHelper.getInstance().getCategoryKey(pagerCategory);
                if (ConstDefine.getViewPagerKey() == null || !ConstDefine.getViewPagerKey().equals(pagerCategoryKey)) {
                    ConstDefine.setViewPagerKey(pagerCategoryKey);
                    MessageUtil.postPagerCategoryChangeMsg(pagerCategoryKey);
                }
            }

            @Override
            public void onBelowItemClick(int position) {
            }

            @Override
            public void onDragItemEditClick(int position) {
                saveData();
            }

            @Override
            public void onBelowItemEditClick(int position) {
                saveData();
            }
        });
        return view;
    }

    private void saveData() {
        DataCacheHelper.getInstance().saveRightChildCategoryList(
                channelView.getDragViewCategoryList(), channelView.getBelowViewCategoryList());
    }

    public void onBackPress() {
        channelView.setEditMode(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
