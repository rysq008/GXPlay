package com.game.helper.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.views.iWebView;

import butterknife.BindView;

/**
 *
 */
public class CommonWebviewActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "CommonWebviewActivity";
    public static final String EXREA_URL = "extra_url";

    @BindView(R.id.webView)
    iWebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(EXREA_URL);
        Log.e(TAG, "initData: 传进来的url:::::::::::" + url);

        webView.init(this);
        loadUrl();
    }

    private void loadUrl() {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_webview;
    }

    @Override
    public Object newP() {
        return null;
    }
}
