package com.game.helper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.activitys.GameDetailMyAccountActivity;
import com.game.helper.activitys.HuanxinKefuLoginActivity;
import com.game.helper.activitys.OrderConfirmActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.recharge.RechargeGameFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountDiscountResults;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.VipGameAccountResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.GXPlayDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Tian on 2017/12/23.
 */

public class GameDetailRechargeFragment extends XBaseFragment implements View.OnClickListener, CheckBox.OnCheckedChangeListener {
    public static final String TAG = GameDetailRechargeFragment.class.getSimpleName();

    public static GameDetailRechargeFragment newInstance() {
        GameDetailRechargeFragment fragment = new GameDetailRechargeFragment();
        return fragment;
    }

    private static final int check_able_color = R.color.colorBlack;
    private static final int check_disable_color = R.color.colorShadow;
    private String discount_high_vip;
    private String discount_vip;
    private String discount_member;
    private boolean is_vip;

    @BindView(R.id.ll_account_recharge_game_detail)
    View mItemAccount;
    @BindView(R.id.ll_discount_1_recharge_game_detail)
    View mItemDiscount1;
    @BindView(R.id.ll_discount_2_recharge_game_detail)
    View mItemDiscount2;
    @BindView(R.id.ll_discount_3_recharge_game_detail)
    View mItemDiscount3;

    @BindView(R.id.tv_account_recharge_game_detail)
    TextView mAccount;
    @BindView(R.id.et_balance_recharge_game_detail)
    EditText mBalance;
    @BindView(R.id.tv_discount_1_recharge_game_detail)
    TextView mDiscount1;
    @BindView(R.id.cb_discount_1_recharge_game_detail)
    CheckBox mCbDiscount1;
    @BindView(R.id.tv_discount_2_recharge_game_detail)
    TextView mDiscount2;
    @BindView(R.id.cb_discount_2_recharge_game_detail)
    CheckBox mCbDiscount2;
    @BindView(R.id.tv_discount_3_recharge_game_detail)
    TextView mDiscount3;
    @BindView(R.id.cb_discount_3_recharge_game_detail)
    CheckBox mCbDiscount3;
    @BindView(R.id.tv_discount_hint_3_recharge_game_detail)
    TextView mDiscountHint3;
    @BindView(R.id.tv_total_discount_recharge_game_detail)
    TextView mTotalDiscount;
    @BindView(R.id.tv_total_balance_recharge_game_detail)
    TextView mTotalBalance;

    private GameAccountResultModel.ListBean gameBean;//游戏bean
    private GameAccountDiscountResults discountList;//折扣bean
    private VipGameAccountResults accountBean;//账户bean
    //private VipLevelResults vipList;//vip列表
    private float mTotalDiscountValue = 0;
    private float mTotalBalanceValue = 0;

    public static final int REQUEST_CODE = 99;
    public static final int RESULT_CODE = 98;
    private GXPlayDialog dialog;

    public GameDetailRechargeFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initView();
    }

    private void initIntent() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge_game_detail;
    }

    private void initView() {
        setCheckStatus(-1, true);
        if (getArguments() != null) {
            setChooseGameData(true);
        }
        //getVipLevel();//获取最高vip

        discount_high_vip = getResources().getString(R.string.recharge_high_vip_discount);
        discount_vip = getResources().getString(R.string.recharge_vip_discount);
        discount_member = getResources().getString(R.string.recharge_member_discount);

        mItemAccount.setOnClickListener(this);
        mItemDiscount1.setOnClickListener(this);
        mItemDiscount2.setOnClickListener(this);
        mItemDiscount3.setOnClickListener(this);
        mCbDiscount1.setOnCheckedChangeListener(this);
        mCbDiscount2.setOnCheckedChangeListener(this);
        mCbDiscount3.setOnCheckedChangeListener(this);

        mBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputValue = s.toString().trim();
                if (StringUtils.isEmpty(inputValue) || (Integer.parseInt(inputValue)) <= 0) return;
                getCheckDiscount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDiscountHint3.setVisibility(View.GONE);
    }

    /**
     * 根据拿到的游戏bean设置ui
     *
     * @param isArguments 是否一开始就传过来的bean
     */
    private void setChooseGameData(boolean isArguments) {
        if (isArguments)
            gameBean = (GameAccountResultModel.ListBean) getArguments().getSerializable(RechargeGameFragment.TAG);
        if (gameBean == null) {
            Toast.makeText(getContext(), "获取数据失败！请重试", Toast.LENGTH_SHORT).show();
            return;
        }

        mAccount.setText(gameBean.getGame_account());
        if (gameBean.isIs_xc()) {
            setCheckStatus(-1, true);
            setCheckStatus(0, false);
            mItemDiscount1.performClick();
        }
        getGameAccountDiscount(gameBean.getId());
    }

    private void setVipHint(int count) {
        if (count != 0) {
            mDiscountHint3.setVisibility(View.VISIBLE);
            mDiscountHint3.setText(getResources().getString(R.string.recharge_can_use_discount) + count + "个");
        }
    }

    private void getVipGameAccount() {
        Flowable<HttpResultModel<VipGameAccountResults>> fr = DataService.getVipGameAccount();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<VipGameAccountResults>>() {
            @Override
            public void accept(HttpResultModel<VipGameAccountResults> vipGameAccountResultsHttpResultModel) throws Exception {
                accountBean = vipGameAccountResultsHttpResultModel.data;
                /*
                进来页面选完游戏账号 先判断游戏首充
                有首充默认首充其他不可点
                无首充 判断该游戏账号绑定vip
                    有绑定 默认vip折扣不弹窗
                    没有酒普通会员折扣
                这时候点击VIP折扣再执行判断弹窗的逻辑
                * */
                if (gameBean == null) return;
                if (gameBean.isIs_xc()) {
                    setCheckStatus(-1, true);
                    setCheckStatus(0, false);
                    mItemDiscount1.performClick();
                } else {
                    //打开普通会员选择
                    setCheckStatus(1, false);
                    //打开vip会员选择
                    setCheckStatus(2, false);
                    if (gameBean.isIs_vip()) {
                        //当前游戏肯定是vip 默认选中vip折扣 不需要判断vip数量
                        setCheckStatus(0, true);
                        setCheckStatus(1, true);
                        setChecked(2);
                    } else {
                        mItemDiscount2.performClick();
                    }
                }
                //打开vip剩余数量提示
                setVipHint(accountBean.count);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void getGameAccountDiscount(int gameAccountId) {
        Flowable<HttpResultModel<GameAccountDiscountResults>> fr = DataService.getGameAccountDiscount(new SingleGameIdRequestBody(gameAccountId));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountDiscountResults>>() {
            @Override
            public void accept(HttpResultModel<GameAccountDiscountResults> gameAccountDiscountResultsHttpResultModel) throws Exception {
                discountList = gameAccountDiscountResultsHttpResultModel.data;
                mDiscount1.setText(discount_high_vip + discountList.high_vip_discount + "折");
                mDiscount2.setText(discount_member + discountList.member_discount + "折");
                mDiscount3.setText(discount_vip + discountList.vip_discount + "折");

                getCheckDiscount();
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void getVipLevel() {
        Flowable<HttpResultModel<VipLevelResults>> fr = DataService.getVipLevel();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<VipLevelResults>>() {
            @Override
            public void accept(HttpResultModel<VipLevelResults> vipLevelResultsHttpResultModel) throws Exception {
                //vipList = vipLevelResultsHttpResultModel.data;
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void setCheckStatus(int position, boolean checkEnable) {
        switch (position) {
            case 0:
                mDiscount1.setTextColor(getResources().getColor(checkEnable ? check_disable_color : check_able_color));
                mCbDiscount1.setEnabled(checkEnable ? true : false);
                mCbDiscount1.setChecked(false);
                break;
            case 1:
                mDiscount2.setTextColor(getResources().getColor(checkEnable ? check_disable_color : check_able_color));
                mCbDiscount2.setEnabled(checkEnable ? true : false);
                mCbDiscount2.setChecked(false);
                break;
            case 2:
                mDiscount3.setTextColor(getResources().getColor(checkEnable ? check_disable_color : check_able_color));
                mCbDiscount3.setEnabled(checkEnable ? true : false);
                mCbDiscount3.setChecked(false);
                break;
            default:
                mDiscount1.setTextColor(getResources().getColor(checkEnable ? check_disable_color : check_able_color));
                mDiscount2.setTextColor(getResources().getColor(checkEnable ? check_disable_color : check_able_color));
                mDiscount3.setTextColor(getResources().getColor(checkEnable ? check_disable_color : check_able_color));
                mCbDiscount1.setEnabled(checkEnable ? true : false);
                mCbDiscount2.setEnabled(checkEnable ? true : false);
                mCbDiscount3.setEnabled(checkEnable ? true : false);
                mCbDiscount1.setChecked(false);
                mCbDiscount2.setChecked(false);
                mCbDiscount3.setChecked(false);
                break;
        }
    }

    private void clearCheck() {
        mCbDiscount1.setChecked(false);
        mCbDiscount2.setChecked(false);
        mCbDiscount3.setChecked(false);
    }

    private void setChecked(int position) {
        switch (position) {
            case 0:
                if (mCbDiscount1.isEnabled()) return;
                clearCheck();
                mCbDiscount1.setChecked(true);
                is_vip = false;
                break;
            case 1:
                if (mCbDiscount2.isEnabled()) return;
                clearCheck();
                mCbDiscount2.setChecked(true);
                is_vip = false;
                break;
            case 2:
                if (mCbDiscount3.isEnabled()) return;
                clearCheck();
                mCbDiscount3.setChecked(true);
                is_vip = true;
                break;
        }
    }

    private void getCheckDiscount() {
        if (discountList == null) {
            caculateBalanceVlue();
            return;
        }
        if (mCbDiscount1.isChecked()) {
            mTotalDiscountValue = discountList.high_vip_discount;
        } else if (mCbDiscount2.isChecked()) {
            mTotalDiscountValue = discountList.member_discount;
        } else if (mCbDiscount3.isChecked()) {
            mTotalDiscountValue = discountList.vip_discount;
        }
        mTotalDiscount.setText(mTotalDiscountValue + "折");
        caculateBalanceVlue();
    }

    private void caculateBalanceVlue() {
        int inputVlaue = 0;
        try {
            inputVlaue = Integer.parseInt(mBalance.getText().toString());
        } catch (NumberFormatException e) {
        }

        if (mTotalDiscountValue < 0 || inputVlaue <= 0) return;
        if (mTotalDiscountValue == 0) mTotalDiscountValue = 10;
        else mTotalBalanceValue = (float) (inputVlaue * mTotalDiscountValue / 10.0);

        mTotalBalanceValue = Utils.m2(mTotalBalanceValue);
        mTotalBalance.setText(mTotalBalanceValue + "元");
    }

    @Override
    public void onClick(View v) {
        if (v == mItemAccount) {
            startActivityForResult(new Intent(getActivity(), GameDetailMyAccountActivity.class), REQUEST_CODE);
        }
        if (v == mItemDiscount1) {
            if (mCbDiscount1.isEnabled()) return;
            setChecked(0);
        }
        if (v == mItemDiscount2) {
            if (mCbDiscount2.isEnabled()) return;
            setCheckStatus(0, true);
            setChecked(1);
        }
        if (v == mItemDiscount3) {
            if (accountBean == null || mCbDiscount1.isChecked() || mCbDiscount3.isEnabled() || mCbDiscount3.isChecked())
                return;
            if (accountBean.count == 0) {
                if (accountBean.is_highest_vip) {//是最高等级
                    showVipHintDialog(2);
                } else {
                    showVipHintDialog(1);
                }
            } else {
                showVipHintDialog(0);
            }

            setCheckStatus(0, true);
            setChecked(2);
        }
    }

    /**
     * type
     * 0：仅提示消耗vip数量
     * 1：升级vip
     * 2：vip最高联系管理员
     */
    private void showVipHintDialog(final int type) {
        if (mCbDiscount3.isEnabled()) {
            return;
        }
        String content = "";
        if (type == 0) content = "您当前选择VIP折扣，将会占用1个VIP名额，您确定使用此折扣支付吗？";
        if (type == 1) content = "您的VIP账户名额不足，升级会员等级可增加VIP账户名额，是否去升级VIP？";
        if (type == 2) content = "您的VIP账户名额已用完，并且是皇冠会员，已无法再升级会员，若您仍想绑定该账号为VIP账号，请联系客服！";
        dialog = null;
        if (type == 2 || type == 0) {
            dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Single_Confirm, "温馨提示", content);
        }
        if (type == 1) {
            dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Full_Confirm, "温馨提示", content);
        }
        dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
            @Override
            public void onCancel() {
                dialog.dismiss();
                mItemDiscount2.performClick();
            }

            @Override
            public void onConfirm() {
                dialog.dismiss();
                if (type == 2) goToKefu();
                if (type == 1) goToVipLevel();
            }
        });
        dialog.show(getChildFragmentManager(), GXPlayDialog.TAG);
    }

    // TODO: 2017/12/18 补全跳转
    private void goToVipLevel() {
        //跳转vip升级页面
        mItemDiscount2.performClick();
        Bundle bundle = new Bundle();
        bundle.putString(WebviewFragment.PARAM_TITLE, "VIP");
        bundle.putString(WebviewFragment.PARAM_URL, SharedPreUtil.getH5url(SharedPreUtil.H5_URL_VIP));
        DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
    }

    private void goToKefu() {
        //跳转客服
        mItemDiscount2.performClick();
        Toast.makeText(getContext(), "最高等级vip，跳转客服", Toast.LENGTH_SHORT).show();
    }

    public GameAccountResultModel.ListBean getGameBean() {
        return gameBean;
    }

    public double getTotalBalanceValue() {
        return mTotalBalanceValue;
    }

    public boolean getIs_VIP() {
        return is_vip;
    }

    public double getInputValue() {
        String inputValue = mBalance.getText().toString().trim();
        if (TextUtils.isEmpty(inputValue)) {
            inputValue = "0.0";
        }
        return Double.parseDouble(inputValue);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        getCheckDiscount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE && data != null) {
            if (!data.hasExtra(TAG)) return;
            if (data.getSerializableExtra(TAG) instanceof GameAccountResultModel.ListBean) {
                gameBean = (GameAccountResultModel.ListBean) data.getSerializableExtra(TAG);
                clearCheck();
                getVipGameAccount();//获取当前平台账户vip信息
                setChooseGameData(false);
                mBalance.setText("");
            }
        }
    }

    public void resetFragment() {
        gameBean = null;
        discountList = null;
        accountBean = null;
        mTotalDiscountValue = 0;
        mTotalBalanceValue = 0;
        initView();
        mAccount.setText("");
        mBalance.setText("");
        mTotalDiscount.setText("0.0折");
        mDiscount1.setText(discount_high_vip);
        mDiscount2.setText(discount_member);
        mDiscount3.setText(discount_vip);
        caculateBalanceVlue();
    }


    @Override
    public Object newP() {
        return null;
    }

    @OnClick({R.id.ll_connect_kefu_recharge_game_detail, R.id.tv_confirm_order_recharge_game_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_connect_kefu_recharge_game_detail:
                Intent intentKefu = new Intent(context, HuanxinKefuLoginActivity.class);
                startActivity(intentKefu);
                break;
            case R.id.tv_confirm_order_recharge_game_detail:
                confirmOrder();
                break;
        }
    }


    private void confirmOrder() {
        GameAccountResultModel.ListBean gameBean = getGameBean();
        double totalBalanceValue = getTotalBalanceValue();
        double inputValue = getInputValue();
        if (gameBean == null || totalBalanceValue <= 0 || inputValue <= 0) {
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
            bundle.putDouble(OrderConfirmActivity.BUNDLE_INPUT_VALUE, inputValue);
            bundle.putDouble(OrderConfirmActivity.BUNDLE_TOTAL_BALANCE, totalBalanceValue);
            bundle.putString(OrderConfirmActivity.PAYPURPOSE, "1");
            bundle.putString(OrderConfirmActivity.VIPLEVEL, "0");
            intent.putExtra(OrderConfirmActivity.TAG, bundle);
            startActivity(intent);
        }
    }


}

