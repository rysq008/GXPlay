package com.game.helper.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.activitys.MainActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.VersionCheckResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.VersionCheckRequestBody;
import com.game.helper.utils.DownloadApkHelper;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.ToastUtil;
import com.game.helper.utils.Utils;
import com.game.helper.views.GXPlayDialog;

import java.io.File;
import java.util.concurrent.Executors;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import util.UpdateAppUtils;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * A simple {@link Fragment} subclass.
 * 系统相关设置
 */
public class SettingSystemFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = SettingSystemFragment.class.getSimpleName();
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.ll_about_us)
    View mAboutUs;
    @BindView(R.id.tv_exit_login)
    View mExit;
    @BindView(R.id.tv_cache)
    TextView mCache;
    @BindView(R.id.ll_clear_cache)
    View mClearCache;
    @BindView(R.id.ll_update_version_setting_system)
    LinearLayout mLlUpdateVersion;

    private Handler handler = new Handler();
    private DownloadApkHelper downloadHelper;
    private static final int REQUEST_CODE =1001;

    public static SettingSystemFragment newInstance() {
        return new SettingSystemFragment();
    }

    public SettingSystemFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting_system;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_setting_system));
        mHeadBack.setOnClickListener(this);

        try {
            mCache.setText(Utils.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAboutUs.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mClearCache.setOnClickListener(this);
        mLlUpdateVersion.setOnClickListener(this);
    }

    private void loginOut() {

        Flowable<HttpResultModel<LogoutResults>> fr = DataService.logout();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<LogoutResults>>() {
            @Override
            public void accept(HttpResultModel<LogoutResults> logoutResultsHttpResultModel) throws Exception {
                if (logoutResultsHttpResultModel.isSucceful()) {
                    SharedPreUtil.clearSessionId();
                    SharedPreUtil.cleanLoginUserInfo();
                    ILFactory.getLoader().clearMemoryCache(context);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            ILFactory.getLoader().clearDiskCache(context);
                        }
                    });
                    getActivity().onBackPressed();
                } else {
                    //Toast.makeText(getContext(), logoutResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });

//        Utils.clearLoginInfo(getContext());
//        SharedPreUtil.saveSessionId("");
//        getActivity().onBackPressed();
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
        if (v == mExit) {
            GXPlayDialog dialog = new GXPlayDialog(GXPlayDialog.Ddialog_Without_tittle_Block_Confirm, "退出登录", "确定要退出登录？");
            dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onConfirm() {
                    loginOut();
                }
            });
            dialog.show(getChildFragmentManager(), GXPlayDialog.TAG);
        }
        if (v == mAboutUs) {
            DetailFragmentsActivity.launch(getContext(), null, AboutUsFragment.newInstance());
        }
        if (v == mClearCache) {
            GXPlayDialog dialog = new GXPlayDialog(GXPlayDialog.Ddialog_Without_tittle_Block_Confirm, "", "确认清除本地缓存？");
            dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onConfirm() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCache.setText("0MB");
                            Utils.cleanFiles(getContext());//data/data/file
                            Utils.cleanExternalCache(getContext());//外存
                            Utils.cleanInternalCache(getContext());//清除本应用内部缓存
                            Utils.clearAllCache(getContext());
                            Toast.makeText(getContext(), "清除缓存成功！", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            });
            dialog.show(getChildFragmentManager(), GXPlayDialog.TAG);
        }
        if (v == mLlUpdateVersion) {
            G9RequestPermissions();
        }
    }



    private void updateVersion() {
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        Flowable<HttpResultModel<VersionCheckResults>> fv = DataService.updateVersion(new VersionCheckRequestBody(packageInfo.versionName));
        RxLoadingUtils.subscribeWithDialog(context, fv, this.bindToLifecycle(), new Consumer<HttpResultModel<VersionCheckResults>>() {
            @Override
            public void accept(final HttpResultModel<VersionCheckResults> versionCheckResultsHttpResultModel) throws Exception {
                if (versionCheckResultsHttpResultModel.isSucceful()) {
                    if (versionCheckResultsHttpResultModel.data.isHas_new()) {
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("亲,确定更新版本吗?")
                                .setMessage("要更新的版本是" + versionCheckResultsHttpResultModel.data.getVersion())
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                })
                                .create().show();*/
                        UpdateAppUtils.from(getActivity())
                                //.checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                                .serverVersionCode(versionCheckResultsHttpResultModel.data.getVersion_code())
                                .serverVersionName(versionCheckResultsHttpResultModel.data.getVersion())
                                .apkPath(versionCheckResultsHttpResultModel.data.getUrl())
                                .showNotification(true) //是否显示下载进度到通知栏，默认为true
                                .updateInfo(versionCheckResultsHttpResultModel.data.getDesc())  //更新日志信息 String
                                //.downloadBy(UpdateAppUtils.DOWNLOAD_BY_BROWSER) //下载方式：app下载、手机浏览器下载。默认app下载
                                .isForce(versionCheckResultsHttpResultModel.data.isIs_force_update()) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                                .update();


                    } else {
                        ToastUtil.showToast("已经是最新的版本");

                    }
                }

            }
        });
    }




    //权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateVersion();
                } else {
                    SettingSystemFragment.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE);
                }
                break;
        }

    }

    private void G9RequestPermissions() {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            SettingSystemFragment.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {
            updateVersion();
        }
    }


}

