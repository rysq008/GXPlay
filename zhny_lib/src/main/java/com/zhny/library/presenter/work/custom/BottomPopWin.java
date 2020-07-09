package com.zhny.library.presenter.work.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zhny.library.R;
import com.zhny.library.databinding.BottomPopTrackSettingBinding;

import androidx.databinding.DataBindingUtil;

/**
 * created by liming
 */
public class BottomPopWin extends PopupWindow implements View.OnClickListener {

    private OnBottomPopWinListener listener;
    private BottomPopTrackSettingBinding binding;
    private Window window;
    private int type;


    @SuppressLint("ClickableViewAccessibility")
    public BottomPopWin(Context context, final OnBottomPopWinListener listener) {
        super(context);
        this.listener = listener;
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_pop_track_setting, null, false);
        binding.bottomPopTrackSettingCancel.setOnClickListener(v -> dismiss());
        binding.bottomPopTrackSettingOk.setOnClickListener(this);
        this.setContentView(binding.getRoot());
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.bottomPopWinAnim);
        // 设置自定义PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        // popup window 消失，恢复透明度
        this.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 1f;
            window.setAttributes(lp);
        });

        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        binding.getRoot().setOnTouchListener((v, event) -> {
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < binding.getRoot().getHeight()) {
                    dismiss();
                }
            }
            // setOutsideTouchable(true)则点击PopupWindow之外的地方PopupWindow会消失
            return true;
        });

    }


    public void show(View parent, Window window, int value, int type) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        if (type == 1) {
            binding.vsvBottomPopTrackSetting.refreshData(25, 45, value);
        } else if (type == 2) {
            binding.vsvBottomPopTrackSetting.refreshData(0, 10, value);
        } else if (type == 3) {
            binding.vsvBottomPopTrackSetting.refreshData(0, 20, value);
        }
        this.window = window;
        this.type = type;
        // 设置背景变暗
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        listener.onBottomPopWinClick(type, binding.vsvBottomPopTrackSetting.getSelectedValue());
    }

    public interface OnBottomPopWinListener {
        void onBottomPopWinClick(int type, int value);
    }

}
