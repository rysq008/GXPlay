package com.zhny.library.presenter.driver.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhny.library.R;
import com.zhny.library.utils.DisplayUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class DeleteDialog extends AppCompatDialogFragment implements View.OnClickListener {


    private TextView delete, deleteText;
    private Window mWindow;
    private Object object;
    /**
     *  1: 删除机手
     *  2: 删除围栏 - 有数据
     *  3: 删除围栏 - 无数据
     */
    private int type;

    private OnDeleteListener listener;

    public DeleteDialog(@Nullable OnDeleteListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_delete_driver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        delete = view.findViewById(R.id.tv_delete);
        deleteText = view.findViewById(R.id.tv_delete_msg);
        delete.setOnClickListener(this);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);

        mWindow = getDialog().getWindow();

        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.gravity = Gravity.CENTER;
            mWindow.setAttributes(params);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        String msg = "";
        switch (type) {
            case 1:  msg = getString(R.string.confirm_delete_driver); break;
            case 2:  msg = getString(R.string.fence_disable_has_content); break;
            case 3:  msg = getString(R.string.fence_disable_no_content); break;
        }
        deleteText.setText(msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWindow != null) {
            mWindow.setBackgroundDrawableResource(R.color.white);
            mWindow.setLayout(DisplayUtils.dp2px(302.4f), DisplayUtils.dp2px(193.36f));
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onClick(View v) {
        if (v == delete) {
            if (listener != null) {
                listener.onDelete(object);
            }
        }
        dismiss();
    }

    public void setParams(int type, Object obj) {
        this.type = type;
        this.object = obj;
    }


    /**
     * 监听按钮动作
     */
    public interface OnDeleteListener {
        void onDelete(Object o);
    }
}
