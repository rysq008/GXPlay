package com.game.helper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.game.helper.GameMarketApplication;
import com.game.helper.R;
import com.game.helper.activitys.GeneralizeIncomeActivity;
import com.game.helper.activitys.MyAccountActivity;
import com.game.helper.activitys.OrderConfirmActivity;
import com.game.helper.activitys.RankingListActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.WxPayInfoBean;
import com.game.helper.model.model.MemberBean;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.PayRequestBody;
import com.game.helper.share.UMengShare;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.WXPayUtils;
import com.game.helper.views.BannerView;
import com.game.helper.views.HeadImageView;
import com.game.helper.views.widget.StateView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xstatecontroller.XStateController;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by zr on 2017-10-13.
 */

public class GeneralizePagerFragment extends XBaseFragment implements View.OnClickListener{

    public static final String TAG ="GeneralizePagerFragment" ;
    @BindView(R.id.generalize_root_layout)
    XStateController xStateController;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.generalize_banner_view)
    BannerView bannerView;
    @BindView(R.id.generalize_total_income_tv)
    TextView total_tv;
    @BindView(R.id.generalize_expect_income_tv)
    TextView expect_tv;
    @BindView(R.id.generalize_income_tv)
    TextView generalize_tv;
    @BindView(R.id.generalize_head_iv)
    HeadImageView head_iv;
    StateView stateView;
    @BindView(R.id.rangeTv)
    TextView rangeTv;
    @BindView(R.id.mallTv)
    TextView mallTv;
    @BindView(R.id.activityTv)
    TextView activityTv;
    @BindView(R.id.shareIncome)
    TextView shareIncome;
    @BindView(R.id.shareDiscount)
    TextView shareDiscount;


    @Override
    public void initData(Bundle savedInstanceState) {

        rangeTv.setOnClickListener(this);
        mallTv.setOnClickListener(this);
        activityTv.setOnClickListener(this);
        shareIncome.setOnClickListener(this);
        shareDiscount.setOnClickListener(this);
        expect_tv.setOnClickListener(this);
        total_tv.setOnClickListener(this);
        generalize_tv.setOnClickListener(this);

        swipeRefreshLayout.setColorSchemeResources(
                cn.droidlover.xrecyclerview.R.color.x_red,
                cn.droidlover.xrecyclerview.R.color.x_blue,
                cn.droidlover.xrecyclerview.R.color.x_yellow,
                cn.droidlover.xrecyclerview.R.color.x_green
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        if (null == stateView) {
            stateView = new StateView(context);
            stateView.setCustomClickListener(new StateView.StateViewClickListener() {
                @Override
                public void doAction() {
                    refreshData();
                }
            });
        }
        xStateController.errorView(stateView);
        xStateController.loadingView(View.inflate(context, R.layout.view_loading, null));
//        xStateController.showLoading();
        refreshData();
        getBannerInfo();
    }

    private void getBannerInfo() {
        Flowable<HttpResultModel<BannerResults>> fb = DataService.getHomeBanner(new BannerRequestBody(1));

        RxLoadingUtils.subscribe(fb, this.bindToLifecycle(), new Consumer<HttpResultModel<BannerResults>>() {
            @Override
            public void accept(HttpResultModel<BannerResults> data) throws Exception {
                bannerView.setData(data.data);
                xStateController.showContent();
                xStateController.getLoadingView().setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                xStateController.getLoadingView().setVisibility(View.GONE);
                xStateController.showError();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshData() {
        Flowable<HttpResultModel<GeneralizeResults>> flowable = DataService.getGeneralizeData();
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<GeneralizeResults>>() {
            @Override
            public void accept(HttpResultModel<GeneralizeResults> generalizeResultsHttpResultModel) throws Exception {

                GeneralizeResults generalizeResults = generalizeResultsHttpResultModel.data;
                if (!Kits.Empty.check(generalizeResults)) {
                    MemberBean memberBean = generalizeResults.getMember();
                    BusProvider.getBus().post(new MsgEvent<String>(0, RxConstant.Head_Image_Change_Type, memberBean.getIcon()));
                    total_tv.setText(generalizeResults.zongshouyi);
                    generalize_tv.setText(generalizeResults.yue);
                    expect_tv.setText(generalizeResults.yujizongshouyi);
                }
                xStateController.showContent();
                xStateController.getLoadingView().setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                xStateController.getLoadingView().setVisibility(View.GONE);
                xStateController.showError();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_generalize_layout;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static GeneralizePagerFragment newInstance() {
        return new GeneralizePagerFragment();
    }

    private void login(){
        Flowable<HttpResultModel<LoginResults>> f = DataService.login(new LoginRequestBody("13312341234","9870","1",""));
        RxLoadingUtils.subscribe(f, bindToLifecycle(), new Consumer<HttpResultModel<LoginResults>>() {
            @Override
            public void accept(HttpResultModel<LoginResults> payRequestBody ) throws Exception {
                if (payRequestBody.isSucceful()) {
                    Log.d("","accept");

                }else {
                    Toast.makeText(getActivity(), payRequestBody.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e("", "Link Net Error! Error Msg: "+netError.getMessage().trim());
            }
        });
    }

    private void umShare() {
        UMengShare share = new UMengShare(getActivity());
        share.shareLinkWithBoard(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }

    private void AliPay() {
        Flowable<HttpResultModel<PayResultModel>> fr = DataService.ApiPay(new PayRequestBody("1","0.01","1","1","0"));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<PayResultModel>>() {
            @Override
            public void accept(HttpResultModel<PayResultModel> payRequestBody ) throws Exception {
                if (payRequestBody.isSucceful()) {
                    final String info = payRequestBody.data.orderInfo;
                    Log.e(TAG, "accept: info:::::"+info);
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(getActivity());
                            Map<String, String> result = alipay.payV2(info, true);
                            Log.i("msp", result.toString());

//                        Message msg = new Message();
//                        msg.what = 1;
//                        msg.obj = result;
//                        mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }else {
                    Toast.makeText(getActivity(), payRequestBody.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e("", "Link Net Error! Error Msg: "+netError.getMessage().trim());
            }
        });

    }

    private void weixinPay() {
        Flowable<HttpResultModel<PayResultModel>> fr = DataService.ApiPay(new PayRequestBody("1","0.01","2","1","0"));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<PayResultModel>>() {
            @Override
            public void accept(HttpResultModel<PayResultModel> payRequestBody ) throws Exception {
                if (payRequestBody.isSucceful()) {
                    Log.d("","accept");
                    WxPayInfoBean bean = new WxPayInfoBean();
                    bean.setAppid(RxConstant.ThirdPartKey.WeixinId);
                    bean.setNoncestr(payRequestBody.data.getWxorderInfo().getNoncestr());
                    bean.setPackagestr(payRequestBody.data.getWxorderInfo().getPackagevalue());
                    bean.setPartnerid(payRequestBody.data.getWxorderInfo().getPartnerid());
                    bean.setPrepayid(payRequestBody.data.getWxorderInfo().getPrepayid());
                    bean.setSign(payRequestBody.data.getWxorderInfo().getSign());
                    bean.setTimestamp(payRequestBody.data.getWxorderInfo().getTimestamp());
                    GameMarketApplication.api.sendReq(WXPayUtils.weChatPay(bean));
                }else {
                    Toast.makeText(getActivity(), payRequestBody.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e("", "Link Net Error! Error Msg: "+netError.getMessage().trim());
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.rangeTv://排行榜
                intent.setClass(getActivity(),RankingListActivity.class);
                startActivity(intent);
                break;
            case R.id.mallTv://商城
                //TODO h5
                weixinPay();
                break;
            case R.id.activityTv://活动
                AliPay();
                //TODO h5
                break;
            case R.id.generalize_total_income_tv:
            case R.id.generalize_expect_income_tv:
            case R.id.generalize_income_tv:
                intent.setClass(getActivity(),GeneralizeIncomeActivity.class);
                startActivity(intent);
                break;
            case R.id.shareIncome://分享收益
                intent.setClass(getActivity(),OrderConfirmActivity.class);
                startActivity(intent);
                break;
            case R.id.shareDiscount://分享折扣
                intent.setClass(getActivity(),MyAccountActivity.class);
                startActivity(intent);
                //TODO
                break;

        }
    }
}
