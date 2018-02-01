package com.hd.step;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hd.splashscreen.SimpleConfig;
import com.hd.splashscreen.SimpleSplashFinishCallback;
import com.hd.splashscreen.SimpleSplashScreen;

public class SplashActivity extends AppCompatActivity implements SimpleSplashFinishCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleSplashScreen screen=findViewById(R.id.splash_container);
        SimpleConfig simpleConfig=new SimpleConfig();
        simpleConfig.setText("STEPBAR");
        simpleConfig.setIconId(R.mipmap.icon);
        simpleConfig.setTextColor(R.color.colorPrimary);
        simpleConfig.setTextSize(40);
        simpleConfig.setIconDelayTime(1000);
        simpleConfig.setCallback(this);
        screen.addConfig(simpleConfig);
        screen.start();
    }

    @Override
    public void loadFinish() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
