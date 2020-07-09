package com.zhny.library.presenter.fence.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.zhny.library.R;
import com.zhny.library.databinding.ActivityFenceInfoBinding;
import com.zhny.library.presenter.driver.dialog.DeleteDialog;
import com.zhny.library.presenter.fence.FenceConstant;
import com.zhny.library.presenter.fence.adapter.FenceInfoMachineAdapter;
import com.zhny.library.presenter.fence.base.FenceBaseActivity;
import com.zhny.library.presenter.fence.custom.FenceAddMachinePopWin;
import com.zhny.library.presenter.fence.helper.FenceHelper;
import com.zhny.library.presenter.fence.listener.OnFenceInfoViewListener;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;
import com.zhny.library.presenter.fence.model.vo.FenceVo;
import com.zhny.library.presenter.fence.viewmodel.FenceInfoViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class FenceInfoActivity extends FenceBaseActivity implements FenceAddMachinePopWin.OnFenceAddMachineListener,
        OnFenceInfoViewListener, OnItemMenuClickListener, DeleteDialog.OnDeleteListener {

    private ActivityFenceInfoBinding binding;
    private FenceInfoViewModel viewModel;
    private DeleteDialog deleteDialog;
    private FenceAddMachinePopWin fenceAddMachinePopWin;
    private FenceInfoMachineAdapter fenceInfoMachineAdapter;
    private List<FenceMachine> fenceMachines = new ArrayList<>();
    private Fence fence;
    private Integer fenceId;
    private String fenceDrawPointsArr, fenceDrawPoints;
    private boolean isAdd;

    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            isAdd = params.getBoolean(FenceConstant.IS_ADD, false);
            fenceId = params.getInt(FenceConstant.FENCE_ID, -1);
            fenceDrawPoints = params.getString(FenceConstant.FENCE_DRAW_POINTS, null);
            fenceDrawPointsArr = new JSONArray().put(fenceDrawPoints).toString();
            params.clear();
        }
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(FenceInfoViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fence_info);
        binding.setOnFenceInfoListener(this);
        binding.srvFenceInfoLinkedMachine.setSwipeMenuCreator(swipeMenuCreator);
        binding.srvFenceInfoLinkedMachine.setOnItemMenuClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.fence_info_title));
        binding.setViewModel(viewModel);

        fenceInfoMachineAdapter = new FenceInfoMachineAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.srvFenceInfoLinkedMachine.setLayoutManager(manager);
        binding.srvFenceInfoLinkedMachine.setAdapter(fenceInfoMachineAdapter);

        fenceAddMachinePopWin = new FenceAddMachinePopWin(this, this);

        deleteDialog = new DeleteDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: isAdd ==> " + isAdd + " , fenceId ==> " + fenceId + " , fenceDrawPoints" + fenceDrawPoints);
        viewModel.setEditFence(!isAdd);
        if (isAdd) { //添加
            fence = new Fence();
        } else { //编辑
            getFenceDetails(fenceId);
            //显示删除按钮
            showDelete();
        }
    }

    @Override
    protected void onDeleteListener() {
        boolean hasMachines = fence.fenceMachines != null && fence.fenceMachines.size() > 0;
        deleteDialog.setParams(hasMachines ? 2 : 3, fenceId);
        deleteDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onDelete(Object o) {
        disableFence((Integer) o);
    }

    //点击了确认删除按钮
    private void disableFence(int fenceId) {
        viewModel.disableFence(fenceId).observe(this, baseDto -> {
            if (baseDto.getContent() != null && baseDto.getContent()) {
                binding.srvFenceInfoLinkedMachine.smoothCloseMenu();
                Toast.makeText(this, getString(R.string.fence_disable_success), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    //查询围栏详情数据
    private void getFenceDetails(int fenceId) {
        viewModel.getFenceDetails(fenceId).observe(this, baseDto -> {
            if (baseDto.getContent() != null) {
                fence = baseDto.getContent();
                fence.fenceMachines = FenceHelper.getFenceMachines(baseDto.getContent());
                fenceMachines.clear();
                fenceMachines.addAll(fence.fenceMachines);
                fenceInfoMachineAdapter.refresh(fenceMachines);
                viewModel.setFenceData(fence);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
        fenceMachines.remove(adapterPosition);
        fenceInfoMachineAdapter.refresh(fenceMachines);
    }

    @Override
    public void onAddMachineListener(List<FenceMachine> params, Fence fence) {
        //添加机具的回调
        fenceAddMachinePopWin.dismiss();
        if (params != null && params.size() > 0) {
            List<String> paramSns = FenceHelper.getDevicesSns(params);
            List<String> devicesSns = FenceHelper.getDevicesSns(fenceMachines);
            for (String sn : paramSns) {
                if (devicesSns.contains(sn)) {
                    continue;
                }
                FenceMachine fenceMachine = FenceHelper.getFenceMachine(params, sn);
                if (fenceMachine != null) {
                    fenceMachines.add(fenceMachine);
                }
            }
        }
        fenceInfoMachineAdapter.refresh(fenceMachines);
    }

    @Override
    public void onAddMachineListener() {
        //添加选择的机具
        getFenceCanAddDevices();
    }

    //查询围栏机具
    private void getFenceCanAddDevices() {
        if (isAdd) fenceId = null;
        //显示添加界面
        if (!fenceAddMachinePopWin.isShowing()) {
            fenceAddMachinePopWin.show(binding.getRoot(), getWindow());
        }
        //请求数据
        viewModel.getFenceCanAddDevices(fenceId, fenceDrawPointsArr).observe(this, baseDto -> {
            fence.fenceSelectMachines = baseDto.getContent();
            fenceAddMachinePopWin.refreshData(fence);
        });
    }

    @Override
    public void onFenceSaveListener() {
        //点击保存按钮
        String fenceName = binding.etFenceInfoName.getText().toString().trim();
        if (TextUtils.isEmpty(fenceName)) {
            Toast.makeText(this, getString(R.string.fence_add_input_name), Toast.LENGTH_SHORT).show();
            return;
        }
        FenceVo fenceVo = new FenceVo();
        fenceVo.name = fenceName;
        fenceVo.deviceSns = FenceHelper.getDevicesSns(fenceMachines);

        fenceVo.points = fenceDrawPointsArr;
        fenceVo.pointList = Collections.singletonList(fenceDrawPoints);

        if (isAdd) {
            viewModel.addFence(fenceVo).observe(this, baseDto -> {
                if (baseDto.getContent() != null) {
                    Toast.makeText(this, getString(R.string.fence_add_success), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            fenceVo.geofenceId = fence.geofenceId;
            fenceVo.objectVersionNumber = fence.objectVersionNumber;

            viewModel.updateFence(fenceVo).observe(this, baseDto -> {
                if (baseDto.getContent() != null && baseDto.getContent()) {
                    Toast.makeText(this, getString(R.string.fence_edit_success), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

}
