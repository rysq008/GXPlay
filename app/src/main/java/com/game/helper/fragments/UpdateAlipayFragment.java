package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateAlipayFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = UpdateAlipayFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    public static UpdateAlipayFragment newInstance(){
        return new UpdateAlipayFragment();
    }

    public UpdateAlipayFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_alipay;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_update_alipay));
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
