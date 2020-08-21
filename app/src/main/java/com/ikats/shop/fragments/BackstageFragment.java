package com.ikats.shop.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
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

public class BackstageFragment extends XBaseFragment {
    @BindView(R.id.backstage_viewpager)
    VerticalViewPager viewPager;
    @BindView(R.id.backstage_rg)
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
        mFragment.add(SearchFragment.newInstance());
        mFragment.add(StatisticsFragment.newInstance());
        viewPager.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragment, null));
        viewPager.setOffscreenPageLimit(2);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            int pos = group.indexOfChild(radioButton);
            viewPager.setCurrentItem(pos);
        });

    }

    @OnClick({R.id.backstage_rb_goods,R.id.backstage_rb_store})
    public void OnViewClick(View view) {
        switch (view.getId()) {
            case R.id.backstage_rb_goods:
                break;
            case R.id.backstage_rb_store:
                break;
        }
    }

    @Override
    public boolean onBackPress(Activity activity) {
        return ((XBaseFragment) mFragment.get(viewPager.getCurrentItem())).onBackPress(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_backstage;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static BackstageFragment newInstance() {
        return new BackstageFragment();
    }


}
