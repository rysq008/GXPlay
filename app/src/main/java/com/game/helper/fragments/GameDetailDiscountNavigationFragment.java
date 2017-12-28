package com.game.helper.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.views.XReloadableStateContorller;

import butterknife.BindView;

/**
 * Created by Tian on 2017/12/27.
 */

public class GameDetailDiscountNavigationFragment extends XBaseFragment {

    @BindView(R.id.wv_discount_navigation_game_detail)
    WebView wvDiscount;
    @BindView(R.id.xController_discount_navigation_game_detail)
    XReloadableStateContorller xController;
    public static GameDetailDiscountNavigationFragment newInstance() {
        return new GameDetailDiscountNavigationFragment();
    }
    @Override
    public void initData(Bundle savedInstanceState) {
        //声明WebSettings子类
        WebSettings webSettings = wvDiscount.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String url = arguments.getString("discount");
            wvDiscount.loadUrl(url);
            wvDiscount.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    xController.showLoading();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    xController.showContent();
                }
            });

        } else {
            xController.showError();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_discount_navigation;
    }

    @Override
    public Object newP() {
        return null;
    }

}
