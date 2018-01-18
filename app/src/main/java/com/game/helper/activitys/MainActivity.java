package com.game.helper.activitys;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.GamePagerFragment;
import com.game.helper.fragments.GeneralizePagerFragment;
import com.game.helper.fragments.HomePagerFragment;
import com.game.helper.fragments.MinePagerFragment;
import com.game.helper.fragments.login.SetPasswordFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CommonShareResults;
import com.game.helper.model.H5UrlListResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.VersionCheckResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.VersionCheckRequestBody;
import com.game.helper.share.UMengShare;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.ToastUtil;
import com.game.helper.views.widget.CustomBadgeItem;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends XBaseActivity implements ViewPager.OnPageChangeListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.fragmentViewpager)
    ViewPager fragmentsViewPager;
    @BindView(R.id.bottom_bar)
    BottomNavigationBar bottomBar;

    private List<Fragment> radioFragmentList = new ArrayList<>();

    private boolean isFirst = false;

    CustomBadgeItem[] numberBadgeItem = new CustomBadgeItem[4];

    private BottomNavigationItem getTabItemBuilder(int drawableRes, int stringRes) {
        final BottomNavigationItem bottomNavigationItem =
                new BottomNavigationItem(drawableRes, stringRes).setActiveColor(getResources().getColor(R.color.app_color))
                        .setInActiveColor(getResources().getColor(R.color.defaultColor));
        switch (drawableRes) {
            case R.mipmap.tabbar_home_selected:
                numberBadgeItem[0] = new CustomBadgeItem();
                bottomNavigationItem.setBadgeItem(numberBadgeItem[0]);
                break;
            case R.mipmap.tabbar_game_selected:
                numberBadgeItem[1] = new CustomBadgeItem();
                bottomNavigationItem.setBadgeItem(numberBadgeItem[1]);
                break;
            case R.mipmap.tabbar_extension_selected:
                numberBadgeItem[2] = new CustomBadgeItem();
                bottomNavigationItem.setBadgeItem(numberBadgeItem[2]);
                break;
            case R.mipmap.tabbar_mine_selected:
                numberBadgeItem[3] = new CustomBadgeItem();
                bottomNavigationItem.setBadgeItem(numberBadgeItem[3]);
                break;
        }
        return bottomNavigationItem;
    }

    public void onSwitchFragment(SwitchFragmentEvent event) {
        switch (event) {
            case HOME:
                fragmentsViewPager.setCurrentItem(0);
                break;
            case GAME:
                fragmentsViewPager.setCurrentItem(1);
                break;
            case GENERALIZE:
                fragmentsViewPager.setCurrentItem(2);
                break;
            case MINE:
                fragmentsViewPager.setCurrentItem(3);
                break;
        }
    }

    private void initBottomNavigationBar() {
        int pos = bottomBar.getCurrentSelectedPosition();
        bottomBar.clearAll();
//        numberBadgeItem = null;
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.addItem(getTabItemBuilder(R.mipmap.tabbar_home_selected, R.string.home_tab_index));
        bottomBar.addItem(getTabItemBuilder(R.mipmap.tabbar_game_selected, R.string.home_tab_game));
        bottomBar.addItem(getTabItemBuilder(R.mipmap.tabbar_extension_selected, R.string.home_tab_extension));
        bottomBar.addItem(getTabItemBuilder(R.mipmap.tabbar_mine_selected, R.string.home_tab_mine));

        bottomBar.initialise();

        bottomBar.setVisibility(View.VISIBLE);

        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                fragmentsViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        bottomBar.selectTab(pos > 0 ? pos : 0);
        for (CustomBadgeItem item : numberBadgeItem) {
            item.setBorderColor(Color.TRANSPARENT)// Badge的Border颜色
                    .setBackgroundColor(Color.TRANSPARENT);// Badge背景颜色
            item.setPoint(true);
            item.refreshDrawable();
        }

        BusProvider.getBus().receive(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
            @Override
            public void accept(MsgEvent msgEvent) throws Exception {
                if ("CustomBadgeItem".equals(msgEvent.getMsg())) {
                    int position = msgEvent.getType();
                    int n = msgEvent.getRequestCode();
                    numberBadgeItem[position].setBorderColor(Color.TRANSPARENT)// Badge的Border颜色
                            .setBackgroundColor(Color.TRANSPARENT);// Badge背景颜色
                    if (n > 0) {
                        numberBadgeItem[position].setBorderWidth(0)// Badge的Border(边界)宽度
                                .setBorderColor(getResources().getColor(R.color.colorOrangeDepth))// Badge的Border颜色
                                .setBackgroundColor(getResources().getColor(R.color.colorOrangeDepth))// Badge背景颜色
                                .setGravity(Gravity.RIGHT | Gravity.TOP)// 位置，默认右上角
                                .setText(String.valueOf(n));// 显示的文本
                    }
                    numberBadgeItem[position].setPoint(n == 0);
                    numberBadgeItem[position].refreshDrawable();
                }
            }
        });
    }

    private void initView() {
        initBottomNavigationBar();
        radioFragmentList.clear();
        radioFragmentList.add(HomePagerFragment.newInstance());
        radioFragmentList.add(GamePagerFragment.newInstance());
        radioFragmentList.add(GeneralizePagerFragment.newInstance());
        radioFragmentList.add(MinePagerFragment.newInstance());

        fragmentsViewPager.setOffscreenPageLimit(3);
        fragmentsViewPager.setAdapter(new SwitchFragmentsAdapter(getSupportFragmentManager()));
        fragmentsViewPager.addOnPageChangeListener(this);

        if (isFirst) {
            bottomBar.selectTab(0);
            fragmentsViewPager.setCurrentItem(0);
            isFirst = false;
        }
        if (SharedPreUtil.isLogin()) {
            LoginUserInfo userInfo = SharedPreUtil.getLoginUserInfo();
            if (!userInfo.has_passwd) showSetPassWord();
        }
    }

    private void showSetPassWord() {
        DetailFragmentsActivity.launch(this, null, SetPasswordFragment.newInstance());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        //h5
        getH5UrlFromNet();
        updateVersion();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Object newP() {
        return null;
    }

    public class SwitchFragmentsAdapter extends FragmentStatePagerAdapter {
        public SwitchFragmentsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return radioFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return radioFragmentList.size();
        }

    }

    public enum SwitchFragmentEvent {
        HOME,
        GAME,
        GENERALIZE,
        MINE,
    }

    /**
     * h5
     */
    private void getH5UrlFromNet() {
        Flowable<HttpResultModel<H5UrlListResults>> fr = DataService.getH5UrlList();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<H5UrlListResults>>() {
            @Override
            public void accept(HttpResultModel<H5UrlListResults> h5UrlListResultsHttpResultModel) throws Exception {
                if (h5UrlListResultsHttpResultModel.isSucceful()) {
                    String market_url = h5UrlListResultsHttpResultModel.data.getMarket_url();
                    String vip_url = h5UrlListResultsHttpResultModel.data.getVip_url();
                    String account_guide_url = h5UrlListResultsHttpResultModel.data.getAccount_guide_url();
                    String expected_url = h5UrlListResultsHttpResultModel.data.getExpected_url();
                    String share_discount_url = h5UrlListResultsHttpResultModel.data.getShare_discount_url();
                    SharedPreUtil.saveH5Url(SharedPreUtil.H5_URL_MARKET, market_url);
                    SharedPreUtil.saveH5Url(SharedPreUtil.H5_URL_VIP, vip_url);
                    SharedPreUtil.saveH5Url(SharedPreUtil.H5_URL_ACCOUNT_GUIDE, account_guide_url);
                    SharedPreUtil.saveH5Url(SharedPreUtil.EXPECTED_URL, expected_url);
                    SharedPreUtil.saveH5Url(SharedPreUtil.SHARE_DISCOUNT_URL, share_discount_url);
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * umeng 分享
     *
     * @param shareInfo
     */
    public void umShare(CommonShareResults shareInfo) {
        if (null == shareInfo) {
            return;
        }

        UMengShare share = new UMengShare(this);
        share.shareLinkWithBoard(shareInfo, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e(TAG, "onStart: umShare");
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Log.e(TAG, "onResult: umShare");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Log.e(TAG, "onError: umShare");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Log.e(TAG, "onCancel: umShare");
            }
        });
    }


    private long pressTime = 0;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - pressTime > 2000) {
            //TotoroToast.makeText(context, "再次点击退出", 20).show();
            ToastUtil.showToast("再次点击退出");
            pressTime = curTime;
        } else {
            super.onBackPressed();
        }
    }


    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!SharedPreUtil.isLogin()) {
            //第一个参数为是否解绑推送的devicetoken
            ChatClient.getInstance().logout(true, new Callback() {
                @Override
                public void onSuccess() {
                    SharedPreUtil.remove(SharedPreUtil.TEMP_HUANXIN_NAME);
                    Log.d(TAG, "SharedPreUtil.TEMP_HUANXIN_NAME:" + SharedPreUtil.getString(SharedPreUtil.TEMP_HUANXIN_NAME, ""));

                }

                @Override
                public void onError(int i, String s) {
                    Log.d(TAG, "退出失败s-----" + s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
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
                        VersionParams.Builder builder = new VersionParams.Builder();
                        //如果仅使用下载功能，downloadUrl是必须的
                        builder.setOnlyDownload(true)
                                .setShowNotification(true)
                                .setForceRedownload(versionCheckResultsHttpResultModel.data.getIs_force_update())
                                .setDownloadAPKPath(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath() + "/G9Game")
                                .setDownloadUrl(versionCheckResultsHttpResultModel.data.getUrl())
                                //.setDownloadUrl("http://down1.uc.cn/down2/zxl107821.uc/miaokun1/UCBrowser_V11.5.8.945_android_pf145_bi800_(Build170627172528).apk")
                                .setTitle("检测到新版本")
                                .setUpdateMsg(versionCheckResultsHttpResultModel.data.getDesc());

                        AllenChecker.startVersionCheck(context.getApplication(), builder.build());
                        AllenChecker.init(true);


                    } else {
//                        ToastUtil.showToast("已经是最新的版本");

                    }
                }

            }
        });
    }
}
