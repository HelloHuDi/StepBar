package com.hd.stepbar;

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

    public interface stepCallback {

        /**
         * will be called all the time if set the state{@link StepShowState#DYNAMIC}
         *
         * @param bean current running bean{@link StepBarBean}
         * @param position current running position
         *
         * @return start the next step if return true
         */
        boolean step(StepBarBean bean, int position);
    }

    /**
     * the outside icon ring width
     */
    private int outsideIconRingWidth=5;
    /**
     * the text size
     */
    private float textSize=12f;
    /**
     * the icon circle radius,
     * if the value is not set, the icon will be automatically resized in the allowable range，
     * on the contrary, the icon will remain the specified size, and the view will slide
     * if the sliding direction{@link android.widget.LinearLayout#HORIZONTAL}
     * or {@link android.widget.LinearLayout#VERTICAL} is set at the same time.
     */
    private int iconCircleRadius=0;

    private LinkedList<StepBarBean> beanList;

    private StepShowState showState=StepShowState.STATIC;

    private stepCallback callback;

    public int getOutsideIconRingWidth() {
        return outsideIconRingWidth;
    }

    public void setOutsideIconRingWidth(int outsideIconRingWidth) {
        this.outsideIconRingWidth = outsideIconRingWidth;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getIconCircleRadius() {
        return iconCircleRadius;
    }

    public void setIconCircleRadius(int iconCircleRadius) {
        this.iconCircleRadius = iconCircleRadius;
    }

    public LinkedList<StepBarBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(LinkedList<StepBarBean> beanList) {
        this.beanList = beanList;
    }

    public StepShowState getShowState() {
        return showState;
    }

    public void setShowState(StepShowState showState) {
        this.showState = showState;
    }

    public stepCallback getCallback() {
        return callback;
    }

    public void setCallback(stepCallback callback) {
        this.callback = callback;
    }
}
