package com.zhny.zhny_app.present;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.andorid.greenorange.utils.RxLoadingUtils;
import com.andorid.greenorange.utils.Utils;
import com.andorid.greenorange.views.ToastMgr;
import com.andorid.greenorange.views.ToastMgrView;
import com.zhny.zhny_app.fragments.RegisterFragment;
import com.zhny.zhny_app.model.BaseModel.HttpResultModel;
import com.zhny.zhny_app.model.RegisterBean;
import com.zhny.zhny_app.net.DataService;
import com.zhny.zhny_app.net.api.Api;
import com.zhny.zhny_app.net.api.ApiService;

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
                ToastMgrView.getInstance().showLengthShort(context, 0, result.msg);
            } else
                ToastMgrView.getInstance().showLengthShort(context, 1, result.msg);
        }, netError -> ToastMgrView.getInstance().showLengthShort(context, 1, netError.getMessage()));
    }

    private boolean checkParams(Activity activity, String userName, String verifCode, String password, String repassword) {
        String loginNameRex = "^[0-9a-zA-Z_]{6,20}$";
        String passwordRex = "^[0-9a-zA-Z_]{6,16}$";
        if (!Utils.isMobileNO(userName)) {
            ToastMgrView.getInstance().showLengthShort(activity, 1, "手机号格式错误");
            return false;
        }
        if (TextUtils.isEmpty(verifCode)) {
            ToastMgrView.getInstance().showLengthShort(activity, 1, "验证码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ToastMgrView.getInstance().showLengthShort(activity, 1, "密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(repassword)) {
            ToastMgrView.getInstance().showLengthShort(activity, 1, "确认密码不能为空");
            return false;
        }
        if (!(password).equals(repassword)) {
            ToastMgrView.getInstance().showLengthShort(activity, 1, "密码不一致");
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
                .buildHostType(Api.HostType.ADMIN)
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
                ToastMgrView.getInstance().showLengthShort(context, 1, result.msg);
            }
        }, netError -> ToastMgrView.getInstance().showLengthShort(context, 1, "请求失败！请检查网络是否正常"), true);
    }

}
