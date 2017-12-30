package com.game.helper.fragments.BaseFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.game.helper.R;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.present.GameFragmentPresent;
import com.game.helper.views.SearchComponentView;
import com.game.helper.views.XReloadableStateContorller;

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
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by wanglei on 2016/12/31.
 */

public abstract class GameBasePagerFragment extends XBaseFragment<GameFragmentPresent> {

    @BindView(R.id.game_root_layout)
    XReloadableStateContorller xStateController;
    @BindView(R.id.game_viewpager)
    ViewPager viewPager;//content
    @BindView(R.id.common_search_view)
    SearchComponentView searchComponentView;
    @BindView(R.id.game_tabs)
    MagicIndicator tabStrip;

    List<ItemType> list = new ArrayList<ItemType>();

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        getP().onInitData(xStateController, true);
    }

    private void initAdapter() {
        viewPager.setAdapter(getPageAdapter(list));
        viewPager.setOffscreenPageLimit(2);
    }

    public abstract PagerAdapter getPageAdapter(List<ItemType> list);

    public void showError(NetError error) {
    }

    public void showData(List<ItemType> model) {
        if (Kits.Empty.check(model)) {
            xStateController.showEmpty();
        } else {
            xStateController.showContent();
            list.addAll(model);
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
                    ItemType itemType = list.get(index);
                    if (itemType instanceof ClassicalResults.ClassicalItem) {
                        colorTransitionPagerTitleView.setText(((ClassicalResults.ClassicalItem) itemType).name);
                    } else {
                        colorTransitionPagerTitleView.setText(((CommonResults.CommonItem) itemType).name);
                    }
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_layout;
    }

    @Override
    public GameFragmentPresent newP() {
        return new GameFragmentPresent();
    }

}
