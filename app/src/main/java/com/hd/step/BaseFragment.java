package com.hd.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;

import java.util.LinkedList;


/**
 * Created by hd on 2018/1/1 .
 *
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected LinkedList<StepBarBean> createBean(int count, int runningPosition) {
        int a = 0;
        LinkedList<StepBarBean> beanLinkedList = new LinkedList<>();
        while (a < count) {
            StepBarBean bean = new StepBarBean.Builder(getActivity()).build();
            if (a == runningPosition) {
                bean.setState(StepBarConfig.StepSate.RUNNING);
            } else if (a < runningPosition) {
                bean.setState(StepBarConfig.StepSate.COMPLETED);
            } else {
                bean.setState(StepBarConfig.StepSate.WAITING);
            }
            bean.setRunningIcon(ContextCompat.getDrawable(getActivity(), R.drawable.running));
            bean.setWaitingIcon(ContextCompat.getDrawable(getActivity(), R.drawable.waiting));
            bean.setCompletedIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complete));
            bean.setFailedIcon(ContextCompat.getDrawable(getActivity(), R.drawable.fail));
            bean.setCompletedText("完成任务");
            bean.setRunningText("执行任务");
            bean.setWaitingText("等待任务");
            bean.setFailedText("任务失败了啊");
            beanLinkedList.add(bean);
            a++;
        }
        return beanLinkedList;
    }
}
