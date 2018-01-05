package com.game.helper.fragments.recharge;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.game.helper.GameMarketApplication;
import com.game.helper.R;
import com.game.helper.activitys.HuanxinKefuLoginActivity;
import com.game.helper.activitys.OrderConfirmActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.WxPayInfoBean;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.PayRequestBody;
import com.game.helper.utils.AliPayResultUtils;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.ToastUtil;
import com.game.helper.utils.Utils;
import com.game.helper.utils.WXPayUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechargeFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = RechargeFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_setting)
    View mHeadAction;
    @BindView(R.id.iv_action)
    ImageView mIvAction;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @BindView(R.id.game_detail_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.ll_connect_kefu)
    View mConnectKefu;
    @BindView(R.id.tv_confirm_order)
    View mConfirmOrder;

    private int current_page = 0;
    private List<Fragment> list = new ArrayList<Fragment>();
    private RechargeGameFragment rechargeGameFragment;
    private RechargeGoldFragment rechargeGoldFragment;
    private RechargeVIPLevelFragment rechargeVIPLevelFragment;
    private ProgressDialog dialog;
    public static final int Reset_Ui_Code = 11;
    public static final String VIP = "vip";
    private String level = "0";

    public static RechargeFragment newInstance() {
        return new RechargeFragment();
    }

    public RechargeFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        initView();
        if(arguments != null){
            int vip = arguments.getInt(VIP);
            if(vip == 3){
                viewPager.setCurrentItem(2);
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_recharge));
        mHeadAction.setVisibility(View.VISIBLE);
        mIvAction.setImageResource(R.mipmap.ic_help);
        mHeadAction.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);
        mConfirmOrder.setOnClickListener(this);
        mConnectKefu.setOnClickListener(this);

        if (getArguments() != null){
            level = getArguments().getString(TAG);
        }

        rechargeGameFragment = RechargeGameFragment.newInstance();
        list.add(rechargeGameFragment);
        rechargeGoldFragment = RechargeGoldFragment.newInstance();
        list.add(rechargeGoldFragment);
        rechargeVIPLevelFragment = RechargeVIPLevelFragment.newInstance();
        list.add(rechargeVIPLevelFragment);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                current_page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        loadData();
    }

    public void loadData() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setPadding(Utils.dip2px(getContext(),30),0,Utils.dip2px(getContext(),30),0);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                String title = null;
                switch (index) {
                    case 0:
                        title = "游戏";
                        break;
                    case 1:
                        title = "金币";
                        break;
                    case 2:
                        title = "VIP";
                        break;
                }
                colorTransitionPagerTitleView.setText(title);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(getResources().getColor(R.color.colorPrimary));
                return indicator;
            }
        });
        tabStrip.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabStrip, viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    private void confirmOrder() {
        if (current_page == 0) {
            GameAccountResultModel.ListBean gameBean = rechargeGameFragment.getGameBean();
            double totalBalanceValue = rechargeGameFragment.getTotalBalanceValue();
            double inputValue = rechargeGameFragment.getInputValue();
            boolean is_vip = rechargeGameFragment.getIs_VIP();
            if (gameBean == null || totalBalanceValue <= 0 || inputValue <= 0) {
                Toast.makeText(getContext(), "数据异常！请重试", Toast.LENGTH_SHORT).show();
            } else {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ConfirmOrderFragment.BUNDLE_GAME_BEAN, gameBean);
//                bundle.putDouble(ConfirmOrderFragment.BUNDLE_TOTAL_BALANCE, totalBalanceValue);
//                ConfirmOrderFragment confirmOrderFragment = ConfirmOrderFragment.newInstance();
//                confirmOrderFragment.setArguments(bundle);
//                DetailFragmentsActivity.launch(getContext(), bundle, confirmOrderFragment);

                //订单确认页面用OrderConfirmActivity
                Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(OrderConfirmActivity.BUNDLE_GAME_BEAN, gameBean);
                bundle.putDouble(OrderConfirmActivity.BUNDLE_INPUT_VALUE, inputValue);
                bundle.putDouble(OrderConfirmActivity.BUNDLE_TOTAL_BALANCE, totalBalanceValue);
                bundle.putString(OrderConfirmActivity.PAYPURPOSE, "1");
                bundle.putString(OrderConfirmActivity.VIPLEVEL, "0");
                bundle.putBoolean(OrderConfirmActivity.IS_VIP, is_vip);
                intent.putExtra(OrderConfirmActivity.TAG, bundle);
                startActivityForResult(intent, Reset_Ui_Code);
            }
        }
        if (current_page == 1) {
            int select_pay_mode = rechargeGoldFragment.getSelect_pay_mode();
            int totalChargeGold = rechargeGoldFragment.getmTotalChrgeGold();
            if (totalChargeGold <= 0 || select_pay_mode == -1) {
                Toast.makeText(getContext(), "数据异常！请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            if (select_pay_mode == RechargeGoldFragment.Pay_Type_Wechat) {
                //微信支付
                weixinPay(totalChargeGold,false,level);
            }
            if (select_pay_mode == RechargeGoldFragment.Pay_Type_Alipay) {
                //支付宝支付
                aliPay(totalChargeGold,false,level);
            }
        }

        if (current_page == 2) {
            Boolean payType = rechargeVIPLevelFragment.getPayType();
            int totalCharge = rechargeVIPLevelFragment.getTotal();
            String selectVIPLevel = rechargeVIPLevelFragment.getSelectedVIPLevel();
            if (totalCharge <= 0 ) {
                ToastUtil.showToast("实付金额为0元，无法充值");
            }else{
                if (payType ) {
                    //微信支付
                    weixinPay(totalCharge,true,selectVIPLevel);
                }else{
                    //支付宝支付
                    aliPay(totalCharge,true,selectVIPLevel);
                }
            }

        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            dismissWaittingDialog();
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    AliPayResultUtils payResult = new AliPayResultUtils((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getContext(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    /**
     * ali支付
     */
    private void aliPay(float amount, boolean vip,String vipLevel) {
        showWaittingDialog();
        Flowable<HttpResultModel<PayResultModel>> fr = DataService.ApiPay(new PayRequestBody(
                SharedPreUtil.getLoginUserInfo().member_id + "",
                amount + "",
                "1",
                vip ? "2" : "1",
                vipLevel));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<PayResultModel>>() {
            @Override
            public void accept(HttpResultModel<PayResultModel> payRequestBody) throws Exception {
                if (payRequestBody.isSucceful()) {
                    final String info = payRequestBody.data.orderInfo;
                    Log.e(TAG, "accept: info:::::" + info);
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(getActivity());
                            Map<String, String> result = alipay.payV2(info, true);
                            Log.i("msp", result.toString());

                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    Toast.makeText(getContext(), payRequestBody.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                dismissWaittingDialog();
                Log.e("", "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });

    }
    /**
     * 微信支付
     */
    private void weixinPay(float mNeedPay,boolean vip,String vipLevel) {
        showWaittingDialog();
        Flowable<HttpResultModel<PayResultModel>> fr = DataService.ApiPay(new PayRequestBody(
                SharedPreUtil.getLoginUserInfo().member_id + "",
                mNeedPay + "",
                "2",
                vip ? "2" : "1",
                vipLevel));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<PayResultModel>>() {
            @Override
            public void accept(HttpResultModel<PayResultModel> payRequestBody) throws Exception {
                if (payRequestBody.isSucceful()) {
                    WxPayInfoBean bean = new WxPayInfoBean();
                    bean.setAppid(RxConstant.ThirdPartKey.WeixinId);
                    bean.setNoncestr(payRequestBody.data.getWxorderInfo().getNoncestr());
                    bean.setPackagestr(payRequestBody.data.getWxorderInfo().getPackagevalue());
                    bean.setPartnerid(payRequestBody.data.getWxorderInfo().getPartnerid());
                    bean.setPrepayid(payRequestBody.data.getWxorderInfo().getPrepayid());
                    bean.setSign(payRequestBody.data.getWxorderInfo().getSign());
                    bean.setTimestamp(payRequestBody.data.getWxorderInfo().getTimestamp());
                    GameMarketApplication.api.sendReq(WXPayUtils.weChatPay(bean));
                    dismissWaittingDialog();
                }
                Toast.makeText(getContext(), payRequestBody.getResponseMsg(), Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("", "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });

    }

    private void showWaittingDialog() {
        dialog = null;
        dialog = new ProgressDialog(getContext(), ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("支付中");
        dialog.show();
    }

    private void dismissWaittingDialog() {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
        if (v == mConfirmOrder) {
            confirmOrder();
        }
        if (v == mConnectKefu) {
            //Toast.makeText(getContext(), "跳转客服", Toast.LENGTH_SHORT).show();
            Intent intentKefu = new Intent(context, HuanxinKefuLoginActivity.class);
            startActivity(intentKefu);
        }
        if (v == mHeadAction){
            // TODO: 2017/12/29 补全跳转
            Toast.makeText(getContext(), "跳转说明", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (rechargeGameFragment != null) rechargeGameFragment.resetFragment();
    }
}
