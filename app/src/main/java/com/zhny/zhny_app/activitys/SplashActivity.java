package com.zhny.zhny_app.activitys;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zhny.zhny_app.R;
import com.zhny.zhny_app.activitys.BaseActivity.XBaseActivity;
import com.zhny.zhny_app.model.LoginBean;
import com.zhny.zhny_app.utils.ShareUtils;

import butterknife.BindView;


/**
 * 时   间：2019/1/12
 * 简   述：<功能简述>
 */
public class SplashActivity extends XBaseActivity {
    @BindView(R.id.splash_countdown_tv)
    TextView tv_countdown;
    @BindView(R.id.splash_iv)
    ImageView iv_splash;

    private static final int REQUEST_CODE = 0;//权限请求码
    private static final String PACKAGE_URL_SCHEME = "package:";//权限方案
    //配置需要取的权限
    private static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.READ_PHONE_STATE,  //读取权限
            Manifest.permission.ACCESS_COARSE_LOCATION,//定位权限
            Manifest.permission.INTERNET
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        iv_splash.setOnClickListener(v -> {
            DetailFragmentsActivity.launch(context, null, com.andorid.greenorange.fragments.GuideFragment.newInstance());
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getRxPermissions().request(PERMISSION).subscribe(isGranted -> {
            if (isGranted) {
//                ShareUtils.putString("phoneImei", SystemUtil.getIMEI(SplashActivity.this) + "");
//                LocationManagerUtils.requesetLocation(context, (amapLocation) -> {
//                    toLogin();
//                });
                CountDownTimer cdt = new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tv_countdown.setText(String.valueOf(millisUntilFinished / 1000 + 0));
                    }

                    @Override
                    public void onFinish() {
                        if (!context.isFinishing())
                            DetailFragmentsActivity.launch(context, null, com.andorid.greenorange.fragments.GuideFragment.newInstance());
                        context.finish();
                    }
                };
                cdt.start();
            } else {
                showMissingPermissionDialog();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public Object newP() {
        return null;
    }

    private void toLogin() {
        if (!com.andorid.greenorange.net.api.Api.isOnlineHost()) {
            com.andorid.greenorange.views.ToastMgrView.getInstance().showLengthShort(context, 2, ShareUtils.getHost("host"));
        }
        LoginBean bean = ShareUtils.getLoginInfo();
        tv_countdown.postDelayed(() -> finish(), 1500);
    }


    //显示对话框提示用户缺少权限
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("帮助");//提示帮助
        builder.setMessage("当前应用缺少必要权限,\\n\\n会造成应用无法运行!\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。\\n\\n最后点击两次后退按钮，即可返回。");

        //如果是拒绝授权，则退出应用
        //退出
        builder.setNegativeButton("退出", (dialog, which) -> {
            //权限不足
            finish();
        });
        //打开设置，让用户选择打开权限
        builder.setPositiveButton("设置", (dialog, which) -> {
            dialog.dismiss();
            startAppSettings();//打开设置
        });
        builder.setCancelable(false);
        builder.show();
    }

    //打开系统应用设置(ACTION_APPLICATION_DETAILS_SETTINGS:系统设置权限)
    private void startAppSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        if (!TextUtils.isEmpty(PACKAGE_URL_SCHEME + getPackageName())) {
            intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
