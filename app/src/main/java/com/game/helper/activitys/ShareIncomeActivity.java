package com.game.helper.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.views.HeadImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareIncomeActivity extends XBaseActivity implements View.OnClickListener {

    @BindView(R.id.share_head_iv)
    HeadImageView shareHeadIv;
    @BindView(R.id.phoneNumTv)
    TextView phoneNumTv;
    @BindView(R.id.shareNum)
    TextView shareNum;
    @BindView(R.id.coinNum)
    TextView coinNum;
    @BindView(R.id.shareBtn)
    TextView shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        shareBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareBtn:

                break;
            default:
                break;
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_share_income;
    }

    @Override
    public Object newP() {
        return null;
    }
}
