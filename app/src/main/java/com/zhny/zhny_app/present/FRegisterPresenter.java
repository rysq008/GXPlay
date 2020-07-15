package com.zhny.zhny_app.present;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zhny.zhny_app.fragments.RegisterFragment;
import com.zhny.zhny_app.model.BaseModel.HttpResultModel;
import com.zhny.zhny_app.model.RegisterBean;
import com.zhny.zhny_app.net.DataService;
import com.zhny.zhny_app.net.api.Api;
import com.zhny.zhny_app.net.api.ApiService;
import com.zhny.zhny_app.utils.RxLoadingUtils;
import com.zhny.zhny_app.utils.Utils;
import com.zhny.zhny_app.views.ToastMgr;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


public class FRegisterPresenter extends XPresent<RegisterFragment> {

    /**
     * 注册
     *
     * @param context
     * @param loginName
     * @param password
     */
    public void requestRegister(Activity context, String loginName, String verifcode, String password, String repassword) {

        if (!checkParams(context, loginName, verifcode, password, repassword)) {
            return;
        }
        Flowable<HttpResultModel<RegisterBean>> fr = DataService.builder().buildReqUrl("api/reg")
                .buildReqParams("phoneNo", loginName)
                .buildReqParams("code", verifcode)
                .buildReqParams("password", password)
                .buildParseDataClass(RegisterBean.class)
                .request(ApiService.HttpMethod.POST);
        RxLoadingUtils.subscribeWithDialog(context, fr, getV().bindToLifecycle(), (Consumer<HttpResultModel<RegisterBean>>) result -> {

            if (result.isSucceful()) {
                getV().getUserData(result.data);
                ToastMgr.showShortToast(result.msg);
            } else
                ToastMgr.showShortToast(result.msg);
        }, netError -> ToastMgr.showShortToast(netError.getMessage()));
    }

    private boolean checkParams(Activity activity, String userName, String verifCode, String password, String repassword) {
        String loginNameRex = "^[0-9a-zA-Z_]{6,20}$";
        String passwordRex = "^[0-9a-zA-Z_]{6,16}$";
        if (!Utils.isMobileNO(userName)) {
            ToastMgr.showShortToast("手机号格式错误");
            return false;
        }
        if (TextUtils.isEmpty(verifCode)) {
            ToastMgr.showShortToast("验证码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ToastMgr.showShortToast("密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(repassword)) {
            ToastMgr.showShortToast("确认密码不能为空");
            return false;
        }
        if (!(password).equals(repassword)) {
            ToastMgr.showShortToast("密码不一致");
            return false;
        }
        return true;
    }

    public void requestGetCode(Activity context, String phoneNum, Handler.Callback callback) {
        if (!Utils.isMobileNO(phoneNum)) {
            ToastMgr.showShortToast("手机号格式错误");
            return;
        }
        Flowable<HttpResultModel> fr = DataService.builder()
                .buildReqUrl("smsCode")
                .buildReqParams("mobile", phoneNum)
                .request(ApiService.HttpMethod.GET);
        RxLoadingUtils.subscribeWithDialog(context, fr, getV().bindToLifecycle(), result -> {

            if (result.isSucceful()) {
                if (callback != null) {
                    Message msg = Message.obtain();
                    msg.obj = result;
                    callback.handleMessage(msg);
                }
            } else {
                ToastMgr.showShortToast(result.msg);
            }
        }, netError -> ToastMgr.showShortToast("请求失败！请检查网络是否正常"), true);
    }

}
