package com.wgh.aiyue.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by   : WGH.
 */
public class AnimationUtil {
    public static void rotateSelf(View view, float fromDegrees, float toDegrees) {
        Animation rotateAnimation  = new RotateAnimation(fromDegrees, fromDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(100);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotateAnimation);
    }
}
