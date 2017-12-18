package com.game.helper.fragments.recharge;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechargeGoldFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = RechargeGoldFragment.class.getSimpleName();

    @BindView(R.id.tv_total_gold_up)
    TextView mTotalbalanceUp;
    @BindView(R.id.tv_total_gold_down)
    TextView mTotalBalanceDown;

    @BindView(R.id.ll_value_1)
    View mItemValue10;
    @BindView(R.id.ll_value_2)
    View mItemValue50;
    @BindView(R.id.ll_value_3)
    View mItemValue100;
    @BindView(R.id.ll_value_4)
    View mItemValue200;
    @BindView(R.id.ll_wechat)
    View mItemWechat;
    @BindView(R.id.ll_alipay)
    View mItemAlipay;
    @BindView(R.id.et_input_value)
    EditText mInputValue;
    @BindView(R.id.cb_value_1)
    CheckBox mCbValue10;
    @BindView(R.id.cb_value_2)
    CheckBox mCbValue50;
    @BindView(R.id.cb_value_3)
    CheckBox mCbValue100;
    @BindView(R.id.cb_value_4)
    CheckBox mCbValue200;
    @BindView(R.id.cb_wechat)
    CheckBox mCbWechat;
    @BindView(R.id.cb_alipay)
    CheckBox mCbAlipay;

    public static final int Charge_Gold_10 = 0;
    public static final int Charge_Gold_50 = 1;
    public static final int Charge_Gold_100 = 2;
    public static final int Charge_Gold_200 = 3;
    public static final int Charge_Gold_Custom = -1;

    /**
     * mode
     * 0：10金币
     * 1：50金币
     * 2：100金币
     * 3：200金币
     * -1：其他金额
     * */
    private int select_charge_mode = Charge_Gold_10;

    public static final int Pay_Type_Wechat = 0;
    public static final int Pay_Type_Alipay = 1;
    /**
     * mode
     * 0：微信
     * 1：支付宝
     * */
    private int select_pay_mode = Pay_Type_Wechat;
    private int mTotalChrgeGold = 0;

    public static RechargeGoldFragment newInstance(){
        return new RechargeGoldFragment();
    }

    public RechargeGoldFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge_gold;
    }

    private void initView(){
        mItemValue10.setOnClickListener(this);
        mItemValue50.setOnClickListener(this);
        mItemValue100.setOnClickListener(this);
        mItemValue200.setOnClickListener(this);
        mItemAlipay.setOnClickListener(this);
        mItemWechat.setOnClickListener(this);
        setCoinSelect(Charge_Gold_10);
        setPaySelect(Pay_Type_Wechat);

        mInputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearCoinSelect();
                if (s.toString().length() <= 0) return;
                try {
                    mTotalChrgeGold = Integer.parseInt(s.toString());
                    clearCoinSelect();
                    refreshGold();
                }catch (NumberFormatException e){}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mItemAlipay){
            setPaySelect(Pay_Type_Alipay);
        }
        if (v == mItemWechat){
            setPaySelect(Pay_Type_Wechat);
        }
        if (v == mItemValue10){
            setCoinSelect(Charge_Gold_10);
        }
        if (v == mItemValue50){
            setCoinSelect(Charge_Gold_50);
        }
        if (v == mItemValue100){
            setCoinSelect(Charge_Gold_100);
        }
        if (v == mItemValue200){
            setCoinSelect(Charge_Gold_200);
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    private void clearCoinSelect(){
        mCbValue10.setChecked(false);
        mCbValue50.setChecked(false);
        mCbValue100.setChecked(false);
        mCbValue200.setChecked(false);
        select_charge_mode = -1;
    }

    private void setCoinSelect(int position){
        clearCoinSelect();
        if (position == 0) mCbValue10.setChecked(true);
        if (position == 1) mCbValue50.setChecked(true);
        if (position == 2) mCbValue100.setChecked(true);
        if (position == 3) mCbValue200.setChecked(true);
        select_charge_mode = position;
        //refresh
        refreshGold();
    }

    private void clearPaySelect(){
        mCbWechat.setChecked(false);
        mCbAlipay.setChecked(false);
        select_pay_mode = -1;
    }

    private void setPaySelect(int mode){
        clearPaySelect();
        select_pay_mode = mode;
        if (mode == 0) mCbWechat.setChecked(true);
        if (mode == 1) mCbAlipay.setChecked(true);
    }

    private void refreshGold(){
        if (select_charge_mode == 0) mTotalChrgeGold = 10;
        if (select_charge_mode == 1) mTotalChrgeGold = 50;
        if (select_charge_mode == 2) mTotalChrgeGold = 100;
        if (select_charge_mode == 3) mTotalChrgeGold = 200;
        if (select_charge_mode == -1) {
            if (mInputValue.getText().toString().length() != 0){
                mTotalChrgeGold = Integer.parseInt(mInputValue.getText().toString());
            }else {
                mTotalChrgeGold = 0;
            }
        }
        mTotalbalanceUp.setText("金币："+mTotalChrgeGold);
        mTotalBalanceDown.setText(mTotalChrgeGold+".0");
    }

    /**
     * 付款模式
     * */
    public int getSelect_pay_mode() {
        return select_pay_mode;
    }

    /**
     * 充值数量
     * */
    public int getmTotalChrgeGold() {
        return mTotalChrgeGold;
    }
}
