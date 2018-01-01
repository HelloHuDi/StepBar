package com.hd.stepbar.indicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;
import com.hd.stepbar.animator.StepBarAnimator;

import java.util.LinkedList;


/**
 * Created by hd on 2017/12/30 .
 * step bar
 */
public abstract class StepBarViewIndicator extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private String TAG = StepBarViewIndicator.class.getSimpleName();

    protected StepBarConfig config;

    protected SurfaceHolder holder;

    protected Paint textPaint, linePaint, mainPaint, runPaint;

    protected boolean isDraw;

    protected float iconRadius, outsideIconRingRadius;

    protected float textSize;

    protected float paddingRight, paddingLeft, paddingTop, paddingBottom;

    protected float middleMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

    protected float connectLineLength ;

    /**
     * all icon center point position
     */
    protected LinkedList<Point> centerPointList = new LinkedList<>();

    /**
     * all icon rect position
     */
    protected Rect[] iconRectArray;

    /**
     * all connect line position ,
     * connectLineArray[0] show {@link #position},
     * connectLineArray[position] ={startX, startY, stopX, stopY}
     */
    protected float[][] connectLineArray;

    /**
     * current running state icon position
     */
    protected int position = -1;

    protected StepBarAnimator ringAnimator;

    private Canvas canvas;

    /**
     * step bar orientation ,0: horizontal, 1 :vertical
     */
    private int orientation;

    protected abstract void startDraw(Canvas canvas);

    protected Paint initSmoothPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        return paint;
    }

    protected void init() {
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder = getHolder();
        holder.addCallback(this);
        mainPaint = initSmoothPaint();
        textPaint = initSmoothPaint();
        linePaint = initSmoothPaint();
        runPaint = initSmoothPaint();
        linePaint.setStrokeWidth(10);
        if (config != null && config.getBeanList() != null) {
            iconRectArray = new Rect[config.getBeanList().size()];
            connectLineArray = new float[config.getBeanList().size() - 1][4];
            setPaintTextSize();
            checkStartPosition();
        }
    }

    protected void checkStartPosition() {
        for (StepBarBean bean : config.getBeanList()) {
            if (bean.getState() == StepBarConfig.StepSate.RUNNING) {
                position = config.getBeanList().indexOf(bean);
                break;
            }
        }
        if (position < 0)
            position = 0;
    }

    protected void setPaintTextSize() {
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, config.getTextSize(), getResources().getDisplayMetrics());
        textPaint.setTextSize(textSize);
    }

    protected int[] measureFontSize(String text, float size) {
        int[] fontSize = new int[2];
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(size);
        paint.getTextBounds(text, 0, text.length(), rect);
        fontSize[0] = rect.right - rect.left;
        fontSize[1] = rect.bottom - rect.top;
        return fontSize;
    }

    protected int[] measureFontSize(String text) {
        return measureFontSize(text, textSize);
    }

    protected int[] measureFontSize() {
        return measureFontSize("测试");
    }

    protected void stopRingAnimator() {
        post(new Runnable() {
            @Override
            public void run() {
                if (ringAnimator != null) {
                    ringAnimator.cancelAnimator();
                    ringAnimator = null;
                }
            }
        });
    }

    protected void startRingAnimator() {
        if (config.getOutSideIconRingCallback() == null)
            post(new Runnable() {
                @Override
                public void run() {
                    initOutSideRingAnimator();
                }
            });
    }

    protected void initOutSideRingAnimator() {
        ringAnimator = new StepBarAnimator();
        Path path = new Path();
        Point point = centerPointList.get(position);
        path.addCircle(point.x, point.y, outsideIconRingRadius, Path.Direction.CW);
        ringAnimator.createAnimator(path, 800, ValueAnimator.INFINITE);
    }

    /**
     * draw default outside icon ring dynamic effect
     */
    protected void drawDefaultRingAnimator(Canvas canvas, StepBarBean bean) {
        ringAnimator.mPath.reset();
        ringAnimator.mPath.lineTo(0, 0);
        float stop = ringAnimator.pathLength * ringAnimator.animatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(ringAnimator.animatorValue - 0.5)) * ringAnimator.pathLength));
        ringAnimator.pathMeasure.getSegment(start, stop, ringAnimator.mPath, true);
        runPaint.setStrokeWidth(config.getOutsideIconRingWidth());
        runPaint.setStyle(Paint.Style.STROKE);
        runPaint.setColor(bean.getOutsideIconRingColor());
        canvas.drawPath(ringAnimator.mPath, runPaint);
    }

    /**
     * select right circle radius
     * the length of a connecting line is at least a outside circle radius
     */
    protected void selectRightRadius(int width, int height, int beanSize) {
        if (config.getIconCircleRadius() > 0) {
            iconRadius = config.getIconCircleRadius();
            outsideIconRingRadius = iconRadius + config.getOutsideIconRingWidth();
        } else {
            // automatically resized
            outsideIconRingRadius = (orientation == 0 ? width : height) / (beanSize * 3.0f + 1);
            iconRadius = outsideIconRingRadius - config.getOutsideIconRingWidth();
        }
        middleMargin = iconRadius;
        if (orientation == 0) {//horizontal
            paddingTop = paddingBottom = (height - outsideIconRingRadius * 2 - measureFontSize()[1] - middleMargin) / 2.0f;
            connectLineLength = paddingLeft = paddingRight = outsideIconRingRadius;
        } else {//vertical
            // TODO: 2018/1/1
        }
    }

    /**
     * update connect line ,adjust the outside icon ring position
     */
    protected void adjustConnectLineLength() {
        for (int index = 0, count = config.getBeanList().size(); index < count; index++) {
            Point point = centerPointList.get(index);
            if (index < count - 1) {
                float startX, endX;
                if (config.getBeanList().get(index).getState() == StepBarConfig.StepSate.RUNNING) {
                    startX = (orientation == 0 ? point.x : point.y) + outsideIconRingRadius;
                } else {
                    startX = (orientation == 0 ? point.x : point.y) + iconRadius;
                }
                if (config.getBeanList().get(index + 1).getState() == StepBarConfig.StepSate.RUNNING) {
                    endX = (orientation == 0 ? point.x : point.y) + outsideIconRingRadius + connectLineLength;
                } else {
                    endX = (orientation == 0 ? point.x : point.y) + iconRadius + 2 * config.getOutsideIconRingWidth() + connectLineLength;
                }
                connectLineArray[index][0] = startX;
                connectLineArray[index][1] = orientation == 1 ? point.x : point.y;
                connectLineArray[index][2] = endX;
                connectLineArray[index][3] = orientation == 1 ? point.x : point.y;
            }
        }
    }

    /**
     * adjust text font size，avoid the overlay of the text
     */
    protected void adjustFontSize() {
        String maxCountText = "";
        if (config != null && config.getBeanList() != null) {
            // select max count of text
            for (StepBarBean bean : config.getBeanList()) {
                String running_text = bean.getRunningText();
                String waiting_text = bean.getWaitingText();
                String completed_text = bean.getCompletedText();
                String failed_text = bean.getFailedText();
                maxCountText = running_text.length() > waiting_text.length() ? running_text : waiting_text;
                maxCountText = maxCountText.length() > completed_text.length() ? maxCountText : completed_text;
                maxCountText = maxCountText.length() > failed_text.length() ? maxCountText : failed_text;
            }
        }
        Log.e(TAG, "text font size adjust start :" + textSize + "==max count text :" + maxCountText);
        int maxTextCountSize = measureFontSize(maxCountText)[orientation];
        while (maxTextCountSize > 2.8 * outsideIconRingRadius) {
            textSize -= 0.2f;
            config.setTextSize(textSize);
            setPaintTextSize();
            maxTextCountSize = measureFontSize(maxCountText)[orientation];
        }
        Log.e(TAG, "text font size adjust complete :" + textSize);
    }


    public void addConfig(StepBarConfig config) {
        this.config = config;
        init();
        requestLayout();
    }

    public interface SlideCallback {

        /**
         * slide view
         */
        void slide(int x, int y,float radius);
    }

    private SlideCallback slideCallback;

    public void addSlideCallback(SlideCallback slideCallback) {
        this.slideCallback = slideCallback;
    }

    public StepBarViewIndicator(Context context, int orientation) {
        super(context);
        this.orientation = orientation <= 0 ? 0 : 1;
        init();
    }

    public StepBarViewIndicator(Context context, AttributeSet attrs, int orientation) {
        super(context, attrs);
        this.orientation = orientation <= 0 ? 0 : 1;
        init();
    }

    public StepBarViewIndicator(Context context, AttributeSet attrs, int defStyleAttr, int orientation) {
        super(context, attrs, defStyleAttr);
        this.orientation = orientation <= 0 ? 0 : 1;
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDraw = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDraw = false;
    }

    @Override
    public void run() {
        if (config != null) {
            if (config.getShowState() == StepBarConfig.StepShowState.DYNAMIC) {
                startRingAnimator();
                while (isDraw) {
                    if (config.getStepCallback() != null) {
                        if (config.getStepCallback().step(config, position)) {
                            updatePosition();
                            adjustConnectLineLength();
                            stopRingAnimator();
                            startRingAnimator();
                        } else {
                            //the current position icon state is no longer the running state,
                            //set static display，not refresh all the time
                            if (config.getBeanList().get(position).getState() != StepBarConfig.StepSate.RUNNING) {
                                isDraw = false;
                                config.setShowState(StepBarConfig.StepShowState.STATIC);
                                break;
                            }
                        }
                    }
                    drawStepBar();
                    if (slideCallback != null) {
                        Point point = centerPointList.get(position);
                        slideCallback.slide(point.x, point.y,outsideIconRingRadius);
                    }
                }
            }
        }
        stopRingAnimator();
        drawStepBar();
    }

    private void updatePosition() {
        config.getBeanList().get(position).setState(StepBarConfig.StepSate.COMPLETED);
        if (position == config.getBeanList().size() - 1) {//completed
            config.setShowState(StepBarConfig.StepShowState.STATIC);
            stopRingAnimator();
            isDraw = false;
        } else {
            //open the next step
            position += 1;
            config.getBeanList().get(position).setState(StepBarConfig.StepSate.RUNNING);
        }
    }

    private void drawStepBar() {
        try {
            canvas = holder.lockCanvas();
            if (canvas == null)
                return;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            startDraw(canvas);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }
}
