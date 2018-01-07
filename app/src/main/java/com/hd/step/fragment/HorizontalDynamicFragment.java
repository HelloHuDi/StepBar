package com.hd.step.fragment;

import com.hd.step.R;
import com.hd.stepbar.StepBarConfig;


/**
 * Created by hd on 2018/1/5 .
 * horizontal dynamic fragment
 */
public class HorizontalDynamicFragment extends DynamicFragment {
    @Override
    int setLayoutId() {
        return R.layout.fragment_horizontal_dynamic;
    }

    @Override
    void initStepBar() {
        StepBarConfig config = new StepBarConfig().setBeanList(createShortTextBean(12, 0))//
                                                  .setIconCircleRadius(50)
                                                  .setShowState(StepBarConfig.StepShowState.DYNAMIC)
                                                  .addStepCallback(this);
        stepBar.addConfig(config);
    }

}
