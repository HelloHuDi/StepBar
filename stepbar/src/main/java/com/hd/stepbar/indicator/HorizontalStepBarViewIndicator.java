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
import android.util.Log;

import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;


/**
 * Created by hd on 2017/12/30 .
 * horizontal step bar view indicator
 */
public class HorizontalStepBarViewIndicator extends StepBarViewIndicator {

    public HorizontalStepBarViewIndicator(Context context) {
        super(context);
    }

    public HorizontalStepBarViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalStepBarViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (config != null && config.getIconCircleRadius() > 0 && config.getBeanList() != null) {
            int beanSize = config.getBeanList().size();
            int Height = MeasureSpec.getSize(heightMeasureSpec);
            selectRightRadius(0, Height, beanSize);
            int Width = outsideIconRingRadius * (beanSize * 3 + 1) + getPaddingLeft() + getPaddingRight();
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
            selectRightRadius(getWidth(), getHeight(), beanSize);
            adjustFontSize();
            for (int index = 0, count = config.getBeanList().size(); index < count; index++) {
                @SuppressLint("DrawAllocation") Point point = new Point();
                point.x = paddingLeft + index * outsideIconRingRadius * 2 + index * connectLineLength + outsideIconRingRadius;
                point.y = paddingTop + outsideIconRingRadius;
                centerPointList.add(point);
                //add icon rect
                iconRectArray[index] = new Rect(point.x - iconRadius, point.y - iconRadius, point.x + iconRadius, point.y + iconRadius);
                //connect line
                if (index < count - 1) {
                    float startX, endX;
                    if (config.getBeanList().get(index).getState() == StepBarConfig.StepSate.RUNNING) {
                        startX = point.x + outsideIconRingRadius;
                    } else {
                        startX = point.x + iconRadius;
                    }
                    if (config.getBeanList().get(index + 1).getState() == StepBarConfig.StepSate.RUNNING) {
                        endX = point.x + outsideIconRingRadius + connectLineLength;
                    } else {
                        endX = point.x + iconRadius + 2 * config.getOutsideIconRingWidth() + connectLineLength;
                    }
                    connectLineArray[index][0] = startX;
                    connectLineArray[index][1] = point.y;
                    connectLineArray[index][2] = endX;
                    connectLineArray[index][3] = point.y;
                }
            }
        }
    }

    /**
     * select right circle radius
     * the length of a connecting line is at least a outside circle radius
     */
    private void selectRightRadius(int width, int height, int beanSize) {
        if (config.getIconCircleRadius() > 0) {
            iconRadius = config.getIconCircleRadius();
            outsideIconRingRadius = iconRadius + config.getOutsideIconRingWidth();
        } else {
            // automatically resized
            outsideIconRingRadius = width / (beanSize * 3 + 1);
            iconRadius = outsideIconRingRadius - config.getOutsideIconRingWidth();
        }
        middleMargin = iconRadius;
        paddingTop = paddingBottom = (height - outsideIconRingRadius * 2 - measureFontSize()[1] - middleMargin) / 2;
        connectLineLength = paddingLeft = paddingRight = outsideIconRingRadius;
    }

    /**
     * adjust text font size，avoid the overlay of the text
     */
    private void adjustFontSize() {
        String maxCountText = "";
        if (config != null && config.getBeanList() != null) {
            // select max count of text
            for (StepBarBean bean : config.getBeanList()) {
                String running_text = bean.getRunningText();
                String waiting_text = bean.getRunningText();
                String completed_text = bean.getRunningText();
                String failed_text = bean.getRunningText();
                maxCountText = running_text.length() > waiting_text.length() ? running_text : waiting_text;
                maxCountText = maxCountText.length() > completed_text.length() ? maxCountText : completed_text;
                maxCountText = maxCountText.length() > failed_text.length() ? maxCountText : failed_text;
            }
        }
        Log.d("tag", "text font size adjust start :" + textSize);
        int maxTextCountWidth = measureFontSize(maxCountText)[0];
        while (maxTextCountWidth > 3 * outsideIconRingRadius) {
            textSize -= 0.2f;
            config.setTextSize(textSize);
            setPaintTextSize();
            maxTextCountWidth = measureFontSize(maxCountText)[0];
        }
        Log.d("tag", "text font size adjust complete :" + textSize);
    }

    @Override
    protected void startDraw(Canvas canvas) {
        for (int index = 0, count = centerPointList.size(); index < count; index++) {
            Point point = centerPointList.get(index);
            StepBarBean bean = config.getBeanList().get(index);
            //draw icon and draw text
            drawIconAndText(canvas, iconRectArray[index], point, bean);
            mainPaint.setColor(bean.getDefaultConnectLineColor());
            //draw connect background line
            if (index < count - 1) {
                canvas.drawLine(connectLineArray[index][0], connectLineArray[index][1], connectLineArray[index][2], connectLineArray[index][3], mainPaint);
            }
            if (config.getShowState() == StepBarConfig.StepShowState.DYNAMIC) {
                //draw dynamic outside ring
                if (bean.getState() == StepBarConfig.StepSate.RUNNING) {
                    if (valueAnimator != null) {
                        setDefaultRingAnimator(canvas, bean);
                    } else if (config.getOutSideIconRingCallback() != null) {
                        config.getOutSideIconRingCallback().drawRing(config, canvas, position);
                    }
                }
                //draw connect switch line

            } else {//static state
                //draw static outside ring
                if (bean.getState() == StepBarConfig.StepSate.RUNNING) {
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
        canvas.drawText(text, 0, text.length(), point.x - textWidth / 2, point.y + outsideIconRingRadius + textHeight + middleMargin, textPaint);
    }

}
