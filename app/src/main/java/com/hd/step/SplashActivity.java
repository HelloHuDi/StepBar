package com.hd.step;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private View[] views;

    private void initView() {
        views = new View[]{findViewById(R.id.tvS),//
                            findViewById(R.id.tvT),//
                            findViewById(R.id.tvE),//
                            findViewById(R.id.tvP),//
                            findViewById(R.id.tvB),//
                            findViewById(R.id.tvA),//
                            findViewById(R.id.tvR),//
                            findViewById(R.id.ivI)};
        views[0].post(new Runnable() {
            @Override
            public void run() {
                for (final View v : views) {
                    if(v.getId()==R.id.ivI){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startAnimator(v);
                            }
                        },1000);
                    }else {
                        startAnimator(v);
                    }
                }
            }
        });
    }

    private void startAnimator(View v) {
        v.setVisibility(View.VISIBLE);
        startTextInAnim(v);
    }

    private void startTextInAnim(View v) {
        Random r = new Random();
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int x = r.nextInt(metrics.widthPixels * 4 / 3);
        int y = r.nextInt(metrics.heightPixels * 4 / 3);
        float s = r.nextFloat() + 4.0f;
        ValueAnimator tranY = ObjectAnimator.ofFloat(v, "translationY", y - v.getY(), 0);
        ValueAnimator tranX = ObjectAnimator.ofFloat(v, "translationX", x - v.getX(), 0);
        ValueAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", s, 1.0f);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", s, 1.0f);
        ValueAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 0.0f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(tranX, tranY, scaleX, scaleY, alpha);
        if (v == findViewById(R.id.ivI)) {
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    SystemClock.sleep(500);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        set.start();
    }

}
