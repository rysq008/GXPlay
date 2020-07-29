package com.ikats.shop.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.ikats.shop.R;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;

import butterknife.OnClick;

public class OpenShopFragment extends XBaseFragment {


    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_shop_layout;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("注册代表您同意《服务条款》和《隐私协议》");
        ssb.setSpan(new URLSpan("http://www.baidu.com"), 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new URLSpan("http://www.qq.com"), 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#F9993F")), 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#F9993F")), 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 7, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 14, 20, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

    }

    final CountDownTimer[] cdt = new CountDownTimer[1];

    @OnClick({R.id.add_shop_save_btn, R.id.add_shop_cancel_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_shop_save_btn:
//                getP().requestRegister(context, user_et.getText().toString(), code_et.getText().toString(), pwd_et.getText().toString(), repwd_et.getText().toString());
                break;
            case R.id.add_shop_cancel_btn:
//                getP().requestRegister(context, user_et.getText().toString(), code_et.getText().toString(), pwd_et.getText().toString(), repwd_et.getText().toString());
                break;
            default:
                break;
        }
    }

    public static OpenShopFragment newInstance() {
        return new OpenShopFragment();
    }

}
