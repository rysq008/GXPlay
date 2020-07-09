package com.zhny.library.presenter.data.dialog;

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
import com.zhny.library.databinding.BottomPopDataMachineBinding;
import com.zhny.library.presenter.data.adapter.DataDeviceAdapter;
import com.zhny.library.presenter.data.listener.OnDataDeviceAddViewListener;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.util.TableDataHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 选择添加机具
 *
 * created by liming
 */
public class DataDevicePopWin extends PopupWindow implements OnDataDeviceAddViewListener, DataDeviceAdapter.OnDataMachineListener {

    private DataDeviceAdapter dataDeviceAdapter;
    private OnDataAddMachineListener onAddMachineListener;
    private BottomPopDataMachineBinding binding;
    private Window window;
    private List<DataDeviceDto> data = new ArrayList<>();


    @SuppressLint("ClickableViewAccessibility")
    public DataDevicePopWin(Context context, OnDataAddMachineListener onAddMachineListener) {
        super(context);
        this.onAddMachineListener = onAddMachineListener;
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_pop_data_machine, null, false);
        binding.setOnDataDeviceAddViewListener(this);
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

//        setOutsideTouchable(true);
        setOutsideTouchable(false);
        binding.getRoot().setOnTouchListener((v, event) -> {
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < binding.getRoot().getHeight()) {
                    return true;
                }
            }
            return true;
        });


        dataDeviceAdapter = new DataDeviceAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvBottomPopFenceAddMachine.setLayoutManager(manager);
        binding.rvBottomPopFenceAddMachine.setAdapter(dataDeviceAdapter);
    }


    public void show(View parent, Window window) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        this.window = window;
        // 设置背景变暗
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
        binding.setHasContent(false);
    }

    public void refreshData(boolean hasSelected, List<DataDeviceDto> params) {
        binding.setHasContent(true);
        //刷新页面数据
        data.clear();
        data.addAll(params);
        dataDeviceAdapter.refresh(params);
        binding.setShowOk(hasSelected);
    }



    @Override
    public void onAddMachineListener() {
        //点击界面添加按钮
        String snList = TableDataHelper.getDevicesSn(data);
        onAddMachineListener.onAddMachineListener(snList);
        dismiss();
    }

    @Override
    public void onViewCloseListener() {
        this.dismiss();
    }

    @Override
    public void onCheckMachineListener(DataDeviceDto selectDto) {
        if (data != null) {
            for (DataDeviceDto dto : data) {
                if (TextUtils.equals(dto.sn, selectDto.sn)) {
                    if (dto.checkType == 0) dto.checkType = 1;
                    else if (dto.checkType == 1) dto.checkType = 0;
                    break;
                }
            }
            dataDeviceAdapter.refresh(data);
            binding.setShowOk(!TextUtils.isEmpty(TableDataHelper.getDevicesSn(data)));
        }
    }


    //让activity实现
    public interface OnDataAddMachineListener {
        void onAddMachineListener(String snList);
    }

}
