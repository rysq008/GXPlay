package com.zhny.zhny_app.views;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastMgr {

    private static Toast mToast;
    private static ToastMgr instance;

    private ToastMgr(){

    }

    public static ToastMgr getInstance(){

        if (instance == null){
            synchronized (ToastMgr.class){
                instance = new ToastMgr();
            }
        }
        return instance;
    }

    public static void showShortToast(String message){

        if (!TextUtils.isEmpty(message)){

            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public static void showShortToast(int resId){

        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showLongToast(String message){

        if (!TextUtils.isEmpty(message)){

            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    public static void showLongToast(int resId){

        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public void init(Context context){

        if (mToast == null){
            synchronized (ToastMgr.class){
                if (mToast == null){
                    mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                    //mToast.setGravity(Gravity.CENTER, 0, 0);
                }
            }
        }
    }
}
