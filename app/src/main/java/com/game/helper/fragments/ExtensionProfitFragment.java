package com.game.helper.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.adapters.RechargeCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.wallet.WalletListFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.MarketInfoResults;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.FeedbackRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.MemberDescDialog;

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
 * 推广收益
 */
public class ExtensionProfitFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = ExtensionProfitFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @BindView(R.id.game_detail_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_detail_viewpager)
    ViewPager viewPager;

    @BindView(R.id.tv_value)
    TextView mTotalValue;
    @BindView(R.id.tv_balance_left)
    TextView mLeftValue;
    @BindView(R.id.tv_balance_right)
    TextView mRightValue;
    @BindView(R.id.iv_help)
    ImageView mHelp;

    private List<Fragment> list = new ArrayList<Fragment>();
    private MarketInfoResults marketInfo;

    public static ExtensionProfitFragment newInstance(){
        return new ExtensionProfitFragment();
    }

    public ExtensionProfitFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_extension_profit;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_extension_profit));
        mHeadBack.setOnClickListener(this);
        mHelp.setOnClickListener(this);

        getMarketInfo();
        list.add(ExtensionProfitItemFragment.newInstance(ExtensionProfitItemFragment.Type_Extension_Gold));
        list.add(ExtensionProfitItemFragment.newInstance(ExtensionProfitItemFragment.Type_Plan_Gold));
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
                        title = "推广收益";
                        break;
                    case 1:
                        title = "预期收益";
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
                indicator.setColors(getResources().getColor(R.color.colorPrimary));
                return indicator;
            }
        });
        tabStrip.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabStrip, viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    private void setUserData(){
        if (marketInfo == null) return;
        mTotalValue.setText(marketInfo.zongshouyi);
        mLeftValue.setText(marketInfo.yue);
        mRightValue.setText(marketInfo.yujizongshouyi);
    }

    private void getMarketInfo(){
        Flowable<HttpResultModel<MarketInfoResults>> fr = DataService.getMarketInfo();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MarketInfoResults>>() {
            @Override
            public void accept(HttpResultModel<MarketInfoResults> marketInfoResultsHttpResultModel) throws Exception {
                if (marketInfoResultsHttpResultModel.isSucceful()) {
                    marketInfo = marketInfoResultsHttpResultModel.data;
                    setUserData();
                } else {
                    Toast.makeText(getContext(), marketInfoResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mHelp){
            MemberDescDialog dialog = new MemberDescDialog();
            dialog.show(getChildFragmentManager(),MemberDescDialog.TAG);
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
