package com.hd.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hd.stepbar.StepBar;
import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hd on 2017/12/30 .
 * test
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StepBar stepBar = (StepBar) findViewById(R.id.stepBar);
        initTimeTask();
        initStepBar(stepBar);
    }

    private boolean startNextStep;

    private void initTimeTask() {
        startNextStep = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startNextStep = true;
                Log.d("tag", "定时器");
            }
        }, 5000);
    }

    private int count = 10;

    private void initStepBar(StepBar stepBar) {
        int a = 0;
        LinkedList<StepBarBean> beanLinkedList = new LinkedList<>();
        while (a < count) {
            StepBarBean bean = new StepBarBean.Builder(this).build();
            if (a == 3) {
                bean.setState(StepBarConfig.StepSate.RUNNING);
            } else if (a < 3) {
                bean.setState(StepBarConfig.StepSate.COMPLETED);
            } else {
                bean.setState(StepBarConfig.StepSate.WAITING);
            }
            bean.setRunningIcon(ContextCompat.getDrawable(this, R.drawable.running));
            bean.setWaitingIcon(ContextCompat.getDrawable(this, R.drawable.waiting));
            bean.setCompletedIcon(ContextCompat.getDrawable(this, R.drawable.complete));
            bean.setFailedIcon(ContextCompat.getDrawable(this, R.drawable.fail));
            bean.setCompletedText("完成任务");
            bean.setRunningText("执行任务");
            bean.setWaitingText("等待任务");
            bean.setFailedText("任务失败");
            beanLinkedList.add(bean);
            a++;
        }
        StepBarConfig config = new StepBarConfig()//
                .setBeanList(beanLinkedList)//
                .setShowState(StepBarConfig.StepShowState.STATIC)//
                .addStepCallback(new StepBarConfig.StepCallback() {
                    @Override
                    public boolean step(StepBarConfig config, int position) {
                        if (position == 5) {
                            config.getBeanList().get(position).setState(StepBarConfig.StepSate.FAILED);
                            return false;
                        }
                        try {
                            return startNextStep;
                        } finally {
                            if (startNextStep && position < count - 1) {
                                Log.d("tag", "开始定时器:" + position);
                                initTimeTask();
                            }
                        }
                    }
                });
        stepBar.addConfig(config);
    }

}
