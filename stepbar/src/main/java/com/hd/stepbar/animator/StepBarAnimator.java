package com.hd.stepbar.animator;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;

/**
 * Created by hd on 2018/1/1 .
 *
 */
public class StepBarAnimator {

    public PathMeasure pathMeasure;

    public float animatorValue = 0.5f;

    public float pathLength = 0;

    public Path mPath;

    private ValueAnimator valueAnimator;

    public void cancelAnimator() {
        Log.d("tag", "cancel animator ");
        if (valueAnimator != null)
            valueAnimator.cancel();
        valueAnimator = null;
    }

    public void createAnimator(Path path, long duration, int repeatCount) {
        mPath = new Path();
        pathMeasure = new PathMeasure();
        pathMeasure.setPath(path, true);
        pathLength = pathMeasure.getLength();
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorValue = (float) valueAnimator.getAnimatedValue();
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(repeatCount);
        valueAnimator.start();
    }

}
