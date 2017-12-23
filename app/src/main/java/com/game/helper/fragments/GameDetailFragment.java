package com.game.helper.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.GamePackageInfoRequestBody;
import com.game.helper.utils.RxLoadingUtils;

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
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class GameDetailFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = GameDetailFragment.class.getSimpleName();
    List<Fragment> list = new ArrayList<Fragment>();
    @BindView(R.id.action_bar_tittle)
    TextView mTvTittle;
    @BindView(R.id.iv_game_detail_logothumb)
    ImageView ivLogothumb;
    @BindView(R.id.tv_game_detail_name)
    TextView tvName;
    @BindView(R.id.tv_game_detail_discount_vip)
    TextView tvDiscount;
    @BindView(R.id.tv_game_detail_type_name)
    TextView tvTypeName;
    @BindView(R.id.tv_game_detail_package_filesize)
    TextView tvPackageFilesize;
    @BindView(R.id.tv_game_detail_content)
    TextView tvContent;
    @BindView(R.id.iv_game_detail_load)
    ImageView ivLoad;
    @BindView(R.id.tv_discount_game_detail_common)
    TextView tvDiscountCommon;
    @BindView(R.id.tv_discount_game_detail_vip)
    TextView tvDiscountVip;
    @BindView(R.id.game_detail_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_detail_viewpager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.tv_game_detail_bottom_download)
    TextView tvBottomDownload;
    @BindView(R.id.et_game_detail_edit_content)
    EditText etEditContent;
    @BindView(R.id.tv_game_detail_edit_send)
    TextView tvEditSend;
    @BindView(R.id.ll_game_detail_action_edit)
    LinearLayout etitLayout;

    private int gamepackeId;
    private int gameId;
    private GameDetailInfoFragment gameDetailInfoFragment;

    public static GameDetailFragment newInstance() {
        return new GameDetailFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTvTittle.setText("游戏详情");
        Bundle arguments = getArguments();
        tvDiscountCommon.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvDiscountVip.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        if (arguments != null) {
            gamepackeId = arguments.getInt("gamepackeId");
            gameId = arguments.getInt("gameId");
            initGamePackage();
        }
        if(gameDetailInfoFragment == null){
            gameDetailInfoFragment = GameDetailInfoFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("gameId",gameId);
            gameDetailInfoFragment.setArguments(bundle);
        }
        list.add(GameDetailRechargeFragment.newInstance());
        list.add(gameDetailInfoFragment);
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
                        tvBottomDownload.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        tvBottomDownload.setVisibility(View.VISIBLE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        tvBottomDownload.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 3:
                        tvBottomDownload.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        loadData();
    }

    private void initGamePackage() {
        Flowable<HttpResultModel<GamePackageInfoResult>> fr = DataService.getGamePackageInfo(new GamePackageInfoRequestBody(gamepackeId));
        RxLoadingUtils.subscribe(fr, this.bindToLifecycle(), new Consumer<HttpResultModel<GamePackageInfoResult>>() {
            @Override
            public void accept(HttpResultModel<GamePackageInfoResult> gameInfo) throws Exception {
                ILFactory.getLoader().loadNet(ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(gameInfo.data.getGame().getLogo()), ILoader.Options.defaultOptions());
                tvName.setText(gameInfo.data.getGame().getName());
                tvDiscount.setText(String.valueOf(gameInfo.data.getDiscount_vip()));
                tvTypeName.setText(gameInfo.data.getGame().getType().getName());
                tvPackageFilesize.setText(String.valueOf(gameInfo.data.getFilesize() + "M"));
                tvContent.setText(gameInfo.data.getGame().getIntro());

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                //showError(netError);
            }
        });

    }

    public void loadData() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdjustMode(true);
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
                        title = "充值";
                        break;
                    case 1:
                        title = "详情";
                        break;
                    case 2:
                        title = "礼包";
                        break;
                    case 3:
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

    @OnClick(R.id.action_bar_back)
    public void onClick() {
        getActivity().finish();
    }

    @OnClick(R.id.iv_action)
    public void onShareClick() {
        //分享
    }


    @OnClick({R.id.tv_game_detail_bottom_download, R.id.tv_game_detail_edit_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_game_detail_bottom_download:
                break;
            case R.id.tv_game_detail_edit_send:
                break;
        }
    }
}
