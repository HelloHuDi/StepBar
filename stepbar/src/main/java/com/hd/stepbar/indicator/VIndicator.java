package com.hd.stepbar.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by hd on 2018/1/3 .
 * view indicator
 */
public abstract class VIndicator extends View implements Indicator, Runnable {

    public VIndicator(Context context) {
        super(context);
        init();
    }

    public VIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (!isStatic()) {
            SystemClock.sleep(20);
            postInvalidate();
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStepBar(canvas);
    }
}
