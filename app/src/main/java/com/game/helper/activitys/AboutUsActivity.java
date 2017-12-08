package com.game.helper.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.AboutUsFragment;
import com.game.helper.fragments.VersionInfoFragment;

/**
 * 版本发展／关于我们 共用
 * */
public class AboutUsActivity extends XBaseActivity {
    private static final String TAG = AboutUsActivity.class.getSimpleName();
    private FragmentManager manager;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    private void initView() {
        manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        AboutUsFragment aboutUsFragment = AboutUsFragment.newInstance();
        VersionInfoFragment versionInfoFragment = VersionInfoFragment.newInstance();

        ft.add(R.id.root_layout,aboutUsFragment,AboutUsFragment.TAG)
                .add(R.id.root_layout,versionInfoFragment,VersionInfoFragment.TAG)
                .show(aboutUsFragment)
                .commit();
    }

    public void switchFragment(){
        Fragment fragment = manager.findFragmentByTag(AboutUsFragment.TAG);
        if (fragment.isVisible()) {
            manager.beginTransaction()
                    .hide(fragment)
                    .show(manager.findFragmentByTag(VersionInfoFragment.TAG))
                    .commit();
        }else {
            manager.beginTransaction()
                    .hide(manager.findFragmentByTag(VersionInfoFragment.TAG))
                    .show(fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = manager.findFragmentByTag(AboutUsFragment.TAG);
        if(fragment.isVisible()){
            finish();
        }else {
            switchFragment();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
