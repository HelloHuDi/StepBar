package com.hd.stepbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by hd on 2017/12/29 .
 * set the state of each step
 */
public class StepBarBean {
    /**
     * 失败后的图标
     */
    private Drawable failedIcon;
    /**
     * 等待状态下的图标
     */
    private Drawable waitingIcon;
    /**
     * 当前执行状态下的图标
     */
    private Drawable runningIcon;
    /**
     * 完成后的图标
     */
    private Drawable completedIcon;
    /**
     * 连接线颜色
     */
    private int connectLineColor = Color.WHITE;
    /**
     * 当前执行状态下文本颜色
     */
    private int runningTextColor = Color.YELLOW;
    /**
     * 等待状态下文本颜色
     */
    private int waitingTextColor = Color.DKGRAY;
    /**
     * 完成后文本颜色
     */
    private int completedTextColor = Color.GREEN;
    /**
     * 失败后文本颜色
     */
    private int failedTextColor = Color.RED;
    /**
     * 图标外环光圈前景颜色
     */
    private int outsideIconRingForegroundColor = Color.YELLOW;
    /**
     * 图标外环光圈背景颜色
     */
    private int outsideIconRingBackgroundColor = Color.WHITE;
    /**
     * 当前执行状态下文本内容
     */
    private String runningText;
    /**
     * 等待状态下文本内容
     */
    private String waitingText;
    /**
     * 完成后文本内容，若未设置则保持于正常状态下文本内容一致
     */
    private String completedText;
    /**
     * 失败后文本内容，若未设置则保持于正常状态下文本内容一致
     */
    private String failedText;
    /**
     * 若文本不需要跟随状态发生变化，则可以直接设置该值
     */
    private String text;
    /**
     * 设置进行状态,默认状态下为等待状态
     */
    private StepBarConfig.StepSate state = StepBarConfig.StepSate.WAITING;

    public StepBarBean(Context context) {
        failedIcon = ContextCompat.getDrawable(context, R.drawable.failed_icon);
        waitingIcon = ContextCompat.getDrawable(context, R.drawable.waiting_icon);
        runningIcon = ContextCompat.getDrawable(context, R.drawable.running_icon);
        completedIcon = ContextCompat.getDrawable(context, R.drawable.completed_icon);
    }

    public Drawable getFailedIcon() {
        return failedIcon;
    }

    public void setFailedIcon(Drawable failedIcon) {
        this.failedIcon = failedIcon;
    }

    public Drawable getWaitingIcon() {
        return waitingIcon;
    }

    public void setWaitingIcon(Drawable waitingIcon) {
        this.waitingIcon = waitingIcon;
    }

    public Drawable getRunningIcon() {
        return runningIcon;
    }

    public void setRunningIcon(Drawable runningIcon) {
        this.runningIcon = runningIcon;
    }

    public Drawable getCompletedIcon() {
        return completedIcon;
    }

    public void setCompletedIcon(Drawable completedIcon) {
        this.completedIcon = completedIcon;
    }

    public int getConnectLineColor() {
        return connectLineColor;
    }

    public void setConnectLineColor(int connectLineColor) {
        this.connectLineColor = connectLineColor;
    }

    public int getRunningTextColor() {
        return runningTextColor;
    }

    public void setRunningTextColor(int runningTextColor) {
        this.runningTextColor = runningTextColor;
    }

    public int getWaitingTextColor() {
        return waitingTextColor;
    }

    public void setWaitingTextColor(int waitingTextColor) {
        this.waitingTextColor = waitingTextColor;
    }

    public int getCompletedTextColor() {
        return completedTextColor;
    }

    public void setCompletedTextColor(int completedTextColor) {
        this.completedTextColor = completedTextColor;
    }

    public int getFailedTextColor() {
        return failedTextColor;
    }

    public void setFailedTextColor(int failedTextColor) {
        this.failedTextColor = failedTextColor;
    }

    public int getOutsideIconRingForegroundColor() {
        return outsideIconRingForegroundColor;
    }

    public void setOutsideIconRingForegroundColor(int outsideIconRingForegroundColor) {
        this.outsideIconRingForegroundColor = outsideIconRingForegroundColor;
    }

    public int getOutsideIconRingBackgroundColor() {
        return outsideIconRingBackgroundColor;
    }

    public void setOutsideIconRingBackgroundColor(int outsideIconRingBackgroundColor) {
        this.outsideIconRingBackgroundColor = outsideIconRingBackgroundColor;
    }

    public String getRunningText() {
        return runningText;
    }

    public void setRunningText(String runningText) {
        this.runningText = runningText;
    }

    public String getWaitingText() {
        return waitingText;
    }

    public void setWaitingText(String waitingText) {
        this.waitingText = waitingText;
    }

    public String getCompletedText() {
        return completedText;
    }

    public void setCompletedText(String completedText) {
        this.completedText = completedText;
    }

    public String getFailedText() {
        return failedText;
    }

    public void setFailedText(String failedText) {
        this.failedText = failedText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StepBarConfig.StepSate getState() {
        return state;
    }

    public void setState(StepBarConfig.StepSate state) {
        this.state = state;
    }

    public static class Builder {

        private StepBarBean bean;

        public Builder(Context context) {
            bean = new StepBarBean(context.getApplicationContext());
        }

        public Builder setFailedIcon(Drawable failedIcon) {
            bean.failedIcon = failedIcon;
            return this;
        }

        public Builder setWaitingIcon(Drawable waitingIcon) {
            bean.waitingIcon = waitingIcon;
            return this;
        }

        public Builder setRunningIcon(Drawable runningIcon) {
            bean.runningIcon = runningIcon;
            return this;
        }

        public Builder setCompletedIcon(Drawable completedIcon) {
            bean.completedIcon = completedIcon;
            return this;
        }

        public Builder setConnectLineColor(int connectLineColor) {
            bean.connectLineColor = connectLineColor;
            return this;
        }

        public Builder setRunningTextColor(int runningTextColor) {
            bean.runningTextColor = runningTextColor;
            return this;
        }

        public Builder setWaitingTextColor(int waitingTextColor) {
            bean.waitingTextColor = waitingTextColor;
            return this;
        }
        public Builder setCompletedTextColor(int completedTextColor) {
            bean.completedTextColor = completedTextColor;
            return this;
        }

        public Builder setFailedTextColor(int failedTextColor) {
            bean.failedTextColor = failedTextColor;
            return this;
        }

        public Builder setOutsideIconRingForegroundColor(int outsideIconRingForegroundColor) {
            bean.outsideIconRingForegroundColor = outsideIconRingForegroundColor;
            return this;
        }

        public Builder setOutsideIconRingBackgroundColor(int outsideIconRingBackgroundColor) {
            bean.outsideIconRingBackgroundColor = outsideIconRingBackgroundColor;
            return this;
        }

        public Builder setRunningText(String runningText) {
            bean.runningText = runningText;
            return this;
        }

        public Builder setWaitingText(String waitingText) {
            bean.waitingText = waitingText;
            return this;
        }

        public Builder setCompletedText(String completedText) {
            bean.completedText = completedText;
            return this;
        }

        public Builder setFailedText(String failedText) {
            bean.failedText = failedText;
            return this;
        }

        public Builder setText(String text) {
            bean.text = text;
            return this;
        }

        public Builder setState(StepBarConfig.StepSate state) {
            bean.state = state;
            return this;
        }

        public StepBarBean build() {
            return bean;
        }
    }
}
