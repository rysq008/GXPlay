package com.game.helper.fragments.wallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
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
public class CashFragment extends XBaseFragment implements View.OnClickListener, ToggleButton.OnToggleChanged, GXPlayDialog.onDialogActionListner {
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
    @BindView(R.id.et_cash_to)
    TextView mCashTo;
    @BindView(R.id.et_cash_value)
    TextView mCashValue;
    @BindView(R.id.tv_cash_apply)
    TextView mApply;

    private MemberInfoResults userInfo;

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

        userInfo = (MemberInfoResults) getArguments().getSerializable(TAG);
        mTotalValue.setText(StringUtils.isEmpty(userInfo.total_balance) ? "0.00" : userInfo.total_balance);
        mBalanceRight.setText(StringUtils.isEmpty(userInfo.market_balance) ? "0.00" : userInfo.market_balance);
        mBalanceLeft.setText(StringUtils.isEmpty(userInfo.balance) ? "0.00" : userInfo.balance);
        mToggle.setToggleOn();
        mToggle.setOnToggleChanged(this);
        mCashAccount.setText(userInfo.phone);
        mAlipay.setChecked(true);
        mApply.setOnClickListener(this);
    }

    private void apply(){
        String account = mCashAccount.getText().toString();
        String cashTo = mCashTo.getText().toString();
        final String cashValue = mCashValue.getText().toString();

        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(cashTo) || StringUtils.isEmpty(cashValue)){
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
        if (userInfo == null || Float.parseFloat(userInfo.total_balance) <= 0
                || (isUseAccountBalance() && Float.parseFloat(userInfo.balance) <= 0) ){
            Toast.makeText(getContext(), "该账户暂无可提现金额！", Toast.LENGTH_SHORT).show();
            // TODO: 2017/12/15 方便测试，去除对比金额限制，上线请移除
            //return;
        }

        PasswordEditDialog dialog = new PasswordEditDialog();
        dialog.addOnPassWordEditListener(new PasswordEditDialog.OnPassWordEditListener() {
            @Override
            public void onConfirmComplete(String password) {
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
                    applyFromNet(Utils.getLoginInfo(getContext()).member_id,cashValue,isUseAccountBalance()+"",password);
                }else if (checkTradePasswdResultsHttpResultModel.isNoneTradePassword()) {
                    //设置交易密码
                    GXPlayDialog dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Full_Confirm,
                            getResources().getString(R.string.common_dialog_trade_passwd_hint),
                            getResources().getString(R.string.common_dialog_without_trade_passwd));
                    dialog.addOnDialogActionListner(CashFragment.this);
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
    private void applyFromNet(String memberId,String amount,String isAccount,String tradePassword){
        Flowable<HttpResultModel<CashToResults>> fr = DataService.cashTo(new CashToRequestBody(memberId,amount,isAccount,tradePassword));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CashToResults>>() {
            @Override
            public void accept(HttpResultModel<CashToResults> cashToResultsHttpResultModel) throws Exception {
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    @Override
    public void onToggle(boolean on) {
        if (userInfo == null || Float.parseFloat(userInfo.balance) <= 0){
            mToggle.setToggleOff(true);
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
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onConfirm() {//跳转设置交易密码

    }
}
