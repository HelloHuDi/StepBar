package com.hd.stepbar.indicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hd.stepbar.R;
import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;
import com.hd.stepbar.animator.StepBarAnimator;

import java.util.LinkedList;


/**
 * Created by hd on 2017/12/30 .
 * step bar
 */
public abstract class StepBarViewIndicator extends VIndicator {

    private String TAG = StepBarViewIndicator.class.getSimpleName();

    protected StepBarConfig config;

    protected Paint mainPaint, runPaint;

    protected float iconRadius, outsideIconRingRadius;

    protected float textSize;

    protected float availableTextWidth;

    protected float paddingRight, paddingLeft, paddingTop, paddingBottom;

    protected float middleMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 10, getResources().getDisplayMetrics());

    protected float connectLineLength;

    /**
     * all icon center point position
     */
    protected LinkedList<Point> centerPointList = new LinkedList<>();

    /**
     * all icon rect position
     */
    protected Rect[] iconRectArray;

    /**
     * all text rect position
     */
    protected Rect[] textRectArray;

    /**
     * all text drawable position
     */
    protected Drawable[] textDrawableArray;

    /**
     * all icon drawable position
     */
    protected Drawable[] iconDrawableArray;

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

    /**
     * step bar orientation ,0: horizontal, 1 :vertical
     */
    private int orientation;

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
        mainPaint = initSmoothPaint();
        runPaint = initSmoothPaint();
        if (config != null && config.getBeanList() != null) {
            iconRectArray = new Rect[config.getBeanList().size()];
            textRectArray = new Rect[config.getBeanList().size()];
            textDrawableArray = new Drawable[config.getBeanList().size()];
            iconDrawableArray = new Drawable[config.getBeanList().size()];
            connectLineArray = new float[config.getBeanList().size() - 1][4];
            setPaintTextSize();
            checkStartPosition();
            startRingAnimator();
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
    }

    protected float[] measureFontSize(String text, float size) {
        float[] fontSize = new float[2];
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(size);
        paint.getTextBounds(text, 0, text.length(), rect);
        fontSize[0] = rect.right - rect.left;
        fontSize[1] = rect.bottom - rect.top;
        return fontSize;
    }

    protected float[] measureFontSize(String text) {
        return measureFontSize(text, textSize);
    }

    protected float[] measureFontSize() {
        return measureFontSize(getResources().getString(R.string.test_text));
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
     * start draw step bar
     */
    protected void startDraw(Canvas canvas) {
        for (int index = 0, count = centerPointList.size(); index < count; index++) {
            Point point = centerPointList.get(index);
            StepBarBean bean = config.getBeanList().get(index);
            drawIconAndText(index, canvas);
            drawConnectLine(canvas, index, bean);
            drawRing(canvas, point, bean);
        }
    }

    /**
     * draw icon and text
     */
    protected void drawIconAndText(int index, Canvas canvas) {
        //draw icon
        Drawable drawable = iconDrawableArray[index];
        Rect bounds = iconRectArray[index];
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        //draw text
        drawable = textDrawableArray[index];
        bounds = textRectArray[index];
        drawable.setBounds(bounds);
        drawable.draw(canvas);
    }

    /**
     * draw connect background line
     */
    protected void drawConnectLine(Canvas canvas, int index, StepBarBean bean) {
        mainPaint.setColor(bean.getConnectLineColor());
        if (index < centerPointList.size() - 1) {
            canvas.drawLine(connectLineArray[index][0], connectLineArray[index][1], //
                            connectLineArray[index][2], connectLineArray[index][3], mainPaint);
        }
    }

    /**
     * draw outside icon ring
     */
    protected void drawRing(Canvas canvas, Point point, StepBarBean bean) {
        if (config.getShowState() == StepBarConfig.StepShowState.DYNAMIC) {
            //draw dynamic outside ring
            if (bean.getState() == StepBarConfig.StepSate.RUNNING && outsideIconRingRadius > iconRadius) {
                if (config.getOutSideIconRingCallback() != null) {
                    config.getOutSideIconRingCallback().drawRing(config, canvas, point, outsideIconRingRadius, position);
                } else if (ringAnimator != null) {
                    drawDefaultRingAnimatorEffect(canvas, point, bean);
                }
            }
        } else {//static state
            //draw static outside ring
            if (bean.getState() == StepBarConfig.StepSate.RUNNING && outsideIconRingRadius > iconRadius) {
                if (config.getOutSideIconRingCallback() != null) {
                    config.getOutSideIconRingCallback().drawRing(config, canvas, point, outsideIconRingRadius, position);
                } else {
                    drawBackgroundRing(canvas, point, bean);
                }
            }
        }
    }

    /**
     * draw default outside icon ring dynamic effect
     */
    protected void drawDefaultRingAnimatorEffect(Canvas canvas, Point point, StepBarBean bean) {
        drawBackgroundRing(canvas, point, bean);
        drawForegroundRing(canvas, bean);
    }

    /**
     * draw ring with background color
     */
    private void drawBackgroundRing(Canvas canvas, Point point, StepBarBean bean) {
        runPaint.setStrokeWidth(config.getOutsideIconRingWidth());
        runPaint.setStyle(Paint.Style.STROKE);
        runPaint.setColor(bean.getOutsideIconRingBackgroundColor());
        canvas.drawCircle(point.x, point.y, outsideIconRingRadius, runPaint);
    }

    /**
     * draw ring with foreground color
     */
    private void drawForegroundRing(Canvas canvas, StepBarBean bean) {
        ringAnimator.mPath.reset();
        ringAnimator.mPath.lineTo(0, 0);
        float stop = ringAnimator.pathLength * ringAnimator.animatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(ringAnimator.animatorValue - 0.5)) * ringAnimator.pathLength));
        ringAnimator.pathMeasure.getSegment(start, stop, ringAnimator.mPath, true);
        runPaint.setStrokeWidth(config.getOutsideIconRingWidth());
        runPaint.setStyle(Paint.Style.STROKE);
        runPaint.setColor(bean.getOutsideIconRingForegroundColor());
        canvas.drawPath(ringAnimator.mPath, runPaint);
    }

    /**
     * select right circle radius
     * the length of a connecting line is at least a outside circle radius
     */
    protected void selectRightRadius(int width, int height, int beanSize) {
        if (config.getIconCircleRadius() > 0) {
            iconRadius = config.getIconCircleRadius();
            adjustRingWidth(iconRadius);
            outsideIconRingRadius = iconRadius + config.getOutsideIconRingWidth();
        } else {
            // automatically resized
            outsideIconRingRadius = (orientation == 0 ? width : height) / ((beanSize * 3.0f + 1.0f));
            adjustRingWidth(outsideIconRingRadius);
            iconRadius = outsideIconRingRadius - config.getOutsideIconRingWidth();
        }
        middleMargin = outsideIconRingRadius / 2.0f;
        if (orientation == 0) {//horizontal
            paddingTop = paddingBottom = (height - outsideIconRingRadius * 2 - measureFontSize()[1] - middleMargin) / 2.0f;
            connectLineLength = paddingLeft = paddingRight = outsideIconRingRadius;
            availableTextWidth = 3 * outsideIconRingRadius;
        } else {//vertical
            connectLineLength = paddingTop = paddingBottom = outsideIconRingRadius;
            paddingLeft = paddingRight = outsideIconRingRadius / 2.0f;
            availableTextWidth = width - paddingLeft - paddingRight - getPaddingLeft() - //
                    getPaddingRight() - middleMargin - outsideIconRingRadius * 2;
        }
    }

    /**
     * adjust ring width size automatically
     */
    private void adjustRingWidth(float size) {
        if (config.getOutsideIconRingWidth() < 0) {
            config.setOutsideIconRingWidth(size * 0.08f);
        }
    }

    /**
     * adjust text font size，avoid the overlay of the text
     */
    protected void adjustFontSize() {
        adjustFontSize(getMaxCountText());
    }

    protected void adjustFontSize(String maxCountText) {
        Log.e(TAG, "text font size adjust start :" + textSize + "==max count text :" + maxCountText);
        float maxTextCountSize = measureFontSize(maxCountText)[orientation];
        while (maxTextCountSize > 2.8 * outsideIconRingRadius) {
            textSize -= 0.2f;
            config.setTextSize(textSize);
            setPaintTextSize();
            maxTextCountSize = measureFontSize(maxCountText)[orientation];
        }
        Log.e(TAG, "text font size adjust complete :" + textSize);
    }

    @NonNull
    protected String getMaxCountText() {
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
        return maxCountText;
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
        void slide(int iconCenterX, int iconCenterY, float radius);
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
    public boolean isStatic() {
        return config != null && config.getShowState() == StepBarConfig.StepShowState.STATIC;
    }

    @Override
    public void drawStepBar(Canvas canvas) {
        if (!isStatic()) {
            if (config.getStepCallback() != null) {
                if (config.getStepCallback().step(config, position)) {
                    updatePosition();
                    updateCurrentData();
                    stopRingAnimator();
                    startRingAnimator();
                    slideView();
                } else {
                    //the current position icon state is no longer the running state,
                    //set static display，not refresh all the time
                    if (config.getBeanList().get(position).getState() != StepBarConfig.StepSate.RUNNING) {
                        config.setShowState(StepBarConfig.StepShowState.STATIC);
                        updateCurrentData();
                        slideView();
                    }
                }
            }
            startDraw(canvas);
        } else {
            stopRingAnimator();
            startDraw(canvas);
        }
    }

    private void slideView() {
        if (slideCallback != null) {
            Point point = centerPointList.get(position);
            slideCallback.slide(point.x, point.y, outsideIconRingRadius);
        }
    }

    private void updatePosition() {
        config.getBeanList().get(position).setState(StepBarConfig.StepSate.COMPLETED);
        if (position == config.getBeanList().size() - 1) {//completed
            config.setShowState(StepBarConfig.StepShowState.STATIC);
            stopRingAnimator();
        } else {
            //open the next step
            position += 1;
            config.getBeanList().get(position).setState(StepBarConfig.StepSate.RUNNING);
        }
    }

    protected void updateCurrentData() {
        clearReference();
        for (int index = 0, count = config.getBeanList().size(); index < count; index++) {
            Point point = centerPointList.get(index);
            adjustConnectLineLength(index, count, point);
            updateTextRectAndDrawableData(index, point, config.getBeanList().get(index));
        }
    }

    private void clearReference() {
        if (textDrawableArray[0] != null) {
            for (int index = 0; index > textDrawableArray.length; index++) {
                textDrawableArray[index].setCallback(null);
                iconDrawableArray[index].setCallback(null);
                textRectArray[index] = null;
                iconRectArray[index] = null;
                connectLineArray[index] = null;
            }
            textView.destroyDrawingCache();
            relativeLayout.destroyDrawingCache();
            if (linearLayout != null) {
                linearLayout.destroyDrawingCache();
            }
            textView = null;
            relativeLayout = null;
            linearLayout = null;
        }
    }

    /**
     * update connect line ,adjust the outside icon ring position
     */
    private void adjustConnectLineLength(int index, int count, Point point) {
        if (index < count - 1) {
            float startX, startY, endX, endY;
            if (orientation == 0) {//horizontal
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
                startY = endY = point.y;
            } else {//vertical
                if (config.getBeanList().get(index).getState() == StepBarConfig.StepSate.RUNNING) {
                    startY = point.y + outsideIconRingRadius;
                } else {
                    startY = point.y + iconRadius;
                }
                if (config.getBeanList().get(index + 1).getState() == StepBarConfig.StepSate.RUNNING) {
                    endY = point.y + outsideIconRingRadius + connectLineLength;
                } else {
                    endY = point.y + iconRadius + 2 * config.getOutsideIconRingWidth() + connectLineLength;
                }
                startX = endX = point.x;
            }
            connectLineArray[index][0] = startX;
            connectLineArray[index][1] = startY;
            connectLineArray[index][2] = endX;
            connectLineArray[index][3] = endY;
        }
    }

    private void updateTextRectAndDrawableData(int index, Point point, StepBarBean bean) {
        Drawable icon;
        String text;
        int textColor;
        switch (bean.getState()) {
            case RUNNING:
                icon = bean.getRunningIcon();
                text = bean.getRunningText();
                textColor = bean.getRunningTextColor();
                break;
            case WAITING:
                icon = bean.getWaitingIcon();
                text = bean.getWaitingText();
                textColor = bean.getWaitingTextColor();
                break;
            case COMPLETED:
                icon = bean.getCompletedIcon();
                text = bean.getCompletedText();
                textColor = bean.getCompletedTextColor();
                break;
            case FAILED:
                icon = bean.getFailedIcon();
                text = bean.getFailedText();
                textColor = bean.getFailedTextColor();
                break;
            default:
                icon = bean.getWaitingIcon();
                text = "";
                textColor = bean.getWaitingTextColor();
                break;
        }
        iconDrawableArray[index] = icon;
        initTextContainer();
        float[] textSizes = measureFontSize(text);
        float textWidth = textSizes[0];
        float textHeight = textSizes[1];
        Rect rect;
        if (orientation == 0) {//horizontal
            if (config.getTextLocation() == StepBarConfig.StepTextLocation.TOP) {
                rect = new Rect((int) (point.x - textWidth / 2), (int) paddingTop,//
                                (int) (point.x + textWidth / 2), (int) (paddingTop+textHeight));
            } else {
                rect = new Rect((int) (point.x - textWidth / 2), (int) (point.y + outsideIconRingRadius + middleMargin),//
                                (int) (point.x + textWidth / 2), (int) (point.y + outsideIconRingRadius + textHeight + middleMargin));
            }
        } else {//vertical
            if(config.getTextLocation()== StepBarConfig.StepTextLocation.LEFT){
                rect = new Rect((int)paddingLeft,(int) (point.y - outsideIconRingRadius - outsideIconRingRadius / 3.0f),//
                                (int) (paddingLeft+availableTextWidth),//
                                (int) (point.y + outsideIconRingRadius + outsideIconRingRadius / 3.0f));
            }else {
                rect = new Rect(
                        (int) (point.x + outsideIconRingRadius + middleMargin), //
                        (int) (point.y - outsideIconRingRadius - outsideIconRingRadius / 3.0f),//
                        (int) (getMeasuredWidth() - getPaddingRight() - paddingRight),//
                        (int) (point.y + outsideIconRingRadius + outsideIconRingRadius / 3.0f));
            }
        }
        if (config.isShowTextBold()) {
            if (bean.getState() == StepBarConfig.StepSate.RUNNING) {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        } else {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        textView.setText(text);
        textView.setTextColor(textColor);
        relativeLayout.addView(textView);
        if (linearLayout != null) {
            linearLayout.addView(relativeLayout);
        }
        Drawable drawable = convertViewToDrawable(orientation == 1 ? linearLayout : relativeLayout);
        textDrawableArray[index] = drawable;
        textRectArray[index] = rect;
        drawable.setCallback(null);
    }

    /**
     * textView convert drawable
     */
    private Drawable convertViewToDrawable(View view) {
        view.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), //
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return new BitmapDrawable(null, view.getDrawingCache());
    }

    private LinearLayout linearLayout = null;
    private RelativeLayout relativeLayout;
    private TextView textView;

    protected void initTextContainer() {
        relativeLayout = new RelativeLayout(getContext());
        textView = new TextView(getContext());
        if (orientation == 0) {
            relativeLayout.setGravity(Gravity.CENTER);
        } else {
            relativeLayout.setGravity(Gravity.START | Gravity.CENTER);
        }
        if (orientation == 0) {
            textView.setGravity(Gravity.CENTER);
            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams((int) availableTextWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));
        } else if (orientation == 1) {
            textView.setGravity(Gravity.START | Gravity.CENTER);
            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                    (int) (availableTextWidth),//
                    (int) (3 * outsideIconRingRadius + 2 * (outsideIconRingRadius / 3.0f))));
            linearLayout = new LinearLayout(getContext());
            linearLayout.setGravity(Gravity.START | Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        textView.setSingleLine(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

}
