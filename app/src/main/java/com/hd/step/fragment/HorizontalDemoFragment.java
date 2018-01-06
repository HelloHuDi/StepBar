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
public class HorizontalDemoFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_horizontal_demo, null);
        initStepBar1((StepBar) rootView.findViewById(R.id.stepBar1));
        initStepBar2((StepBar) rootView.findViewById(R.id.stepBar2));
        initStepBar3((StepBar) rootView.findViewById(R.id.stepBar3));
        initStepBar4((StepBar) rootView.findViewById(R.id.stepBar4));
        initStepBar5((StepBar) rootView.findViewById(R.id.stepBar5));
        return rootView;
    }

    /**
     * 静态 ，大小动态调整
     */
    private void initStepBar1(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createShortTextBean(30, 2))//
                                                  .setShowState(StepBarConfig.StepShowState.STATIC);
        stepBar.addConfig(config);
    }

    /**
     * 静态 ，大小动态调整
     */
    private void initStepBar2(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createShortTextBean(10, 0));
        stepBar.addConfig(config);
    }

    /**
     * 静态 ，设置图标大小，可滑动
     */
    private void initStepBar3(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createShortTextBean(10, 2))//
                                                  .setIconCircleRadius(50)//
                                                  .setShowState(StepBarConfig.StepShowState.STATIC);
        stepBar.addConfig(config);
    }

    /**
     * 动态 ，设置图标大小（若不设置，自动调整大小，满足宽度全屏）
     */
    private void initStepBar4(StepBar stepBar) {
        initTimeTask();
        StepBarConfig config = new StepBarConfig().setBeanList(createShortTextBean(6, 0))//
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

    /**
     * 设置外圆环宽度
     */
    private void initStepBar5(StepBar stepBar) {
        StepBarConfig config = new StepBarConfig().setBeanList(createShortTextBean(7, 2))//
                                                  .setOutsideIconRingWidth(0)//
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
