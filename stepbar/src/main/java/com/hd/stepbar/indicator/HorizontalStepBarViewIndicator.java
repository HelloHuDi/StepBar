package com.hd.stepbar.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;


/**
 * Created by hd on 2017/12/30 .
 * horizontal step bar view indicator
 */
public class HorizontalStepBarViewIndicator extends StepBarViewIndicator {

    public HorizontalStepBarViewIndicator(Context context) {
        super(context, 0);
    }

    public HorizontalStepBarViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public HorizontalStepBarViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (config != null && config.getIconCircleRadius() > 0 && config.getBeanList() != null) {
            int beanSize = config.getBeanList().size();
            int Height = MeasureSpec.getSize(heightMeasureSpec);
            selectRightRadius(0, Height, beanSize);
            int Width = (int) (outsideIconRingRadius * (beanSize * 3 + 1) + getPaddingLeft() + getPaddingRight());
            setMeasuredDimension(Width, Height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (config != null && config.getBeanList() != null) {
            centerPointList.clear();
            int beanSize = config.getBeanList().size();
            selectRightRadius(w, h, beanSize);
            if (config.isAdjustTextSize())
                adjustFontSize();
            for (int index = 0, count = config.getBeanList().size(); index < count; index++) {
                @SuppressLint("DrawAllocation") Point point = new Point();
                point.x = (int) (paddingLeft + index * outsideIconRingRadius * 2 + index * connectLineLength + outsideIconRingRadius);
                point.y = (int) (paddingTop + outsideIconRingRadius);
                centerPointList.add(point);
                //add icon rect
                iconRectArray[index] = new Rect(point.x - (int) iconRadius, point.y - (int) iconRadius, //
                                                point.x + (int) iconRadius, point.y + (int) iconRadius);
            }
            //connect line
            updateCurrentData();
        }
    }

}
