package com.ikats.shop.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ikats.shop.R;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.views.VerticalViewPager;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;

public class CashierFragment extends XBaseFragment {
    @BindView(R.id.cashier_viewpager)
    VerticalViewPager viewPager;
    @BindView(R.id.cashier_rg)
    RadioGroup radioGroup;

    private List<Fragment> mFragment = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setTranslucent(context, 0);
        viewPager.setScroll(false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mFragment.add(BillingFragment.newInstance());
        mFragment.add(SearchFragment.newInstance());
        mFragment.add(StatisticsFragment.newInstance());
        mFragment.add(SettingFragment.newInstance());
//        viewPager.setScrollMode(UltraViewPager.ScrollMode.VERTICAL);
        viewPager.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragment, null));
        viewPager.setOffscreenPageLimit(3);
//        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            RadioButton radioButton = group.findViewById(checkedId);
//            int pos = group.indexOfChild(radioButton);
//            if (pos < 3)
//                viewPager.setCurrentItem(pos);
//        });
    }

    @OnClick({R.id.cashier_rb_billing, R.id.cashier_rb_search, R.id.cashier_rb_statis, R.id.cashier_rb_setting})
    public void OnViewClick(View view) {
        switch (view.getId()) {
            case R.id.cashier_rb_billing:
                if (viewPager.getCurrentItem() == 0)
                    ((BillingFragment) mFragment.get(0)).handler.sendEmptyMessage(-1);
                else
                    viewPager.setCurrentItem(0);
                break;
            case R.id.cashier_rb_search:
                viewPager.setCurrentItem(1);
                break;
            case R.id.cashier_rb_statis:
                viewPager.setCurrentItem(2);
                break;
            case R.id.cashier_rb_setting:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public boolean onBackPress(Activity activity) {
        return ((XBaseFragment) mFragment.get(viewPager.getCurrentItem())).onBackPress(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_cashier;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static CashierFragment newInstance() {
        return new CashierFragment();
    }


}
