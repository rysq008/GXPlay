package com.ikats.shop.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


import com.ikats.shop.R;

import java.io.IOException;

/**
 * dialog帮助类
 */
public class DialogFragmentHelper {
    private static final String DIALOG_POSITIVE = "确定";
    private static final String DIALOG_NEGATIVE = "取消";
    private static final String TAG_HEAD = DialogFragmentHelper.class.getSimpleName();

    /**
     * 加载中的弹出窗
     */
    private static final int PROGRESS_THEME = R.style.Base_AlertDialog;
    private static final String PROGRESS_TAG = TAG_HEAD + ":progress";


    public static CommonDialogFragment showProgress(FragmentManager fragmentManager, String message) {
        return showProgress(fragmentManager, message, true, null);
    }

    public static CommonDialogFragment showProgress(FragmentManager fragmentManager, String message, boolean cancelable) {
        return showProgress(fragmentManager, message, cancelable, null);
    }

    public static CommonDialogFragment showProgress(FragmentManager fragmentManager, final String message, boolean cancelable
            , CommonDialogFragment.OnDialogCancelListener cancelListener) {

        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(Context context) {
                ProgressDialog progressDialog = new ProgressDialog(context, PROGRESS_THEME);
                progressDialog.setMessage(message);
                return progressDialog;
            }
        }, cancelable, cancelListener, null);
        dialogFragment.show(fragmentManager, PROGRESS_TAG);
        return dialogFragment;
    }

    /**
     * 带输入框的弹出窗
     */
    private static final int INSERT_THEME = R.style.Base_AlertDialog;
    private static final String INSERT_TAG = TAG_HEAD + ":insert";

    public static void showInsertDialog(FragmentManager manager, final String title, final IDialogResultListener<String> resultListener, final boolean cancelable) {

        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public AlertDialog getDialog(Context context) {
                final EditText editText = new EditText(context);
                editText.setBackground(null);
                editText.setPadding(60, 40, 0, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(context, INSERT_THEME);
                builder.setTitle(title);
                builder.setView(editText);
                builder.setPositiveButton(DIALOG_POSITIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (resultListener != null) {
                            try {
                                resultListener.onDataResult(editText.getText().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builder.setNegativeButton(DIALOG_NEGATIVE, null);
                return builder.create();

            }
        }, cancelable, null, null);
        dialogFragment.show(manager, INSERT_TAG);
    }

    /**
     * 确定取消框
     */
    private static final int CONFIRM_THEME = R.style.Base_AlertDialog;
    private static final String CONFIRM_TAG = TAG_HEAD + ":confirm";

    public static void showConfirmDialog(FragmentManager fragmentManager, final String message, final IDialogResultListener<Integer> listener
            , boolean cancelable, CommonDialogFragment.OnDialogCancelListener cancelListener) {
        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public AlertDialog getDialog(Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, CONFIRM_THEME);
                builder.setMessage(message);
                builder.setPositiveButton(DIALOG_POSITIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            try {
                                listener.onDataResult(which);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builder.setNegativeButton(DIALOG_NEGATIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                return builder.create();
            }
        }, cancelable, cancelListener, null);
        dialogFragment.show(fragmentManager, CONFIRM_TAG);
    }

    /**
     * 自定义的确定取消框
     */
    private static final int MY_CONFIRM_THEME = R.style.Base_AlertDialog;
    private static final String MY_CONFIRM_TAG = TAG_HEAD + ":my_confirm";

    public static void showMyConfirmDialog(FragmentManager fragmentManager, int layoutID, final CommonDialogFragment.OnProcessView onProcessView, final IDialogResultListener<Integer> listener
            , boolean cancelable, CommonDialogFragment.OnDialogCancelListener cancelListener) {
        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public AlertDialog getDialog(Context context) {
                View view = View.inflate(context, layoutID, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context, MY_CONFIRM_THEME);
                builder.setView(view);
                builder.setPositiveButton(DIALOG_POSITIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            try {
                                listener.onDataResult(which);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builder.setNegativeButton(DIALOG_NEGATIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                if (null != onProcessView) onProcessView.onViewProcess(alertDialog, view);
                return alertDialog;
            }
        }, cancelable, cancelListener, null);
        dialogFragment.show(fragmentManager, MY_CONFIRM_TAG);
    }

    public static CommonDialogFragment builder(CommonDialogFragment.OnCallDialog callDialog, boolean canCancel) {
        CommonDialogFragment cdf = CommonDialogFragment.newInstance(callDialog, canCancel);
        cdf.setDailogStyle(DialogFragment.STYLE_NO_TITLE, R.style.transparentBgDialog);
        return cdf;
    }

    public static CommonDialogFragment builder(int layoutId, boolean canCancel) {
        CommonDialogFragment cdf = CommonDialogFragment.newInstance(layoutId, canCancel);
        cdf.setDailogStyle(DialogFragment.STYLE_NO_TITLE, R.style.transparentBgDialog);
        return cdf;
    }

    public static void showBuilderDialog(FragmentManager fragmentManager, DialogFragment dialogFragment, String tag) {
        dialogFragment.show(fragmentManager, tag);
    }
}
