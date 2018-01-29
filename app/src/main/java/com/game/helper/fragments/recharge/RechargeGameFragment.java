package com.game.helper.fragments.recharge;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.activitys.MyAccountActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.WebviewFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountDiscountResults;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.VipGameAccountResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.ToastUtil;
import com.game.helper.views.GXPlayDialog;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechargeGameFragment extends XBaseFragment {
    public static final String TAG = RechargeGameFragment.class.getSimpleName();

    private static final int check_able_color = R.color.colorBlack;
    private static final int check_disable_color = R.color.colorShadow;
    @BindView(R.id.tv_account_game_recharge)
    TextView mAccount;
    @BindView(R.id.ll_account_game_recharge)
    LinearLayout mItemAccount;
    @BindView(R.id.tv_name_game_recharge)
    TextView mGameName;
    @BindView(R.id.ll_name_game_recharge)
    LinearLayout llName;
    @BindView(R.id.tv_platfrom_game_recharge)
    TextView mPlatfrom;
    @BindView(R.id.ll_platfrom_game_recharge)
    LinearLayout llPlatfrom;
    @BindView(R.id.et_balance_game_recharge)
    EditText mEtBalance;
    @BindView(R.id.ll_balance_game_recharge)
    LinearLayout llBalance;
    @BindView(R.id.tv_memmber_vip_level_game_recharge)
    TextView tvUserVipLevel;
    @BindView(R.id.tv_game_account_type_game_recharge)
    TextView tvGameAccountType;
    @BindView(R.id.tv_upgrade_vip_alert_game_recharge)
    TextView mTvUpgradeVipAlert;
    @BindView(R.id.rb_vip0_game_recharge)
    RadioButton rbVip0;
    @BindView(R.id.rb_vip1_game_recharge)
    RadioButton rbVip1;
    @BindView(R.id.rb_vip2_game_recharge)
    RadioButton rbVip2;
    @BindView(R.id.rb_vip3_game_recharge)
    RadioButton rbVip3;
    @BindView(R.id.rg_vip_game_recharge)
    RadioGroup rgVip;
    @BindView(R.id.tv_total_discount_game_recharge)
    TextView mTotalDiscount;
    @BindView(R.id.tv_total_balance)
    TextView mTotalBalance;
    @BindView(R.id.pb_game_recharge)
    ProgressBar pbVip;
    @BindView(R.id.tv_upgrade_vip_name_game_recharge)
    TextView mTvUpgradeVipName;
    @BindView(R.id.tv_upgrade_vip_discount_game_recharge)
    TextView mTvUpgradeVipDiscount;
    @BindView(R.id.ll_upgrade_vip_game_recharge)
    LinearLayout mLlUpgradeVip;
    private String discount_activity;
    private String discount_high_vip;
    private String discount_vip;
    private String discount_member;

    private GameAccountResultModel.ListBean gameBean;//游戏bean
    private GameAccountDiscountResults discountList;//折扣bean
    private VipGameAccountResults BindVipAccountNumberBean;//账户bean
    //private VipLevelResults vipList;//vip列表
    private float mTotalDiscountValue = 0;
    private float mTotalBalanceValue = 0;
    private GXPlayDialog dialog;
    private int currentUserVipLevel = -1;

    private boolean is_vip;

    public static final int REQUEST_CODE = 99;
    public static final int RESULT_CODE = 98;
    private int[] checkedIcons = new int[]{R.mipmap.vip0_checked, R.mipmap.vip1_checked, R.mipmap.vip2_checked, R.mipmap.vip3_checked};
    private int[] normalIcons = new int[]{R.mipmap.vip0_normal, R.mipmap.vip1_normal, R.mipmap.vip2_normal, R.mipmap.vip3_normal};
    private boolean isGotoVip = false;
    private boolean gameVipAccountNotEnough = false;
    private PopupWindow popupWindow;


    public static RechargeGameFragment newInstance() {
        return new RechargeGameFragment();
    }

    public RechargeGameFragment() {
        // Required empty public constructor
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge_game;
    }

    private void initView() {
        is_vip = false;
        isGotoVip = false;
        gameVipAccountNotEnough = false;
        //获取vip充值游戏账号数量
        getVipGameAccount();
        initVipLevel();
        rgVip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_vip0_game_recharge:
                        //mTotalBalance.setText(rbVip0.getText().toString().trim());
                        break;
                    case R.id.rb_vip1_game_recharge:
                        //mTotalBalance.setText(rbVip1.getText().toString().trim());
                        break;
                    case R.id.rb_vip2_game_recharge:
                        //mTotalBalance.setText(rbVip2.getText().toString().trim());
                        break;
                    case R.id.rb_vip3_game_recharge:
                        //mTotalBalance.setText(rbVip3.getText().toString().trim());
                        break;
                }
            }
        });
        rbVip0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Drawable image = null;
                int vipLevel = 0;
                if (isChecked) {
                    image = getResources().getDrawable(checkedIcons[vipLevel]);
                    pbVip.setProgress(vipLevel);
                } else {
                    image = getResources().getDrawable(normalIcons[vipLevel]);
                }
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                rbVip0.setCompoundDrawables(null, image, null, null);
            }
        });
        rbVip1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Drawable image = null;
                int vipLevel = 1;
                if (isChecked) {
                    image = getResources().getDrawable(checkedIcons[vipLevel]);
                    pbVip.setProgress(vipLevel);
                } else {
                    image = getResources().getDrawable(normalIcons[vipLevel]);
                }
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                rbVip1.setCompoundDrawables(null, image, null, null);
            }
        });
        rbVip2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Drawable image = null;
                int vipLevel = 2;
                if (isChecked) {
                    image = getResources().getDrawable(checkedIcons[vipLevel]);
                    pbVip.setProgress(vipLevel);
                } else {
                    image = getResources().getDrawable(normalIcons[vipLevel]);
                }
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                rbVip2.setCompoundDrawables(null, image, null, null);
            }
        });
        rbVip3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Drawable image = null;
                int vipLevel = 3;
                if (isChecked) {
                    image = getResources().getDrawable(checkedIcons[vipLevel]);
                    pbVip.setProgress(3);
                    /*if (gameBean.isIs_xc()) {
                        if (Float.compare(discountList.discount_activity, 0.0f) == 0) {
                            //体验皇冠价一次
                            initFirstRecharge(3);
                        }
                    }*/
                } else {
                    image = getResources().getDrawable(normalIcons[vipLevel]);
                }
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                rbVip3.setCompoundDrawables(null, image, null, null);
            }
        });

        mEtBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputValue = s.toString().trim();
                if (gameBean != null && discountList != null) {
                    if (gameBean.isIs_xc()) {
                        //首充
                        if (Float.compare(discountList.discount_activity, 0.0f) == 0) {
                            //体验皇冠价一次
                            showFirstRechargeTotalDiscountAndBalance(3);
                        } else {
                            //体验活动价一次
                            showFirstRechargeTotalDiscountAndBalance(4);
                        }
                    } else {
                        if (gameBean.isIs_vip()) {
                            //是VIP
                            showTotalDiscountAndBalance(currentUserVipLevel);
                        } else {
                            //普通账号
                            showTotalDiscountAndBalance(gameBean.getVip_level());
                        }

                    }
                    rbVip0.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.member_discount).toString() + "元");
                    rbVip1.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.vip1_discount).toString() + "元");
                    rbVip2.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.high_vip_discount).toString() + "元");
                    rbVip3.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.high_vip_discount).toString() + "元");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showPopWindow(View shoView) {
        View contentView = layoutInflater.inflate(R.layout.popwindow_go_to_vip, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredWidth = contentView.getMeasuredWidth();
        int measuredHeight = contentView.getMeasuredHeight();
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        if(popupWindow == null){
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        //popupWindow.setFocusable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        int[] location = new int[2];
        shoView.getLocationOnScreen(location);
        popupWindow.showAtLocation(shoView, Gravity.NO_GRAVITY, (location[0] + shoView.getWidth() / 2) - measuredWidth / 2, location[1] - measuredHeight - 30);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                RechargeFragment parentFragment = (RechargeFragment) getParentFragment();
                parentFragment.viewPager.setCurrentItem(2);
            }
        });
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        //window.showAsDropDown(anchor, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        //window.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * 根据拿到的游戏bean设置ui
     */
    private void initVipProgressUi(GameAccountResultModel.ListBean gameAccountBean) {


    }


    //获取vip充值游戏账号数量
    private void getVipGameAccount() {
        Flowable<HttpResultModel<VipGameAccountResults>> fr = DataService.getVipGameAccount();
        //Flowable<HttpResultModel<VipGameAccountResults>> fr = DataService.getVipGameAccount(new BindVipAccountNumRequestBody(gameBean.getGame_id()));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<VipGameAccountResults>>() {
            @Override
            public void accept(HttpResultModel<VipGameAccountResults> vipGameAccountResultsHttpResultModel) throws Exception {
                BindVipAccountNumberBean = vipGameAccountResultsHttpResultModel.data;
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

                } else {

                    if (gameBean.isIs_vip()) {
                        //当前游戏肯定是vip 默认选中vip折扣 不需要判断vip数量

                    } else {

                    }
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    //获取会员折扣
    private void getGameAccountDiscount(int gameAccountId) {
        Flowable<HttpResultModel<GameAccountDiscountResults>> fr = DataService.getGameAccountDiscount(new SingleGameIdRequestBody(gameAccountId));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountDiscountResults>>() {
            @Override
            public void accept(HttpResultModel<GameAccountDiscountResults> gameAccountDiscountResultsHttpResultModel) throws Exception {
                discountList = gameAccountDiscountResultsHttpResultModel.data;
                String inputValue = mEtBalance.getText().toString().trim();
                if (gameBean.isIs_xc()) {
                    if (Float.compare(discountList.discount_activity, 0.0f) == 0) {
                        //体验皇冠价一次
                        initFirstRecharge(3);
                    } else {
                        //体验活动价一次
                        initFirstRecharge(4);
                    }
                } else {
                    if (gameBean.isIs_vip()) {
                        initVIPGameAccount(currentUserVipLevel);
                        initUpgradeVipDiscount(currentUserVipLevel);
                    } else {
                        initGeneralGameAccount(gameBean.getVip_level());
                        initUpgradeVipDiscount(gameBean.getVip_level());
                    }
                }
                rbVip0.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.member_discount).toString() + "元");
                rbVip1.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.vip1_discount).toString() + "元");
                rbVip2.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.high_vip_discount).toString() + "元");
                rbVip3.setText(calculateDiscountAfterBalanceValue(inputValue, discountList.high_vip_discount).toString() + "元");


                //mDiscount2.setText(discount_member + discountList.member_discount + "折");
                //mDiscount3.setText(discount_vip + discountList.vip_discount + "折");
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

    private String calculateDiscountAfterBalanceValue(String inputBalance, float discount) {
        int inputValue = 0;
        double discountAfterValue = 0;
        String format = "0.00";
        try {
            inputValue = Integer.parseInt(inputBalance);
        } catch (NumberFormatException e) {
        }

        if (discount < 0 || inputValue <= 0) {
            //ToastUtil.showToast("输入金额有误!");
            discountAfterValue = 0;
            DecimalFormat df = new DecimalFormat("0.00");
            format = df.format(discountAfterValue);
        } else {
            discountAfterValue = (inputValue * (discount / 10.0));
            //discountAfterValue = Utils.m2(discountAfterValue);
            DecimalFormat df = new DecimalFormat("0.00");
            format = df.format(discountAfterValue);
        }
        return format;
    }


    /**
     * type
     * 0：仅提示消耗vip数量
     * 1：升级vip
     * 2：vip最高联系管理员
     */
    private void showVipHintDialog(final int type) {
        String content = "";
        if (type == 0) content = "您当前选择VIP折扣，将会占用1个VIP名额，您确定使用此折扣支付吗？";
        if (type == 1) content = "您的VIP账户名额不足，升级会员等级可增加VIP账户名额，是否去升级VIP？";
        if (type == 2) content = "您的VIP账户名额已用完，并且是皇冠会员，已无法再升级会员，若您仍想绑定该账号为VIP账号，请联系客服！";
        dialog = null;
        dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Full_Confirm, "温馨提示", content);
        dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
            @Override
            public void onCancel() {
                dialog.dismiss();
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

    //跳转vip升级页面
    private void goToVipLevel() {
        Bundle bundle = new Bundle();
        bundle.putString(WebviewFragment.PARAM_TITLE, "VIP");
        bundle.putString(WebviewFragment.PARAM_URL, SharedPreUtil.getH5url(SharedPreUtil.H5_URL_VIP));
        DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
    }

    private void goToKefu() {
        //跳转客服
        //Toast.makeText(getContext(), "最高等级vip，跳转客服", Toast.LENGTH_SHORT).show();
    }

    public GameAccountResultModel.ListBean getGameBean() {
        return gameBean;
    }

    public double getTotalBalanceValue() {
        mTotalBalanceValue = Float.valueOf(calculateDiscountAfterBalanceValue(mEtBalance.getText().toString().trim(), mTotalDiscountValue));
        return mTotalBalanceValue;
    }

    public boolean getIs_VIP() {
        return is_vip;
    }

    public int getInputValue() {
        String inputValue = mEtBalance.getText().toString().trim();
        if (TextUtils.isEmpty(inputValue)) {
            inputValue = "0";
        }
        return Integer.parseInt(inputValue);
    }

    public boolean getIsGoToVip() {

        return isGotoVip;
    }

    public boolean getGameVipAccountNotEnough() {

        return gameVipAccountNotEnough;
    }

    @Override
    public Object newP() {
        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE && data != null) {
            if (!data.hasExtra(TAG)) return;
            if (data.getSerializableExtra(TAG) instanceof GameAccountResultModel.ListBean) {
                gameBean = (GameAccountResultModel.ListBean) data.getSerializableExtra(TAG);
                showInitGameType(gameBean.getVip_level());
                is_vip = false;
                isGotoVip = false;
                gameVipAccountNotEnough = false;
                initView();

            }
        }
    }

    public void resetFragment() {
        gameBean = null;
        discountList = null;
        BindVipAccountNumberBean = null;
        mTotalDiscountValue = 0;
        mTotalBalanceValue = 0;
        initView();
        mAccount.setText("");
        mGameName.setText("");
        mPlatfrom.setText("");
        mEtBalance.setText("");
        mTotalDiscount.setText("0.0折");
        mTotalBalance.setText("0.00元");
        rbVip0.setText("0.00元");
        rbVip1.setText("0.00元");
        rbVip2.setText("0.00元");
        rbVip3.setText("0.00元");
        //calculateDiscountAfterBalanceValue();
    }


    public void initVipLevel() {
        Flowable<HttpResultModel<MemberInfoResults>> fm = DataService.getMemberInfo();
        RxLoadingUtils.subscribeWithDialog(context, fm, bindToLifecycle(), new Consumer<HttpResultModel<MemberInfoResults>>() {
            @Override
            public void accept(HttpResultModel<MemberInfoResults> memberInfoResultsHttpResultModel) throws Exception {
                MemberInfoResults memberInfoResults = memberInfoResultsHttpResultModel.data;
                //用户当前的VIP等级
                tvUserVipLevel.setText(memberInfoResults.getVip_level().getName());
                //根据等级,选中对应的图标
                int level = memberInfoResults.getVip_level().getLevel();
                currentUserVipLevel = level;
                if (gameBean != null) {
                    //已经选择了游戏账号

                    //初始化用户账号信息
                    mAccount.setText(gameBean.getGame_account());
                    mGameName.setText(gameBean.getGame_name());
                    mPlatfrom.setText(gameBean.getGame_channel_name());
                    //获取会员折扣
                    getGameAccountDiscount(gameBean.getId());
                } else {
                    initUserAccount(level);
                }

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                ToastUtil.showToast("网络错误!");
            }
        });
    }

    public void initUserAccount(int selectedVIP) {
        initUpgradeVipDiscount(selectedVIP);
        switch (selectedVIP) {
            case 0:
                rbVip0.setChecked(true);
                rbVip0.setClickable(true);
                rbVip1.setClickable(true);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 1:
                rbVip1.setChecked(true);
                rbVip0.setClickable(false);
                rbVip1.setClickable(true);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 2:
                rbVip2.setChecked(true);
                rbVip0.setClickable(false);
                rbVip1.setClickable(false);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 3:
                rbVip3.setChecked(true);
                rbVip0.setClickable(false);
                rbVip1.setClickable(false);
                rbVip2.setClickable(false);
                rbVip3.setClickable(true);
                break;
        }
    }
    public void initVIPGameAccount(int selectedVIP) {
        //初始化最终的折扣和最终的实付金额
        showTotalDiscountAndBalance(selectedVIP);
        switch (selectedVIP) {
            case 0:
                rbVip0.setChecked(true);
                rbVip0.setClickable(true);
                rbVip1.setClickable(true);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 1:
                rbVip1.setChecked(true);
                rbVip0.setClickable(false);
                rbVip1.setClickable(true);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 2:
                rbVip2.setChecked(true);
                rbVip0.setClickable(false);
                rbVip1.setClickable(false);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 3:
                rbVip3.setChecked(true);
                rbVip0.setClickable(false);
                rbVip1.setClickable(false);
                rbVip2.setClickable(false);
                rbVip3.setClickable(true);
                break;
        }
    }

    public void initFirstRecharge(int selectedVIP) {
        //初始化最终的折扣和最终的实付金额
        initUpgradeVipDiscount(0);
        showFirstRechargeTotalDiscountAndBalance(selectedVIP);
        switch (selectedVIP) {
            case 3:
                rbVip3.setChecked(true);
                rbVip0.setClickable(false);
                rbVip1.setClickable(false);
                rbVip2.setClickable(false);
                rbVip3.setClickable(false);
                break;
            case 4:
                pbVip.setProgress(0);
                rbVip0.setChecked(false);
                rbVip1.setChecked(false);
                rbVip2.setChecked(false);
                rbVip3.setChecked(false);
                rbVip0.setClickable(false);
                rbVip1.setClickable(false);
                rbVip2.setClickable(false);
                rbVip3.setClickable(false);
                break;
        }
    }

    public void initGeneralGameAccount(int generalGameAccountLevel) {
        //初始化最终的折扣和最终的实付金额
        showTotalDiscountAndBalance(generalGameAccountLevel);
        rbVip0.setChecked(true);
        rbVip0.setClickable(true);
        switch (currentUserVipLevel) {
            case 0:
                rbVip1.setClickable(true);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 1:
                rbVip1.setClickable(true);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 2:
                rbVip1.setClickable(false);
                rbVip2.setClickable(true);
                rbVip3.setClickable(true);
                break;
            case 3:
                rbVip1.setClickable(false);
                rbVip2.setClickable(false);
                rbVip3.setClickable(true);
                break;
        }

    }

    public void showTotalDiscountAndBalance(int selectedLevel) {
        String inputValue = mEtBalance.getText().toString().trim();
        switch (selectedLevel) {
            case 0:
                mTotalDiscountValue = discountList.member_discount;
                tvGameAccountType.setText("普通账号");
                break;
            case 1:
                mTotalDiscountValue = discountList.vip1_discount;
                tvGameAccountType.setText("VIP账号");
                break;
            case 2:
                mTotalDiscountValue = discountList.high_vip_discount;
                tvGameAccountType.setText("VIP账号");
                break;
            case 3:
                mTotalDiscountValue = discountList.high_vip_discount;
                tvGameAccountType.setText("VIP账号");
                break;
        }
        mTotalDiscount.setText(mTotalDiscountValue + "折");
        mTotalBalance.setText(calculateDiscountAfterBalanceValue(inputValue, mTotalDiscountValue).toString() + "元");
    }
    public void showInitGameType(int selectedLevel) {
        String inputValue = mEtBalance.getText().toString().trim();
        switch (selectedLevel) {
            case 0:
                tvGameAccountType.setText("普通账号");
                break;
            case 1:
                tvGameAccountType.setText("VIP账号");
                break;
            case 2:
                tvGameAccountType.setText("VIP账号");
                break;
            case 3:
                tvGameAccountType.setText("VIP账号");
                break;
        }
    }

    public void showFirstRechargeTotalDiscountAndBalance(int selectedLevel) {
        String inputValue = mEtBalance.getText().toString().trim();
        switch (selectedLevel) {
            case 3:
                mTotalDiscountValue = discountList.high_vip_discount;
                mTotalDiscount.setText(mTotalDiscountValue + "折(皇冠价体验一次)");
                break;
            case 4:
                mTotalDiscountValue = discountList.discount_activity;
                mTotalDiscount.setText(mTotalDiscountValue + "折(活动价体验一次)");
                break;
        }
        mTotalBalance.setText(calculateDiscountAfterBalanceValue(inputValue, mTotalDiscountValue).toString() + "元");
    }


    public void onClickChanged(int vipLevel, View buttonView, boolean isChecked) {
        int level = vipLevel;
            Log.d(TAG,"onClickChanged==="+"vipLevel::"+vipLevel+"-----isChecked ::"+isChecked);
            if (gameBean != null) {
                //选择了游戏账号
                showTotalDiscountAndBalance(level);
                if (gameBean.isIs_vip()) {
                    vipGameAccount(level, buttonView);
                } else {
                    generalGameAccount(level, buttonView);
                }
            } else {
                userAccount(level, buttonView);
            }

    }

    @OnClick({R.id.ll_account_game_recharge, R.id.et_balance_game_recharge, R.id.rb_vip0_game_recharge
            , R.id.rb_vip1_game_recharge, R.id.rb_vip2_game_recharge, R.id.rb_vip3_game_recharge})
    public void onViewClicked(View view) {
        int level = 0;
        switch (view.getId()) {
            case R.id.ll_account_game_recharge:
                startActivityForResult(new Intent(getActivity(), MyAccountActivity.class), REQUEST_CODE);
                break;
            case R.id.et_balance_game_recharge:
                if (TextUtils.isEmpty(mAccount.getText().toString().trim())) {
                    mEtBalance.setCursorVisible(false);
                    mEtBalance.setFocusable(false);
                    mEtBalance.setFocusableInTouchMode(false);
                    ToastUtil.showToast("请选择游戏账号");
                } else {
                    mEtBalance.setFocusable(true);
                    mEtBalance.setCursorVisible(true);
                    mEtBalance.setFocusableInTouchMode(true);
                    mEtBalance.requestFocus();
                }
                break;
            case R.id.rb_vip0_game_recharge:
                level = 0;
                onClickChanged(level, view, rbVip0.isChecked());
                break;
            case R.id.rb_vip1_game_recharge:
                /*if (rbVip1.isChecked()) {
                    showPopWindow(view);
                }*/
                level = 1;
                onClickChanged(level, view, rbVip1.isChecked());
                break;
            case R.id.rb_vip2_game_recharge:
                /*if (rbVip2.isChecked()) {
                    showPopWindow(view);
                }*/
                level = 2;
                onClickChanged(level, view, rbVip2.isChecked());
                break;
            case R.id.rb_vip3_game_recharge:
                /*if (rbVip3.isChecked()) {
                    showPopWindow(view);
                }*/
                level = 3;
                onClickChanged(level, view, rbVip3.isChecked());
                break;
        }
    }

    //会员等级:会员,游戏账号: 普通账号
    private void generalGameAccount(int vipLevel, View view) {
        if (BindVipAccountNumberBean.count <= 0) {
            //可以绑定的vip游戏账号个数为0
            if (vipLevel == 0) {
                is_vip = false;
                isGotoVip = false;
                gameVipAccountNotEnough = false;
                showUpgradeVipDiscount(0);
            } else if (vipLevel == currentUserVipLevel) {
                is_vip = false;
                isGotoVip = false;
                gameVipAccountNotEnough = true;
                mTvUpgradeVipAlert.setVisibility(View.VISIBLE);
                mLlUpgradeVip.setVisibility(View.GONE);
                mTvUpgradeVipAlert.setText("VIP账号名额已用完,请升级会员");
            } else {
                is_vip = true;
                isGotoVip = true;
                gameVipAccountNotEnough = false;
                //弹出popwindow升级会员
                showPopWindow(view);
                showUpgradeVipDiscount(vipLevel);
            }
        } else {
            if (vipLevel == 0) {
                is_vip = false;
                isGotoVip = false;
                gameVipAccountNotEnough = false;
                showUpgradeVipDiscount(0);
            } else if (vipLevel == currentUserVipLevel) {
                is_vip = true;
                isGotoVip = false;
                gameVipAccountNotEnough = false;
                mTvUpgradeVipAlert.setVisibility(View.VISIBLE);
                mLlUpgradeVip.setVisibility(View.GONE);
                mTvUpgradeVipAlert.setText("您当前选择VIP折扣，将会占用1个VIP名额");
            } else {
                is_vip = true;
                isGotoVip = true;
                gameVipAccountNotEnough = false;
                //弹出popwindow升级会员
                showPopWindow(view);
                showUpgradeVipDiscount(vipLevel);
            }
        }

    }

    //会员等级:会员 ,游戏账号: Vip
    private void vipGameAccount(int vipLevel, View view) {
        if (vipLevel > currentUserVipLevel) {
            is_vip = true;
            isGotoVip = true;
            gameVipAccountNotEnough = false;
            //弹出popwindow升级会员
            showPopWindow(view);
            showUpgradeVipDiscount(vipLevel);

        } else if (vipLevel == currentUserVipLevel) {
            is_vip = true;
            isGotoVip = false;
            gameVipAccountNotEnough = false;
            showUpgradeVipDiscount(0);
        }
    }

    //当前用户账号
    private void userAccount(int vipLevel, View view) {
        if (currentUserVipLevel != -1) {
            if (vipLevel > currentUserVipLevel) {
                //弹出popwindow升级会员
                showPopWindow(view);
            } else if (vipLevel == currentUserVipLevel) {

            }
        }
    }

    //显示等级进度条上面布局的折扣
    private void showUpgradeVipDiscount(int selectedVip) {
        mTvUpgradeVipAlert.setVisibility(View.GONE);
        mLlUpgradeVip.setVisibility(View.VISIBLE);
        switch (selectedVip) {
            case 0:
                mLlUpgradeVip.setVisibility(View.GONE);
                mTvUpgradeVipAlert.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mTvUpgradeVipName.setText("黑钻");
                mTvUpgradeVipDiscount.setText(String.valueOf(discountList.member_discount));
                break;
            case 2:
                mTvUpgradeVipName.setText("红钻");
                mTvUpgradeVipDiscount.setText(String.valueOf(discountList.high_vip_discount));
                break;
            case 3:
                mTvUpgradeVipName.setText("皇冠");
                mTvUpgradeVipDiscount.setText(String.valueOf(discountList.high_vip_discount));
                break;
        }
    }


    //初始化显示等级进度条上面布局的折扣
    private void initUpgradeVipDiscount(int selectedVip) {

        if (selectedVip == 0 || selectedVip == currentUserVipLevel || currentUserVipLevel == -1) {
            mLlUpgradeVip.setVisibility(View.GONE);
            mTvUpgradeVipAlert.setVisibility(View.INVISIBLE);
        }

    }

}
