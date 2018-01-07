package com.hd.step.fragment;

import com.hd.step.R;
import com.hd.stepbar.StepBarConfig;


/**
 * Created by hd on 2018/1/5 .
 * vertical dynamic
 */
public class VerticalDynamicFragment extends DynamicFragment {

    @Override
    int setLayoutId() {
        return R.layout.fragment_vertical_dynamic;
    }

    @Override
    void initStepBar() {
        StepBarConfig config = new StepBarConfig().setBeanList(createLongTextBean(12, 0))//
                                                  .setIconCircleRadius(50)
                                                  .setShowState(StepBarConfig.StepShowState.DYNAMIC)//
                                                  .addStepCallback(this);
        stepBar.addConfig(config);
    }
}
