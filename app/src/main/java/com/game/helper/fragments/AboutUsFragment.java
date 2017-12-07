package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.game.helper.R;
import com.game.helper.activitys.AboutUsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = AboutUsFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.ll_verision)
    View mVersion;

    public static AboutUsFragment newInstance(){
        return new AboutUsFragment();
    }

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about_us;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_about_us));
        mHeadBack.setOnClickListener(this);
        mVersion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mVersion){
            if (getActivity() instanceof AboutUsActivity) {
                AboutUsActivity aboutUs = (AboutUsActivity) getActivity();
                aboutUs.switchFragment();
            }
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
