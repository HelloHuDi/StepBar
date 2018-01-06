package com.hd.step;

import android.graphics.Color;
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
 * base fragment
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
        return getStepBarBeans(count, runningPosition, new String[]{"楼下超市有个妹子，长得很漂亮。前天为了要她微信就买了几百的吃的，结账时假装没带钱要求加个微信转账。然后她头也不抬的指着柜台的二维码，上面写着：不加好友也能支付!" ,//
                                                                    "金钱不能买到一切但能买到我，暴力不能解决一切但能解决你", //
                                                                    "这冬天嘴唇容易干裂，交代老妈上超市时帮买支润唇膏，涂抹了快两个月，刚发现是一只固体胶。。",//
                                                                    "酒天天哼着小曲，结果变成了曲酒"});
    }

    @NonNull
    private LinkedList<StepBarBean> getStepBarBeans(int count, int runningPosition, String[] texts) {
        int a = 0;
        LinkedList<StepBarBean> beanLinkedList = new LinkedList<>();
        while (a < count) {
            StepBarBean.Builder builder = new StepBarBean.Builder(getActivity());
            if (a == runningPosition) {
                builder.setState(StepBarConfig.StepSate.RUNNING);
            } else if (a < runningPosition) {
                builder.setState(StepBarConfig.StepSate.COMPLETED);
            } else {
                builder.setState(StepBarConfig.StepSate.WAITING);
            }
            builder.setRunningIcon(ContextCompat.getDrawable(getActivity(), R.drawable.running))//
                   .setWaitingIcon(ContextCompat.getDrawable(getActivity(), R.drawable.waiting))//
                   .setCompletedIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complete))//
                   .setFailedIcon(ContextCompat.getDrawable(getActivity(), R.drawable.fail))//
                   .setOutsideIconRingForegroundColor(Color.RED)//
                   .setOutsideIconRingBackgroundColor(Color.parseColor("#EEEE00"))//
                   .setConnectLineColor(Color.WHITE)//
                   .setRunningTextColor(Color.YELLOW)//
                   .setCompletedTextColor(Color.WHITE)//
                   .setWaitingTextColor(Color.LTGRAY)//
                   .setFailedTextColor(Color.RED)//
                   .setCompletedText(texts[0])//
                   .setRunningText(texts[1])//
                   .setWaitingText(texts[2])//
                   .setFailedText(texts[3]);
            StepBarBean bean = builder.build();
            beanLinkedList.add(bean);
            a++;
        }
        return beanLinkedList;
    }

}
