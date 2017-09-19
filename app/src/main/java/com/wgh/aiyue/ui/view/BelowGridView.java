package com.wgh.aiyue.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by   : WGH.
 */
public class BelowGridView extends GridView {
    public BelowGridView(Context context) {
        super(context);
    }

    public BelowGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BelowGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
