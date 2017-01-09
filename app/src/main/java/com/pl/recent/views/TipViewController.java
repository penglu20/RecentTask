package com.pl.recent.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.pl.recent.MyApplication;
import com.pl.recent.R;

public class TipViewController implements View.OnClickListener, View.OnTouchListener {
    private static final String TAG="TipViewController";

    private static final int MOVETOEDGE=10010;


    private WindowManager mWindowManager;
    private Context mContext;
    private View mWholeView;
    private ImageView imageView;
    private TextView textView;

    private Handler mainHandler;
    private WindowManager.LayoutParams layoutParams;
    private float mTouchStartX,mTouchStartY;
    private int rotation;
    private boolean isMovingToEdge=false;
    private float density=0;


    private static class InnerClass {
        private static TipViewController instance = new TipViewController(MyApplication.getInstance());
    }

    public static TipViewController getInstance() {
        return InnerClass.instance;
    }

    private TipViewController(Context application) {
        mContext = application;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        mainHandler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MOVETOEDGE:
                        int desX= (int) msg.obj;
                        if (desX==0){
                            layoutParams.x= (int) (layoutParams.x - density*10);
                            if (layoutParams.x<0){
                                layoutParams.x=0;
                            }
                        }else {
                            layoutParams.x= (int) (layoutParams.x + density*10);
                            if (layoutParams.x>desX){
                                layoutParams.x=desX;
                            }
                        }
                        updateViewPosition(layoutParams.x,layoutParams.y);
                        if (layoutParams.x!=desX) {
                            mainHandler.sendMessageDelayed(mainHandler.obtainMessage(MOVETOEDGE, desX),10);
                        }else {
                            isMovingToEdge = false;

                        }
                        break;
                }
            }
        };
    }

    public void show(final int floatType, final String time) {

        if (mWholeView!=null){

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWholeView.setVisibility(View.VISIBLE);
                    textView.setText(time);
                    switchFloat(floatType);
                    if (rotation!=mWindowManager.getDefaultDisplay().getRotation()){
                        moveToEdge();
                    }
                }
            });
            return;
        }

        mWholeView = View.inflate(mContext, R.layout.view_float, null);
        imageView = (ImageView) mWholeView.findViewById(R.id.float_image);
        textView = (TextView) mWholeView.findViewById(R.id.float_text);

        switchFloat(floatType);

        DisplayMetrics displayMetrics=new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        density=displayMetrics.density;

        // event listeners
        mWholeView.setOnTouchListener(this);

        int w = WindowManager.LayoutParams.WRAP_CONTENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }


        int width,height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point point=new Point();
            mWindowManager.getDefaultDisplay().getSize(point);
            width=point.x;
            height=point.y;
        }else {
            width= mWindowManager.getDefaultDisplay().getWidth();
            height= mWindowManager.getDefaultDisplay().getHeight();
        }
        rotation = mWindowManager.getDefaultDisplay().getRotation();
        int x=0,y=0;
        x=width;
        y=height/2;


        layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity= Gravity.TOP| Gravity.LEFT;
        layoutParams.x=x;
        layoutParams.y=y;

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mWindowManager.addView(mWholeView, layoutParams);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void hide(){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mWholeView!=null) {
                    mWholeView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void remove(){
        if (mWindowManager!=null&&mWholeView!=null) {
            try {
                mWindowManager.removeView(mWholeView);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void switchFloat(int type){//type=1,图片，type=2,文字
        if (type==1){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        if (layoutParams!=null) {
            try {
                mWindowManager.updateViewLayout(mWholeView, layoutParams);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * touch the outside of the content view, remove the popped view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isMovingToEdge){
            return true;
        }
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition(x,y);
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition(x,y);
                mTouchStartX = mTouchStartY = 0;
                moveToEdge();
                break;
        }
        return true;
    }

    private void updateViewPosition(float x,float y) {
        layoutParams.x = (int) (x - mTouchStartX);
        layoutParams.y = (int) (y - mTouchStartY);
//        layoutParams.x= (int) x;
//        layoutParams.y= (int) y;
        layoutParams.gravity=Gravity.TOP|Gravity.LEFT;
        try {
            mWindowManager.updateViewLayout(mWholeView, layoutParams);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void moveToEdge(){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                isMovingToEdge=true;
                rotation=mWindowManager.getDefaultDisplay().getRotation();
                int width=0,height=0;
                int x=0,y=0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    Point point=new Point();
                    mWindowManager.getDefaultDisplay().getSize(point);
                    width=point.x;
                    height=point.y;
                }else {
                    width= mWindowManager.getDefaultDisplay().getWidth();

                    height= mWindowManager.getDefaultDisplay().getHeight();
                }

                int desX=0;
                if (layoutParams.x>width/2){
                    desX=width;
                }else {
                    desX=0;
                }
                mainHandler.sendMessage(mainHandler.obtainMessage(MOVETOEDGE,desX));
            }
        });
    }

}
