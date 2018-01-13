package com.game.helper.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.utils.ToastUtil;

import butterknife.BindView;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = AboutUsFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.ll_verision)
    View mVersion;
    @BindView(R.id.tv_version_name_about_us)
    TextView mTvVersionName;
    @BindView(R.id.ll_company_phone_about_us)
    LinearLayout mLlPhone;
    @BindView(R.id.tv_company_phone_about_us)
    TextView mTvPhone;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about_us;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_about_us));
        mHeadBack.setOnClickListener(this);
        mVersion.setOnClickListener(this);
        mLlPhone.setOnClickListener(this);
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            mTvVersionName.setText( "V"+packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {


        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
        if (v == mVersion) {
            DetailFragmentsActivity.launch(getContext(), null, VersionInfoFragment.newInstance());
        }
        if (v == mLlPhone) {
//            G9RequestPermissions();
            callPhone();
        }
    }

    private void G9RequestPermissions() {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
               /* if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                        Manifest.permission.CALL_PHONE)) {
                    // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                    // 弹窗需要解释为何需要该权限，再次请求授权
                    ToastUtil.showToast("请授权！");

                    // 帮跳转到该应用的设置界面，让用户手动授权
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }*/
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // 已经获得授权，可以打电话
            callPhone();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    private void callPhone() {
        Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mTvPhone.getText().toString().trim()));
        startActivity(intentPhone);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    callPhone();
                } else {
                    // 授权失败！
                    ToastUtil.showToast("授权失败！");
                }
                break;
            }
        }

    }
}
