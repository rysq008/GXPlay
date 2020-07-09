package com.zhny.library.presenter.login.view;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityLoginBinding;
import com.zhny.library.presenter.login.viewmodel.LoginViewModel;
import com.zhny.library.presenter.monitor.view.MonitorActivity;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends BaseActivity {


    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private boolean isHasPermission;


    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);

        requestPermission();
    }

    //请求权限
    private void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE)
                .subscribe(granted -> {
                    isHasPermission = granted;
                    if (!granted) {
                        Toast.makeText(this, "获取权限失败!", Toast.LENGTH_SHORT).show();
                    }
                }));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) compositeDisposable.dispose();
        if (binding != null) binding.unbind();
    }

    public void login(View view) {
        if (!isHasPermission) {
            requestPermission();
            return;
        }

        String username = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入账号!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        Map<String, String> params = new HashMap<>(5);
        params.put("grant_type", Constant.Server.GRANT_TYPE);
        params.put("client_id", Constant.Server.CLIENT_ID);
        params.put("client_secret", Constant.Server.CLIENT_SECRET);
        params.put("username", username);
        params.put("password", password);

        viewModel.getToken(loadingDialog, params).observe(this, tokenInfoDto -> {
            if (tokenInfoDto != null && !TextUtils.isEmpty(tokenInfoDto.getAccess_token())) {
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.TOKEN, tokenInfoDto.getAccess_token());
                //获取数据
                getUserInfo(password);
            } else {
                dismissLoading();
                Toast.makeText(this, getString(R.string.oauth_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserInfo(String password) {
        viewModel.getUserInfo(loadingDialog).observe(this, userInfoDto -> {
            dismissActualLoading();

            if (userInfoDto != null) {
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.USER_ID, userInfoDto.getId());
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.USERNAME, userInfoDto.getName());
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.PASSWORD, password);
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.ORGANIZATION_ID, userInfoDto.getOrganizationId());
                startActivity(MonitorActivity.class);
            } else {
                Toast.makeText(this, "获取用户信息失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void mapToLogin(View view) {
        startActivity(MapTestActivity.class);
    }


    public void driver(View view) {

    }

}