package com.game.helper.fragments.recharge;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RechargeSuccFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = RechargeSuccFragment.class.getSimpleName();
    public static final int Type_Cash_Succ = 0;
    public static final int Type_Recharge_Succ = 1;
    private int type = 0;
    private float value = 0;

    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.rl_confirm)
    View mConfirm;
    @BindView(R.id.tv_balance)
    TextView mBalance;
    @BindView(R.id.tv_type)
    TextView mType;
    @BindView(R.id.tv_balance1)
    TextView mBalance1;
    @BindView(R.id.tv_hint)
    TextView mHint;

    public static RechargeSuccFragment newInstance(int type, float value){
        return new RechargeSuccFragment(type,value);
    }

    @SuppressLint("ValidFragment")
    public RechargeSuccFragment(int type, float value) {
        // Required empty public constructor
        this.type = type;
        this.value = value;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge_successful;
    }

    private void initView(){
        mConfirm.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);

        if (type == Type_Cash_Succ){
            mHeadTittle.setText("提现成功");
            mType.setText("本次提现");
            mHint.setText(getResources().getString(R.string.recharge_cash_hint));
        }

        if (type == Type_Recharge_Succ){
            mHeadTittle.setText("充值成功");
            mType.setText("本次充值");
            mHint.setText(getResources().getString(R.string.recharge_succ_hint));
        }
        if (value != 0.00){
            mBalance.setText(value+"元");
            mBalance1.setText("￥"+value);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mConfirm){
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

}
