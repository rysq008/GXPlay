package com.game.helper.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alipay.sdk.app.PayTask;
import com.game.helper.GameMarketApplication;
import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.RedPackEvent;
import com.game.helper.fragments.UpdateTradePasswordFragment;
import com.game.helper.model.AllAccountsResultsModel;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CheckTradePasswdResults;
import com.game.helper.model.FeedbackListResults;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.WxPayInfoBean;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
import com.game.helper.net.model.ConsumeRequestBody;
import com.game.helper.net.model.PayRequestBody;
import com.game.helper.utils.AliPayResultUtils;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.Utils;
import com.game.helper.utils.WXPayUtils;
import com.game.helper.views.PasswordEditDialog;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class OrderConfirmActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "OrderConfirmActivity";
    public static final String OPTION_GAME_ID = "option_game_id";
    public static final String RED_PACK_LIMIT = "red_pack_limit";
    public static final String BUNDLE_GAME_BEAN = "game_bean";
    public static final String BUNDLE_TOTAL_BALANCE = "after_diacount_total_balance";
    public static final String BUNDLE_INPUT_VALUE = "input_value";
    public static final String RED_PACK_AMOUNT = "red_pack_amount";
    public static final String RED_PACK_TYPE = "red_pack_type";
    public static final String RED_PACK_ID = "input_value_id";
    public static final String PAYPURPOSE = "payPurpose";
    public static final String VIPLEVEL = "vipLevel";


    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.cancelTv)
    TextView cancelTv;
    @BindView(R.id.suretv)
    TextView suretv;
    @BindView(R.id.bottomLayout)
    LinearLayout bottomLayout;
    @BindView(R.id.iv_action)
    ImageView ivAction;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.action_bar_setting)
    RelativeLayout actionBarSetting;
    @BindView(R.id.gameName)
    TextView gameName;
    @BindView(R.id.gameLayout)
    LinearLayout gameLayout;
    @BindView(R.id.accountName)
    TextView accountName;
    @BindView(R.id.accountLayout)
    LinearLayout accountLayout;
    @BindView(R.id.moneyNum)
    TextView moneyNum;
    @BindView(R.id.moneyLayout)
    LinearLayout moneyLayout;
    @BindView(R.id.redPackNum)
    TextView redPackNum;
    @BindView(R.id.redPackLayout)
    LinearLayout redPackLayout;
    @BindView(R.id.realPay)
    TextView realPayTv;
    @BindView(R.id.realPayLayout)
    LinearLayout realPayLayout;
    @BindView(R.id.channelName)
    TextView channelName;
    @BindView(R.id.ChannelLayout)
    LinearLayout ChannelLayout;
    @BindView(R.id.chargeAccountText)
    TextView chargeAccountText;
    @BindView(R.id.chargeAccountTv)
    TextView chargeAccountTv;
    @BindView(R.id.pushAccountTv)
    TextView pushAccountTv;
    @BindView(R.id.toggleBtn)
    ToggleButton toggleBtn;
    @BindView(R.id.availableCoinTv)
    TextView availableCoinTv;
    @BindView(R.id.usefulCoin)
    CheckBox usefulCoin;
    @BindView(R.id.wxPayCb)
    CheckBox wxPayCb;
    @BindView(R.id.aliPayCb)
    CheckBox aliPayCb;
    @BindView(R.id.needPayTv)
    TextView needPayTv;

    private GameAccountResultModel.ListBean gameBean;//整个bean
    private Double totalBalance = 0.0;//充值页面传过来的计算折扣以后的金额
    private Double inputBalance = 0.0;//充值页面用户输入的金额

    /**
     * 游戏名称
     */
    private String mGameName;
    /**
     * 账户名称
     */
    private String mAccountName;
    /**
     * 用户选择红包抵用券金额
     */
    private Double mRedpackAmount = 0.0;
    /**
     * 用户选择红包抵用券类型
     */
    private String mRedpackType = "0";
    /**
     * 用户选择红包抵用券金id
     */
    private String mRedpackId = "";
    /**
     * 实际支付金额 = 用户输入金额 * 折扣 - 红包抵用金额
     */
    private Double mRealPay = 0.0;
    /**
     * 还需支付
     */
    private Double mNeedPay;
    /**
     * 平台名称
     */
    private String mChannel;

    /**
     * 游戏账户 ID
     */
    private int gameAccountId;
    /**
     * 游戏 ID
     */
    private int gameId;
    /**
     * 用户选择的支付方式
     * 1:ali支付  2:wx支付
     */
    private int payWay = 2;
    /**
     * 是否使用可用金币
     */
    private boolean useCoin = true;
    /**
     * 是否使用推广账户
     */
    private boolean usePushAccount = true;

    /**
     * 充值账户可用余额
     */
    private Double mAccountAvailableBalance = 0.0;

    /**
     * 推广账户可用余额
     */
    private Double mPushAccountAvailableBalance = 0.0;

    /**
     * 可用金币
     */
    private Double mAvailableCoin = 0.0;

    /**
     * 是否是vip
     */
    private boolean is_vip;

    /**
     * 支付密码
     */
    public String password = "";

    //支付用途（1：普通充值、2：充值会员）
    public String payPurpose = "1";

    public String vipLevel = "0";

    private AvailableRedpackResultModel mRedPacks = new AvailableRedpackResultModel();

    String accountAmount = "";//使用充值账户金额
    String marketingAmount = "";//使用推广账户金额

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    AliPayResultUtils payResult = new AliPayResultUtils((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderConfirmActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        doConsume(accountAmount, marketingAmount);
                    } else {
                        Toast.makeText(OrderConfirmActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();
        initListeners();
        fetchAvailableRedpackInfo(1);
    }

    @Override
    protected void onResume() {
        fetchAccountInfo();
        super.onResume();
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
                    //充值账户余额
                    mAccountAvailableBalance = Double.parseDouble(data.data.getBalance());
                    chargeAccountTv.setText(data.data.getBalance());
                    //推广账户余额
                    mPushAccountAvailableBalance = Double.parseDouble(data.data.getYue());
                    pushAccountTv.setText(data.data.getYue());

                    //可用金币
                    if (usePushAccount) {
                        mAvailableCoin = mAccountAvailableBalance + mPushAccountAvailableBalance;
                    } else {
                        mAvailableCoin = mAccountAvailableBalance;
                    }
                    BigDecimal b = new BigDecimal(mAvailableCoin + "");
                    String result = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    availableCoinTv.setText(result);

                    //计算还需支付
                    mNeedPay = calcNeedPay();
                } else {
                    mAccountAvailableBalance = 0.0;
                    chargeAccountTv.setText(new BigDecimal(String.valueOf(mAccountAvailableBalance)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    mPushAccountAvailableBalance = 0.0;
                    pushAccountTv.setText(String.valueOf(mPushAccountAvailableBalance));

                    //可用金币
                    if (usePushAccount) {
                        mAvailableCoin = mAccountAvailableBalance + mPushAccountAvailableBalance;
                    } else {
                        mAvailableCoin = mAccountAvailableBalance;
                    }
                    BigDecimal b = new BigDecimal(mAvailableCoin + "");
                    String result = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    availableCoinTv.setText(result);

                    //计算还需支付
                    mNeedPay = calcNeedPay();
                }

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                mAccountAvailableBalance = 0.0;
                chargeAccountTv.setText(new BigDecimal(String.valueOf(mAccountAvailableBalance)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                mPushAccountAvailableBalance = 0.0;
                pushAccountTv.setText(String.valueOf(mPushAccountAvailableBalance));

                //可用金币
                if (usePushAccount) {
                    mAvailableCoin = mAccountAvailableBalance + mPushAccountAvailableBalance;
                } else {
                    mAvailableCoin = mAccountAvailableBalance;
                }
                BigDecimal b = new BigDecimal(mAvailableCoin + "");
                String result = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                availableCoinTv.setText(result);

                //计算还需支付
                mNeedPay = calcNeedPay();
            }
        });
    }

    private void initListeners() {
        redPackLayout.setOnClickListener(this);
        suretv.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

        BusProvider.getBus().receive(RedPackEvent.class).subscribe(new Consumer<RedPackEvent>() {
            @Override
            public void accept(RedPackEvent redPackEvent) throws Exception {
                if (null != redPackEvent) {
                    if (redPackEvent.getType() == RxConstant.WX_PAY) {
                        switch (redPackEvent.getData()) {
                            case 0:
                                doConsume(accountAmount, marketingAmount);
                                break;
                            case -1:
//                                Toast.makeText(OrderConfirmActivity.this, "充值失败，请重试", Toast.LENGTH_SHORT).show();
                                break;
                            case -2:
//                                Toast.makeText(OrderConfirmActivity.this, "充值失败，请重试", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }

            }
        });

        //wx支付
        wxPayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //ali置为未选中
                    aliPayCb.setChecked(false);
                    payWay = 2;
                }
            }
        });

        //ali支付
        aliPayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //ali置为未选中
                    wxPayCb.setChecked(false);
                    payWay = 1;
                }
            }
        });

        //可用金币
        usefulCoin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    useCoin = true;
                    // 打开了可用金币   还需支付金额= 实付-红包-可用金币
                    mNeedPay = calcNeedPay();

                } else {
                    useCoin = false;
                    // 关闭了可用金币   还需支付金额= 实付-红包
                    mNeedPay = calcNeedPay();
                }
            }
        });

        //推广账户余额
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    usePushAccount = true;
                    // 打开了推广账户余额   可用金币=充值账户余额+ 推广账户余额
                    mAvailableCoin = mAccountAvailableBalance + mPushAccountAvailableBalance;
                    BigDecimal bigDecimal = new BigDecimal(mAvailableCoin + "");
                    String result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    availableCoinTv.setText(result);

                    //计算还需支付
                    mNeedPay = calcNeedPay();
                } else {
                    usePushAccount = false;
                    // 关闭了推广账户余额   可用金币=充值账户余额
                    mAvailableCoin = mAccountAvailableBalance;
                    BigDecimal bigDecimal = new BigDecimal(mAvailableCoin + "");
                    String result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    availableCoinTv.setText(result);

                    //计算还需支付
                    mNeedPay = calcNeedPay();
                }
            }
        });
    }

    /**
     * 计算实际支付
     */
    public String calcRealPay() {
        mRealPay = totalBalance - mRedpackAmount;
        String result = new BigDecimal(String.valueOf(mRealPay)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return result;
    }

    /**
     * 计算还需支付
     */
    public Double calcNeedPay() {
        double needPay = 0.0;
        if (useCoin) {
            needPay = mRealPay - mAvailableCoin;
            if (needPay <= 0) {
                needPayTv.setText(new BigDecimal(String.valueOf("0.0")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else {
                BigDecimal b = new BigDecimal(needPay + "");
                String result = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                needPayTv.setText(result);
            }
        } else {
            needPay = mRealPay;
            if (needPay <= 0) {
                needPayTv.setText(new BigDecimal(String.valueOf("0.0")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else {
                BigDecimal b = new BigDecimal(needPay + "");
                String result = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                needPayTv.setText(result);
            }

        }
        return needPay;
    }

    /**
     * 获取可用红包/卡券
     */
    private void fetchAvailableRedpackInfo(int page) {
        Flowable<HttpResultModel<AvailableRedpackResultModel>> flowable = DataService.getRedPackInfo(new AvailableRedpackRequestBody(page, gameId, inputBalance + ""));
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<AvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<AvailableRedpackResultModel> generalizeResultsHttpResultModel) throws Exception {
                if (generalizeResultsHttpResultModel.isSucceful()) {
                    if (generalizeResultsHttpResultModel.isNull()) {
                        redPackNum.setTextColor(getResources().getColor(R.color.black));
                        redPackNum.setText("无可用红包");
                    } else {
                        redPackNum.setTextColor(Color.parseColor("#fe4430"));
                        redPackNum.setText(generalizeResultsHttpResultModel.data.getList().size() + "个可用");
                    }
                    mRedPacks = generalizeResultsHttpResultModel.data;
                }else{
                    redPackNum.setTextColor(getResources().getColor(R.color.black));
                    redPackNum.setText("无可用红包");
                }

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                redPackNum.setTextColor(getResources().getColor(R.color.black));
                redPackNum.setText("无可用红包");
            }
        });
    }

    private void initIntentData(Intent intent) {
        Bundle bundle = intent.getBundleExtra(TAG);
        if (bundle != null) {
            gameBean = (GameAccountResultModel.ListBean) bundle.getSerializable(BUNDLE_GAME_BEAN);
            totalBalance = bundle.getDouble(BUNDLE_TOTAL_BALANCE);
            inputBalance = bundle.getDouble(BUNDLE_INPUT_VALUE);
            payPurpose = bundle.getString(PAYPURPOSE);
            vipLevel = bundle.getString(VIPLEVEL);

        }

        //分解数据
        mGameName = gameBean.getGame_name();
        mAccountName = gameBean.getGame_account();
        mChannel = gameBean.getGame_channel_name();
        gameAccountId = gameBean.getId();
        gameId = gameBean.getGame_id();
        is_vip = gameBean.isIs_vip();
    }

    private void initView() {
        mHeadTittle.setText("确认订单");

        gameName.setText(mGameName);
        accountName.setText(mAccountName);
        moneyNum.setText(new BigDecimal(String.valueOf(inputBalance)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        realPayTv.setText(calcRealPay());
        channelName.setText(mChannel);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_order_confirm;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.action_bar_back://返回
            case R.id.cancelTv://取消
                onBackPressed();
                break;
            case R.id.redPackLayout://红包
                intent.setClass(OrderConfirmActivity.this, ChoiceRedPackActivity.class);
                intent.putExtra(OPTION_GAME_ID, gameId);
                intent.putExtra(RED_PACK_LIMIT, inputBalance + "");
                startActivityForResult(intent, 0);
                break;
            case R.id.suretv://确定
                //1.先判断页面信息是否填写完整
                if (!checkPageInfo()) {
                    Log.e("", "页面信息不完整");
                    return;
                }

                //是否设置了交易密码
                if (SharedPreUtil.getLoginUserInfo().has_trade_passwd) {
                    final PasswordEditDialog dialog = new PasswordEditDialog();
                    dialog.addOnPassWordEditListener(new PasswordEditDialog.OnPassWordEditListener() {
                        @Override
                        public void onConfirmComplete(String password) {
                            dialog.dismiss();
                            ProvingTradePssword(password);
                        }
                    });
                    dialog.show(getSupportFragmentManager(), TAG);
                } else {
                    goToSetTradePassword();
                }

//                weixinPay();

                break;

            default:
                break;
        }
    }

    /**
     * 检查页面信息是否填写完整
     */
    private boolean checkPageInfo() {
        if (TextUtils.isEmpty(getGameName())) {
            Toast.makeText(this, "游戏名字为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(getAccountName())) {
            Toast.makeText(this, "账户名称为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(getMoneyAmount())) {
            Toast.makeText(this, "金额为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(getRealPayAmount())) {
            Toast.makeText(this, "实付金额为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(getChannelName())) {
            Toast.makeText(this, "平台名称为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //如果还需支付的金额不是0的话，就要选择支付宝或者微信（单选）
        if (mNeedPay > 0) {
            if (!aliPayCb.isChecked() && !wxPayCb.isChecked()) {
                Toast.makeText(this, "请选择一种支付方式", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    /**
     * 获取游戏名字
     *
     * @return
     */
    public String getGameName() {
        return gameName.getText().toString().trim();
    }

    /**
     * 账户名称
     *
     * @return
     */
    public String getAccountName() {
        return accountName.getText().toString().trim();
    }

    /**
     * 充值金额
     *
     * @return
     */
    public String getMoneyAmount() {
        return moneyNum.getText().toString().trim();
    }

    /**
     * 实付
     *
     * @return
     */
    public String getRealPayAmount() {
        return realPayTv.getText().toString().trim();
    }

    /**
     * 平台名称
     *
     * @return
     */
    public String getChannelName() {
        return channelName.getText().toString().trim();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            String amount = data.getStringExtra(RED_PACK_AMOUNT);
            String type = data.getStringExtra(RED_PACK_TYPE);
            String red_id = data.getStringExtra(RED_PACK_ID);
            onRedPackSelected(amount, type, red_id);
        }
    }

    private void onRedPackSelected(String amount, String type, String red_id) {
        //重新赋值红包金额,id
        mRedpackAmount = Double.parseDouble(amount);
        mRedpackType = type;
        mRedpackId = red_id;
        //展示红包抵用金额
        redPackNum.setTextColor(Color.parseColor("#fe4430"));
        redPackNum.setText("-" + new BigDecimal(String.valueOf(mRedpackAmount)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        //重新计算实际支付
        realPayTv.setText(calcRealPay());
        //计算还需支付
        mNeedPay = calcNeedPay();
    }

    /**
     * 跳转设置交易密码
     */
    private void goToSetTradePassword() {
        DetailFragmentsActivity.launch(this, null, UpdateTradePasswordFragment.newInstance());
    }

    /**
     * 校验输入的交易密码
     */
    private void ProvingTradePssword(final String password) {
        Flowable<HttpResultModel<CheckTradePasswdResults>> fr = DataService.checkTradePassword(new CheckTradePasswdRequestBody(password));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CheckTradePasswdResults>>() {
            @Override
            public void accept(HttpResultModel<CheckTradePasswdResults> checkTradePasswdResultsHttpResultModel) throws Exception {
                //校验支付密码成功
                if (checkTradePasswdResultsHttpResultModel.isSucceful()) {
                    OrderConfirmActivity.this.password = password;
                    consumeOrCharge();
                } else {
                    Toast.makeText(OrderConfirmActivity.this, "交易密码验证失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 支付还是消费
     */
    private void consumeOrCharge() {

        if (useCoin) {//使用可用金币
            if (usePushAccount) {//使用推广账户余额
                if (mAccountAvailableBalance >= mRealPay) {//先判断充值账户余额够不够,充值账户余额够
                    accountAmount = String.valueOf(mRealPay);
                    marketingAmount = "0";

                } else {//充值账户余额不够
                    accountAmount = String.valueOf(mAccountAvailableBalance);

                    if (mPushAccountAvailableBalance >= (mRealPay - mAccountAvailableBalance)) {//推广账户余额足够
                        marketingAmount = String.valueOf((mRealPay - mAccountAvailableBalance));
                    } else {
                        marketingAmount = String.valueOf(mPushAccountAvailableBalance);
                    }


                }
            } else {//不使用推广账户余额，只使用充值账户余额
                if (mAccountAvailableBalance >= mRealPay) {//充值账户余额够
                    accountAmount = String.valueOf(mRealPay);
                } else {//充值账户余额不够
                    accountAmount = String.valueOf(mAccountAvailableBalance);
                }
                marketingAmount = "0";

            }

        } else {
            //不使用可用金币
            accountAmount = "0";
            marketingAmount = "0";
        }

        //如果还需支付>0,先去微信或支付宝充值，否则直接消费
        if (mNeedPay > 0) {
            doCharge();
        } else {
            doConsume(accountAmount, marketingAmount);
        }
    }

    /**
     * 充值
     */
    private void doCharge() {
        if (payWay == 1) {
            aliPay();
        } else if (payWay == 2) {
            weixinPay();
        } else {
            Toast.makeText(OrderConfirmActivity.this, "非法参数", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * ali支付
     */
    private void aliPay() {
        Flowable<HttpResultModel<PayResultModel>> fr = DataService.ApiPay(new PayRequestBody(SharedPreUtil.getLoginUserInfo().member_id+"", mNeedPay + "", "1", payPurpose, vipLevel));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<PayResultModel>>() {
            @Override
            public void accept(HttpResultModel<PayResultModel> payRequestBody) throws Exception {
                if (payRequestBody.isSucceful()) {
                    final String info = payRequestBody.data.orderInfo;
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(OrderConfirmActivity.this);
                            Map<String, String> result = alipay.payV2(info, true);
                            Log.i("msp", result.toString());

                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    Toast.makeText(OrderConfirmActivity.this, payRequestBody.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e("", "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });

    }

    /**
     * 微信支付
     */
    private void weixinPay() {
        Flowable<HttpResultModel<PayResultModel>> fr = DataService.ApiPay(new PayRequestBody(SharedPreUtil.getLoginUserInfo().member_id+"", mNeedPay + "", "2", payPurpose, vipLevel));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<PayResultModel>>() {
            @Override
            public void accept(HttpResultModel<PayResultModel> payRequestBody) throws Exception {
                if (payRequestBody.isSucceful()) {
                    WxPayInfoBean bean = new WxPayInfoBean();
                    bean.setAppid(RxConstant.ThirdPartKey.WeixinId);
                    bean.setNoncestr(payRequestBody.data.getWxorderInfo().getNoncestr());
                    bean.setPackagestr(payRequestBody.data.getWxorderInfo().getPackagevalue());
                    bean.setPartnerid(payRequestBody.data.getWxorderInfo().getPartnerid());
                    bean.setPrepayid(payRequestBody.data.getWxorderInfo().getPrepayid());
                    bean.setSign(payRequestBody.data.getWxorderInfo().getSign());
                    bean.setTimestamp(payRequestBody.data.getWxorderInfo().getTimestamp());
                    GameMarketApplication.api.sendReq(WXPayUtils.weChatPay(bean));
                } else {
                    Toast.makeText(OrderConfirmActivity.this, payRequestBody.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e("", "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });

    }

    /**
     * 消费
     */
    private void doConsume(String accountAmount, String marketingAmount) {
        Log.e("nuoyan", "gameAccountId：：：" + gameAccountId + "\r\n"
                +"gameId：：：" + gameId + "\r\n"
                + "consumeAmount:::" + inputBalance + "\r\n"
                + "accountAmount:::" + accountAmount + "\r\n"
                + "marketingAmount:::" + marketingAmount + "\r\n"
                + "rechargeAmount:::" + mNeedPay + "\r\n"
                + "is_vip:::" + is_vip + "\r\n"
                + "tradePassword:::" + password + "\r\n"
                + "redpacketType:::" + mRedpackType + "\r\n"
                + "redpacketId:::" + mRedpackId + "\r\n");

        Flowable<HttpResultModel<FeedbackListResults>> fr = DataService.consume(new ConsumeRequestBody(gameAccountId + "", inputBalance + "", accountAmount, marketingAmount, "0", is_vip ? "1" : "0", password, mRedpackType, mRedpackId));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<FeedbackListResults>>() {
            @Override
            public void accept(HttpResultModel<FeedbackListResults> checkTradePasswdResultsHttpResultModel) {
//                if (checkTradePasswdResultsHttpResultModel.isSucceful()) {
                Toast.makeText(OrderConfirmActivity.this, "消费成功！", Toast.LENGTH_SHORT).show();
                finish();
//                } else {
//                    Toast.makeText(OrderConfirmActivity.this, "消费失败！", Toast.LENGTH_SHORT).show();
//                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(OrderConfirmActivity.this, "消费失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        BusProvider.getBus().unregister(this);
        super.onDestroy();
    }
}
