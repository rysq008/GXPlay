package com.game.helper.fragments.wallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletListFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = WalletListFragment.class.getSimpleName();

    public static WalletListFragment newInstance(){
        return new WalletListFragment();
    }

    public WalletListFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet_list;
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
