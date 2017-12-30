package com.hd.stepbar;

import java.util.LinkedList;

/**
 * Created by hd on 2017/12/29 .
 * set step bar
 */
public class StepBarConfig {

    public enum StepSate {
        /**
         * 当前正在进行中的状态
         */
        RUNNING,

        /**
         * 待完成(等待)状态
         */
        WAITING,

        /**
         * 已完成
         */
        COMPLETED,

        /**
         * 失败
         */
        FAILED
    }

    private LinkedList<StepBarBean> beanList;

    /**
     * 设置文本文字大小
     */
    private int textSize;

    public StepBarConfig() {
    }

    public LinkedList<StepBarBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(LinkedList<StepBarBean> beanList) {
        this.beanList = beanList;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

}
