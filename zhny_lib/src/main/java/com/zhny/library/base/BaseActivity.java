package com.zhny.library.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zhny.library.R;
import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    public LoadingDialog loadingDialog = null;
    private Timer timer = new Timer();

    {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) ->
                new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate));
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) ->
                new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate));
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dismissLoading() {
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> dismissLoading(true));
                }
            }, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissActualLoading() {
        dismissLoading(true);
    }


    public void dismissLoading(boolean isDismiss) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            if (isDismiss) loadingDialog.dismiss();
            else loadingDialog.hide();
        }
    }



    //是否沉浸状态栏
    private boolean isSetStatusBar = true;
    //是否允许全屏
    private boolean mAllowFullScreen = false;
    //是否禁止旋转屏幕
    private boolean isAllowScreenRotate = false;
    private Toolbar toolBar;
    private TextView titleTextView;
    private ImageView backImageView;
    private ImageView alarmImageView;
    private ImageView addImageView;
    private ImageView settingImageView;
    private ImageView editImageView;
    private ImageView chartImageView;
    private ImageView deleteImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
//            Density.setDensity( this);

//            AutoSizeConfig.getInstance().setCustomFragment(true);

            Utils.init(getApplication());
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            Bundle bundle = getIntent().getExtras();
            initParams(bundle);

            if (mAllowFullScreen) {
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
            }

            if (isSetStatusBar) {
                steepStatusBar();
            }

            Object obj = onBindView(savedInstanceState);
            if (obj instanceof Integer) {
                setContentView((Integer) obj);
            } else if (obj instanceof View) {
                setContentView((View) obj);
            }
            toolBar = findViewById(R.id.toolbar_app);
            titleTextView = findViewById(R.id.textview_toolbar_title);
            backImageView = findViewById(R.id.img_tootlbar_back);
            alarmImageView = findViewById(R.id.img_tootlbar_alarm);
            addImageView = findViewById(R.id.img_tootlbar_add);
            settingImageView = findViewById(R.id.img_tootlbar_setting);
            editImageView = findViewById(R.id.img_tootlbar_edit);
            chartImageView = findViewById(R.id.img_tootlbar_chart);
            deleteImageView = findViewById(R.id.img_tootlbar_delete);
            if (toolBar != null) {
                setSupportActionBar(toolBar);
            }
            if (titleTextView != null) {
                titleTextView.setText(getTitle());
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            initBusiness();

            if (isAllowScreenRotate) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            ImmersionBar.with(this).statusBarDarkFont(true).init();
            if (isImmersionBarEnabled() && setStatusBarView() != 0) {
                ImmersionBar.setStatusBarView(this, findViewById(setStatusBarView()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        }
        if (null != getToolbar() && isShowAlarming()) {
            showAlarm();
        }
        if (null != getToolbar() && isShowAdding()) {
            showAdd();
        }
        if (null != getToolbar() && isShowSetting()) {
            showSetting();
        }
        if (null != getToolbar() && isShowEdit()) {
            showEdit();
        }
        if (null != getToolbar() && isShowChart()) {
            showChart();
        }
        if (null != getToolbar() && isShowDelete()) {
            showDelete();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ImmersionBar.with(this) != null) ImmersionBar.with(this).destroy();
        if (loadingDialog != null)  loadingDialog.dismiss();
        if (timer != null) timer.cancel();
    }


    public Toolbar getToolbar() {
        return toolBar;
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getToolbarTitle() {
        return titleTextView;
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if (titleTextView != null) {
            titleTextView.setText(title);
        } else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }


    private void showBack() {
        backImageView.setVisibility(View.VISIBLE);
        backImageView.setOnClickListener(v -> onBackPressed());
    }

    protected void showAlarm() {
        alarmImageView.setVisibility(View.VISIBLE);
        alarmImageView.setOnClickListener(v -> onAlarmListener());
    }

    protected void showAdd() {
        addImageView.setVisibility(View.VISIBLE);
        addImageView.setOnClickListener(v -> onAddListener());
    }

    protected void showSetting() {
        settingImageView.setVisibility(View.VISIBLE);
        settingImageView.setOnClickListener(v -> onSettingListener());
    }

    protected void showEdit() {
        editImageView.setVisibility(View.VISIBLE);
        editImageView.setOnClickListener(v -> onEditListener());
    }

    protected void showChart() {
        chartImageView.setVisibility(View.VISIBLE);
        chartImageView.setOnClickListener(v -> onChartListener());
    }

    protected void showDelete() {
        deleteImageView.setVisibility(View.VISIBLE);
        deleteImageView.setOnClickListener(v -> onDeleteListener());
    }

    protected boolean isShowBacking() {
        return false;
    }

    protected boolean isShowAlarming() {
        return false;
    }

    protected boolean isShowAdding() {
        return false;
    }

    protected boolean isShowSetting() {
        return false;
    }

    protected boolean isShowEdit() {
        return false;
    }

    protected boolean isShowChart() {
        return false;
    }

    protected boolean isShowDelete() {
        return false;
    }

    /**
     * [初始化Bundle参数]
     *
     * @param params [初始化Bundle参数] 	 * 	 * @param params
     */
    public abstract void initParams(Bundle params);


    protected abstract Object onBindView(@Nullable Bundle savedInstanceState);

    public abstract void initBusiness();


    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }



    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * [是否允许全屏] 	 * 	 * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏] 	 * 	 * @param allowFullScreen
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [是否允许屏幕旋转] 	 * 	 * @param isAllowScreenRoate
     */
    public void setScreenRotate(boolean isAllowScreenRotate) {
        this.isAllowScreenRotate = isAllowScreenRotate;
    }

    protected int setStatusBarView() {
        return 0;
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void Toast(String text) {
//        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    //点击预警的按钮
    protected void onAlarmListener() {
    }

    //点击添加按钮
    protected void onAddListener() {
    }

    //点击设置按钮
    protected void onSettingListener() {
    }

    //点击编辑按钮
    protected void onEditListener() {
    }

    //点击数据报表按钮
    protected void onChartListener() {
    }

    //点击删除按钮
    protected void onDeleteListener() {
    }



}
