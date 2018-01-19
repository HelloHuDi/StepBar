package com.hd.stepbar;

import android.widget.LinearLayout;

import java.util.LinkedList;

/**
 * Created by hd on 2018/1/19 .
 * config adapter
 */
public class StepBarConfigAdapter {

    public void formatConfig(int orientation, StepBarConfig config) {
        //set text default show location
        formatOrientation(orientation, config);
        //set bean show state
        formatState(config);
        //set bean text
        formatText(config);
    }

    private void formatOrientation(int orientation, StepBarConfig config) {
        if (config.getTextLocation() == null) {
            if (orientation == LinearLayout.HORIZONTAL) {
                config.setTextLocation(StepBarConfig.StepTextLocation.BOTTOM);
            } else {
                config.setTextLocation(StepBarConfig.StepTextLocation.RIGHT);
            }
        }
    }

    private void formatState(StepBarConfig config) {
        LinkedList<StepBarBean> beans = config.getBeanList();
        int runningPosition = -1;
        runningPosition = setState(beans, runningPosition, false);
        setState(beans, runningPosition, true);
    }

    private int setState(LinkedList<StepBarBean> beans, int runningPosition, boolean changeState) {
        for (int index = 0, len = beans.size(); index < len; index++) {
            if (changeState) {
                if (index < runningPosition) {
                    beans.get(index).setState(StepBarConfig.StepSate.COMPLETED);
                } else if (index > runningPosition) {
                    beans.get(index).setState(StepBarConfig.StepSate.WAITING);
                }
            } else {
                StepBarConfig.StepSate stepSate = beans.get(index).getState();
                if (stepSate == StepBarConfig.StepSate.RUNNING || stepSate == StepBarConfig.StepSate.FAILED) {
                    runningPosition = index;
                    return runningPosition;
                }
            }
        }
        runningPosition = beans.size();
        return runningPosition;
    }

    private void formatText(StepBarConfig config) {
        LinkedList<StepBarBean> beans = config.getBeanList();
        String text;
        for (int index = 0, len = beans.size(); index < len; index++) {
            StepBarBean bean = beans.get(index);
            text = bean.getText();
            if (isEmpty(text)) {
                if (isEmpty(bean.getFailedText())) {
                    bean.setFailedText(getWaitingText(bean));
                }
                if (isEmpty(bean.getWaitingText())) {
                    bean.setWaitingText(getRunningText(bean));
                }
                if (isEmpty(bean.getRunningText())) {
                    bean.setRunningText(getCompletedText(bean));
                }
                if (isEmpty(bean.getCompletedText())) {
                    getCompletedText(bean);
                }
            } else {
                bean.setCompletedText(text);
                bean.setRunningText(text);
                bean.setFailedText(text);
                bean.setWaitingText(text);
            }
        }
    }

    private String getWaitingText(StepBarBean bean) {
        if (isEmpty(bean.getWaitingText())) {
            String waitText = getRunningText(bean);
            bean.setWaitingText(waitText);
            return waitText;
        } else {
            return bean.getWaitingText();
        }
    }

    private String getRunningText(StepBarBean bean) {
        if (isEmpty(bean.getRunningText())) {
            String completedText = getCompletedText(bean);
            bean.setRunningText(completedText);
            return completedText;
        } else {
            return bean.getRunningText();
        }
    }

    private String getCompletedText(StepBarBean bean) {
        if (isEmpty(bean.getCompletedText())) {
            String text = bean.getRunningText();
            if (isEmpty(text)) {
                text = bean.getWaitingText();
                if (isEmpty(text)) {
                    text = bean.getFailedText();
                }
            }
            if (isEmpty(text)) {
                throw new NullPointerException("please set StepBarBean text");
            } else {
                bean.setCompletedText(text);
            }
        }
        return bean.getCompletedText();
    }

    private boolean isEmpty(String text) {
        return text == null || text.length() <= 0;
    }


}
