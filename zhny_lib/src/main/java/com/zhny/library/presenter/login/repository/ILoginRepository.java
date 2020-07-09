package com.zhny.library.presenter.login.repository;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.login.model.dto.LoginDto;
import com.zhny.library.presenter.login.model.dto.TokenInfoDto;
import com.zhny.library.presenter.login.model.dto.UserInfoDto;
import com.zhny.library.presenter.login.model.vo.LoginVo;
import com.zhny.library.presenter.monitor.model.dto.LookUpVo;

import java.util.Map;

import androidx.lifecycle.LiveData;

/**
 * 登录
 */
public interface ILoginRepository {

    LiveData<BaseDto<LoginDto>> login(LoginVo loginVo);

    LiveData<UserInfoDto> getUserInfo();

    LiveData<TokenInfoDto> getToken(Context context, LoadingDialog dialog, Map<String, String> params);

    LiveData<BaseDto<LookUpVo>> queryFastCode(String organizationId, String fastCode);


}
