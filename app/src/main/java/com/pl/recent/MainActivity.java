package com.pl.recent;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.pl.recent.service.AccessibilityMonitorService;
import com.pl.recent.utils.RunningTaskUtil;
import com.pl.recent.views.TipViewController;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    RunningTaskUtil runningTaskUtil;
    Timer timer;
    TimerTask timerTask;
    TipViewController tipViewController;
    public static final String HIDE_FLOAT_VIEW_PACKAGE_NAME="com.android.chrome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runningTaskUtil=new RunningTaskUtil(getApplicationContext());
        tipViewController=TipViewController.getInstance();

        timer=new Timer();
    }

    public void openUsageSettings(View view){
        try {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }catch (Throwable e){
            Toast.makeText(this,"无法打开系统设置，请手动设置",Toast.LENGTH_SHORT).show();
        }
    }

    public void setAccissibilityAPP(View view){
        try {
            Intent intent = new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS);
            startActivity(intent);
        }catch (Throwable e){
            Toast.makeText(this,"无法打开系统设置，请手动设置",Toast.LENGTH_SHORT).show();
        }
    }

    public void setAccissibilityService(View view){
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }catch (Throwable e){
            Toast.makeText(this,"无法打开系统设置，请手动设置",Toast.LENGTH_SHORT).show();
        }
    }

    public void startCheckGetRunningTasks(View view){
        if (timerTask!=null) {
            timerTask.cancel();
        }
        timerTask=new TimerTask() {
            @Override
            public void run() {
                ComponentName componentName = runningTaskUtil.getTopActivtyFromLolipopOnwards();
                if (componentName.getPackageName().equals(HIDE_FLOAT_VIEW_PACKAGE_NAME)){
                    tipViewController.hide();
                }else {
                    tipViewController.show(1,"");
                }
            }
        };
        timer.schedule(timerTask,1000,1000);
    }

    public void startCheckOriginal(View view){
        if (timerTask!=null) {
            timerTask.cancel();
        }
        timerTask=new TimerTask() {
            @Override
            public void run() {
                ComponentName componentName = runningTaskUtil.getTopRunningTasksOrigin();
                if (componentName.getPackageName().equals(HIDE_FLOAT_VIEW_PACKAGE_NAME)){
                    tipViewController.hide();
                }else {
                    tipViewController.show(1,"");
                }
            }
        };
        timer.schedule(timerTask,1000,1000);
    }

    public void startCheckImproved(View view){
        if (timerTask!=null) {
            timerTask.cancel();
        }
        timerTask=new TimerTask() {
            @Override
            public void run() {
                ComponentName componentName = runningTaskUtil.getTopRunningTasks();
                if (componentName.getPackageName().equals(HIDE_FLOAT_VIEW_PACKAGE_NAME)){
                    tipViewController.hide();
                }else {
                    tipViewController.show(1,"");
                }
            }
        };
        timer.schedule(timerTask,1000,1000);
    }

    public void startCheckAccissibility(View view){
        Intent intent=new Intent(this, AccessibilityMonitorService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        if (timerTask!=null) {
            timerTask.cancel();
        }
        tipViewController.remove();
        super.onDestroy();
    }
}
