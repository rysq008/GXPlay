package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.model.AllAccountsResultsModel;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.utils.NumberUtil;
import com.game.helper.utils.RxLoadingUtils;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class OrderConfirmActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "OrderConfirmActivity";
    public static final String OPTION_GAME_ID = "option_game_id";
    public static final String BUNDLE_GAME_BEAN = "game_bean";
    public static final String BUNDLE_TOTAL_BALANCE = "after_diacount_total_balance";
    public static final String BUNDLE_INPUT_VALUE = "input_value";
    public static final String RED_PACK_AMOUNT = "red_pack_amount";
    public static final String RED_PACK_ID = "input_value_id";

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
    TextView realPay;
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
    private String mGameName ;
    /**
     * 账户名称
     */
    private String mAccountName ;
    /**
     * 显示充值页面用户输入的金额
     */
    private Double mMoney = 0.0;
    /**
     * 用户选择红包抵用券金额
     */
    private Double mRedpack  = 0.0;
    /**
     * 用户选择红包抵用券金id
     */
    private int mRedpackId = 0;
    /**
     * 实际支付金额 = 用户输入金额 * 折扣 - 红包抵用金额
     */
    private Double mRealPay = 0.0;
    /**
     * 还需支付
     */
    private Double mNeedPay = 0.0;
    /**
     * 平台名称
     */
    private String mChannel;

    /**
     * 游戏 ID
     */
    private int mGameId;
    /**
     * 用户选择的支付方式
     * 1:ali支付  2:wx支付
     */
    private int payWay;
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

    private AvailableRedpackResultModel mRedPacks = new AvailableRedpackResultModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();
        initListeners();
        fetchAvailableRedpackInfo(1, mGameId);
        fetchAccountInfo();
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
                    if(usePushAccount){
                        mAvailableCoin = mAccountAvailableBalance + mPushAccountAvailableBalance;
                    }else{
                        mAvailableCoin = mAccountAvailableBalance;
                    }
                    availableCoinTv.setText(String.valueOf(mAvailableCoin));

                    //计算还需支付
                    calcNeedPay();
                }else{
                    mAccountAvailableBalance = 0.0;
                    chargeAccountTv.setText(String.valueOf(mAccountAvailableBalance));
                    mPushAccountAvailableBalance = 0.0;
                    pushAccountTv.setText(String.valueOf(mPushAccountAvailableBalance));

                    //可用金币
                    if(usePushAccount){
                        mAvailableCoin = mAccountAvailableBalance + mPushAccountAvailableBalance;
                    }else{
                        mAvailableCoin = mAccountAvailableBalance;
                    }
                    availableCoinTv.setText(String.valueOf(mAvailableCoin));

                    //计算还需支付
                    calcNeedPay();
                }

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                mAccountAvailableBalance = 0.0;
                chargeAccountTv.setText(String.valueOf(mAccountAvailableBalance));
                mPushAccountAvailableBalance = 0.0;
                pushAccountTv.setText(String.valueOf(mPushAccountAvailableBalance));

                //可用金币
                if(usePushAccount){
                    mAvailableCoin = mAccountAvailableBalance + mPushAccountAvailableBalance;
                }else{
                    mAvailableCoin = mAccountAvailableBalance;
                }
                availableCoinTv.setText(String.valueOf(mAvailableCoin));

                //计算还需支付
                calcNeedPay();
            }
        });
    }

    private void initListeners() {
        redPackLayout.setOnClickListener(this);
        suretv.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

//        BusProvider.getBus().receive(RedPackEvent.class).subscribe(new Consumer<RedPackEvent>() {
//            @Override
//            public void accept(RedPackEvent redPackEvent) throws Exception {
//                if (null != redPackEvent) {
//                    if (redPackEvent.getType() == RxConstant.Chooice_RedPack) {
//                        redPackNum.setText("-" + ((AvailableRedpackResultModel.ListBean) redPackEvent.getData()).getAmount());
//                    }
//                }
//
//            }
//        });

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
                    availableCoinTv.setText(String.valueOf(mAvailableCoin));

                    //计算还需支付
                    calcNeedPay();
                } else {
                    usePushAccount = false;
                    // 关闭了推广账户余额   可用金币=充值账户余额
                    mAvailableCoin = mAccountAvailableBalance;
                    availableCoinTv.setText(String.valueOf(mAvailableCoin));

                    //计算还需支付
                    calcNeedPay();
                }
            }
        });
    }

    /**
     * 计算实际支付
     */
    public Double calcRealPay() {
        mRealPay = totalBalance - mRedpack;
        return mRealPay;
    }

    /**
     * 计算还需支付
     */
    public Double calcNeedPay() {
        double needPay = 0.0;
        if(useCoin){
            needPay = mRealPay - mRedpack - mAvailableCoin;
            if(needPay>0){
                needPayTv.setText(String.valueOf(needPay));
            }else{
                needPayTv.setText("0.0");
            }
            return needPay;
        }else{
            needPay = mRealPay - mRedpack ;
            if(needPay>0){
                needPayTv.setText(String.valueOf(needPay));
            }else{
                needPayTv.setText("0.0");
            }
            return needPay;
        }

    }

    /**
     * 获取可用红包/卡券
     */
    private void fetchAvailableRedpackInfo(int page, int option_game_id) {
        Flowable<HttpResultModel<AvailableRedpackResultModel>> flowable = DataService.getRedPackInfo(new AvailableRedpackRequestBody(page, option_game_id));
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<AvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<AvailableRedpackResultModel> generalizeResultsHttpResultModel) throws Exception {
                if (generalizeResultsHttpResultModel.isSucceful()) {
                    if (generalizeResultsHttpResultModel.isNull()) {
                        redPackNum.setText("无可用红包");
                    }
                    mRedPacks = generalizeResultsHttpResultModel.data;
                }

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {

            }
        });
    }

    private void initIntentData(Intent intent) {
        Bundle bundle = intent.getBundleExtra(TAG);
        if (bundle != null) {
            gameBean = (GameAccountResultModel.ListBean) bundle.getSerializable(BUNDLE_GAME_BEAN);
            totalBalance = bundle.getDouble(BUNDLE_TOTAL_BALANCE);
            inputBalance = bundle.getDouble(BUNDLE_INPUT_VALUE);
        }

        //分解数据
        mGameName = gameBean.getGame_name();
        mAccountName = gameBean.getGame_account();
        mMoney = inputBalance;
        mChannel = gameBean.getGame_channel_name();
        mGameId = gameBean.getGame_id();
    }

    private void initView() {
        mHeadTittle.setText("确认订单");

        gameName.setText(mGameName);
        accountName.setText(mAccountName);
        moneyNum.setText(String.valueOf(mMoney));
        redPackNum.setText(String.valueOf(mRedpack));
        realPay.setText(String.valueOf(calcRealPay()));
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
                intent.putExtra(OPTION_GAME_ID, mGameId);
                startActivityForResult(intent, 0);
                break;
            case R.id.suretv://确定
                //1.先判断页面信息是否填写完整
                checkPageInfo();


                //TODO 是否设置过交易密码  如果用户没有设置过交易密码则弹出框提示用户去设置，否则弹出交易密码输入框

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
        if(NumberUtil.compare(String.valueOf(mNeedPay),NumberUtil.Zero)>0){
            if(!aliPayCb.isChecked() && !wxPayCb.isChecked()){
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
        return realPay.getText().toString().trim();
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
            int red_id = data.getIntExtra(RED_PACK_ID, 0);
            if (null == amount || TextUtils.isEmpty(amount)) {
                amount = "0.0";
            }
            onRedPackSelected(amount,red_id);
        }
    }

    private void onRedPackSelected(String amount,int red_id) {
        //重新赋值红包金额,id
        if(null == amount || TextUtils.isEmpty(amount) || NumberUtil.compare(amount,NumberUtil.Zero)<=0){
            mRedpack = 0.0;
        }else{
            mRedpack = Double.parseDouble(amount);
        }
        mRedpackId = red_id;
        //展示红包抵用金额
        redPackNum.setText("-" + mRedpack);
        //重新计算实际支付
        realPay.setText(String.valueOf(calcRealPay()));
        //计算还需支付
        calcNeedPay();
    }

    @Override
    protected void onDestroy() {
//        BusProvider.getBus().unregister(this);
        super.onDestroy();
    }
}
