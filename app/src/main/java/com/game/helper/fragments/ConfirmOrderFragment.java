//package com.game.helper.fragments;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.game.helper.R;
//import com.game.helper.fragments.BaseFragment.XBaseFragment;
//import com.game.helper.fragments.coupon.CouponListFragment;
//import com.game.helper.model.BaseModel.HttpResultModel;
//import com.game.helper.model.CheckTradePasswdResults;
//import com.game.helper.model.GameAccountResultModel;
//import com.game.helper.net.DataService;
//import com.game.helper.net.model.CheckTradePasswdRequestBody;
//import com.game.helper.utils.RxLoadingUtils;
//import com.game.helper.views.PasswordEditDialog;
//
//import net.lucode.hackware.magicindicator.MagicIndicator;
//import net.lucode.hackware.magicindicator.ViewPagerHelper;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import cn.droidlover.xdroidmvp.net.NetError;
//import io.reactivex.Flowable;
//import io.reactivex.functions.Consumer;
//
///**
// * A simple {@link Fragment} subclass.
// * 确认订单
// */
//public class ConfirmOrderFragment extends XBaseFragment implements View.OnClickListener{
//    public static final String TAG = ConfirmOrderFragment.class.getSimpleName();
//    public static final String BUNDLE_GAME_BEAN = "game_bean";
//    public static final String BUNDLE_TOTAL_BALANCE = "after_diacount_total_balance";
//
//    @BindView(R.id.action_bar_back)
//    View mHeadBack;
//    @BindView(R.id.action_bar_tittle)
//    TextView mHeadTittle;
//
//    @BindView(R.id.tv_confirm_order)
//    View mConfirmOrder;
//    @BindView(R.id.tv_cancel_order)
//    View mCancelOrder;
//
//    private GameAccountResultModel.ListBean gameBean;
//    private Double totalBalance;
//
//    public static ConfirmOrderFragment newInstance(){
//        return new ConfirmOrderFragment();
//    }
//
//    public ConfirmOrderFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void initData(Bundle savedInstanceState) {
//        initView();
//    }
//
//    /**
//     * 获取上级页面传的game bean以及目标支付金额
//     * */
//    private void getData(){
//        if (getArguments()==null) return;
//        Bundle arguments = getArguments();
//        gameBean = (GameAccountResultModel.ListBean) arguments.getSerializable(BUNDLE_GAME_BEAN);
//        totalBalance = arguments.getDouble(BUNDLE_TOTAL_BALANCE);
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_confirm_order;
//    }
//
//    private void initView(){
//        mHeadTittle.setText(getResources().getString(R.string.common_confirm_order));
//        mHeadBack.setOnClickListener(this);
//        mConfirmOrder.setOnClickListener(this);
//        mCancelOrder.setOnClickListener(this);
//
//        getData();
//    }
//
//    /**********************      验证交易密码流程 start      **************************/
//    private void showTradePassWord(){
//        if (gameBean == null || totalBalance <= 0){
//            Toast.makeText(getContext(), "数据异常！请重试", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        PasswordEditDialog dialog = new PasswordEditDialog();
//        dialog.addOnPassWordEditListener(new PasswordEditDialog.OnPassWordEditListener() {
//            @Override
//            public void onConfirmComplete(String password) {
//                ProvingTradePssword(password);
//            }
//        });
//        dialog.show(getChildFragmentManager(),PasswordEditDialog.TAG);
//    }
//
//    /**
//     * 验证交易密码
//     * */
//    private void ProvingTradePssword(String password){
//        Flowable<HttpResultModel<CheckTradePasswdResults>> fr = DataService.checkTradePassword(new CheckTradePasswdRequestBody(password));
//        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CheckTradePasswdResults>>() {
//            @Override
//            public void accept(HttpResultModel<CheckTradePasswdResults> checkTradePasswdResultsHttpResultModel) throws Exception {
//                if (checkTradePasswdResultsHttpResultModel.isSucceful()) confirmOrder();
//                else Toast.makeText(getContext(), "交易密码验证失败！", Toast.LENGTH_SHORT).show();
//            }
//        }, new Consumer<NetError>() {
//            @Override
//            public void accept(NetError netError) throws Exception {
//                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
//            }
//        });
//    }
//
//    /**
//     * 确认订单调支付
//     * */
//    private void confirmOrder(){
//    }
//    /**********************      验证交易密码流程 end      **************************/
//
//    @Override
//    public void onClick(View v) {
//        if (v == mHeadBack){
//            getActivity().onBackPressed();
//        }
//        if (v == mConfirmOrder){
//            showTradePassWord();
//        }
//        if (v == mCancelOrder){
//            getActivity().onBackPressed();
//        }
//    }
//
//    @Override
//    public Object newP() {
//        return null;
//    }
//}
