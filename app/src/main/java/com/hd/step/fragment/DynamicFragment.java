package com.hd.step.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hd.step.BaseFragment;
import com.hd.step.R;
import com.hd.stepbar.StepBar;
import com.hd.stepbar.StepBarConfig;


/**
 * Created by hd on 2018/1/6 .
 * dynamic fragment
 */
public abstract class DynamicFragment extends BaseFragment implements View.OnClickListener, StepBarConfig.StepCallback  {

    abstract @LayoutRes int setLayoutId();

    abstract void initStepBar();

    protected StepBar stepBar;

    protected boolean nextStep, failed = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(setLayoutId(),null);
        stepBar = (StepBar) rootView.findViewById(R.id.verticalStepBar);
        initStepBar();
        rootView.findViewById(R.id.startShow).setOnClickListener(this);
        rootView.findViewById(R.id.controlFailed).setOnClickListener(this);
        rootView.findViewById(R.id.controlNextStep).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startShow:
                failed = false;
                nextStep=false;
                initStepBar();
                break;
            case R.id.controlFailed:
                failed = true;
                nextStep=false;
                break;
            case R.id.controlNextStep:
                nextStep = true;
                failed = false;
                break;
        }
    }

    @Override
    public boolean step(StepBarConfig config, int position) {
        if (failed) {
            config.getBeanList().get(position).setState(StepBarConfig.StepSate.FAILED);
            return false;
        }
        try {
            return nextStep;
        } finally {
            if (nextStep)
                nextStep = false;
        }
    }
}
