package com.game.helper.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;

import butterknife.BindView;

public class FeedBackActivity extends XBaseActivity implements View.OnClickListener{
    private static final String TAG = FeedBackActivity.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.feedback_tittle));
        mHeadBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
