package com.hd.step.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hd.step.BaseFragment;
import com.hd.step.R;
import com.hd.stepbar.StepBar;
import com.hd.stepbar.StepBarConfig;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hd on 2018/1/1 .
 *
 */
public class VerticalDemoFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vertical_demo, null);
        initStepBar1((StepBar) rootView.findViewById(R.id.stepBar1));
        initStepBar2((StepBar) rootView.findViewById(R.id.stepBar2));
        initStepBar3((StepBar) rootView.findViewById(R.id.stepBar3));
        initStepBar4((StepBar) rootView.findViewById(R.id.stepBar4));
        return rootView;
    }

    private void initStepBar1(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createLongTextBean(20, 2))//
                                                  .setShowState(StepBarConfig.StepShowState.STATIC);
        stepBar.addConfig(config);
    }

    private void initStepBar2(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createLongTextBean(20, 2))//
                                                  .setIconCircleRadius(30)//
                                                  .setTextLocation(StepBarConfig.StepTextLocation.LEFT)
                                                  .setShowState(StepBarConfig.StepShowState.STATIC);
        stepBar.addConfig(config);
    }

    private void initStepBar3(StepBar stepBar) {
        initTimeTask();
        StepBarConfig config = new StepBarConfig().setBeanList(createLongTextBean(15, 0))//
                                                  .setIconCircleRadius(40)//注释后自动调整大小
                                                  .setShowState(StepBarConfig.StepShowState.DYNAMIC)//
                                                  .addStepCallback(new StepBarConfig.StepCallback() {
                                                      @Override
                                                      public boolean step(StepBarConfig config, int position) {
                                                          if (position == config.getBeanList().size() - 1) {
                                                              config.getBeanList().get(position).setState(StepBarConfig.StepSate.FAILED);
                                                              return false;
                                                          }
                                                          try {
                                                              return startNextStep;
                                                          } finally {
                                                              if (startNextStep && position < config.getBeanList().size() - 1) {
                                                                  initTimeTask();
                                                              }
                                                          }
                                                      }
                                                  });
        stepBar.addConfig(config);
    }

    private void initStepBar4(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createLongTextBean(12, 2))//
                                                  .setOutsideIconRingWidth(5)//
                                                  .setTextLocation(StepBarConfig.StepTextLocation.LEFT)
                                                  .setShowState(StepBarConfig.StepShowState.STATIC);
        stepBar.addConfig(config);
    }

    private boolean startNextStep;

    private void initTimeTask() {
        startNextStep = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startNextStep = true;
            }
        }, 1000);
    }
}
