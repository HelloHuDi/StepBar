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
     * 正常状态下的图标
     */
    private Drawable normalIcon;
    /**
     * 完成后的图标
     */
    private Drawable completedIcon;
    /**
     * 默认状态下连接线颜色
     */
    private int defaultConnectLineColor = Color.WHITE;
    /**
     * 切换位置状态下连接线颜色
     */
    private int switchConnectLineColor = Color.RED;
    /**
     * 从当前位置切换到下一个位置用时，单位ms，默认100ms
     */
    private int switchTime = 100;
    /**
     * 正常状态下文本颜色
     */
    private int normalTextColor = Color.BLACK;
    /**
     * 完成后文本颜色
     */
    private int completedTextColor = Color.BLACK;
    /**
     * 失败后文本颜色
     */
    private int failedTextColor = Color.RED;
    /**
     * 图标外环光圈的颜色
     */
    private int outsideIconRingColor = Color.GREEN;
    /**
     * 正常状态下文本内容
     */
    private String normalText;
    /**
     * 完成后文本内容，若未设置则保持于正常状态下文本内容一致
     */
    private String completedText;
    /**
     * 失败后文本内容，若未设置则保持于正常状态下文本内容一致
     */
    private String failedText;
    /**
     * 设置进行状态,默认状态下为等待状态
     */
    private StepBarConfig.StepSate state = StepBarConfig.StepSate.WAITING;

    public StepBarBean(Context context) {
        failedIcon = ContextCompat.getDrawable(context, R.drawable.failed_icon);
        normalIcon = ContextCompat.getDrawable(context, R.drawable.normal_icon);
        completedIcon = ContextCompat.getDrawable(context, R.drawable.completed_icon);
    }

    public Drawable getFailedIcon() {
        return failedIcon;
    }

    public void setFailedIcon(Drawable failedIcon) {
        this.failedIcon = failedIcon;
    }

    public Drawable getNormalIcon() {
        return normalIcon;
    }

    public void setNormalIcon(Drawable normalIcon) {
        this.normalIcon = normalIcon;
    }

    public Drawable getCompletedIcon() {
        return completedIcon;
    }

    public void setCompletedIcon(Drawable completedIcon) {
        this.completedIcon = completedIcon;
    }

    public int getDefaultConnectLineColor() {
        return defaultConnectLineColor;
    }

    public void setDefaultConnectLineColor(int defaultConnectLineColor) {
        this.defaultConnectLineColor = defaultConnectLineColor;
    }

    public int getSwitchConnectLineColor() {
        return switchConnectLineColor;
    }

    public void setSwitchConnectLineColor(int switchConnectLineColor) {
        this.switchConnectLineColor = switchConnectLineColor;
    }

    public int getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(int switchTime) {
        this.switchTime = switchTime;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
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

    public int getOutsideIconRingColor() {
        return outsideIconRingColor;
    }

    public void setOutsideIconRingColor(int outsideIconRingColor) {
        this.outsideIconRingColor = outsideIconRingColor;
    }

    public String getNormalText() {
        return normalText;
    }

    public void setNormalText(String normalText) {
        this.normalText = normalText;
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

        public Builder setNormalIcon(Drawable normalIcon) {
            bean.normalIcon = normalIcon;
            return this;
        }

        public Builder setCompletedIcon(Drawable completedIcon) {
            bean.completedIcon = completedIcon;
            return this;
        }

        public Builder setDefaultConnectLineColor(int defaultConnectLineColor) {
            bean.defaultConnectLineColor = defaultConnectLineColor;
            return this;
        }

        public Builder setSwitchConnectLineColor(int switchConnectLineColor) {
            bean.switchConnectLineColor = switchConnectLineColor;
            return this;
        }

        public Builder setSwitchTime(int switchTime) {
            bean.switchTime = switchTime;
            return this;
        }

        public Builder setNormalTextColor(int normalTextColor) {
            bean.normalTextColor = normalTextColor;
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

        public Builder setOutsideIconRingColor(int outsideIconRingColor) {
            bean.outsideIconRingColor = outsideIconRingColor;
            return this;
        }

        public Builder setNormalText(String normalText) {
            bean.normalText = normalText;
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

        public Builder setState(StepBarConfig.StepSate state) {
            bean.state = state;
            return this;
        }

        public StepBarBean build() {
            return bean;
        }
    }
}
