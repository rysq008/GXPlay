package com.game.helper.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

import butterknife.BindView;

public class FeedBackFragment extends XBaseFragment implements View.OnClickListener{
    private static final String TAG = FeedBackFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    public static FeedBackFragment newInstance(){
        return new FeedBackFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_feed_back;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.feedback_tittle));
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
