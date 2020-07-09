package com.zhny.library.presenter.playback.customer;

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
import com.zhny.library.databinding.SingleBottomPopBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;

/**
 * created by liming
 */
public class SingleBottomPopWin extends PopupWindow implements View.OnClickListener {

    private OnSingleBottomPopWinListener listener;
    private SingleBottomPopBinding binding;
    private Context context;
    private Window window;
    private int type;
    private List<String> data;


    @SuppressLint("ClickableViewAccessibility")
    public SingleBottomPopWin(Context context, List<String> data, final OnSingleBottomPopWinListener listener) {
        super(context);
        this.context = context;
        this.data = data;
        this.listener = listener;
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.single_bottom_pop, null, false);
        binding.singleBottomPopCancel.setOnClickListener(v -> dismiss());
        binding.singleBottomPopOk.setOnClickListener(this);
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


    /**
     * @param type 1 : 选择速度
     *             2 ：选择排序方式
     */
    public void show(View parent, Window window, int defaultIndex, int type) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        if (type == 1) {
            binding.tvSingleSelectTitle.setText(context.getString(R.string.bottom_pop_select_speed));
        } else if (type == 2) {
            binding.tvSingleSelectTitle.setText(context.getString(R.string.bottom_pop_select_sort));
        }
        binding.ssvSingleBottomPop.refreshData(defaultIndex, data);
        this.window = window;
        this.type = type;
        // 设置背景变暗
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        listener.onSingleBottomPopWinClick(type, binding.ssvSingleBottomPop.getSelectedIndex());
    }

    public interface OnSingleBottomPopWinListener {
        void onSingleBottomPopWinClick(int type, int index);
    }

}
