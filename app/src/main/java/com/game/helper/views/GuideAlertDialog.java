package com.game.helper.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.game.helper.R;
import com.game.helper.utils.ScreenUtils;

/**
 * Created by sung on 2017/12/15.
 * 共用dialog
 */

@SuppressLint("ValidFragment")
public class GuideAlertDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    public static final String TAG = GuideAlertDialog.class.getSimpleName();
    private onDialogActionListner onDialogActionListner;

    //标题／内容
    private String tittle;
    private String content;
    private WebView webView;

    @SuppressLint("ValidFragment")
    public GuideAlertDialog(int type, @Nullable String tittle, @Nullable String content) {
        if (tittle != null) this.tittle = tittle;
        if (content != null) this.content = content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resLayout = 0;
        resLayout = R.layout.dialog_guide_alert;
        return inflater.inflate(resLayout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        webView = view.findViewById(R.id.dialog_alert_webView);

        webView.loadUrl(content);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() == null) return;
        if (!getDialog().isShowing()) return;
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = ScreenUtils.getScreenWidth(getContext()) / 5 * 4;
        lp.height = ScreenUtils.getScreenHeight(getContext()) / 5 * 4;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    public void addOnDialogActionListner(onDialogActionListner onDialogActionListner) {
        this.onDialogActionListner = onDialogActionListner;
    }

    public interface onDialogActionListner {
        void onCancel();

        void onConfirm();
    }
}