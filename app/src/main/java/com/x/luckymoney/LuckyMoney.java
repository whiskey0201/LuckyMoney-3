package com.x.luckymoney;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.x.luckymoney.work.AccessbilityJob;
import com.x.luckymoney.work.WechatAccessbilityJob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by x on 2016/2/4.
 */
public class LuckyMoney extends AccessibilityService {
    private static final String TAG = "LuckMoney";
    private static final Class[] ACCESSBILITY_JOBS = {
        WechatAccessbilityJob.class,
    };
    private static LuckyMoney service;
    private Config mConfig;
    private List<AccessbilityJob> mAccessbilityJobs;

    @Override
    public void onCreate(){
        super.onCreate();
        mAccessbilityJobs = new ArrayList<>();
        mConfig = new Config(this);
        //初始化辅助插件工作
        for(Class clazz: ACCESSBILITY_JOBS){
            try{
                Object object = clazz.newInstance();
                if (object instanceof AccessbilityJob){
                    AccessbilityJob job = (AccessbilityJob) object;
                    job.onCreateJob(this);
                    mAccessbilityJobs.add(job);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "LuckyMoney service destory");
        if (mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()){
            for (AccessbilityJob job : mAccessbilityJobs){
                job.onStopJob();
            }
            mAccessbilityJobs.clear();
        }
        service = null;
        mAccessbilityJobs =null;
        //发送广播，已经断开辅助服务
        Intent intent = new Intent(Config.ACTION_LUCKYMONKEY_SERVICE_DISCONNECT);
        sendBroadcast(intent);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "luckymonkey server interrupt");
        Toast.makeText(this,"中断抢红包服务", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();
        service = this;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACCTION_LUCKYMONKRY_SERVICE_CONNECT);
        sendBroadcast(intent);
        Toast.makeText(this,"已链接抢红包服务",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"事件-->" + event);
        }
        String pkn = String.valueOf(event.getPackageName());
        if (mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()){
            for (AccessbilityJob job : mAccessbilityJobs){
                if (pkn.equals(job.getTargetPackageName()) && job.isEnable()){
                    job.onReceiveJob(event);
                }
            }
        }
    }

    public Config getConfig(){ return mConfig;}

    /**
     * 判断当前服务是否正在运行
    * */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning(){
        if (service == null){
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if (info == null){
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if(i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if(!isConnect) {
            return false;
        }
        return true;
    }



}
