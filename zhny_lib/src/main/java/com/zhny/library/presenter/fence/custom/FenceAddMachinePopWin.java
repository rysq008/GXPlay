package com.zhny.library.presenter.fence.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zhny.library.R;
import com.zhny.library.databinding.BottomPopFenceAddMachineBinding;
import com.zhny.library.presenter.fence.adapter.FenceAddMachineAdapter;
import com.zhny.library.presenter.fence.helper.FenceHelper;
import com.zhny.library.presenter.fence.listener.OnFenceAddViewListener;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;

import java.util.ArrayList;
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
public class FenceAddMachinePopWin extends PopupWindow implements OnFenceAddViewListener, FenceAddMachineAdapter.OnFenceCheckMachineListener {

    private static final int LOADING = -1;
    private static final int HAS_CONTENT = 1;
    private static final int NO_CONTENT = 0;

    private FenceAddMachineAdapter fenceAddMachineAdapter;
    private BottomPopFenceAddMachineBinding binding;
    private Window window;
    private OnFenceAddMachineListener onFenceAddMachineListener;
    private List<Object> data = new ArrayList<>();
    private Fence fence;


    @SuppressLint("ClickableViewAccessibility")
    public FenceAddMachinePopWin(Context context, final OnFenceAddMachineListener listener) {
        super(context);
        this.onFenceAddMachineListener = listener;
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_pop_fence_add_machine, null, false);
        binding.setOnFenceAddViewListener(this);
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


        fenceAddMachineAdapter = new FenceAddMachineAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvBottomPopFenceAddMachine.setLayoutManager(manager);
        binding.rvBottomPopFenceAddMachine.setAdapter(fenceAddMachineAdapter);
    }


    public void show(View parent, Window window) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        this.window = window;
//        // 设置背景变暗
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
        //正在加载显示 ProgressBar
        binding.setHasContent(LOADING);
    }

    public void refreshData(Fence fence) {
        this.fence = fence;
        //刷新页面数据
        if (fence.fenceSelectMachines == null || fence.fenceSelectMachines.size() == 0) {
            //显示无数据图片
            binding.setHasContent(NO_CONTENT);
        } else {
            //有数据显示RecycleView
            binding.setHasContent(HAS_CONTENT);
            List<Object> objects = FenceHelper.convertData(fence.fenceSelectMachines);
            if (objects != null) {
                data.clear();
                data.addAll(objects);
                fenceAddMachineAdapter.refresh(objects);
            }
        }
    }


    @Override
    public void onCheckMachineListener(FenceMachine selectMachine) {
        for (Object o : data) {
            if (o instanceof FenceMachine) {
                FenceMachine machine = (FenceMachine) o;
                if (TextUtils.equals(machine.sn, selectMachine.sn)) {
                    if (machine.checkType == 0) machine.checkType = 1;
                    else if (machine.checkType == 1) machine.checkType = 0;
                    break;
                }
            }
        }
        fenceAddMachineAdapter.refresh(data);
    }

    @Override
    public void onAddMachineListener() {
        //点击界面添加按钮
        List<FenceMachine> selectMachines = FenceHelper.getSelectMachines(data);
        onFenceAddMachineListener.onAddMachineListener(selectMachines, fence);
    }

    @Override
    public void onViewCloseListener() {
        this.dismiss();
    }


    //让activity实现
    public interface OnFenceAddMachineListener {
        void onAddMachineListener(List<FenceMachine> selectMachines, Fence fence);
    }

}
