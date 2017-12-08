package com.game.helper.fragments.BaseFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.game.helper.R;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.present.GameFragmentPresent;
import com.game.helper.views.SearchComponentView;
import com.game.helper.views.widget.StateView;

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
import cn.droidlover.xstatecontroller.XStateController;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by wanglei on 2016/12/31.
 */

public abstract class GameBasePagerFragment extends XBaseFragment<GameFragmentPresent> {

    @BindView(R.id.game_root_layout)
    XStateController xStateController;
    @BindView(R.id.game_viewpager)
    ViewPager viewPager;//content
    @BindView(R.id.common_search_view)
    SearchComponentView searchComponentView;
    @BindView(R.id.game_tabs)
    MagicIndicator tabStrip;
    StateView errorView;

    List<ItemType> list = new ArrayList<ItemType>();
    private View loadingView;

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        getP().onInitData();
    }

    private void initAdapter() {
        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setCustomClickListener(new StateView.StateViewClickListener() {
                @Override
                public void doAction() {
                    xStateController.loadingView(loadingView);
                    getP().onInitData();
                }
            });
        }

        if (errorView.getParent() != null) {
            ((ViewGroup) errorView.getParent()).removeView(errorView);
        }
        xStateController.errorView(errorView);

        if (loadingView == null) {
            loadingView = View.inflate(context, R.layout.view_loading, null);
        }
        if (null != loadingView.getParent()) {
            ((ViewGroup) loadingView.getParent()).removeView(loadingView);
        }
        xStateController.loadingView(loadingView);

        xStateController.showLoading();

        viewPager.setAdapter(getPageAdapter(list));
    }

    public abstract PagerAdapter getPageAdapter(List<ItemType> list);

    public void showError(NetError error) {
        xStateController.showError();
        xStateController.getLoadingView().setVisibility(View.GONE);
    }

    public void showData(List<ItemType> model) {
        xStateController.getLoadingView().setVisibility(View.GONE);
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
                            viewPager.getAdapter().notifyDataSetChanged();
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
