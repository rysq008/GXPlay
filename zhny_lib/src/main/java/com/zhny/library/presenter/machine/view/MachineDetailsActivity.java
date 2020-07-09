package com.zhny.library.presenter.machine.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.databinding.ActivityMachineDetailsBinding;
import com.zhny.library.presenter.machine.MachineConstants;
import com.zhny.library.presenter.machine.model.dto.MachineDetailsDto;
import com.zhny.library.presenter.machine.model.dto.MachineDto;
import com.zhny.library.presenter.machine.util.MachineUtil;
import com.zhny.library.presenter.machine.viewmodel.MachineDetailsViewmodel;

import java.io.Serializable;
import java.text.DecimalFormat;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


public class MachineDetailsActivity extends BaseActivity {

    private ActivityMachineDetailsBinding binding;
    private MachineDetailsViewmodel viewModel;
    private MachineDto machineDto;

    private MachineDetailsDto machineDetailsDto;

    @Override
    public void initParams(Bundle params) {
        Serializable machineDto = params.getSerializable(MachineConstants.BUNDLE_MACHINEDTO);
        if (machineDto instanceof MachineDto) {
            this.machineDto = (MachineDto) machineDto;
        }
    }


    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(MachineDetailsViewmodel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_machine_details);
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
        setToolBarTitle(getString(R.string.machine_details));
        binding.setLifecycleOwner(this);
        initView();
    }

    private void initView() {
        if (machineDto != null) {
            binding.setMachineDto(machineDto);

            //type
            String type = machineDto.productType;
            if (!TextUtils.isEmpty(type)) {
                //show width
                int widthVisible = TextUtils.equals(type, getString(R.string.tractor)) ? View.GONE : View.VISIBLE;
                binding.layoutWidth.setVisibility(widthVisible);
            }
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        //请求机具详情数据
        if (!TextUtils.isEmpty(machineDto.sn)) {
            viewModel.getMachineDetails(machineDto.sn).observe(this, baseDto -> {
                if (baseDto.getContent() != null) {
                    machineDetailsDto = baseDto.getContent();
                    refreshData();
                }

            });
        }
    }

    private void refreshData() {
        String horsepower = MachineUtil.queryValueFromList(getString(R.string.horsepower)
                , machineDetailsDto.propertyList);
        if (!TextUtils.isEmpty(horsepower)) {
            viewModel.horsepower.setValue(horsepower + getString(R.string.horsepower_company));
        }

        viewModel.produceDate.setValue(MachineUtil.getYMD(machineDetailsDto.outDate));

        viewModel.produceNumber.setValue(machineDetailsDto.code);
        viewModel.carNumber.setValue(machineDetailsDto.lpn);
        String width = MachineUtil.queryValueFromList(getString(R.string.width)
                , machineDetailsDto.propertyList);

        if (!TextUtils.isEmpty(width)) {
            try {
                double d = Double.parseDouble(width);
                d = d / 100;
                DecimalFormat df = new DecimalFormat("#.00");
                String format = df.format(d);
                viewModel.width.setValue(format + getString(R.string.company_m));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        viewModel.monitorTerminalID.setValue(machineDetailsDto.tsn);
        String creationDate = machineDetailsDto.creationDate;
        creationDate = MachineUtil.getYMD(creationDate);
        viewModel.creationDate.setValue(creationDate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

}
