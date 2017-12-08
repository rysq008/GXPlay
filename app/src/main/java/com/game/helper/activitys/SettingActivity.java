package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.SettingSystemFragment;
import com.game.helper.fragments.SettingUserFragment;

/**
 * 个人信息/系统设置共用
 * */
public class SettingActivity extends XBaseActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    public static final String SETTING_SHOW_FRAGMENT = "setting_show_fragment";

    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    private void initView() {
        Intent intent = this.getIntent();
        if (intent == null || !intent.hasExtra(SETTING_SHOW_FRAGMENT))
            return;

        String showWhichFragment = intent.getStringExtra(SETTING_SHOW_FRAGMENT);
        transaction = this.getSupportFragmentManager().beginTransaction();

        if (showWhichFragment.equals(SettingSystemFragment.TAG)) {
            SettingSystemFragment fragment = SettingSystemFragment.newInstance();
            transaction.add(R.id.root_layout, fragment, SettingSystemFragment.TAG)
                    .show(fragment).commit();
        } else if (showWhichFragment.equals(SettingUserFragment.TAG)) {
            SettingUserFragment fragment = SettingUserFragment.newInstance();
            transaction.add(R.id.root_layout, fragment, SettingUserFragment.TAG)
                    .show(fragment).commit();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
