package com.wgh.aiyue.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by   : WGH.
 */
public class AttrsUtil {
    public static int getColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        int color = typedValue.data;
        return color;
    }
}
