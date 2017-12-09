package com.game.helper.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

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

public class GameDetailFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = GameDetailFragment.class.getSimpleName();

    @BindView(R.id.game_detail_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.game_detail_bottom_bar)
    RelativeLayout bottomBar;
    @BindView(R.id.game_detail_download_tv)
    TextView downloadTv;
    @BindView(R.id.game_detail_action_edit)
    LinearLayout etitLayout;

    List<Fragment> list = new ArrayList<Fragment>();

    public static GameDetailFragment newInstance() {
        return new GameDetailFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        list.add(GameDetailInfoFragment.newInstance());
        list.add(GameDetailGiftFragment.newInstance());
        list.add(GameDetailCommunityFragment.newInstance());
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        downloadTv.setVisibility(View.VISIBLE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        downloadTv.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        downloadTv.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        loadData();
    }

    public void loadData() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                String title = null;
                switch (index) {
                    case 0:
                        title = "详情";
                        break;
                    case 1:
                        title = "礼包";
                        break;
                    case 2:
                        title = "社区";
                        break;
                }
                colorTransitionPagerTitleView.setText(title);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        tabStrip.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabStrip, viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_detail_layout;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
