package com.zhny.library.presenter.login.custom;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.zhny.library.R;
import com.zhny.library.utils.DisplayUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * created by liming
 */
public class LoadingDialog extends AlertDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();

        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = DisplayUtils.dp2px(60);
            params.height = DisplayUtils.dp2px(60);
            window.setAttributes(params);
        }
    }

}
