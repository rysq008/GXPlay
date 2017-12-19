package com.game.helper.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

import butterknife.BindView;

public class AccountManageFragment extends XBaseFragment implements View.OnClickListener{
    private static final String TAG = AccountManageFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    public static AccountManageFragment newInstance(){
        return new AccountManageFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_account_mannager;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_account_mannager));
        mHeadBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
