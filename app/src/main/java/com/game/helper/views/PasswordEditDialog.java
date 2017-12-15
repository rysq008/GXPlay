package com.game.helper.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.game.helper.R;
import com.game.helper.utils.StringUtils;
import com.game.helper.views.widget.PasswordEditText;

/**
 * Created by sung on 2017/12/15.
 * 输入交易密码弹窗
 */

public class PasswordEditDialog extends android.support.v4.app.DialogFragment implements PasswordEditText.OnEditCompleteListener,View.OnClickListener{
    public static final String TAG = PasswordEditDialog.class.getSimpleName();

    private TextView tittle;
    private TextView content;
    private TextView cancel;
    private TextView confirm;
    private PasswordEditText passwordEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL , R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_trade_password,container,false);
        tittle = view.findViewById(R.id.dialog_tittle);
        content = view.findViewById(R.id.dialog_content);
        cancel = view.findViewById(R.id.dialog_cancel);
        confirm = view.findViewById(R.id.dialog_confirm);
        passwordEditText = view.findViewById(R.id.dialog_password_edit_text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCancelable(false);
        passwordEditText.addOnEditCompleteListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == cancel) {
            getDialog().dismiss();
        }
        if (v == confirm) {
            passWordComplete(passwordEditText.getText().toString());
        }
    }

    /**
     * 需要自动完成时使用
     * */
    @Override
    public void onEditComplete(String password) {
        Log.e(TAG, "onEditComplete: "+password );
    }

    /**
     * 手动完成时补充
     * */
    private void passWordComplete(String password){
        Log.e(TAG, "onEditComplete: "+password );
    }

    public void setTittleText(String tittle){
        if (StringUtils.isEmpty(tittle)) return;
        this.tittle.setText(tittle);
    }

    public void setContentText(String content){
        if (StringUtils.isEmpty(content)) return;
        this.content.setText(content);
    }
}
