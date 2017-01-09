package com.pl.recent.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import com.pl.recent.views.TipViewController;

import static android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_LONG_CLICKED;
import static com.pl.recent.MainActivity.HIDE_FLOAT_VIEW_PACKAGE_NAME;


public class AccessibilityMonitorService extends AccessibilityService {

    private static final String TAG="BigBangMonitorService";

    private CharSequence mWindowClassName;

    private String mCurrentPackage;
    TipViewController tipViewController;

    AccessibilityServiceInfo mAccessibilityServiceInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        tipViewController=TipViewController.getInstance();
        mAccessibilityServiceInfo=new AccessibilityServiceInfo();
        mAccessibilityServiceInfo.feedbackType=FEEDBACK_GENERIC;
        mAccessibilityServiceInfo.eventTypes=AccessibilityEvent.TYPE_VIEW_CLICKED|AccessibilityEvent.TYPE_VIEW_LONG_CLICKED|AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        int flag=0;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            flag=flag|AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
            flag=flag|AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        }
        mAccessibilityServiceInfo.flags=flag;
        mAccessibilityServiceInfo.notificationTimeout=100;
        setServiceInfo(mAccessibilityServiceInfo);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        setServiceInfo(mAccessibilityServiceInfo);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setServiceInfo(mAccessibilityServiceInfo);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type=event.getEventType();
        switch (type){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mWindowClassName = event.getClassName();
                mCurrentPackage = event.getPackageName()==null?"":event.getPackageName().toString();
                if (mCurrentPackage.equals(HIDE_FLOAT_VIEW_PACKAGE_NAME)){
                    tipViewController.hide();
                }else {
                    tipViewController.show(1,"");
                }
                break;
            case TYPE_VIEW_CLICKED:
            case TYPE_VIEW_LONG_CLICKED:
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

}
