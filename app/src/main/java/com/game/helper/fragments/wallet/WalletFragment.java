package com.game.helper.fragments.wallet;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.adapters.RechargeCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.recharge.RechargeFragment;
import com.game.helper.fragments.recharge.RechargeGameFragment;
import com.game.helper.fragments.recharge.RechargeGoldFragment;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.utils.StringUtils;

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
public class WalletFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = WalletFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @BindView(R.id.game_detail_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_detail_viewpager)
    ViewPager viewPager;

    @BindView(R.id.tv_value)
    TextView mBalance;
    @BindView(R.id.tv_balance1)
    TextView mBalance1;
    @BindView(R.id.tv_balance2)
    TextView mBalance2;
    @BindView(R.id.tv_balance3)
    TextView mBalance3;
    @BindView(R.id.tv_goto_recharge)
    View mGotoRecharge;
    @BindView(R.id.tv_goto_cash)
    View mGotoCash;

    private List<Fragment> list = new ArrayList<Fragment>();
    private MemberInfoResults userInfo;

    public static WalletFragment newInstance(){
        return new WalletFragment();
    }

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_wallet));
        mHeadBack.setOnClickListener(this);
        mGotoRecharge.setOnClickListener(this);
        mGotoCash.setOnClickListener(this);

        if (getArguments() == null) {
            Toast.makeText(getContext(), "数据拉取异常！请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        userInfo = (MemberInfoResults) getArguments().getSerializable(TAG);
        mBalance.setText(StringUtils.isEmpty(userInfo.total_balance) ? "0.00" : userInfo.total_balance);
        mBalance1.setText(StringUtils.isEmpty(userInfo.total_balance) ? "0.00" : userInfo.total_balance);
        mBalance2.setText(StringUtils.isEmpty(userInfo.market_balance) ? "0.00" : userInfo.market_balance);
        mBalance3.setText(StringUtils.isEmpty(userInfo.balance) ? "0.00" : userInfo.balance);

        list.add(WalletListFragment.newInstance(RechargeCommonAdapter.Type_Account_Consume));
        list.add(WalletListFragment.newInstance(RechargeCommonAdapter.Type_Account_Recharge));
        list.add(WalletListFragment.newInstance(RechargeCommonAdapter.Type_Account_Cash));
        list.add(WalletListFragment.newInstance(RechargeCommonAdapter.Type_Account_Profit));
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
                        title = "消费明细";
                        break;
                    case 1:
                        title = "充值明细";
                        break;
                    case 2:
                        title = "提现明细";
                        break;
                    case 3:
                        title = "收益明细";
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
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mGotoRecharge){
            DetailFragmentsActivity.launch(getContext(),null, RechargeFragment.newInstance());
        }
        if (v == mGotoCash){
            Bundle bundle = new Bundle();
            bundle.putSerializable(CashFragment.TAG,userInfo);
            DetailFragmentsActivity.launch(getContext(),bundle, CashFragment.newInstance());
        }
    }

    @Override
    public Object newP() {
        return null;
    }

}
