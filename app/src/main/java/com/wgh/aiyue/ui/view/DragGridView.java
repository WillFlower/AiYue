package com.wgh.aiyue.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.DragViewAdapter;
import com.wgh.aiyue.util.DensityUtil;
import com.wgh.aiyue.util.VibratorUtil;


/**
 * Created by   : WGH.
 */
public class DragGridView extends GridView {
    /**
     * Click on the X position
     */
    public int downX;
    /**
     * Click on the Y position
     */
    public int downY;
    /**
     * Click to correspond to the X position of the entire interface
     */
    public int windowX;
    /**
     * Click to correspond to the Y position of the entire interface
     */
    public int windowY;
    /**
     * X position on the screen
     */
    private int win_view_x;
    /**
     * Y position on the screen
     */
    private int win_view_y;
    /**
     * Distance in drag X
     */
    int dragOffsetX;
    /**
     * Distance in drag Y
     */
    int dragOffsetY;
    /**
     * Long press time corresponds to postion
     */
    public int dragPosition;
    /**
     * Up after Position corresponding to item
     */
    private int dropPosition;
    /**
     * The item of the Position that started dragging
     */
    private int startPosition;
    /**
     * Item hight
     */
    private int itemHeight;
    /**
     * Item width
     */
    private int itemWidth;
    /**
     * When dragging, correspond to the view of item
     */
    private View dragImageView = null;
    /**
     * View item at long press
     */
    private ViewGroup dragItemView = null;
    /**
     * WindowManager Manager
     */
    private WindowManager windowManager = null;
    /**
     * WindowManager Manager LayoutParams
     */
    private WindowManager.LayoutParams windowParams = null;
    /**
     * the total number of items
     */
    private int itemTotalCount;
    /**
     * The number of item in a row
     */
    private int nColumns = 4;
    /**
     * the number of rows
     */
    private int nRows;
    /**
     * Rest
     */
    private int Remainder;
    /**
     * Whether in moving
     */
    private boolean isMoving = false;

    private int holdPosition;
    /**
     * The magnification factor when dragging
     */
    private double dragScale = 1.2D;
    /**
     * The horizontal spacing between each item
     */
    private int mHorizontalSpacing = 12;
    /**
     * The vertical spacing between each item
     */
    private int mVerticalSpacing = 12;
    /**
     * ID of the last animation when moving
     */
    private String LastAnimationID;

    private boolean isEditMode = false;

    private OnItemActionListener mOnItemActionListener = null;

    public interface OnItemActionListener {
        void onEditMode(boolean isEditMode);
        void onItemLongClick(AdapterView<?> parent, View view, int position, long id);
        void onItemDragPosition(int startPosition, int endPosition);
        void onItemDragFinish();
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.mOnItemActionListener = listener;
    }

    public DragGridView(Context context) {
        super(context);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        // Turn the spacing dip set in the layout file to PX
        mHorizontalSpacing = DensityUtil.dip2px(context, mHorizontalSpacing);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) ev.getX();
            downY = (int) ev.getY();
            windowX = (int) ev.getX();
            windowY = (int) ev.getY();
            setOnItemClickListener(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (dragImageView != null && dragPosition != AdapterView.INVALID_POSITION) {
            // The corresponding x, y position when moving
            super.onTouchEvent(ev);
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) ev.getX();
                    windowX = (int) ev.getX();
                    downY = (int) ev.getY();
                    windowY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    onDrag(x, y, (int) ev.getRawX(), (int) ev.getRawY());
                    if (!isMoving) {
                        OnMove(x, y);
                    }
                    if (pointToPosition(x, y) != AdapterView.INVALID_POSITION) {
                        break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    stopDrag();
                    onDrop(x, y);
                    requestDisallowInterceptTouchEvent(false);
                    mOnItemActionListener.onItemDragFinish();
                    break;

                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * In the drag situation.
     */
    private void onDrag(int x, int y, int rawx, int rawy) {
        if (dragImageView != null) {
            windowParams.alpha = 0.6f;
            windowParams.x = rawx - win_view_x;
            windowParams.y = rawy - win_view_y;
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
    }

    /**
     * The situation is letting go
     */
    private void onDrop(int x, int y) {
        // According to the drag to x, the Y coordinate gets the item corresponding to the position below the dragging position
        int tempPostion = pointToPosition(x, y);
        dropPosition = tempPostion;
        DragViewAdapter adapter = (DragViewAdapter) getAdapter();
        // Shows the item that just dragged
        adapter.setShowDropItem(true);
        // Refresh the adapter so that the corresponding item display
        adapter.notifyDataSetChanged();
    }

    /**
     * Long click listener
     */
    public void setOnItemClickListener(final MotionEvent ev) {
        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isEditMode = true;
                mOnItemActionListener.onEditMode(true);
                mOnItemActionListener.onItemLongClick(parent, view, position, id);

                int x = (int) ev.getX();    // The X position of the long press event
                int y = (int) ev.getY();    // The Y position of the long press event
                startPosition = position;   // Position for the first time
                dragPosition = position;
                if (startPosition <= 1) {
                    return false;
                }
                ViewGroup dragViewGroup = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
                TextView dragTextView = (TextView) dragViewGroup.findViewById(R.id.text_item);
                dragTextView.setSelected(true);
                dragTextView.setEnabled(false);
                itemHeight = dragViewGroup.getHeight();
                itemWidth = dragViewGroup.getWidth();
                itemTotalCount = DragGridView.this.getCount();
                int row = itemTotalCount / nColumns;        // Number of rows calculated
                Remainder = (itemTotalCount % nColumns);    // Work out the excess quantity of the last line
                if (Remainder != 0) {
                    nRows = row + 1;
                } else {
                    nRows = row;
                }
                // If this particular one is not the one that drags, and is not equal to -1.
                if (dragPosition != AdapterView.INVALID_POSITION) {
                    // The drawing buffer used by the released resource.
                    // If you call buildDrawingCache () manually without calling setDrawingCacheEnabled (real),
                    // you should clean up the cache using this method.
                    win_view_x = windowX - dragViewGroup.getLeft(); // View relative to its own X
                    win_view_y = windowY - dragViewGroup.getTop();  // View relative to its own Y
                    dragOffsetX = (int) (ev.getRawX() - x); // The X position of the finger on the screen - the position of the finger in the control is the distance from the left
                    dragOffsetY = (int) (ev.getRawY() - y); // The Y position of the finger on the screen - the position of the finger in the control is the distance from the top
                    dragItemView = dragViewGroup;
                    dragViewGroup.destroyDrawingCache();
                    dragViewGroup.setDrawingCacheEnabled(true);
                    Bitmap dragBitmap = Bitmap.createBitmap(dragViewGroup.getDrawingCache());
                    VibratorUtil.Vibrate(50);
                    startDrag(dragBitmap, (int) ev.getRawX(), (int) ev.getRawY());
                    hideDropItem();
                    dragViewGroup.setVisibility(View.INVISIBLE);
                    isMoving = false;
                    requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                return false;
            }
        });
    }

    public void startDrag(Bitmap dragBitmap, int x, int y) {
        stopDrag();
        windowParams = new WindowManager.LayoutParams();// Get the parameters of the window interface
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        // Get the coordinates of the upper left corner of the preview relative to the screen
        windowParams.x = x - win_view_x;
        windowParams.y = y - win_view_y;
        windowParams.width = (int) (dragScale * dragBitmap.getWidth());     // Zoom in dragScale times, you can set the drag multiples
        windowParams.height = (int) (dragScale * dragBitmap.getHeight());   // Zoom in dragScale times, you can set the drag multiples
        this.windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        this.windowParams.format = PixelFormat.TRANSLUCENT;
        this.windowParams.windowAnimations = 0;
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(dragBitmap);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
        windowManager.addView(iv, windowParams);
        dragImageView = iv;
    }

    /**
     * Stop dragging, release and initialize
     */
    private void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    /**
     * In ScrollView, so calculate height
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * Hide and drop item
     */
    private void hideDropItem() {
        ((DragViewAdapter) getAdapter()).setShowDropItem(false);
    }

    /**
     * Get mobile animation
     */
    public Animation getMoveAnimation(float toXValue, float toYValue) {
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, toXValue,
                Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, toYValue);  // The current position moves to the specified location
        mTranslateAnimation.setFillAfter(true);     // After setting up an animation, the View object remains in the stop position
        mTranslateAnimation.setDuration(300L);
        return mTranslateAnimation;
    }

    /**
     * Triggered when moving
     */
    public void OnMove(int x, int y) {
        // Drag the position below view
        int dPosition = pointToPosition(x, y);
        // Determine if the position below is the first 2 that cannot be dragged
        if (dPosition > 1) {
            if ((dPosition == -1) || (dPosition == dragPosition)) {
                return;
            }
            dropPosition = dPosition;
            if (dragPosition != startPosition) {
                dragPosition = startPosition;
            }
            int movecount;
            // Drag = start drag, and drag does not equal to put down
            if ((dragPosition == startPosition) || (dragPosition != dropPosition)) {
                // The number of item needed to move
                movecount = dropPosition - dragPosition;
            } else {
                // The number of moving item needed to move is 0
                movecount = 0;
            }
            if (movecount == 0) {
                return;
            }

            int movecount_abs = Math.abs(movecount);

            if (dPosition != dragPosition) {
                // DragGroup is set to invisible
                ViewGroup dragGroup = (ViewGroup) getChildAt(dragPosition);
                dragGroup.setVisibility(View.INVISIBLE);
                float to_x = 1; // Current positon below
                float to_y;
                // Percentage of distance traveled by x_vlaue (percentage relative to its length)
                float x_vlaue = ((float) mHorizontalSpacing / (float) itemWidth) + 1.0f;
                // Percentage of distance traveled by y_vlaue (percentage relative to its length)
                float y_vlaue = ((float) mVerticalSpacing / (float) itemHeight) + 1.0f;
                for (int i = 0; i < movecount_abs; i++) {
                    to_x = x_vlaue;
                    to_y = y_vlaue;
                    // Turn left
                    if (movecount > 0) {
                        // Judge whether it is the same line or not
                        holdPosition = dragPosition + i + 1;
                        if (dragPosition / nColumns == holdPosition / nColumns) {
                            to_x = -x_vlaue;
                            to_y = 0;
                        } else if (holdPosition % 4 == 0) {
                            to_x = 3 * x_vlaue;
                            to_y = -y_vlaue;
                        } else {
                            to_x = -x_vlaue;
                            to_y = 0;
                        }
                    } else {
                        // Turn right, down to the top, and to the left
                        holdPosition = dragPosition - i - 1;
                        if (dragPosition / nColumns == holdPosition / nColumns) {
                            to_x = x_vlaue;
                            to_y = 0;
                        } else if ((holdPosition + 1) % 4 == 0) {
                            to_x = -3 * x_vlaue;
                            to_y = y_vlaue;
                        } else {
                            to_x = x_vlaue;
                            to_y = 0;
                        }
                    }
                    ViewGroup moveViewGroup = (ViewGroup) getChildAt(holdPosition);
                    Animation moveAnimation = getMoveAnimation(to_x, to_y);
                    moveViewGroup.startAnimation(moveAnimation);
                    // If it is the last move, then set his last animation ID to LastAnimationID
                    if (holdPosition == dropPosition) {
                        LastAnimationID = moveAnimation.toString();
                    }
                    moveAnimation.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            isMoving = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // If the final animation is finished, then execute the following method
                            if (animation.toString().equalsIgnoreCase(LastAnimationID)) {
                                DragViewAdapter mDragAdapter = (DragViewAdapter) getAdapter();
                                mDragAdapter.exchange(startPosition, dropPosition);
                                mOnItemActionListener.onItemDragPosition(startPosition, dropPosition);
                                startPosition = dropPosition;
                                dragPosition = dropPosition;
                                isMoving = false;
                            }
                        }
                    });
                }
            }
        }
    }
}
