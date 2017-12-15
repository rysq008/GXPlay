package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.RedPackEvent;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.utils.RxLoadingUtils;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class OrderConfirmActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "OrderConfirmActivity";
    public static final String OPTION_GAME_ID = "option_game_id";

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
    @BindView(R.id.pushAccountText)
    TextView pushAccountText;
    @BindView(R.id.pushAccountTv)
    TextView pushAccountTv;
    @BindView(R.id.toggleBtn)
    ToggleButton toggleBtn;
    @BindView(R.id.usefulCoin)
    CheckBox usefulCoin;
    @BindView(R.id.wxPayCb)
    CheckBox wxPayCb;
    @BindView(R.id.aliPayCb)
    CheckBox aliPayCb;
    @BindView(R.id.needPayTv)
    TextView needPayTv;

    private String mGameName = "大话西游";
    private String mAccountName = "牛逼的大叔";
    private String mMoney = "100.00";
    private String mRedpack = "-30";
    private String mRealPay = "50.00";
    private String mChannel = "珍爱网";

    private int mGameId ;
    private int payWay ;//1:ali支付  2:wx支付
    private boolean useCoin ;//是否使用可用金币
    private boolean usePushAccount ;//是否使用推广账户

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
        fetchAvailableRedpackInfo(1,mGameId);
    }

    private void initListeners() {
        redPackLayout.setOnClickListener(this);
        suretv.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);

        BusProvider.getBus().receive(RedPackEvent.class).subscribe(new Consumer<RedPackEvent>() {
            @Override
            public void accept(RedPackEvent redPackEvent) throws Exception {
                if(null!=redPackEvent){
                    if (redPackEvent.getType() == RxConstant.Chooice_RedPack) {
                        redPackNum.setText("-"+((AvailableRedpackResultModel.ListBean)redPackEvent.getData()).getAmount());
                    }
                }

            }
        });

        //wx支付
        wxPayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
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
                if(b){
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
                if(b){
                    useCoin = true;
                    //TODO  打开了可用金币   还需支付金额=还需支付金额= 实付-红包-可用金币（
                }else{
                    useCoin = false;
                    //TODO 关闭了可用金币   还需支付金额=还需支付金额= 实付-红包
                }
            }
        });

        //推广账户余额
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    usePushAccount = true;
                    //TODO 打开了推广账户余额   可用金币=充值账户余额+ 推广账户余额
                }else{
                    usePushAccount = false;
                    //TODO 关闭了推广账户余额   可用金币=充值账户余额
                }
            }
        });
    }

    /**
     * 获取可用红包/卡券
     */
    private void fetchAvailableRedpackInfo(int page,int option_game_id) {
        Flowable<HttpResultModel<AvailableRedpackResultModel>> flowable = DataService.getRedPackInfo(new AvailableRedpackRequestBody(page,option_game_id));
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<AvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<AvailableRedpackResultModel> generalizeResultsHttpResultModel) throws Exception {
                if(generalizeResultsHttpResultModel.isSucceful()){
                    if(generalizeResultsHttpResultModel.isNull()){
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

    }

    private void initView() {
        mHeadTittle.setText("确认订单");

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
        Intent intent= new Intent();
        switch (view.getId()) {
            case R.id.action_bar_back://返回
                onBackPressed();
                break;
            case R.id.redPackLayout://红包
                intent.setClass(OrderConfirmActivity.this,ChoiceRedPackActivity.class);
                intent.putExtra(OPTION_GAME_ID,mGameId);
                startActivity(intent);
                break;
            case R.id.suretv://确定
                //todo 如果还需支付的金额不是0的话，就要选择支付宝或者微信（单选）
                //1.西安判断


                //TODO 是否设置过交易密码  如果用户没有设置过交易密码则弹出框提示用户去设置，否则弹出交易密码输入框

                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        BusProvider.getBus().unregister(this);
        super.onDestroy();
    }
}
