package com.zhny.library.presenter.driver.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhny.library.R;
import com.zhny.library.databinding.ActivityDriverDetailsBinding;
import com.zhny.library.presenter.driver.DriverConstants;
import com.zhny.library.presenter.driver.base.MyDriverBaseActivity;
import com.zhny.library.presenter.driver.model.dto.DriverDto;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


public class DriverDetailsActivity extends MyDriverBaseActivity {

    private ActivityDriverDetailsBinding binding;

    private DriverDto driverDto;

    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            Serializable driverDto = params.getSerializable(DriverConstants.Bundle_Driver_Dto);
            if (driverDto instanceof DriverDto) {
                this.driverDto = (DriverDto) driverDto;
            }

        }
    }


    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_details);
        return binding.getRoot();
    }

    @Override
    protected boolean isShowEdit() {
        return true;
    }

    @Override
    public void initBusiness() {
        setToolBarTitle(getString(R.string.driver_details));
        binding.setLifecycleOwner(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        if (driverDto != null) {
            binding.setDriverDto(driverDto);
            binding.layoutDriverDetailsTopView.setDriverDto(driverDto);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    @Override
    protected void onEditListener() {
        super.onEditListener();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DriverConstants.Bundle_Driver_Dto, driverDto);
        startActivityForResult(EditDriverActivity.class, bundle,
                DriverConstants.Request_Code_DriverDetailsActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DriverConstants.Request_Code_DriverDetailsActivity
                && resultCode == DriverConstants.Result_Code_EditDriverActivity) {
            if (data != null) {
                driverDto.workerName = data.getStringExtra(DriverConstants.Intent_Worker_Name);
                driverDto.phoneNumber = data.getStringExtra(DriverConstants.Intent_Phone_Number);
                String remark = data.getStringExtra(DriverConstants.Intent_Remark);
                remark = TextUtils.isEmpty(remark) ? "" : remark;
                driverDto.remark = remark;
            }
        }
    }
}
