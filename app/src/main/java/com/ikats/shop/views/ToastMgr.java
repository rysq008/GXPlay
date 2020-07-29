package com.ikats.shop.views;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;

public class ToastMgr {

    public static void showShortToast(String message) {

        if (!TextUtils.isEmpty(message)) {
            ToastUtils.showShort(message);
        }
    }

    public static void showShortToast(int resId) {
        ToastUtils.showShort(resId);
    }

    public static void showLongToast(String message) {

        if (!TextUtils.isEmpty(message)) {
            ToastUtils.showLong(message);
        }
    }

    public static void showLongToast(int resId) {
        ToastUtils.showLong(resId);
    }

}
