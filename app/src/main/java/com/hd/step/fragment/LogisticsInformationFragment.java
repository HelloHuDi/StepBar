package com.hd.step.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hd.step.BaseFragment;
import com.hd.step.R;
import com.hd.stepbar.StepBar;
import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;

import java.util.LinkedList;


/**
 * Created by hd on 2018/1/19 .
 * 物流信息
 */
public class LogisticsInformationFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logistics_information, null);
        initStepBar((StepBar) rootView.findViewById(R.id.stepBar));
        return rootView;
    }

    private void initStepBar(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createBean(5))//
                                                  .setIconCircleRadius(40)
                                                  .setShowState(StepBarConfig.StepShowState.STATIC);
        stepBar.addConfig(config);
    }

    private String[] texts={"您已提交定单，等待系统确认"//
                            ,"您的商品需要从外地调拨，我们会尽快处理，请耐心等待"//
                            ,"您的订单已经进入亚洲第一仓储中心1号库准备出库"//
                            ,"您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中"//
                            ,"您的订单已打印完毕"//
                            ,"您的订单已拣货完成"//
                            ,"扫描员已经扫描"//
                            ,"打包成功"//
                            ,"您的订单在京东【华东外单分拣中心】发货完成，准备送往京东【北京通州分拣中心】"//
                            ,"您的订单在京东【北京通州分拣中心】分拣完成"//
                            ,"您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京中关村大厦站】"//
                            ,"您的订单在京东【北京中关村大厦站】验货完成，正在分配配送员"//
                            ,"配送员【包牙齿】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦"//
                            ,"感谢你在京东购物，欢迎你下次光临！"};

    private LinkedList<StepBarBean> createBean(int runningPosition){
        int a = 0;
        LinkedList<StepBarBean> beanLinkedList = new LinkedList<>();
        while (a < texts.length) {
            StepBarBean.Builder builder = new StepBarBean.Builder(getActivity());
            if (a == runningPosition) {
                builder.setState(StepBarConfig.StepSate.RUNNING);
            } else if (a < runningPosition) {
                builder.setState(StepBarConfig.StepSate.COMPLETED);
            } else {
                builder.setState(StepBarConfig.StepSate.WAITING);
            }
            builder.setRunningIcon(ContextCompat.getDrawable(getActivity(), R.drawable.li_running))//
                   .setWaitingIcon(ContextCompat.getDrawable(getActivity(), R.drawable.li_running))//
                   .setCompletedIcon(ContextCompat.getDrawable(getActivity(), R.drawable.li_complted))//
                   .setFailedIcon(ContextCompat.getDrawable(getActivity(), R.drawable.li_failed))//
                   .setOutsideIconRingForegroundColor(Color.RED)//
                   .setOutsideIconRingBackgroundColor(Color.parseColor("#EEEE00"))//
                   .setConnectLineColor(Color.WHITE)//
                   .setRunningTextColor(Color.YELLOW)//
                   .setCompletedTextColor(Color.WHITE)//
                   .setWaitingTextColor(Color.WHITE)//
                   .setFailedTextColor(Color.RED)//
                   .setCompletedText(texts[a])//
                   .setRunningText(texts[a])//
                   .setWaitingText(texts[a])//
                   .setFailedText(texts[a]);
            StepBarBean bean = builder.build();
            beanLinkedList.add(bean);
            a++;
        }
        return beanLinkedList;
    }
}
