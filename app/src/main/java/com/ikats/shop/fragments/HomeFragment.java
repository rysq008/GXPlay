package com.ikats.shop.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.ikats.shop.R;
import com.ikats.shop.event.RxBusProvider;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
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
    UltraViewPager viewPager;
    @BindView(R.id.home_rg)
    RadioGroup radioGroup;

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
        mFragment.add(BillingFragment.newInstance());
        mFragment.add(SearchFragment.newInstance());
        mFragment.add(StatisticsFragment.newInstance());
        viewPager.setScrollMode(UltraViewPager.ScrollMode.VERTICAL);
        viewPager.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragment, null));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position < 3)
                    radioGroup.check(radioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setOffscreenPageLimit(3);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            int pos = group.indexOfChild(radioButton);
            if (pos < 3)
                viewPager.setCurrentItem(pos);
        });
//        //initialize built-in indicator
//        viewPager.initIndicator();
////set style of indicators
//        viewPager.getIndicator()
//                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
//                .setFocusColor(Color.GREEN)
//                .setNormalColor(Color.WHITE)
//                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
////set the alignment
//        viewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
////construct built-in indicator, and add it to  UltraViewPager
//        viewPager.getIndicator().build();
//
////set an infinite loop
//        viewPager.setInfiniteLoop(true);
////enable auto-scroll mode
//        viewPager.setAutoScroll(2000);
        mpos.Set(mcom);
    }

    @OnClick({R.id.home_rb_billing})
    public void OnViewClick(View view) {
        switch (view.getId()) {
            case R.id.home_rb_billing:
                ((BillingFragment)mFragment.get(0)).handler.sendEmptyMessage(-1);
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
