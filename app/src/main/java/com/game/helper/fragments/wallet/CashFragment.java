package com.game.helper.fragments.wallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.UpdateTradePasswordFragment;
import com.game.helper.fragments.recharge.RechargeSuccFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CashToResults;
import com.game.helper.model.CheckTradePasswdResults;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.CashToRequestBody;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.PasswordEditDialog;
import com.game.helper.views.widget.ToggleButton;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashFragment extends XBaseFragment implements View.OnClickListener, ToggleButton.OnToggleChanged {
    public static final String TAG = CashFragment.class.getSimpleName();
    private static final int PAY_ALIPAY = 0;
    private static final int PAY_WECHAT = 1;
    private static final int Min_Cash_To = 100;

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @BindView(R.id.tv_value)
    TextView mTotalValue;
    @BindView(R.id.tv_balance_left)
    TextView mBalanceLeft;
    @BindView(R.id.tv_balance_right)
    TextView mBalanceRight;
    @BindView(R.id.tb_recharge_toggle)
    ToggleButton mToggle;
    @BindView(R.id.cb_alipay)
    CheckBox mAlipay;
    @BindView(R.id.tv_cash)
    TextView mCashAccount;
//    @BindView(R.id.et_cash_to)
//    TextView mCashTo;
    @BindView(R.id.et_cash_value)
    TextView mCashValue;
    @BindView(R.id.tv_cash_apply)
    TextView mApply;
    @BindView(R.id.ll_help)
    View mHelp;

    private MemberInfoResults userInfo;
    private float totalValue = 0;

    public static CashFragment newInstance(){
        return new CashFragment();
    }

    public CashFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_cash;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_cash));
        mHeadBack.setOnClickListener(this);
        mHelp.setOnClickListener(this);

        userInfo = (MemberInfoResults) getArguments().getSerializable(TAG);
        mTotalValue.setText(StringUtils.isEmpty(userInfo.total_balance) ? "0.00" : userInfo.total_balance);
        mBalanceRight.setText(StringUtils.isEmpty(userInfo.market_balance) ? "0.00" : userInfo.market_balance);
        mBalanceLeft.setText(StringUtils.isEmpty(userInfo.balance) ? "0.00" : userInfo.balance);
        mToggle.setToggleOn();
        mToggle.setOnToggleChanged(this);
        mCashAccount.setText(Utils.converterSecretPhone(userInfo.phone));
        mAlipay.setChecked(true);
        mApply.setOnClickListener(this);
        catulateCashBalnace();
    }

    private void apply(){
        String account = userInfo.phone;
//        String cashTo = mCashTo.getText().toString();
        final String cashValue = mCashValue.getText().toString();
        catulateCashBalnace();

        if (StringUtils.isEmpty(account) /*|| StringUtils.isEmpty(cashTo)*/ || StringUtils.isEmpty(cashValue)){
            Toast.makeText(getContext(), "请补全提现信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(cashValue) < Min_Cash_To){
            Toast.makeText(getContext(), "提现金额最少"+Min_Cash_To+"！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getCheckPayMethod() == -1){
            Toast.makeText(getContext(), "请选择收款方式！", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断账户总金额／选中充值账户余额状态下的充值状态余额够不够
        if (totalValue <= 0){
            Toast.makeText(getContext(), "该账户暂无可提现金额！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(cashValue) > totalValue){
            Toast.makeText(getContext(), "余额不足！", Toast.LENGTH_SHORT).show();
            return;
        }

        final PasswordEditDialog dialog = new PasswordEditDialog();
        dialog.addOnPassWordEditListener(new PasswordEditDialog.OnPassWordEditListener() {
            @Override
            public void onConfirmComplete(String password) {
                dialog.dismiss();
                ProvingTradePssword(password,cashValue);
            }
        });
        dialog.show(getChildFragmentManager(),PasswordEditDialog.TAG);
    }

    /**
     * 验证交易密码
     * */
    private void ProvingTradePssword(final String password, final String cashValue){
        Flowable<HttpResultModel<CheckTradePasswdResults>> fr = DataService.checkTradePassword(new CheckTradePasswdRequestBody(password));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CheckTradePasswdResults>>() {
            @Override
            public void accept(HttpResultModel<CheckTradePasswdResults> checkTradePasswdResultsHttpResultModel) throws Exception {
                if (checkTradePasswdResultsHttpResultModel.isSucceful()){
                    applyFromNet(SharedPreUtil.getLoginUserInfo().member_id+"",cashValue,isUseAccountBalance()+"",password);
                }else if (checkTradePasswdResultsHttpResultModel.isNoneTradePassword()) {
                    //设置交易密码
                    final GXPlayDialog dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Full_Confirm,
                            getResources().getString(R.string.common_dialog_trade_passwd_hint),
                            getResources().getString(R.string.common_dialog_without_trade_passwd));
                    dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirm() {
                            dialog.dismiss();
                            DetailFragmentsActivity.launch(getContext(),null, UpdateTradePasswordFragment.newInstance());
                        }
                    });
                    dialog.show(getFragmentManager(),GXPlayDialog.TAG);
                }else {
                    Toast.makeText(getContext(), "密码错误!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 提现
     * */
    private void applyFromNet(String memberId, final String amount, String isAccount, String tradePassword){
        Flowable<HttpResultModel<CashToResults>> fr = DataService.cashTo(new CashToRequestBody(memberId,amount,isAccount+"",tradePassword));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CashToResults>>() {
            @Override
            public void accept(HttpResultModel<CashToResults> cashToResultsHttpResultModel) throws Exception {
                if (cashToResultsHttpResultModel.isSucceful()){
                    DetailFragmentsActivity.launch(getContext(),null, RechargeSuccFragment.newInstance(RechargeSuccFragment.Type_Cash_Succ,Float.parseFloat(amount)));
                    getActivity().onBackPressed();
                }else {
                    Toast.makeText(getContext(), cashToResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                if (netError.getMessage().equals("提现成功")){
                    DetailFragmentsActivity.launch(getContext(),null, RechargeSuccFragment.newInstance(RechargeSuccFragment.Type_Cash_Succ,Float.parseFloat(amount)));
                    getActivity().onBackPressed();
                }else {
                    Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void catulateCashBalnace(){
        //可提现余额=金币余额（开关打开）+推广账户余额
        if (userInfo == null) return;
        float coinValue = Float.parseFloat(StringUtils.isEmpty(userInfo.balance) ? "0.00" : userInfo.balance);//金币余额
        float marketValue = Float.parseFloat(StringUtils.isEmpty(userInfo.market_balance) ? "0.00" : userInfo.market_balance);//推广账户余额
        totalValue = isUseAccountBalance() ? coinValue : ( 0 + marketValue );
    }

    @Override
    public void onToggle(boolean on) {
//        if (userInfo == null || Float.parseFloat(userInfo.balance) <= 0){
//            mToggle.setToggleOff(true);
//        }
        if (mToggle.isToggleOn()){
            final GXPlayDialog dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Single_Confirm,
                    getResources().getString(R.string.common_wormheart_hint),
                    getResources().getString(R.string.common_wormheart_hint_desc));
            dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
                @Override
                public void onCancel() {
                    dialog.dismiss();
                }

                @Override
                public void onConfirm() {
                    dialog.dismiss();
                }
            });
            dialog.show(getFragmentManager(),GXPlayDialog.TAG);
        }
    }

    private boolean isUseAccountBalance(){
        return mToggle.isToggleOn();
    }

    private int getCheckPayMethod(){
        if (mAlipay.isChecked()){
            return PAY_ALIPAY;
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mApply){
            apply();
        }
        if (v == mHelp){

        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
