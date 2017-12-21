package com.game.helper.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.utils.ScreenUtils;
import com.game.helper.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2017/12/15.
 * 共用dialog
 */

@SuppressLint("ValidFragment")
public class AvatarEditDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    public static final String TAG = AvatarEditDialog.class.getSimpleName();
    private onDialogActionListner onDialogActionListner;

    private boolean mPermission = false;

    private View mChoosePic;
    private View mTakePhoto;
    private View mCancel;

    @SuppressLint("ValidFragment")
    public AvatarEditDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_avatar_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCancelable(true);
        mChoosePic = view.findViewById(R.id.tv_choose_pic);
        mTakePhoto = view.findViewById(R.id.tv_take_photo);
        mCancel = view.findViewById(R.id.tv_cancel);
        mChoosePic.setOnClickListener(this);
        mTakePhoto.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() == null) return;
        if (!getDialog().isShowing()) return;
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = ScreenUtils.getScreenWidth(getContext());
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        if (v == mCancel) {
            onDialogActionListner.onCancel();
            getDialog().dismiss();
            return;
        }
        if (!mPermission) mPermission = checkCropPermission();
        getDialog().dismiss();
        if (!mPermission) {
            Toast.makeText(getContext(), "权限不足哦", Toast.LENGTH_SHORT).show();
            return;
        }
        if (onDialogActionListner == null) return;
        if (v == mTakePhoto){
            onDialogActionListner.onTakePhoto();
        }
        if (v == mChoosePic){
            onDialogActionListner.onChoosePic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RxConstant.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mPermission = true;
                break;
            default:
                break;
        }
    }

    private boolean checkCropPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(getActivity(),
                        (String[]) permissions.toArray(new String[0]),
                        RxConstant.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    public void addOnDialogActionListner(onDialogActionListner onDialogActionListner) {
        this.onDialogActionListner = onDialogActionListner;
    }

    public interface onDialogActionListner {
        void onCancel();
        void onTakePhoto();
        void onChoosePic();
    }
}
