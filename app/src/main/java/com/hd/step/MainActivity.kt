package com.hd.step

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hd.stepbar.StepBarConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StepBarConfig();

    }
}
