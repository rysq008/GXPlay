//
//package com.andorid.greenorange.present;
//
//import com.andorid.greenorange.activitys.HomeActivity;
//import com.andorid.greenorange.data.RxConstant;
//import com.andorid.greenorange.model.BaseModel.HttpResultModel;
//import com.andorid.greenorange.model.LoginBean;
//import com.andorid.greenorange.net.DataService;
//import com.andorid.greenorange.net.model.LoginRequestBody;
//import com.andorid.greenorange.utils.RxLoadingUtils;
//import com.andorid.greenorange.views.ToastMgrView;
//import com.andorid.greenorange.views.XReloadableStateContorller;
//
//import cn.droidlover.xdroidmvp.mvp.XPresent;
//import io.reactivex.Flowable;
//import io.reactivex.functions.Consumer;
//
//public class HomePresenter extends XPresent<HomeActivity> {
//
//    //调用view类进行数据刷新
//    public void getVerifyStatistics(XReloadableStateContorller tip_info, String deptId) {
//
//        Flowable<HttpResultModel<LoginBean>> fr = DataService.getApiLoginData(new LoginRequestBody(deptId, deptId));
//        RxLoadingUtils.subscribeWithReload(tip_info, fr, getV().bindToLifecycle(), new Consumer<HttpResultModel<LoginBean>>() {
//
//            @Override
//            public void accept(HttpResultModel listHttpResultModel) throws Exception {
//
//                if (RxConstant.RESULT_OK.equals(listHttpResultModel.code)) {
//                    tip_info.showContent();
//                    getV().getVerifyStatistics(listHttpResultModel.data);
//                } else {
//                    tip_info.showError();
//                    ToastMgrView.getInstance().showLengthShort(getV(), 1, listHttpResultModel.msg);
//                }
//
//            }
//        }, null, null, true);
//    }
//}
