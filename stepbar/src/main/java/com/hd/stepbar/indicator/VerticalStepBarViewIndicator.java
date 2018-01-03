package com.hd.stepbar.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.hd.stepbar.StepBarBean;

import java.util.LinkedList;

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
            int Width = MeasureSpec.getSize(heightMeasureSpec);
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
            //            adjustFontSize();
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
            adjustConnectLineLength();
        }
    }

    @Override
    protected void adjustFontSize() {
        //there may be multiple lines of text
        String maxCountText = getMaxCountText();
        float availableWidth = getMeasuredWidth() - paddingLeft - paddingRight -//
                getPaddingLeft() - getPaddingRight() - middleMargin;
        int maxTextWidth = measureFontSize(maxCountText)[0];
        if (maxTextWidth < availableWidth) {//single lines of text
            super.adjustFontSize();
        } else {//multiple lines of text

        }
    }

    private LinkedList<String[]> textsList = new LinkedList<>();

    @Override
    protected void startDraw(Canvas canvas) {
        for (int index = 0, count = centerPointList.size(); index < count; index++) {
            Point point = centerPointList.get(index);
            StepBarBean bean = config.getBeanList().get(index);
            //draw icon and draw text
            Drawable icon;
            String text;
            switch (bean.getState()) {
                case RUNNING:
                    icon = bean.getRunningIcon();
                    text = bean.getRunningText();
                    textPaint.setColor(bean.getRunningTextColor());
                    break;
                case WAITING:
                    icon = bean.getWaitingIcon();
                    text = bean.getWaitingText();
                    textPaint.setColor(bean.getWaitingTextColor());
                    break;
                case COMPLETED:
                    icon = bean.getCompletedIcon();
                    text = bean.getCompletedText();
                    textPaint.setColor(bean.getCompletedTextColor());
                    break;
                case FAILED:
                    icon = bean.getFailedIcon();
                    text = bean.getFailedText();
                    textPaint.setColor(bean.getFailedTextColor());
                    break;
                default:
                    icon = bean.getWaitingIcon();
                    text = "";
                    textPaint.setColor(bean.getWaitingTextColor());
                    break;
            }
            //draw icon
            icon.setBounds(iconRectArray[index]);
            icon.draw(canvas);
            //draw text
            //draw line
            mainPaint.setColor(bean.getConnectLineColor());
            //draw connect background line
            if (index < count - 1) {
                canvas.drawLine(connectLineArray[index][0], connectLineArray[index][1], //
                                connectLineArray[index][2], connectLineArray[index][3], mainPaint);
            }
        }
    }
}
