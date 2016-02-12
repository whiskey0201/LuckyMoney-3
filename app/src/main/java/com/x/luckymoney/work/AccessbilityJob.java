package com.x.luckymoney.work;

import android.view.accessibility.AccessibilityEvent;

import com.x.luckymoney.LuckyMoney;

/**
 * Created by x on 2016/2/4.
 */
public interface AccessbilityJob {
    String getTargetPackageName();
    void onCreateJob(LuckyMoney service);
    void onReceiveJob(AccessibilityEvent event);
    void onStopJob();
    boolean isEnable();


}
