package com.zhny.library.presenter.driver.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.zhny.library.R;
import com.zhny.library.databinding.ActivityEditDriverBinding;
import com.zhny.library.databinding.LayoutEditDriverTopViewBinding;
import com.zhny.library.presenter.driver.DriverConstants;
import com.zhny.library.presenter.driver.base.MyDriverBaseActivity;
import com.zhny.library.presenter.driver.dialog.AddDriverDialog;
import com.zhny.library.presenter.driver.listener.SaveDriverListener;
import com.zhny.library.presenter.driver.model.dto.DriverDto;
import com.zhny.library.presenter.driver.viewmodel.EditDriverViewmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditDriverActivity extends MyDriverBaseActivity implements TextWatcher, SaveDriverListener {

    private static final String TAG = "EditDriverActivity";

    private ActivityEditDriverBinding binding;
    private LayoutEditDriverTopViewBinding layoutEditDriverTopView;

    private EditDriverViewmodel viewModel;
    private EditText remarks;
    private EditText evDriverName;
    private AddDriverDialog addDriverDialog;
    private DriverDto driverDto;

    @Override
    public void initParams(Bundle params) {
        Serializable driverDto = params.getSerializable(DriverConstants.Bundle_Driver_Dto);
        if (driverDto instanceof DriverDto) {
            this.driverDto = (DriverDto) driverDto;
        }
    }


    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(EditDriverViewmodel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_driver);
        layoutEditDriverTopView = binding.layoutEditDriverTopView;
        layoutEditDriverTopView.setViewModel(viewModel);

        evDriverName = layoutEditDriverTopView.evDriverName;
        remarks = layoutEditDriverTopView.remarks;
        remarks.addTextChangedListener(this);

        addDriverDialog = new AddDriverDialog(getString(R.string.edit_driver));
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        setToolBarTitle(getString(R.string.edit_driver));
        binding.setLifecycleOwner(this);
        binding.setSaveDriverListener(this);
        viewModel.enterLimit.setValue(getString(R.string.remarks_enter_limit_default));

        if (driverDto != null) {
            evDriverName.setText(driverDto.workerName);
            int index = TextUtils.isEmpty(driverDto.workerName) ? 0 : driverDto.workerName.length();
            evDriverName.setSelection(index);
            layoutEditDriverTopView.evDriverPhone.setText(driverDto.phoneNumber);
            remarks.setText(driverDto.remark);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged:" + s + " ，长度： " + s.toString().length());
        int length = s.toString().length();
        String enterLimit = length + getString(R.string.remarks_enter_limit);
        viewModel.enterLimit.setValue(enterLimit);
    }

    //页面 保存按钮点击事件
    @Override
    public void onSave() {
        String workerName = layoutEditDriverTopView.evDriverName.getText().toString().trim();
        String phoneNumber = layoutEditDriverTopView.evDriverPhone.getText().toString().trim();
        if (TextUtils.isEmpty(workerName) || TextUtils.isEmpty(phoneNumber)) {
            addDriverDialog.setPhone(false);
            addDriverDialog.show(getSupportFragmentManager(), null);
            return;
        }
        if (phoneNumber.length() != 11) {
            addDriverDialog.setPhone(true);
            addDriverDialog.show(getSupportFragmentManager(), null);
            return;
        }
        JSONArray deviceWorkerList = new JSONArray();
        JSONObject deviceWorker = new JSONObject();

        try {
            deviceWorker.put(DriverConstants.Json_isDel, driverDto.isDel);
            deviceWorker.put(DriverConstants.Json_isEnable, driverDto.isEnable);
            deviceWorker.put(DriverConstants.Json_organizationId, driverDto.organizationId);
            deviceWorker.put(DriverConstants.Json_phoneNumber, phoneNumber);
            deviceWorker.put(DriverConstants.Json_remark, remarks.getText().toString());
            deviceWorker.put(DriverConstants.Json_tenantId, driverDto.tenantId);
            deviceWorker.put(DriverConstants.Json_type, DriverConstants.Type_Edit);
            deviceWorker.put(DriverConstants.Json_workerCode, driverDto.workerCode);
            deviceWorker.put(DriverConstants.Json_workerId, driverDto.workerId);
            deviceWorker.put(DriverConstants.Json_workerName, workerName);
            deviceWorker.put(DriverConstants.Json_objectVersionNumber, driverDto.objectVersionNumber);

            deviceWorkerList.put(deviceWorker);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                    , deviceWorkerList.toString());

            viewModel.saveDriver(requestBody).observe(this, baseDto -> {
                if (baseDto.getContent() != null && baseDto.getContent()) {
                    Toast.makeText(this, getString(R.string.toast_edit_success)
                            , Toast.LENGTH_SHORT).show();
                    //setResult
                    Intent intent = new Intent();
                    intent.putExtra(DriverConstants.Intent_Worker_Name, workerName);
                    intent.putExtra(DriverConstants.Intent_Phone_Number, phoneNumber);
                    intent.putExtra(DriverConstants.Intent_Remark, remarks.getText().toString());
                    setResult(DriverConstants.Result_Code_EditDriverActivity, intent);
                    finish();
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.toast_edit_fail)
                    , Toast.LENGTH_SHORT).show();
        }

    }


}
