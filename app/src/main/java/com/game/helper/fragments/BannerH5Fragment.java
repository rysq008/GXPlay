package com.game.helper.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.views.XReloadableStateContorller;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Tian on 2017/12/27.
 */

public class BannerH5Fragment extends XBaseFragment {

    @BindView(R.id.wv_banner_h5)
    WebView wvBannerH5;
    @BindView(R.id.xController_banner_h5)
    XReloadableStateContorller xControllerBannerH5;

    public static BannerH5Fragment newInstance() {
        return new BannerH5Fragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //声明WebSettings子类
        WebSettings webSettings = wvBannerH5.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String url = arguments.getString("h5url");
            wvBannerH5.loadUrl(url);
            wvBannerH5.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    xControllerBannerH5.showLoading();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    xControllerBannerH5.showContent();
                }
            });

        } else {
            xControllerBannerH5.showError();
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_banner_h5;
    }

    @Override
    public Object newP() {
        return null;
    }

}
