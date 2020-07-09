package com.zhny.library.presenter.login.viewmodel;


import android.content.Context;

import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.login.model.dto.TokenInfoDto;
import com.zhny.library.presenter.login.model.dto.UserInfoDto;
import com.zhny.library.presenter.login.repository.impl.LoginRepository;
import com.zhny.library.presenter.login.repository.ILoginRepository;

import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * 登录页面
 */
public class LoginViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }


    public LiveData<TokenInfoDto> getToken(LoadingDialog dialog, Map<String, String> params){
        ILoginRepository loginRepository = new LoginRepository();
        return loginRepository.getToken(context, dialog, params);
    }

    public LiveData<UserInfoDto> getUserInfo(LoadingDialog dialog){
        ILoginRepository loginRepository = new LoginRepository(dialog, context);
        return loginRepository.getUserInfo();
    }


}
