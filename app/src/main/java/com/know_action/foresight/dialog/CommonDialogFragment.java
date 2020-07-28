package com.know_action.foresight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class
CommonDialogFragment extends DialogFragment {

    /**
     * 监听弹出窗是否被取消
     */
    private OnDialogCancelListener mCancelListener;

    /**
     * 回调获得需要显示的dialog
     */
    private OnCallDialog mOnCallDialog;
    private OnCallWindow mOnCallWindow;

    /**
     * 自定义View处理的dialog
     */
    private OnProcessView mOnProcessView;

    private int mLayoutId;

    private WindowManager.LayoutParams mWindowParams;

    public interface OnDialogCancelListener {
        void onCancel();
    }

    public interface OnCallDialog {
        Dialog getDialog(Context context);
    }

    public interface OnCallWindow {
        Window buildWindow(Window dialogWindow);
    }

    public interface OnProcessView {
        void onViewProcess(Dialog dialog, View view);
    }

    public static CommonDialogFragment newInstance(OnCallDialog call, boolean cancelable) {
        return newInstance(call, cancelable, null, null);
    }

    public static CommonDialogFragment newInstance(OnCallDialog call, boolean cancelable, OnDialogCancelListener cancelListener, WindowManager.LayoutParams wlp) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mCancelListener = cancelListener;
        instance.mOnCallDialog = call;
        instance.mWindowParams = wlp;
        return instance;
    }

    public static CommonDialogFragment newInstance(OnCallDialog call, boolean cancelable, WindowManager.LayoutParams wlp) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mOnCallDialog = call;
        instance.mWindowParams = wlp;
        return instance;
    }

    public static CommonDialogFragment newInstance(int layoutID, boolean cancelable, OnDialogCancelListener cancelListener, WindowManager.LayoutParams wlp) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mCancelListener = cancelListener;
        instance.mLayoutId = layoutID;
        instance.mWindowParams = wlp;
        return instance;
    }

    public static CommonDialogFragment newInstance(int layoutID, boolean cancelable, WindowManager.LayoutParams wlp) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mLayoutId = layoutID;
        instance.mWindowParams = wlp;
        return instance;
    }

    public static CommonDialogFragment newInstance(int layoutID, boolean cancelable) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mLayoutId = layoutID;
        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null == mOnCallDialog) {
            return super.onCreateDialog(savedInstanceState);
        }
        return mOnCallDialog.getDialog(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutId > 0) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(mLayoutId, container);
            if (null != mOnProcessView) {
                mOnProcessView.onViewProcess(getDialog(), view);
            }
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        //full screen
//        try {
//            Window win = getDialog().getWindow();
//            win.getDecorView().setPadding(0, 0, 0, 0);
//            WindowManager.LayoutParams lp = win.getAttributes();
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            win.setAttributes(lp);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
//            //在5.0以下的版本会出现白色背景边框，若在5.0以上设置则会造成文字部分的背景也变成透明
//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//                //目前只有这两个dialog会出现边框
//                if (dialog instanceof ProgressDialog || dialog instanceof DatePickerDialog) {
//                    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                }
//            }

            Window window = getDialog().getWindow();
            assert window != null;
////            window.setLayout(App.w / 5 * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
//            WindowManager.LayoutParams windowParams = window.getAttributes();
//            windowParams.dimAmount = 0.0f;
//            window.setAttributes(windowParams);
            if (null != mWindowParams) {
                window.setAttributes(mWindowParams);
            }
            if (mOnCallWindow != null) {
                mOnCallWindow.buildWindow(window);
            }
        }
    }

    public CommonDialogFragment setWindowParams(WindowManager.LayoutParams windowParams){
        this.mWindowParams = windowParams;
        return this;
    }

    public CommonDialogFragment setDialogWindow(OnCallWindow onCallWindow) {
        this.mOnCallWindow = onCallWindow;
        return this;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mCancelListener != null) {
            mCancelListener.onCancel();
        }
    }

    public CommonDialogFragment setDailogStyle(int style, int theme) {
        super.setStyle(style, theme);
        return this;
    }

    public CommonDialogFragment setOnProcessView(OnProcessView mOnProcessView) {
        this.mOnProcessView = mOnProcessView;
        return this;
    }

    public CommonDialogFragment setCancelListener(OnDialogCancelListener dialogCancelListener) {
        this.mCancelListener = dialogCancelListener;
        return this;
    }

    public CommonDialogFragment setCancelOutSide(boolean canCancelOutSide) {
        getDialog().setCanceledOnTouchOutside(canCancelOutSide);
        return this;
    }

}