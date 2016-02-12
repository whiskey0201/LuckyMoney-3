package com.x.luckymoney;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by x on 2016/2/4.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        LKMYApplication.activityCreateStatistics(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        LKMYApplication.activityResumeStatistics(this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        LKMYApplication.activityPauseStatistics(this);
    }

}
