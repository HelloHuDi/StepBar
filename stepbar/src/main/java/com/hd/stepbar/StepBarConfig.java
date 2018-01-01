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
     * customizing connection line change effect when allowing state switching
     */
    public interface ConnectLineSwitchStateCallback{
        /**
         * @param config get current bean{@link this#getBeanList()} with position,
         * and update others bean
         * @param canvas step bar view canvas
         * @param position current running position
         */
        void drawSwitchChange(StepBarConfig config, Canvas canvas, int position);
    }

    /**
     * the outside icon ring width
     */
    private int outsideIconRingWidth = 5;
    /**
     * the text size
     */
    private float textSize = 30f;
    /**
     * the icon circle radius,
     * if the value is not set, the icon will be automatically resized in the allowable range，
     * on the contrary, the icon will remain the specified size, and the view will slide
     * if the sliding direction{@link android.widget.LinearLayout#HORIZONTAL}
     * or {@link android.widget.LinearLayout#VERTICAL} is set at the same time.
     */
    private int iconCircleRadius = 0;

    private LinkedList<StepBarBean> beanList;

    private StepShowState showState = StepShowState.STATIC;

    private StepCallback callback;

    private OutSideIconRingCallback outSideIconRingCallback;

    private ConnectLineSwitchStateCallback connectLineSwitchStateCallback;

    public int getOutsideIconRingWidth() {
        return outsideIconRingWidth;
    }

    public StepBarConfig setOutsideIconRingWidth(int outsideIconRingWidth) {
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

    public int getIconCircleRadius() {
        return iconCircleRadius;
    }

    public StepBarConfig setIconCircleRadius(int iconCircleRadius) {
        this.iconCircleRadius = iconCircleRadius;
        return this;
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

    public ConnectLineSwitchStateCallback getConnectLineSwitchStateCallback() {
        return connectLineSwitchStateCallback;
    }

    public void addConnectLineSwitchStateCallback(ConnectLineSwitchStateCallback connectLineSwitchStateCallback) {
        this.connectLineSwitchStateCallback = connectLineSwitchStateCallback;
    }
}
