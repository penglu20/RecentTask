package com.pl.recent;

import android.app.Application;
import android.content.Context;


/**
 * Created by l4656_000 on 2015/11/14.
 */
public class MyApplication extends Application {
    private static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

    }

    public static Context getInstance(){
        return instance;
    }

}
