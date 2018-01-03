package com.hd.stepbar.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by hd on 2018/1/3 .
 * surfaceView indicator
 */
public abstract class SIndicator extends SurfaceView implements SurfaceHolder.Callback, Runnable, Indicator {

    private boolean isDraw;

    private SurfaceHolder holder;

    private Canvas canvas;

    public SIndicator(Context context) {
        super(context);
        init();
    }

    public SIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder = getHolder();
        holder.addCallback(this);
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
        while (isDraw && !isStatic()) {
            draw();
        }
        draw();
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            if (canvas == null)
                return;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            drawStepBar(canvas);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }
}
