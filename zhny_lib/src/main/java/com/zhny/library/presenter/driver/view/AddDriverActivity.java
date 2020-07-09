package com.zhny.library.presenter.driver.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.R;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityAddDriverBinding;
import com.zhny.library.databinding.LayoutAddDriverTopViewBinding;
import com.zhny.library.presenter.driver.DriverConstants;
import com.zhny.library.presenter.driver.base.MyDriverBaseActivity;
import com.zhny.library.presenter.driver.dialog.AddDriverDialog;
import com.zhny.library.presenter.driver.listener.AddDriverListener;
import com.zhny.library.presenter.driver.viewmodel.AddDriverViewmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class AddDriverActivity extends MyDriverBaseActivity implements TextWatcher, AddDriverListener {

    private static final String TAG = "AddDriverActivity";

    private ActivityAddDriverBinding binding;
    private LayoutAddDriverTopViewBinding layoutAddDriverTopView;

    private AddDriverViewmodel viewModel;
    private EditText workerName;
    private EditText remarks;
    private AddDriverDialog addDriverDialog;

    @Override
    public void initParams(Bundle params) {

    }


    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(AddDriverViewmodel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_driver);
        layoutAddDriverTopView = binding.layoutAddDriverTopView;
        layoutAddDriverTopView.setViewModel(viewModel);
        workerName = layoutAddDriverTopView.evDriverName;
        remarks = layoutAddDriverTopView.remarks;
        remarks.addTextChangedListener(this);

        addDriverDialog = new AddDriverDialog(getString(R.string.add_driver));

        return binding.getRoot();
    }



    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        setToolBarTitle(getString(R.string.add_driver));
        binding.setLifecycleOwner(this);
        binding.setAddDriverListener(this);
        viewModel.enterLimit.setValue(getString(R.string.remarks_enter_limit_default));
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

    //页面 确定按钮点击事件
    @Override
    public void onAdd() {
        String driverName = layoutAddDriverTopView.evDriverName.getText().toString().trim();
        String driverPhone = layoutAddDriverTopView.evDriverPhone.getText().toString().trim();
        if (TextUtils.isEmpty(driverName)) {
            addDriverDialog.setPhone(false);
            addDriverDialog.show(getSupportFragmentManager(), null);
            return;
        }
        if (driverPhone.length() != 11) {
            addDriverDialog.setPhone(true);
            addDriverDialog.show(getSupportFragmentManager(), null);
            return;
        }

        JSONArray deviceWorkerList = new JSONArray();
        JSONObject deviceWorker = new JSONObject();
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME)
                .getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        try {
            deviceWorker.put(DriverConstants.Json_isDel, DriverConstants.IsDel_No);
            deviceWorker.put(DriverConstants.Json_isEnable, DriverConstants.IsEnable_Yes);
            deviceWorker.put(DriverConstants.Json_organizationId, organizationId);
            deviceWorker.put(DriverConstants.Json_phoneNumber, driverPhone);
            deviceWorker.put(DriverConstants.Json_remark, remarks.getText().toString());
            deviceWorker.put(DriverConstants.Json_type, DriverConstants.Type_Add);
            deviceWorker.put(DriverConstants.Json_workerName, workerName.getText().toString());

            deviceWorkerList.put(deviceWorker);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                    , deviceWorkerList.toString());

            viewModel.saveDriver(requestBody).observe(this, baseDto -> {
                if (baseDto.getContent() != null && baseDto.getContent()) {
                    Toast.makeText(this, getString(R.string.toast_add_success)
                            , Toast.LENGTH_SHORT).show();
                    finish();

                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.toast_add_fail)
                    , Toast.LENGTH_SHORT).show();
        }

    }


}
