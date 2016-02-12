package com.x.luckymoney.work;

import android.content.Context;

import com.x.luckymoney.Config;
import com.x.luckymoney.LuckyMoney;

/**
 * Created by x on 2016/2/4.
 */
public abstract class BaseAccessbilityJob implements AccessbilityJob {
    private LuckyMoney server;
    @Override
    public void onCreateJob(LuckyMoney server){this.server = server;}
    public Context getContext(){return server.getApplicationContext();}
    public Config getConfig(){return server.getConfig();}
    public LuckyMoney getService(){return server;}
}
