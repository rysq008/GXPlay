package com.zhny.library.presenter.machine.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.databinding.ActivityMachineListBinding;
import com.zhny.library.presenter.machine.MachineConstants;
import com.zhny.library.presenter.machine.adapter.MachineAdapter;
import com.zhny.library.presenter.machine.helper.MachineDataHelper;
import com.zhny.library.presenter.machine.listener.ItemClickListener;
import com.zhny.library.presenter.machine.model.dto.MachineDto;
import com.zhny.library.presenter.machine.util.MachineUtil;
import com.zhny.library.presenter.machine.viewmodel.MachineListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MachineListActivity extends BaseActivity implements ItemClickListener, OnRefreshListener {

    private static final String TAG = "MachineListActivity";

    private ActivityMachineListBinding binding;
    private MachineListViewModel viewModel;
    private MachineAdapter adapter;
    List<MachineDto> data = new ArrayList<>();
    List<Object> dataListAfterHandle = new ArrayList<>();


    @Override
    public void initParams(Bundle params) {


    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(MachineListViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_machine_list);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.machine_list));
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        adapter = new MachineAdapter(this);
        binding.machineList.setLayoutManager(gridLayoutManager);
        binding.machineList.setAdapter(adapter);

        binding.smoothRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (data.size() == 0) {
            requestData(false);
        }
    }

    private void requestData(boolean isDropDown) {
        if (!isDropDown) {
            //开始加载
            showLoading();
        }
        //请求机具数据
        viewModel.getMachineList().observe(this, baseDto -> {
            if (baseDto.getContent() != null && baseDto.getContent().size() > 0) {
                data.clear();
                data.addAll(baseDto.getContent());
                refreshData();
            }
            if (isDropDown) {
                binding.smoothRefreshLayout.finishRefresh();
            } else {
                //关闭加载
                dismissLoading();
            }
        });
    }


    private void refreshData() {
        for (MachineDto machineDto : data) {
            if (TextUtils.equals(machineDto.productCategory, MachineConstants.PRODUCT_CATEGORY_1)) {
                if (TextUtils.isEmpty(machineDto.productType)) {
                    machineDto.productType = getString(R.string.unknown_machine);
                }
                machineDto.showTypeInList = machineDto.productType;
            } else if (TextUtils.equals(machineDto.productCategory, MachineConstants.PRODUCT_CATEGORY_2)) {
                machineDto.showTypeInList = getString(R.string.farm_implements);
            } else {
                machineDto.productCategory =  MachineConstants.PRODUCT_CATEGORY_3;
                machineDto.showTypeInList = getString(R.string.unknown);
            }
        }
        data = MachineUtil.sortMachineByProductCategory(data);
        if (data == null || data.size() == 0) return;
        //update data
        dataListAfterHandle.clear();
        dataListAfterHandle.addAll(MachineDataHelper.getDataAfterHandle(data));
        //after handle
        adapter.updateData(dataListAfterHandle);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();

    }


    @Override
    public void onItemClick(MachineDto machineDto) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MachineConstants.BUNDLE_MACHINEDTO, machineDto);
        startActivity(MachineDetailsActivity.class, bundle);
    }



}
