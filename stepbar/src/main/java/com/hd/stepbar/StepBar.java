package com.hd.stepbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hd.stepbar.indicator.HorizontalStepBarViewIndicator;
import com.hd.stepbar.indicator.StepBarViewIndicator;
import com.hd.stepbar.indicator.VerticalStepBarViewIndicator;

/**
 * Created by hd on 2017/12/31 .
 * controllable step bar
 */
public class StepBar extends LinearLayout {

    private StepBarConfig config;

    private AttributeSet attrs;

    public StepBar(Context context) {
        super(context, null);
    }

    public StepBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public StepBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
    }

    public void addConfig(StepBarConfig config) {
        this.config = config;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        removeAllViews();
        checkBeanAttrs(attrs);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.step_bar, getViewGroup());
        LinearLayout linearLayout = rootView.findViewById(R.id.stepBarGroup);
        StepBarViewIndicator stepBarViewIndicator;
        if (getOrientation() == HORIZONTAL) {
            stepBarViewIndicator = new HorizontalStepBarViewIndicator(getContext());
        } else {
            stepBarViewIndicator = new VerticalStepBarViewIndicator(getContext());
        }
        linearLayout.removeAllViews();
        stepBarViewIndicator.addConfig(config);
        linearLayout.addView(stepBarViewIndicator);
    }

    @NonNull
    private ViewGroup getViewGroup() {
        ViewGroup viewGroup = this;
        if (config.getIconCircleRadius() > 0) {
            if (getOrientation() == HORIZONTAL) {
                viewGroup = new HorizontalScrollView(getContext());
                viewGroup.setHorizontalScrollBarEnabled(false);
            } else {
                viewGroup = new ScrollView(getContext());
                viewGroup.setVerticalFadingEdgeEnabled(false);
            }
            addView(viewGroup, 0);
        }
        return viewGroup;
    }

    private void checkBeanAttrs(AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.stepBarViewStyle);
        int defaultConnectLineColor = typedArray.getColor(R.styleable.stepBarViewStyle_defaultConnectLineColor, Color.WHITE);
        int switchConnectLineColor = typedArray.getColor(R.styleable.stepBarViewStyle_switchConnectLineColor, Color.RED);
        int switchTime = typedArray.getInteger(R.styleable.stepBarViewStyle_switchTime, 100);
        int normalTextColor = typedArray.getColor(R.styleable.stepBarViewStyle_normalTextColor, Color.BLACK);
        int completedTextColor = typedArray.getColor(R.styleable.stepBarViewStyle_completedTextColor, Color.BLACK);
        int failedTextColor = typedArray.getColor(R.styleable.stepBarViewStyle_failedTextColor, Color.RED);
        int outsideIconRingColor = typedArray.getColor(R.styleable.stepBarViewStyle_outsideIconRingColor, Color.GREEN);
        int outsideIconRingWidth = typedArray.getInteger(R.styleable.stepBarViewStyle_outsideIconRingWidth, 5);
        float textSize = typedArray.getFloat(R.styleable.stepBarViewStyle_textSize, 12f);
        typedArray.recycle();
        for (StepBarBean bean : config.getBeanList()) {

        }
    }
}
