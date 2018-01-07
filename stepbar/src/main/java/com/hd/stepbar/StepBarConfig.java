package com.hd.stepbar;

import android.graphics.Canvas;

import java.util.LinkedList;

/**
 * Created by hd on 2017/12/29 .
 * set step bar
 */
public class StepBarConfig {

    public enum StepSate {
        /**
         * current working state
         */
        RUNNING,

        /**
         * be waiting state
         */
        WAITING,

        /**
         * be complete
         */
        COMPLETED,

        /**
         * be fail
         */
        FAILED
    }

    public enum StepShowState {

        /**
         * static show
         */
        STATIC,

        /**
         * dynamic show
         */
        DYNAMIC

    }

    /**
     * progress control
     */
    public interface StepCallback {

        /**
         * will be called all the time if set the state{@link StepShowState#DYNAMIC}
         *
         * @param config get current bean{@link this#getBeanList()} with position,
         * and update others bean
         * @param position current running position
         *
         * @return start the next step if return true
         */
        boolean step(StepBarConfig config, int position);
    }

    /**
     * allow outer ring customization
     */
    public interface OutSideIconRingCallback {

        /**
         * @param config get current bean{@link this#getBeanList()} with position,
         * and update others bean
         * @param canvas step bar view canvas
         * @param position current running position
         */
        void drawRing(StepBarConfig config, Canvas canvas, int position);
    }

    /**
     * the outside icon ring width，if the value is not set, then will be adjust automatically
     */
    private float outsideIconRingWidth = -1;
    /**
     * the text size,set the text size in pixel units
     */
    private float textSize =30f;
    /**
     * set whether to actively adjust the size of the text font.
     * when the font size {@link this#textSize} is set beyond the available range,
     * if the adjustment is allowed,then the font size is reduced until the text is fully accommodated
     */
    private boolean adjustTextSize=true;
    /**
     * the icon circle radius,
     * if the value is not set, the icon will be automatically resized in the allowable range，
     * on the contrary, the icon will remain the specified size, and the view will slide
     * if the sliding direction{@link android.widget.LinearLayout#HORIZONTAL}
     * or {@link android.widget.LinearLayout#VERTICAL} is set at the same time.
     */
    private int iconCircleRadius = 0;
    /**
     * the current running status icon exceeds the specified range to allow automatic sliding
     */
    private boolean allowAutoSlide=true;

    private LinkedList<StepBarBean> beanList;

    private StepShowState showState = StepShowState.STATIC;

    private StepCallback callback;

    private OutSideIconRingCallback outSideIconRingCallback;

    public float getOutsideIconRingWidth() {
        return outsideIconRingWidth;
    }

    public StepBarConfig setOutsideIconRingWidth(float outsideIconRingWidth) {
        this.outsideIconRingWidth = outsideIconRingWidth;
        return this;
    }

    public float getTextSize() {
        return textSize;
    }

    public StepBarConfig setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public boolean isAdjustTextSize() {
        return adjustTextSize;
    }

    public void setAdjustTextSize(boolean adjustTextSize) {
        this.adjustTextSize = adjustTextSize;
    }

    public int getIconCircleRadius() {
        return iconCircleRadius;
    }

    public StepBarConfig setIconCircleRadius(int iconCircleRadius) {
        this.iconCircleRadius = iconCircleRadius;
        return this;
    }

    public boolean isAllowAutoSlide() {
        return allowAutoSlide;
    }

    public void setAllowAutoSlide(boolean allowAutoSlide) {
        this.allowAutoSlide = allowAutoSlide;
    }

    public LinkedList<StepBarBean> getBeanList() {
        return beanList;
    }

    public StepBarConfig setBeanList(LinkedList<StepBarBean> beanList) {
        this.beanList = beanList;
        return this;
    }

    public StepShowState getShowState() {
        return showState;
    }

    public StepBarConfig setShowState(StepBarConfig.StepShowState showState) {
        this.showState = showState;
        return this;
    }

    public StepBarConfig.StepCallback getStepCallback() {
        return callback;
    }

    public StepBarConfig addStepCallback(StepBarConfig.StepCallback callback) {
        this.callback = callback;
        return this;
    }

    public StepBarConfig.OutSideIconRingCallback getOutSideIconRingCallback() {
        return outSideIconRingCallback;
    }

    public StepBarConfig addOutSideIconRingCallback(StepBarConfig.OutSideIconRingCallback outSideIconRingCallback) {
        this.outSideIconRingCallback = outSideIconRingCallback;
        return this;
    }
}
