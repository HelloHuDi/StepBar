package com.hd.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hd.step.fragment.HorizontalFragment;
import com.hd.step.fragment.VerticalFragment;


/**
 * Created by hd on 2017/12/30 .
 * test
 */
public class MainActivity extends AppCompatActivity {

    private BaseFragment baseFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        baseFragment=new HorizontalFragment();
        baseFragment=new VerticalFragment();
        fragmentTransaction.add(R.id.stepbarContainer, baseFragment).commitAllowingStateLoss();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.action_horizontal_stepview:
                baseFragment=new HorizontalFragment();
                break;
            case R.id.action_vertical_forward:
                baseFragment=new VerticalFragment();
                break;
        }
        fragmentTransaction.replace(R.id.stepbarContainer,baseFragment).commitAllowingStateLoss();
        return super.onOptionsItemSelected(item);
    }
}
