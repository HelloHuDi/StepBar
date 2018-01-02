package com.hd.stepbar.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;


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

    @SuppressLint("WrongConstant")
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
            adjustConnectLineLength();
        }
    }

    @Override
    protected void startDraw(Canvas canvas) {
        for (int index = 0, count = centerPointList.size(); index < count; index++) {
            Point point = centerPointList.get(index);
            StepBarBean bean = config.getBeanList().get(index);
            //draw icon and draw text
            drawIconAndText(canvas, iconRectArray[index], point, bean);
            mainPaint.setColor(bean.getConnectLineColor());
            //draw connect background line
            if (index < count - 1) {
                canvas.drawLine(connectLineArray[index][0], connectLineArray[index][1], //
                                connectLineArray[index][2], connectLineArray[index][3], mainPaint);
            }
            if (config.getShowState() == StepBarConfig.StepShowState.DYNAMIC) {
                //draw dynamic outside ring
                if (bean.getState() == StepBarConfig.StepSate.RUNNING && outsideIconRingRadius > iconRadius) {
                    if (config.getOutSideIconRingCallback() != null) {
                        config.getOutSideIconRingCallback().drawRing(config, canvas, position);
                    } else if (ringAnimator != null) {
                        drawDefaultRingAnimator(canvas, bean);
                    }
                }
            } else {//static state
                //draw static outside ring
                if (bean.getState() == StepBarConfig.StepSate.RUNNING && outsideIconRingRadius > iconRadius) {
                    runPaint.setStrokeWidth(config.getOutsideIconRingWidth());
                    runPaint.setStyle(Paint.Style.STROKE);
                    runPaint.setColor(bean.getOutsideIconRingColor());
                    canvas.drawCircle(point.x, point.y, outsideIconRingRadius, runPaint);
                }
            }
        }
    }

    private void drawIconAndText(Canvas canvas, Rect bounds, Point point, StepBarBean bean) {
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
        icon.setBounds(bounds);
        icon.draw(canvas);
        //draw text
        int[] textSizes = measureFontSize(text);
        int textWidth = textSizes[0];
        int textHeight = textSizes[1];
        if (bean.getState() == StepBarConfig.StepSate.RUNNING) {
            textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        canvas.drawText(text, 0, text.length(), point.x - textWidth / 2, //
                        point.y + outsideIconRingRadius + textHeight + middleMargin, textPaint);
    }

}
