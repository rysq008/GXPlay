package com.zhny.library.presenter.login.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.api.LoginApiService;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.login.model.dto.LoginDto;
import com.zhny.library.presenter.login.model.dto.TokenInfoDto;
import com.zhny.library.presenter.login.model.dto.UserInfoDto;
import com.zhny.library.presenter.login.model.vo.LoginVo;
import com.zhny.library.presenter.login.repository.ILoginRepository;
import com.zhny.library.presenter.monitor.model.dto.LookUpVo;

import java.util.Map;

import androidx.lifecycle.LiveData;

/**
 * 登录
 */
public class LoginRepository extends BaseRepository implements ILoginRepository {

    private Context context;
    private LoadingDialog dialog;

    public LoginRepository() {
    }

    public LoginRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    @Override
    public LiveData<BaseDto<LoginDto>> login(LoginVo loginVo) {
        return request(RequestRetrofit.getInstance(context, dialog, LoginApiService.class).login(loginVo)).get();
    }

    @Override
    public LiveData<UserInfoDto> getUserInfo() {
        return requestNoContent(RequestRetrofit.getInstance(context, dialog, LoginApiService.class).getUserInfo()).get();
    }

    @Override
    public LiveData<TokenInfoDto> getToken(Context context, LoadingDialog dialog, Map<String, String> params) {
        return requestNoContent(RequestRetrofit.getInstance(context, dialog, LoginApiService.class).getToken(params)).get();
    }

    @Override
    public LiveData<BaseDto<LookUpVo>> queryFastCode(String organizationId, String fastCode) {
        return request(RequestRetrofit.getInstance(context, null, LoginApiService.class).queryFastCode(organizationId, fastCode)).get();
    }
}
