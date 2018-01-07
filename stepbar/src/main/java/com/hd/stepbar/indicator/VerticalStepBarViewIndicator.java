package com.hd.stepbar.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by hd on 2017/12/31 .
 * vertical step bar view indicator
 */
public class VerticalStepBarViewIndicator extends StepBarViewIndicator {

    public VerticalStepBarViewIndicator(Context context) {
        super(context, 1);
    }

    public VerticalStepBarViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs, 1);
    }

    public VerticalStepBarViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (config != null && config.getIconCircleRadius() > 0 && config.getBeanList() != null) {
            int beanSize = config.getBeanList().size();
            int Width = MeasureSpec.getSize(widthMeasureSpec);
            selectRightRadius(Width, 0, beanSize);
            int Height = (int) (outsideIconRingRadius * (beanSize * 3 + 1) + getPaddingTop() + getPaddingBottom());
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
                point.x = (int) (paddingLeft + outsideIconRingRadius);
                point.y = (int) (paddingTop + index * outsideIconRingRadius * 2 + index * connectLineLength + outsideIconRingRadius);
                centerPointList.add(point);
                //add icon rect
                iconRectArray[index] = new Rect(point.x - (int) outsideIconRingRadius, point.y - (int) outsideIconRingRadius,//
                                                point.x + (int) outsideIconRingRadius, point.y + (int) outsideIconRingRadius);
            }
            //connect line
            updateCurrentData();
        }
    }

    /**
     * if the font size is set, the text will not be fully displayed in the maximum available range,
     * then the font size will be automatically adjusted.
     */
    @Override
    protected void adjustFontSize() {
        //there may be multiple lines of text
        String maxCountText = getMaxCountText();
        float maxCountTextWidth = measureFontSize(maxCountText)[0];
        float availableTextHeight = 2 * outsideIconRingRadius + 2 * (outsideIconRingRadius / 3.0f);
        if (maxCountTextWidth < availableTextWidth) {//single lines of text
            adjustFontSize(maxCountText);
        } else {//multiple lines of text
            getNumCount(maxCountText);
            Log.e("tag", "multiple lines of text and need adjust font size,current font size :" + textSize + ",lines :" + numCount);
            int allTextHeight = getAllNumTextHeight();
            while (allTextHeight > availableTextHeight) {
                textSize -= 0.2f;
                config.setTextSize(textSize);
                setPaintTextSize();
                numCount = 1;
                getNumCount(maxCountText);
                allTextHeight = getAllNumTextHeight();
            }
            Log.e("tag", "font size after adjusting :" + textSize + ",lines :" + numCount);
        }
    }

    private int getAllNumTextHeight() {
        int num = numCount;
        int allTextHeight = 0;
        float height = measureFontSize()[1];
        while (num > 0) {
            allTextHeight += height;
            num--;
        }
        return allTextHeight;
    }

    private int numCount = 1;

    private int getNumCount(String text) {
        int text_width = 0;
        for (int index = 0, count = text.length(); index < count; index++) {
            text_width += measureFontSize(String.valueOf(text.charAt(index)))[0];
            if (text_width > availableTextWidth) {
                numCount++;
                String s = text.substring(index, count);
                return getNumCount(s);
            }
        }
        return numCount++;
    }


}
