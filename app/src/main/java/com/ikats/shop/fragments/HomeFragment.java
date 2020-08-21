package com.ikats.shop.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.ikats.shop.R;
import com.ikats.shop.event.RxBusProvider;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.views.VerticalViewPager;
import com.jaeger.library.StatusBarUtil;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.COMIO;
import com.lvrenyang.io.base.IOCallBack;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;

public class HomeFragment extends XBaseFragment {
    @BindView(R.id.home_viewpager)
    ViewPager viewPager;
    @BindView(R.id.home_rb_cashier)
    RadioButton cashier_rb;
    @BindView(R.id.home_rb_server)
    RadioButton server_rb;
    @BindView(R.id.home_tv_user)
    TextView user_tv;

    private List<Fragment> mFragment = new ArrayList<>();
    private XFragmentAdapter mAdapter;
    public static Pos mpos = new Pos();
    public static COMIO mcom = new COMIO();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setTranslucent(context, 0);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mFragment.add(CashierFragment.newInstance());
        mFragment.add(BackstageFragment.newInstance());
//        mFragment.add(CashierFragment.newInstance());

        viewPager.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragment, null));

        viewPager.setOffscreenPageLimit(2);
        mpos.Set(mcom);
    }

    @OnClick({R.id.home_rb_cashier,R.id.home_rb_server,R.id.home_tv_user})
    public void OnViewClick(View view) {
        switch (view.getId()) {
            case R.id.home_rb_cashier:
                viewPager.setCurrentItem(0);
                break;
            case R.id.home_rb_server:
                viewPager.setCurrentItem(1);
                break;
            case R.id.home_tv_user:
//                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public boolean onBackPress(Activity activity) {
        return ((XBaseFragment) mFragment.get(viewPager.getCurrentItem())).onBackPress(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mcom.Close();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


}
