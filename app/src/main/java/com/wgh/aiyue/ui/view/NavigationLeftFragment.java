package com.wgh.aiyue.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.RecyclerSubscriptionAdapter;
import com.wgh.aiyue.helper.DataCacheHelper;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.util.AnimationUtil;
import com.wgh.aiyue.util.IntentUtil;
import com.wgh.aiyue.util.MessageUtil;
import com.wgh.aiyue.util.ConstDefine;
import com.wgh.aiyue.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by   : WGH.
 */
public class NavigationLeftFragment extends Fragment {
    private static final String EXTRA_CATEGORY = "extra_drawerLeft_category";

    Unbinder unbinder;
    @BindView(R.id.icon_image)
    CircleImageView iconImage;
    @BindView(R.id.authorname)
    TextView authorname;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.text_collection)
    TextView textCollection;
    @BindView(R.id.text_subscription)
    TextView textSubscription;
    @BindView(R.id.text_setting)
    TextView textSetting;
    @BindView(R.id.text_about)
    TextView textAbout;
    @BindView(R.id.button_pay)
    TextView buttonPay;
    @BindView(R.id.recycler_view_subscription)
    RecyclerView recyclerViewSubscription;
    @BindView(R.id.imageView_subscription)
    ImageView imageViewSubscription;
    @BindView(R.id.imageView_setting)
    ImageView imageViewSetting;
    @BindView(R.id.button_theme)
    TextView buttonTheme;
    @BindView(R.id.layout_setting)
    LinearLayout layoutSetting;
    @BindView(R.id.layout_about)
    LinearLayout layoutAbout;

    private static Context mContext;
    private static FragmentManager mFragmentManager;
    @BindView(R.id.layout_collection)
    LinearLayout layoutCollection;

    private Category mCategory;
    private boolean flagRecyclerVisible = true;
    private boolean flagSettingVisible = true;


    public NavigationLeftFragment() {
    }

    public static Fragment newInstance(Context context, Category category, FragmentManager supportFragmentManager) {
        mContext = context;
        mFragmentManager = supportFragmentManager;
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CATEGORY, category);
        return Fragment.instantiate(context, NavigationLeftFragment.class.getName(), bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = (Category) getArguments().getSerializable(EXTRA_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_left, viewGroup, false);
        unbinder = ButterKnife.bind(this, view);

        if (ConstDefine.isOfficalVersion()) {
            buttonPay.setText(mContext.getResources().getString(R.string.navigation_left_copyserialnum));
        } else {
            buttonPay.setText(mContext.getResources().getString(R.string.navigation_left_pay));
        }

        iconImage.setImageResource(DataCacheHelper.getInstance().getThemeIconId());

        final RecyclerSubscriptionAdapter subscriptionAdapter = new RecyclerSubscriptionAdapter(mContext, mCategory);
        recyclerViewSubscription.setAdapter(subscriptionAdapter);
        recyclerViewSubscription.setLayoutManager(new GridLayoutManager(mContext, ConstDefine.SubscriptionRecyclerViewColumnNum));

        subscriptionAdapter.setOnItemClickListener(new RecyclerSubscriptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String nextName) {
                String rightCategoryKey = mCategory.getName() + nextName;
                if (ConstDefine.getRightDrawerCategoryKey() == null || !ConstDefine.getRightDrawerCategoryKey().equals(rightCategoryKey)) {
                    ConstDefine.setRightDrawerCategoryKey(rightCategoryKey);
                    MessageUtil.postRightCategoryChangeMsg(rightCategoryKey);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.icon_image, R.id.authorname, R.id.email, R.id.layout_collection, R.id.button_pay, R.id.imageView_subscription, R.id.imageView_setting, R.id.button_theme, R.id.layout_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_collection:
                IntentUtil.openFavorite(mContext);
                break;
            case R.id.imageView_subscription:
                if (flagRecyclerVisible) {
                    recyclerViewSubscription.setVisibility(View.VISIBLE);
                    flagRecyclerVisible = false;
                    AnimationUtil.rotateSelf(imageViewSubscription, 180, 0);
                } else {
                    recyclerViewSubscription.setVisibility(View.GONE);
                    flagRecyclerVisible = true;
                    AnimationUtil.rotateSelf(imageViewSubscription, 0, 180);
                }
                break;
            case R.id.imageView_setting:
                if (flagSettingVisible) {
                    layoutSetting.setVisibility(View.VISIBLE);
                    flagSettingVisible = false;
                    AnimationUtil.rotateSelf(imageViewSetting, 180, 0);
                } else {
                    layoutSetting.setVisibility(View.GONE);
                    flagSettingVisible = true;
                    AnimationUtil.rotateSelf(imageViewSetting, 0, 180);
                }
                break;
            case R.id.button_theme:
                ThemeDialog dialog = new ThemeDialog();
                dialog.show(mFragmentManager, "theme");
                break;
            case R.id.icon_image:
                break;
            case R.id.authorname:
            case R.id.email:
            case R.id.layout_about:
                IntentUtil.openAbout(mContext);
                break;
            case R.id.button_pay:
                ToastUtil.ToastLong(R.string.attention_pay_wait);
                // to pay for offical...
                break;
            default:
                break;
        }
    }

}
