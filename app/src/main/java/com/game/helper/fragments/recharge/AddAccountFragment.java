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
public class AddAccountFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = AddAccountFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    public static AddAccountFragment newInstance() {
        return new AddAccountFragment();
    }

    public AddAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_account;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_recharge_add_account));
        mHeadBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

}
