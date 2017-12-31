package com.hd.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hd.stepbar.StepBar;
import com.hd.stepbar.StepBarBean;
import com.hd.stepbar.StepBarConfig;

import java.util.LinkedList;


/**
 * Created by hd on 2017/12/30 .
 *
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StepBar stepBar = (StepBar) findViewById(R.id.stepBar);
        initStepBar(stepBar);
    }

    private void initStepBar(StepBar stepBar) {
        int a =12;
        LinkedList<StepBarBean> beanLinkedList = new LinkedList<>();
        while (a > 0) {
            a--;
            StepBarBean bean = new StepBarBean.Builder(this).build();
            if(a==2){
                bean.setState(StepBarConfig.StepSate.FAILED);
            }else if(a>2){
                bean.setState(StepBarConfig.StepSate.COMPLETED);
            }else if(a<2){
                bean.setState(StepBarConfig.StepSate.WAITING);
            }
            beanLinkedList.add(bean);
        }
        StepBarConfig config = new StepBarConfig();
        config.setBeanList(beanLinkedList);
        stepBar.addConfig(config);
    }

}
