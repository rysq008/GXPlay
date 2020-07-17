package com.zhny.zhny_app.present;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zhny.zhny_app.DrawTileActivity;
import com.zhny.zhny_app.fragments.LoginFragment;
import com.zhny.zhny_app.model.BaseModel.HttpResultModel;
import com.zhny.zhny_app.model.LoginBean;
import com.zhny.zhny_app.net.DataService;
import com.zhny.zhny_app.net.api.Api;
import com.zhny.zhny_app.net.api.ApiService;
import com.zhny.zhny_app.utils.RxLoadingUtils;
import com.zhny.zhny_app.utils.ShareUtils;
import com.zhny.zhny_app.utils.Utils;
import com.zhny.zhny_app.views.ToastMgr;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.Flowable;


public class FLoginPresenter extends XPresent<LoginFragment> {
    /**
     * 登录接口
     *
     * @param context
     * @param loginName
     * @param password
     */
    public void requestLogin(Activity context, String loginName, String password, boolean isloginbypassword) {

//        if (!checkParams(context, loginName, password)) {
//            return;
//        }
        Flowable<HttpResultModel<LoginBean>> fr = DataService.builder().buildReqUrl("oauth/login")
                .buildReqParams("password", isloginbypassword ? password : "")
//                .buildReqParams("code", isloginbypassword ? "" : password)
                .buildReqParams("username", loginName)
//                .buildReqParams("type", isloginbypassword ? "user" : "mobile")
                .buildParseDataClass(LoginBean.class)
                .request(ApiService.HttpMethod.POST);
        RxLoadingUtils.subscribeWithDialog(context, fr, getV().bindToLifecycle(), result -> {

            if (result.isSucceful()) {
                ShareUtils.saveLoginInfo(result.data);
                getV().getUserData(result.data);
            } else {
                ToastMgr.showShortToast(result.msg);
            }
        }, netError -> {
            Router.newIntent(context).to(DrawTileActivity.class).launch();
            context.finish();
            ToastMgr.showShortToast("请求失败！请检查网络是否正常");
        });
    }

    private boolean checkParams(Activity activity, String userName, String password) {

        String loginNameRex = "^[0-9a-zA-Z_]{6,20}$";
        String passwordRex = "^[0-9a-zA-Z_]{6,16}$";
        if (TextUtils.isEmpty(userName)) {
//            ToastMgr.showShortToast("用户名不能为空");
            ToastMgr.showShortToast("用户名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
//            ToastMgr.showShortToast("密码不能为空");
            ToastMgr.showShortToast("密码不能为空");
            return false;
        }
        if (!userName.matches(loginNameRex)) {
//            ToastMgr.showShortToast("用户名格式错误");
            ToastMgr.showLongToast("用户名格式错误");
            return false;
        }
        if (!password.matches(loginNameRex)) {
//            ToastMgr.showShortToast("密码格式错误");
            ToastMgr.showShortToast("密码格式错误");
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
                ToastMgr.showLongToast(result.msg);
            }
        }, netError -> ToastMgr.showShortToast("请求失败！请检查网络是否正常"), true);
    }

    public void requestVerifCode(Activity context, String phoneNum, Handler.Callback callback) {
        if (!Utils.isMobileNO(phoneNum)) {
            ToastMgr.showShortToast("手机号格式错误");
            return;
        }
        if (null != callback)
            callback.handleMessage(null);
    }

    public void requestResetPwd(Activity context, String phoneNo, String newpassword, Handler.Callback callback) {
        if (!checkParams(context, phoneNo, newpassword)) {
            return;
        }
        Flowable<HttpResultModel<LoginBean>> fr = DataService.builder().buildReqUrl("api/resetPassword")
                .buildReqParams("username", phoneNo)
                .buildReqParams("password", newpassword)
                .request(ApiService.HttpMethod.POST);
        RxLoadingUtils.subscribeWithDialog(context, fr, getV().bindToLifecycle(), result -> {
            if (result.isSucceful()) {
//                    ShareUtils.saveObject("userData", result.data);
                ToastMgr.showLongToast("密码重置成功");
                if (null != callback)
                    callback.handleMessage(Message.obtain());
            } else {
                ToastMgr.showShortToast(result.msg);
                if (null != callback)
                    callback.handleMessage(null);
            }
        }, netError -> {
            ToastMgr.showShortToast("请求失败！请检查网络是否正常");
            if (null != callback)
                callback.handleMessage(null);
        });

    }
}