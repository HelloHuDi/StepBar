package com.hd.stepbar;

import android.content.Context;
import android.os.Handler;
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

    public StepBar(Context context) {
        super(context, null);
    }

    public StepBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StepBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addConfig(StepBarConfig config) {
        this.config = config;
        new StepBarConfigAdapter().formatConfig(getOrientation(),config);
        init();
    }

    private ViewGroup viewGroup;

    private void init() {
        final Handler handler = new Handler();
        getViewGroup();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.step_bar, viewGroup);
        final LinearLayout linearLayout = rootView.findViewById(R.id.stepBarGroup);
        final StepBarViewIndicator stepBarViewIndicator;
        if (getOrientation() == HORIZONTAL) {
            stepBarViewIndicator = new HorizontalStepBarViewIndicator(getContext());
        } else {
            stepBarViewIndicator = new VerticalStepBarViewIndicator(getContext());
        }
        linearLayout.removeAllViews();
        stepBarViewIndicator.addConfig(config);
        addSlideCallback(handler, stepBarViewIndicator);
        linearLayout.addView(stepBarViewIndicator);
    }

    private void addSlideCallback(final Handler handler, StepBarViewIndicator stepBarViewIndicator) {
        if (config.isAllowAutoSlide()) {
            stepBarViewIndicator.addSlideCallback(new StepBarViewIndicator.SlideCallback() {
                @Override
                public void slide(final int iconCenterX, final int iconCenterY, final float radius) {
                    StepBar.this.slide(iconCenterX, iconCenterY, handler);
                }
            });
        }
    }

    private void slide(final int iconCenterX, final int iconCenterY, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getOrientation() == HORIZONTAL) {
                    ((HorizontalScrollView) viewGroup).smoothScrollTo(iconCenterX - viewGroup.getWidth() / 2, iconCenterY);
                } else {
                    ((ScrollView) viewGroup).smoothScrollTo(-iconCenterX / 4, iconCenterY - viewGroup.getMeasuredHeight() / 2);
                }
            }
        });
    }

    private void getViewGroup() {
        removeAllViews();
        if (config.getIconCircleRadius() > 0) {
            if (getOrientation() == HORIZONTAL) {
                viewGroup = new HorizontalScrollView(getContext());
                viewGroup.setHorizontalScrollBarEnabled(false);
                ((HorizontalScrollView) viewGroup).setSmoothScrollingEnabled(true);
            } else {
                viewGroup = new ScrollView(getContext());
                viewGroup.setVerticalScrollBarEnabled(false);
                ((ScrollView) viewGroup).setSmoothScrollingEnabled(true);
            }
            addView(viewGroup, 0);
        } else {
            viewGroup = this;
        }
    }
}
