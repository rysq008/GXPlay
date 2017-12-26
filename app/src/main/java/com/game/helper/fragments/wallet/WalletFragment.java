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
import com.game.helper.model.AllAccountsResultsModel;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;
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
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

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
    @BindView(R.id.tv_balance4)
    TextView mBalance4;
    @BindView(R.id.tv_balance5)
    TextView mBalance5;
    @BindView(R.id.tv_balance6)
    TextView mBalance6;
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
        fetchAccountInfo();

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

    /**
     * 获取账户信息
     */
    private void fetchAccountInfo() {
        Flowable<HttpResultModel<AllAccountsResultsModel>> flowable = DataService.getAllAccounts();
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<AllAccountsResultsModel>>() {
            @Override
            public void accept(HttpResultModel<AllAccountsResultsModel> data) throws Exception {
                if (data.isSucceful()) {
                    AllAccountsResultsModel user = data.data;
                    mBalance.setText(StringUtils.isEmpty(user.total) ? "0.00" : user.total);//账户总额
                    mBalance1.setText(StringUtils.isEmpty(user.balance) ? "0.00" : user.balance);//账户金币额
                    mBalance2.setText(StringUtils.isEmpty(user.yue) ? "0.00" : user.yue);//推广账户余额
                    mBalance3.setText(StringUtils.isEmpty(user.total_consume) ? "0.00" : user.total_consume);//总消费
                    mBalance4.setText(StringUtils.isEmpty(user.total_recharge) ? "0.00" : user.total_recharge);//总充值
                    mBalance5.setText(StringUtils.isEmpty(user.total_reflect) ? "0.00" : user.total_reflect);//总提现
                    mBalance6.setText(StringUtils.isEmpty(user.total_flow) ? "0.00" : user.total_flow);//总收益
                } else {
                    Toast.makeText(getContext(), "获取账户信息失败！请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), "获取账户信息失败！请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
