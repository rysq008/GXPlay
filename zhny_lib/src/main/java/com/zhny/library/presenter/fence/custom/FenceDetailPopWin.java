package com.zhny.library.presenter.fence.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;

import com.zhny.library.R;
import com.zhny.library.databinding.BottomPopFenceDetailBinding;
import com.zhny.library.presenter.fence.adapter.FenceDetailAdapter;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * hasContent:
 *     -1   ： 展示 progressBar
 *      0   ： 无数据
 *      1   ： 有数据，展示RecycleView
 *
 * created by liming
 */
public class FenceDetailPopWin extends PopupWindow {

    private static final int LOADING = -1;
    private static final int HAS_CONTENT = 1;
    private static final int NO_CONTENT = 0;

    private FenceDetailAdapter fenceDetailAdapter;
    private BottomPopFenceDetailBinding binding;
    private Fence fence;
    private Window window;


    @SuppressLint("ClickableViewAccessibility")
    public FenceDetailPopWin(Context context, final OnFenceDetailPopWinListener listener) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_pop_fence_detail, null, false);
        binding.setOnFenceDetailPopWinClick(listener);
        this.setContentView(binding.getRoot());
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.bottomPopWinAnim);
        // 设置自定义PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
//         popup window 消失，恢复透明度
        this.setOnDismissListener(() -> {
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.alpha = 1f;
//            window.setAttributes(lp);

            listener.onAddDismiss(fence);
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


        fenceDetailAdapter = new FenceDetailAdapter(listener);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvBottomPopFenceDetail.setLayoutManager(manager);
        binding.rvBottomPopFenceDetail.setAdapter(fenceDetailAdapter);

    }


    public void show(View parent, Window window, Fence fence) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        this.window = window;
        this.fence = fence;
        // 设置背景变暗
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 1f;
//        window.setAttributes(lp);

        binding.setFence(fence);

        //正在加载显示 ProgressBar
        binding.setHasContent(LOADING);
    }

    public void refreshData(List<FenceMachine> fenceMachines) {
        if (fenceMachines == null || fenceMachines.size() == 0) {
            //显示无数据图片
            binding.setHasContent(NO_CONTENT);
        } else {
            //有数据显示RecycleView
            binding.setHasContent(HAS_CONTENT);
            //刷新页面数据
            fenceDetailAdapter.refresh(fenceMachines);
        }
    }

    public interface OnFenceDetailPopWinListener {
        //dismiss
        void onAddDismiss(Fence fence);
        //点击添加农机按钮
        void onFenceDetailPopWinAdd(Fence fence);
        //点击编辑按钮
        void onFenceDetailPopWinEdit(Fence fence);
        //点击删除某个机具
        void onMachineDeleteListener(FenceMachine dto);
    }

}
