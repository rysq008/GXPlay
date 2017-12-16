package com.game.helper.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.adapters.RechargeCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.wallet.WalletListFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * 推广收益
 */
public class ExtensionProfitItemFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = ExtensionProfitItemFragment.class.getSimpleName();
    private int type;

    @SuppressLint("ValidFragment")
    public ExtensionProfitItemFragment(int type) {
        this.type = type;
    }

    public static ExtensionProfitItemFragment newInstance(int type){
        return new ExtensionProfitItemFragment(type);
    }

    public ExtensionProfitItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_extension_profit_item;
    }

    private void initView(){
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public Object newP() {
        return null;
    }
}
