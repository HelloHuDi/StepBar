package com.hd.stepbar.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by hd on 2017/12/31 .
 * vertical step bar view indicator
 */
public class VerticalStepBarViewIndicator extends StepBarViewIndicator {

    public VerticalStepBarViewIndicator(Context context) {
        super(context,1);
    }

    public VerticalStepBarViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs,1);
    }

    public VerticalStepBarViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr,1);
    }

    @Override
    protected void startDraw(Canvas canvas) {

    }
}
