package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.ExtensionHistoryFragment;
import com.game.helper.fragments.ExtensionProfitFragment;

/**
 * 简单布局共用的activity
 *
 * 使用时必须传intent显示的fragment
 * */
public class CommonSimpleActivity extends XBaseActivity {
    private static final String TAG = CommonSimpleActivity.class.getSimpleName();
    public static final String SHOW_WHICH_FRAGMENT = "show_which_fragment";

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_simple;
    }

    private void initView() {
        Intent intent = this.getIntent();
        if (intent == null || !intent.hasExtra(SHOW_WHICH_FRAGMENT))
            return;

        String showWhichFragment = intent.getStringExtra(SHOW_WHICH_FRAGMENT);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        if (showWhichFragment.equals(ExtensionHistoryFragment.TAG)) {
            ExtensionHistoryFragment fragment = ExtensionHistoryFragment.newInstance();
            transaction.add(R.id.root_layout, fragment, ExtensionHistoryFragment.TAG)
                    .show(fragment).commit();
        }else if (showWhichFragment.equals(ExtensionProfitFragment.TAG)){
            ExtensionProfitFragment fragment = ExtensionProfitFragment.newInstance();
            transaction.add(R.id.root_layout, fragment, ExtensionProfitFragment.TAG)
                    .show(fragment).commit();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
