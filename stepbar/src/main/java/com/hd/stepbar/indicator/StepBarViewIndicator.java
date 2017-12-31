package com.hd.stepbar.indicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;

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

    protected int iconRadius, outsideIconRingRadius;

    protected float textSize;

    protected int paddingRight, paddingLeft;

    protected int paddingTop, paddingBottom;

    protected int middleMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

    protected int connectLineLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

    protected LinkedList<Point> centerPointList = new LinkedList<>();

    protected Rect[] iconRectArray;

    protected float[][] connectLineArray;

    protected StepBarConfig.StepCallback stepCallback;

    protected int position = -1;

    private Canvas canvas;

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
            connectLineArray = new float[config.getBeanList().size()-1][4];
            stepCallback = config.getStepCallback();
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

    protected PathMeasure pathMeasure;

    protected float animatorValue = 0.5f;

    protected float pathLength = 0;

    protected ValueAnimator valueAnimator = null;

    protected Path mDst;

    protected void resetAnimator() {
        if (config.getOutSideIconRingCallback() == null)
            post(new Runnable() {
                @Override
                public void run() {
                    if (valueAnimator != null)
                        valueAnimator.cancel();
                    valueAnimator = null;
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

    protected void setDefaultRingAnimator(Canvas canvas, StepBarBean bean) {
        mDst.reset();
        mDst.lineTo(0, 0);
        float stop = pathLength * animatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(animatorValue - 0.5)) * pathLength));
        pathMeasure.getSegment(start, stop, mDst, true);
        runPaint.setStrokeWidth(config.getOutsideIconRingWidth());
        runPaint.setStyle(Paint.Style.STROKE);
        runPaint.setColor(bean.getOutsideIconRingColor());
        canvas.drawPath(mDst, runPaint);
    }

    protected void initOutSideRingAnimator() {
        mDst = new Path();
        Path path = new Path();
        Point point = centerPointList.get(position);
        path.addCircle(point.x, point.y, outsideIconRingRadius, Path.Direction.CW);
        initAnimator(path,800,ValueAnimator.INFINITE);
    }

    protected void initSwitchConnectLineAnimator(){

    }

    protected void initAnimator(Path path,long duration,int repeatCount){
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

    public void addConfig(StepBarConfig config) {
        this.config = config;
        init();
        requestLayout();
    }

    public StepBarViewIndicator(Context context) {
        super(context);
        init();
    }

    public StepBarViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepBarViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                    if (stepCallback != null) {
                        if (stepCallback.step(config, position)) {
                            updatePosition();
                            resetAnimator();
                            startRingAnimator();
                        } else {
                            //the current position icon state is no longer the running state,
                            //set static display，not refresh all the time
                            if (config.getBeanList().get(position).getState() != StepBarConfig.StepSate.RUNNING) {
                                config.setShowState(StepBarConfig.StepShowState.STATIC);
                                break;
                            }
                        }
                    }
                    drawStepBar();
                }
            }
        }
        resetAnimator();
        drawStepBar();
    }

    private void updatePosition() {
        config.getBeanList().get(position).setState(StepBarConfig.StepSate.COMPLETED);
        if (position == config.getBeanList().size() - 1) {//completed
            config.setShowState(StepBarConfig.StepShowState.STATIC);
            resetAnimator();
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
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            startDraw(canvas);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }
}
