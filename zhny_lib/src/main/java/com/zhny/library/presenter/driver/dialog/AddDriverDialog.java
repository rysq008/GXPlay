package com.zhny.library.presenter.driver.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhny.library.R;
import com.zhny.library.utils.DisplayUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class AddDriverDialog extends AppCompatDialogFragment implements View.OnClickListener {


    private Window mWindow;
    private String title;
    private TextView tvDriverMsg;
    private boolean isPhone;
    private Context context;

    public void setPhone(boolean phone) {
        isPhone = phone;
    }

    public AddDriverDialog(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_driver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getDialog().getContext();
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        ImageView close = view.findViewById(R.id.img_close);
        TextView confirm = view.findViewById(R.id.tv_confirm);
        tvDriverMsg = view.findViewById(R.id.tv_driver_msg);
        close.setOnClickListener(this);
        confirm.setOnClickListener(this);
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
        if (isPhone) {
            tvDriverMsg.setText(context.getString(R.string.add_driver_phone));
        } else {
            tvDriverMsg.setText(context.getString(R.string.add_driver_tip));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWindow != null) {
            mWindow.setBackgroundDrawableResource(R.color.white);
            mWindow.setLayout(DisplayUtils.dp2px(302.4f),
                    DisplayUtils.dp2px(183.36f));
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }


}
