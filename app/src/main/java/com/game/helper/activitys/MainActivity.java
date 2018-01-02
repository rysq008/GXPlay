package com.game.helper.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.GamePagerFragment;
import com.game.helper.fragments.GeneralizePagerFragment;
import com.game.helper.fragments.HomePagerFragment;
import com.game.helper.fragments.MinePagerFragment;
import com.game.helper.fragments.login.ResetPasswdFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CommonShareResults;
import com.game.helper.model.H5UrlListResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.net.DataService;
import com.game.helper.share.UMengShare;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.widget.CustomBadgeItem;
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

public class MainActivity extends XBaseActivity implements ViewPager.OnPageChangeListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.fragmentViewpager)
    ViewPager fragmentsViewPager;
    @BindView(R.id.bottom_bar)
    BottomNavigationBar bottomBar;


    private List<Fragment> radioFragmentList = new ArrayList<>();

    private boolean isFirst = false;

    CustomBadgeItem numberBadgeItem;

    private BottomNavigationItem getTabItemBuilder(int drawableRes, int stringRes) {
        final BottomNavigationItem bottomNavigationItem =
                new BottomNavigationItem(drawableRes, stringRes).setActiveColor(getResources().getColor(R.color.app_color))
                        .setInActiveColor(getResources().getColor(R.color.defaultColor));
        if (drawableRes == R.mipmap.tabbar_home_selected) {
            numberBadgeItem = new CustomBadgeItem();
            bottomNavigationItem.setBadgeItem(numberBadgeItem);
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
        numberBadgeItem = null;
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar
                .addItem(getTabItemBuilder(R.mipmap.tabbar_home_selected, R.string.home_tab_index));
        bottomBar
                .addItem(getTabItemBuilder(R.mipmap.tabbar_game_selected, R.string.home_tab_game));
        bottomBar
                .addItem(getTabItemBuilder(R.mipmap.tabbar_extension_selected, R.string.home_tab_extension));
        bottomBar
                .addItem(getTabItemBuilder(R.mipmap.tabbar_mine_selected, R.string.home_tab_mine));

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
        long n = 0;
        numberBadgeItem.setBorderColor(Color.TRANSPARENT)// Badge的Border颜色
                .setBackgroundColor(Color.TRANSPARENT);// Badge背景颜色
//        if (null != agentTaskCollect)
        {
            n = 9;
            if (n > 0) {
                numberBadgeItem.setBorderWidth(0)// Badge的Border(边界)宽度
                        .setBorderColor(getResources().getColor(R.color.colorOrangeDepth))// Badge的Border颜色
                        .setBackgroundColor(getResources().getColor(R.color.colorOrangeDepth))// Badge背景颜色
                        .setGravity(Gravity.RIGHT | Gravity.TOP)// 位置，默认右上角
                        .setText(String.valueOf(n));// 显示的文本
            }
        }
        numberBadgeItem.setPoint(n == 0);
        numberBadgeItem.refreshDrawable();

    }

    private void initView() {
        initBottomNavigationBar();
        radioFragmentList.clear();
        radioFragmentList.add(HomePagerFragment.newInstance());
        radioFragmentList.add(GamePagerFragment.newInstance());
        radioFragmentList.add(GeneralizePagerFragment.newInstance());
        radioFragmentList.add(MinePagerFragment.newInstance());

        fragmentsViewPager.setOffscreenPageLimit(3);
        fragmentsViewPager.setAdapter(
                new SwitchFragmentsAdapter(getSupportFragmentManager()));
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
        DetailFragmentsActivity.launch(this, null, ResetPasswdFragment.newInstance());
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
                    SharedPreUtil.saveH5Url(SharedPreUtil.H5_URL_MARKET, market_url);
                    SharedPreUtil.saveH5Url(SharedPreUtil.H5_URL_VIP, vip_url);
                    SharedPreUtil.saveH5Url(SharedPreUtil.H5_URL_ACCOUNT_GUIDE, account_guide_url);
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

    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
