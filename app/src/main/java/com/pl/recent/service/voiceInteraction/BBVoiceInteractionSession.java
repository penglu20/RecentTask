package com.pl.recent.service.voiceInteraction;

import android.annotation.TargetApi;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.voice.VoiceInteractionSession;
import android.view.KeyEvent;

import com.pl.recent.views.TipViewController;

import static com.pl.recent.MainActivity.HIDE_FLOAT_VIEW_PACKAGE_NAME;

/**
 * Created by penglu on 2016/12/16.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BBVoiceInteractionSession extends VoiceInteractionSession {
    private Context mContext;
    TipViewController tipViewController;
    public BBVoiceInteractionSession(Context context) {
        super(context);
        mContext=context;
        tipViewController=TipViewController.getInstance();
    }

    public BBVoiceInteractionSession(Context context, Handler handler) {
        super(context, handler);
        mContext=context;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);
        ComponentName componentName=structure.getActivityComponent();
        if (componentName.getPackageName().equals(HIDE_FLOAT_VIEW_PACKAGE_NAME)){
            tipViewController.hide();
        }else {
            tipViewController.show(1,"");
        }

    }

    @Override
    public void onHandleAssistSecondary(Bundle data, AssistStructure structure, AssistContent content, int index, int count) {
        super.onHandleAssistSecondary(data, structure, content, index, count);
    }

    @Override
    public void onShow(Bundle args, int showFlags) {
        super.onShow(args, showFlags);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }


    @Override
    public void onHide() {
        super.onHide();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onHandleScreenshot(Bitmap screenshot) {
        super.onHandleScreenshot(screenshot);
    }
}
