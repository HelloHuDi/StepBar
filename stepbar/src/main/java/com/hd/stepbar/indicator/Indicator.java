package com.hd.stepbar.indicator;

import android.graphics.Canvas;

import com.hd.stepbar.StepBarConfig;

/**
 * Created by hd on 2018/1/3 .
 * step bar indicator
 */
public interface Indicator {

    /**
     * draw step bar view
     */
    void drawStepBar(Canvas canvas);

    /**
     * @return {@link StepBarConfig#getShowState()}
     */
    boolean isStatic();

}
