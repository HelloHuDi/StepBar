package com.hd.step;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

    protected LinkedList<StepBarBean> createShortTextBean(int count, int runningPosition) {
        return getStepBarBeans(count, runningPosition, new String[]{"完成任务", "执行任务", "等待任务", "任务失败了啊"});
    }

    protected LinkedList<StepBarBean> createLongTextBean(int count, int runningPosition) {
        return getStepBarBeans(count, runningPosition, new String[]{
                "楼下超市有个妹子，长得很漂亮。前天为了要她微信就买了几百的吃的，结账时假装没带钱要求加个微信转账。然后她头也不抬的指着柜台的二维码，上面写着：不加好友也能支付！",//
                "金钱不能买到一切但能买到我，暴力不能解决一切但能解决你",
                "这冬天嘴唇容易干裂，交代老妈上超市时帮买支润唇膏，涂抹了快两个月，刚发现是一只固体胶。。",//
                "酒天天哼着小曲，结果变成了曲酒"});
    }

    @NonNull
    private LinkedList<StepBarBean> getStepBarBeans(int count, int runningPosition, String[] texts) {
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
            bean.setCompletedText(texts[0]);
            bean.setRunningText(texts[1]);
            bean.setWaitingText(texts[2]);
            bean.setFailedText(texts[3]);
            beanLinkedList.add(bean);
            a++;
        }
        return beanLinkedList;
    }

}
