package com.game.helper.fragments.recharge;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.OrderConfirmActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.utils.NumberUtil;

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
 */
public class RechargeFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = RechargeFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @BindView(R.id.game_detail_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tv_connect_kefu)
    View mConnectKefu;
    @BindView(R.id.tv_confirm_order)
    View mConfirmOrder;

    private int current_page = 0;
    private List<Fragment> list = new ArrayList<Fragment>();
    private RechargeGameFragment rechargeGameFragment;
    private RechargeGoldFragment rechargeGoldFragment;

    public static RechargeFragment newInstance(){
        return new RechargeFragment();
    }

    public RechargeFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_recharge));
        mHeadBack.setOnClickListener(this);
        mConfirmOrder.setOnClickListener(this);
        mConnectKefu.setOnClickListener(this);

        rechargeGameFragment = RechargeGameFragment.newInstance();
        list.add(rechargeGameFragment);
        rechargeGoldFragment = RechargeGoldFragment.newInstance();
        list.add(rechargeGoldFragment);
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
                current_page = position;
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
                        title = "游戏";
                        break;
                    case 1:
                        title = "金币";
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

    private void confirmOrder(){
        if (current_page == 0) {
            GameAccountResultModel.ListBean gameBean = rechargeGameFragment.getGameBean();
            double totalBalanceValue = rechargeGameFragment.getTotalBalanceValue();
            double inputValue = rechargeGameFragment.getInputValue();
            if (gameBean == null
                    || NumberUtil.compare(String.valueOf(totalBalanceValue),NumberUtil.Zero) <= 0
                    || NumberUtil.compare(String.valueOf(totalBalanceValue),NumberUtil.Zero) <= 0) {
                Toast.makeText(getContext(), "数据异常！请重试", Toast.LENGTH_SHORT).show();
            } else {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ConfirmOrderFragment.BUNDLE_GAME_BEAN, gameBean);
//                bundle.putDouble(ConfirmOrderFragment.BUNDLE_TOTAL_BALANCE, totalBalanceValue);
//                ConfirmOrderFragment confirmOrderFragment = ConfirmOrderFragment.newInstance();
//                confirmOrderFragment.setArguments(bundle);
//                DetailFragmentsActivity.launch(getContext(), bundle, confirmOrderFragment);

                //订单确认页面用OrderConfirmActivity
                Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(OrderConfirmActivity.BUNDLE_GAME_BEAN, gameBean);
                bundle.putDouble(OrderConfirmActivity.BUNDLE_INPUT_VALUE,inputValue);
                bundle.putDouble(OrderConfirmActivity.BUNDLE_TOTAL_BALANCE, totalBalanceValue);
                intent.putExtra(OrderConfirmActivity.TAG,bundle);
                startActivity(intent);
            }
        }
        if (current_page == 1) {

        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mConfirmOrder){
            confirmOrder();
        }
        if (v == mConnectKefu){

        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
