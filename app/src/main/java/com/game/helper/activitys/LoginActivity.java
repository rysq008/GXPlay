package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.LoginFragment;
import com.game.helper.fragments.RegistFragment;

public class LoginActivity extends XBaseActivity {
    public static final String SHOW_WHITCH_FRAGMENT = "show_whitch_fragment";
    private String show_whitch_fragment = null;

    @Override
    public void initData(Bundle savedInstanceState) {
        getData();
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    private void getData() {
        Intent intent = this.getIntent();
        if (intent == null || !intent.hasExtra(SHOW_WHITCH_FRAGMENT)) return;
        show_whitch_fragment = intent.getStringExtra(SHOW_WHITCH_FRAGMENT);
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        if (show_whitch_fragment.equals(LoginFragment.TAG)) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            manager.beginTransaction().add(R.id.root_layout, loginFragment).show(loginFragment).commit();
        } else if (show_whitch_fragment.equals(RegistFragment.TAG)) {
            RegistFragment registFragment = RegistFragment.newInstance();
            manager.beginTransaction().add(R.id.root_layout, registFragment).show(registFragment).commit();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
