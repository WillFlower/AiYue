package com.wgh.aiyue.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.BelowViewAdapter;
import com.wgh.aiyue.adapter.DragViewAdapter;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.ui.MainActivity;

import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
public class ChannelView extends RelativeLayout implements AdapterView.OnItemClickListener {
    private Activity mActivity = MainActivity.activityMain;
    private DragGridView dragGridView;
    private BelowGridView belowGridView;
    private DragViewAdapter dragViewAdapter;
    private BelowViewAdapter belowViewAdapter;
    private boolean mIsEditMode = false;
    private int dragViewPosition = 0;
    private int belowViewPosition = 0;
    boolean isMove = false;

    private OnChannelViewActionListener mChannelViewActionListener = null;

    public void setOnChannelViewActionListener(OnChannelViewActionListener listener) {
        this.mChannelViewActionListener = listener;
    }

    public ChannelView(Context context) {
        this(context, null);
    }

    public ChannelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.layout_channel_view, this);
        initView();
    }

    public void setEditMode(boolean mode) {
        mIsEditMode = mode;
    }

    public void initData(ArrayList<Category> dragCategories, ArrayList<Category> belowCategories) {
        dragViewAdapter = new DragViewAdapter(getContext(), dragCategories);
        dragGridView.setAdapter(dragViewAdapter);
        belowViewAdapter = new BelowViewAdapter(getContext(), belowCategories);
        belowGridView.setAdapter(this.belowViewAdapter);
        belowGridView.setOnItemClickListener(this);
        dragGridView.setOnItemClickListener(this);
    }

    private void initView() {
        dragGridView = (DragGridView) findViewById(R.id.userGridView);
        belowGridView = (BelowGridView) findViewById(R.id.belowGridView);

        dragGridView.setOnItemActionListener(new DragGridView.OnItemActionListener() {
            @Override
            public void onEditMode(boolean isEditMode) {
                mIsEditMode = isEditMode;
                mChannelViewActionListener.onEditMode(isEditMode);
            }

            @Override
            public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mChannelViewActionListener.onItemLongClick(parent, view, position, id);
            }

            @Override
            public void onItemDragPosition(int startPosition, int endPosition) {
                mChannelViewActionListener.onItemDragPosition(startPosition, endPosition);
            }

            @Override
            public void onItemDragFinish() {
                mChannelViewActionListener.onItemDragFinish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        // If you click when the animation has not finished yet, then click event is invalid.
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                if (!mIsEditMode) {
                    mChannelViewActionListener.onDragItemClick(position);
                    break;
                }
                dragViewPosition = position;
                // Position is 0 and 1 cannot be operated on.
                if (position != 0 && position != 1) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final Category category = ((DragViewAdapter) parent.getAdapter()).getItem(position);
                        belowViewAdapter.setVisible(false);
                        // Add to last.
                        belowViewAdapter.addItem(category);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    // Get the coordinates of the finish line.
                                    belowGridView.getChildAt(belowGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, category, dragGridView);
                                    dragViewAdapter.setRemove(position);
                                } catch (Exception localException) {
                                    localException.printStackTrace();
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.belowGridView:
                if (!mIsEditMode) {
                    mIsEditMode = true;
                    mChannelViewActionListener.onEditMode(true);
                    mChannelViewActionListener.onBelowItemClick(position);
                }
                belowViewPosition = position;
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final Category category = ((BelowViewAdapter) parent.getAdapter()).getItem(position);
                    dragViewAdapter.setVisible(false);
                    // Add to last.
                    dragViewAdapter.addItem(category);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                // Get the coordinates of the finish line.
                                dragGridView.getChildAt(dragGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, category, belowGridView);
                                belowViewAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(cache);
        return iv;
    }

    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final Category moveCategory, final GridView clickGridView) {
        int[] initLocation = new int[2];
        // Gets the coordinates of the passed view.
        moveView.getLocationInWindow(initLocation);
        // Get the view that you want to move and put it in the corresponding container.
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        // Creating animated animations.
        TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
        moveAnimation.setDuration(300L);    // Animation time.
        // Animation configuration.
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// After the animation effect is executed, the View object is not left in the stop position.
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // Determine whether to click DragGrid or BelowGridView.
                if (clickGridView instanceof DragGridView) {
                    belowViewAdapter.setVisible(true);
                    belowViewAdapter.notifyDataSetChanged();
                    dragViewAdapter.remove();
                    mChannelViewActionListener.onDragItemEditClick(dragViewPosition);
                } else {
                    dragViewAdapter.setVisible(true);
                    dragViewAdapter.notifyDataSetChanged();
                    belowViewAdapter.remove();
                    mChannelViewActionListener.onBelowItemEditClick(belowViewPosition);
                }
                isMove = false;
            }
        });
    }

    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) mActivity.getWindow().getDecorView();  // Get top view.
        LinearLayout moveLinearLayout = new LinearLayout(mActivity);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    public ArrayList<Category> getDragViewCategoryList() {
        return dragViewAdapter.getCategoryLst();
    }

    public ArrayList<Category> getBelowViewCategoryList() {
        return belowViewAdapter.getCategoryLst();
    }
}