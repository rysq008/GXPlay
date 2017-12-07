package com.game.helper.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.GamePagerFragment;
import com.game.helper.fragments.GeneralizePagerFragment;
import com.game.helper.fragments.HomePagerFragment;
import com.game.helper.fragments.LoginFragment;
import com.game.helper.fragments.MinePagerFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.net.DataService;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.widget.CustomBadgeItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static com.game.helper.GameMarketApplication.getContext;

public class MainActivity extends XBaseActivity implements ViewPager.OnPageChangeListener {

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
        login();
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

    // TODO: 2017/12/7 senssion id 每回退出都失效 在打开主页每回都检测一遍是否登陆 否则模拟测试环境登陆
    private void login(){
        if (!Utils.hasLoginInfo(getContext())) return;

        String account = Utils.getLoginInfo(getContext()).phone;
        String code = "9870";
        int type = LoginFragment.LOGIN_TYPE_MESSAGE;
        Flowable<HttpResultModel<LoginResults>> fr = DataService.login(new LoginRequestBody(account,code,type+"",""));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<LoginResults>>() {
            @Override
            public void accept(HttpResultModel<LoginResults> loginResultsHttpResultModel) throws Exception {
                if (loginResultsHttpResultModel.isSucceful()) {
                    LoginUserInfo userInfo = new LoginUserInfo(
                            loginResultsHttpResultModel.data.phone,loginResultsHttpResultModel.data.member_id);
                    Utils.writeLoginInfo(getContext(),userInfo);
                }else {
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
            }
        });
    }
}
