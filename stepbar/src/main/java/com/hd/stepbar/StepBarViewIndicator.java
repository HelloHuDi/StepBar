package com.hd.stepbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;


/**
 * Created by hd on 2017/12/30 .
 * step bar
 */
public abstract class StepBarViewIndicator extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private String TAG = StepBarViewIndicator.class.getSimpleName();

    protected StepBarConfig config;

    protected SurfaceHolder holder;

    protected Paint paint;

    protected boolean isDraw;

    protected int iconRadius, outsideIconRingRadius;

    protected int width, height;

    protected float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 12, getResources().getDisplayMetrics());

    protected int paddingRight=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

    protected int paddingLeft=paddingRight;

    protected int paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

    protected int paddingTop = paddingBottom;

    protected int connectLineLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

    protected LinkedList<Point> centerPointList=new LinkedList<>();

    private Canvas canvas;

    protected abstract void startDraw(Canvas canvas);

    protected Paint initSmoothPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        return paint;
    }

    protected void init() {
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder = getHolder();
        holder.addCallback(this);
        paint = initSmoothPaint();

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        isDraw = false;
        if (config != null && config.getBeanList() != null) {
            textSize=config.getTextSize();
        }
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

    public void addConfig(StepBarConfig config) {
        this.config = config;
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
        Log.d(TAG, "开始1:"+config);
        if (config != null) {
            if (config.getShowState() == StepBarConfig.StepShowState.DYNAMIC) {
                Log.d(TAG, "开始2"+isDraw);
                while (isDraw) {
                    Log.d(TAG, "开始222");
                    drawStepBar();
                }
            } else {
                Log.d(TAG, "开始3");
                drawStepBar();
            }
        }
    }

    private void drawStepBar() {
        try {
            canvas = holder.lockCanvas();
            startDraw(canvas);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }
}
