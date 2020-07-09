package com.zhny.library.presenter.driver.view;

import android.os.Bundle;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.zhny.library.R;
import com.zhny.library.databinding.ActivityDriverListBinding;
import com.zhny.library.presenter.driver.DriverConstants;
import com.zhny.library.presenter.driver.adapter.DriverListAdapter;
import com.zhny.library.presenter.driver.base.MyDriverBaseActivity;
import com.zhny.library.presenter.driver.dialog.DeleteDialog;
import com.zhny.library.presenter.driver.listener.AddDriverListener;
import com.zhny.library.presenter.driver.model.dto.DriverDto;
import com.zhny.library.presenter.driver.viewmodel.DriverListViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class DriverListActivity extends MyDriverBaseActivity implements DriverListAdapter.DriverItemClickListener
        , AddDriverListener, DeleteDialog.OnDeleteListener, OnItemMenuClickListener, OnRefreshListener {

    private ActivityDriverListBinding binding;
    private DriverListViewModel viewModel;
    private DriverListAdapter adapter;
    private DeleteDialog deleteDialog;
    List<DriverDto> dataList = new ArrayList<>();

    @Override
    public void initParams(Bundle params) {

    }


    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(DriverListViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_list);
        binding.setViewModel(viewModel);
        deleteDialog = new DeleteDialog(this);
        binding.driverList.setSwipeMenuCreator(swipeMenuCreator);
        binding.driverList.setOnItemMenuClickListener(this);
        return binding.getRoot();
    }

    @Override
    protected boolean isShowAdding() {
        return true;
    }

    @Override
    protected void onAddListener() {
        startActivity(AddDriverActivity.class);
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        setToolBarTitle(getString(R.string.my_driver));
        binding.setLifecycleOwner(this);
        binding.setAddDriverListener(this);
        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        adapter = new DriverListAdapter(this);
        binding.driverList.setLayoutManager(manager);
        binding.driverList.setAdapter(adapter);

        binding.smoothRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestData(false);
    }

    private void requestData(boolean isDropDown) {
        if (!isDropDown) {
            //开始加载
            showLoading();
        }
        //请求机手数据
        viewModel.getDrivers().observe(this, baseDto -> {
            List<DriverDto> driverDtos = baseDto.getContent();
            if (driverDtos != null && driverDtos.size() > 0) {
                viewModel.emptyDriver.setValue(false);
                dataList.clear();
                dataList.addAll(driverDtos);
                adapter.refresh(dataList);
            } else {
                viewModel.emptyDriver.setValue(true);
            }
            if (isDropDown) {
                binding.smoothRefreshLayout.finishRefresh();
            } else {
                //关闭加载
                dismissLoading();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    @Override
    public void onDriverItemClick(DriverDto driverDto) {
        if (driverDto != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DriverConstants.Bundle_Driver_Dto, driverDto);
            startActivity(DriverDetailsActivity.class, bundle);
        }
    }

    @Override
    public void onAdd() {
        startActivity(AddDriverActivity.class);
    }


    @Override
    public void onDelete(Object o) {
        if (o != null) {
            DriverDto driverDto = (DriverDto) o;
            JSONArray deviceWorkerList = new JSONArray();
            JSONObject deviceWorker = new JSONObject();

            try {
                deviceWorker.put(DriverConstants.Json_objectVersionNumber, driverDto.objectVersionNumber);
                deviceWorker.put(DriverConstants.Json_isDel, driverDto.isDel);
                deviceWorker.put(DriverConstants.Json_isEnable, driverDto.isEnable);
                deviceWorker.put(DriverConstants.Json_organizationId, driverDto.organizationId);
                deviceWorker.put(DriverConstants.Json_phoneNumber, driverDto.phoneNumber);
                deviceWorker.put(DriverConstants.Json_remark, driverDto.remark);
                deviceWorker.put(DriverConstants.Json_tenantId, driverDto.tenantId);
                deviceWorker.put(DriverConstants.Json_type, DriverConstants.Type_Delete);
                deviceWorker.put(DriverConstants.Json_workerCode, driverDto.workerCode);
                deviceWorker.put(DriverConstants.Json_workerId, driverDto.workerId);
                deviceWorker.put(DriverConstants.Json_workerName, driverDto.workerName);
                deviceWorkerList.put(deviceWorker);

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                        , deviceWorkerList.toString());
                viewModel.saveDriver(requestBody).observe(this, baseDto -> {
                    if (baseDto.getContent() != null && baseDto.getContent()) {
                        binding.driverList.smoothCloseMenu();
                        Toast.makeText(this, getString(R.string.toast_delete_success) , Toast.LENGTH_SHORT).show();
                        requestData(false);
                    }

                });
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.toast_delete_fail)  , Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge, int position) {
        //右滑删除
        if (dataList .size() > 0 && position < dataList .size()) {
            deleteDialog.setParams(1, dataList.get(position));
            deleteDialog.show(getSupportFragmentManager(), null);
        }

    }

}
