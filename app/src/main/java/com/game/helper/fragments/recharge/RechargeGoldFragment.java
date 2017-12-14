package com.game.helper.fragments.recharge;


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
public class RechargeGoldFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = RechargeGoldFragment.class.getSimpleName();

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
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public Object newP() {
        return null;
    }

}
