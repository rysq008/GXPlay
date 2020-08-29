package com.ikats.shop.present;

import android.app.Activity;
import android.text.TextUtils;

import com.ikats.shop.fragments.RegisterFragment;
import com.ikats.shop.fragments.StatisticsFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.RegisterBean;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.utils.Utils;
import com.ikats.shop.views.ToastMgr;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.net.IModel;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


public class FStatisticsPresenter extends XPresent<StatisticsFragment> {

    /**
     * 销售笔数和销售金额
     *
     * @param context
     * @param loginName
     * @param verifcode
     * @param password
     * @param repassword
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
//                getV().getUserData(result.resultData);
                ToastMgr.showShortToast(result.resultContent);
            } else
                ToastMgr.showShortToast(result.resultContent);
        }, netError -> ToastMgr.showShortToast(netError.getMessage()));
    }

    public void requestStatisListData(Activity activity, String startTime, String endTime) {
        Flowable flowable = DataService.builder().buildReqUrl("").buildReqParams("", "").request(ApiService.HttpMethod.POST);
        RxLoadingUtils.subscribeWithDialog(activity, flowable, getV().bindToLifecycle(), new Consumer<IModel>() {
            @Override
            public void accept(IModel iModel) throws Exception {

            }
        }, netError -> getV().showData());
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
}
