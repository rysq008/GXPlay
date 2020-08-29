package com.ikats.shop.present;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.ikats.shop.App;
import com.ikats.shop.fragments.LoginFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.LoginBean;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.utils.Utils;
import com.ikats.shop.views.ToastMgr;

import java.util.UUID;

import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;


public class FLoginPresenter extends XPresent<LoginFragment> {
    /**
     * 登录接口
     *
     * @param context
     * @param loginName
     * @param password
     */
    public void requestLogin(Activity context, String loginName, String password, String verifcode) {
        if (!checkParams(context, loginName, password, verifcode)) {
            return;
        }
        //https://shop.chigoose.com/pos/business/login
        Flowable<HttpResultModel> fr = DataService.builder().buildReqUrl(App.getSettingBean().shop_url + "pos/business/login")
                .buildReqParams("username", loginName)
                .buildReqParams("password", password)
                .buildReqParams("captcha", verifcode)
                .buildReqParams("captchaId", uuid())
//                .buildParseDataClass(LoginBean.class)
                .request(ApiService.HttpMethod.POST);
        RxLoadingUtils.subscribeWithDialog(context, fr, getV().bindToLifecycle(), result -> {

            if (result.isSucceful()) {
//                ShareUtils.saveLoginInfo(result.resultData);
//                getV().showContent(result.resultData);
            } else {
                ToastMgr.showShortToast(result.resultContent);
            }
        }, netError -> {
            getV().showContent(null);
//            ToastMgr.showShortToast("请求失败！请检查网络是否正常");
        });
    }

    private boolean checkParams(Activity activity, String userName, String password, String verifcode) {
//        String loginNameRex = "^[0-9a-zA-Z_]{6,20}$";
        String passwordRex = "/^[A-Za-z_][A-Za-z0-9_]{6,20}$/";//""^[0-9a-zA-Z_]{6,16}$";
        if (!Utils.isMobileNO(userName)) {
            ToastMgr.showShortToast("手机号格式错误");
            return false;
        }
        if (Kits.Empty.check(password)) {
            ToastMgr.showShortToast("密码不能为空");
            return false;
        }
        if (Kits.Empty.check(verifcode)) {
            ToastMgr.showShortToast("验证码不能为空");
            return false;
        }
        return true;
    }

    public void requestCode(Activity context, String phoneNum, Handler.Callback callback) {
        if (!Utils.isMobileNO(phoneNum)) {
            ToastMgr.showShortToast("手机号格式错误");
            return;
        }
        //http://shop.chigoose.com/pos/captcha/image?captchaId=DADF61DB-3417-46D2-8F37-B06131D8FF94&timestamp=15980879160
        Flowable<HttpResultModel> fr = DataService.builder()
                .buildReqUrl(App.getSettingBean().shop_url + "pos/captcha/image")
                .buildReqParams("timestamp", System.currentTimeMillis())
                .buildReqParams("captchaId", uuid())
                .buildInterceptconvert(true)
                .request(ApiService.HttpMethod.GET).map(new Function<ResponseBody, HttpResultModel>() {
                    @Override
                    public HttpResultModel apply(ResponseBody responseBody) throws Exception {
                        HttpResultModel httpResultModel = new HttpResultModel();
                        httpResultModel.resultCode = 1;
                        Bitmap bmp = BitmapFactory.decodeStream(responseBody.byteStream());
                        httpResultModel.resultData = bmp;
                        return httpResultModel;
                    }
                });
        RxLoadingUtils.subscribeWithDialog(context, fr, getV().bindToLifecycle(), result -> {
            if (result.isSucceful()) {
                if (callback != null) {
                    Message msg = Message.obtain();
                    msg.obj = result;
                    callback.handleMessage(msg);
                }
            } else {
                ToastMgr.showLongToast(result.resultContent);
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
//        if (!checkParams(context, phoneNo, newpassword)) {
//            return;
//        }
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
                ToastMgr.showShortToast(result.resultContent);
                if (null != callback)
                    callback.handleMessage(null);
            }
        }, netError -> {
            ToastMgr.showShortToast("请求失败！请检查网络是否正常");
            if (null != callback)
                callback.handleMessage(null);
        });

    }

    public String uuid() {
        String uuidStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//        String[] uuidChars = uuidStr.split("");
//        String r;
//        String[] uuid = {};
//        uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
//        uuid[14] = "4";
//
//        for (int i = 0; i < 36; i++) {
//            if (!uuid[i]) {
//                r = 0 | Math.random() * 16;
//                uuid[i] = uuidChars[(i == 19) ? (r & 0x3) | 0x8 : r];
//            }
//        }
//        return uuid.join("");
        return UUID.nameUUIDFromBytes(uuidStr.getBytes()).toString();
    }


}