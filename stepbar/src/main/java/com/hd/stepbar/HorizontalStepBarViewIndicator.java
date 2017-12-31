package com.hd.stepbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


/**
 * Created by hd on 2017/12/30 .
 * horizontal step bar
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
            selectRightRadius(beanSize);
            int Width = outsideIconRingRadius * (beanSize * 3 + 1) + getPaddingLeft() + getPaddingRight();
            setMeasuredDimension(Width, MeasureSpec.getSize(heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData(getWidth(), getHeight());
    }

    private void initData(int Width, int Height) {
        centerPointList.clear();
        height = Height - getPaddingTop() - getPaddingBottom() - paddingTop - paddingBottom;
        width = Width - getPaddingLeft() - getPaddingRight();
        if (config != null && config.getBeanList() != null) {
            int beanSize = config.getBeanList().size();
            if (connectLineLength > 0) selectRightRadius(beanSize);
            for (int index = 0, length = config.getBeanList().size(); index < length; index++) {
                @SuppressLint("DrawAllocation") Point point = new Point();
                point.x = paddingLeft + index * outsideIconRingRadius * 2 + index * connectLineLength + outsideIconRingRadius;
                point.y = paddingTop + outsideIconRingRadius;
                centerPointList.add(point);
            }
        }
    }

    /**
     * select right circle radius
     * the length of a connecting line is at least a outside circle radius
     */
    private void selectRightRadius(int beanSize) {
        if (config.getIconCircleRadius() > 0) {
            iconRadius = config.getIconCircleRadius();
            outsideIconRingRadius = iconRadius + config.getOutsideIconRingWidth();
        } else {
            // automatically resized
            outsideIconRingRadius = width / (beanSize * 3 + 1);
            iconRadius = outsideIconRingRadius - config.getOutsideIconRingWidth();
        }
        connectLineLength = paddingLeft = paddingRight = outsideIconRingRadius;
    }

    @Override
    protected void startDraw(Canvas canvas) {
        for (int index = 0; index < centerPointList.size(); index++) {
            Point point = centerPointList.get(index);
            StepBarBean bean = config.getBeanList().get(index);
            //select icon
            Drawable icon = bean.getNormalIcon();
            switch (bean.getState()) {
                case RUNNING:
                    icon = bean.getNormalIcon();
                    // TODO: 2017/12/31 外圈
                    break;
                case WAITING:
                    icon = bean.getNormalIcon();
                    break;
                case COMPLETED:
                    icon = bean.getCompletedIcon();
                    break;
                case FAILED:
                    icon = bean.getFailedIcon();
                    break;
            }
            //draw icon
            Rect rect = new Rect(point.x - iconRadius, point.y - iconRadius, point.x + iconRadius, point.y + iconRadius);
            icon.setBounds(rect);
            icon.draw(canvas);
            //draw connect line
            if (index < centerPointList.size() - 1) {
                canvas.drawLine(point.x + outsideIconRingRadius, point.y, point.x + outsideIconRingRadius + connectLineLength, point.y, paint);
            }
        }
    }
}
