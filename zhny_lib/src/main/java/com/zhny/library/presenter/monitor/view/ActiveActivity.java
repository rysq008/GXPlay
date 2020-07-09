package com.zhny.library.presenter.monitor.view;

import android.os.Bundle;
import android.view.View;

import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.databinding.ActivityActiveBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * created by liming
 */
public class ActiveActivity extends BaseActivity {

    private ActivityActiveBinding binding;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active);
        return null;
    }

    @Override
    public void initBusiness() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    public void handleReturn(View view) {
        finish();
    }
}
