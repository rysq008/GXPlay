package com.zhny.library.presenter.login.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActiveMapTestBinding;
import com.zhny.library.presenter.monitor.view.MonitorActivity;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 模拟map跳转
 *
 * created by liming
 */
public class MapTestActivity extends BaseActivity {

    private ActiveMapTestBinding binding;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.active_map_test);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        binding.setLifecycleOwner(this);
        setToolBarTitle("测试map跳转");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    public void mpForUser(View view) {
        String userName = binding.userName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "用户名必输", Toast.LENGTH_SHORT).show();
            return;
        }
        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.LOGIN_USERNAME, userName);

        startActivity(MonitorActivity.class);
    }
}
