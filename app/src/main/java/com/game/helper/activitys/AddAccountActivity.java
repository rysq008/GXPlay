package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.recharge.RechargeFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.VipGameAccountResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SimpleTextWatcher;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.widget.ChannelPopupWindow;
import com.game.helper.views.widget.GamePopupWindow;
import com.game.helper.views.widget.ToggleButton;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class AddAccountActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "AddAccountActivity";
    public static final String GAME_ID = "game_id";
    public static final String CHANNEL_ID = "channel_id";


    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.accountEdit)
    EditText accountEdit;
    @BindView(R.id.gameEdit)
    EditText gameEdit;
    @BindView(R.id.selectGame)
    TextView selectGame;
    @BindView(R.id.channelEdit)
    EditText channelEdit;
    @BindView(R.id.selectChannel)
    TextView selectChannel;
    @BindView(R.id.tb_add_account_toggle)
    ToggleButton toggleButt;
    @BindView(R.id.add_account_tips_tv)
    TextView tipsTv;
    @BindView(R.id.submitTv)
    TextView submitTv;


    private int gid = 1;//当乐端ID号
    private String mGameId = "";
    private String mChannelId;
    private boolean canEdit = true;

    private GamePopupWindow mGameWindow;
    private ChannelPopupWindow mChannelWindow;
    private GXPlayDialog dialog;
    private int mAccountNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        //initIntentData(getIntent());
        initView();

        mGameWindow = new GamePopupWindow(AddAccountActivity.this);
        mChannelWindow = new ChannelPopupWindow(AddAccountActivity.this);
    }

    private void initIntentData(Intent intent) {
        mGameId = String.valueOf(intent.getIntExtra(GAME_ID, 0));
        mChannelId = String.valueOf(intent.getIntExtra(CHANNEL_ID, 0));

//        if(!TextUtils.isEmpty(mGameId) && !TextUtils.isEmpty(mChannelId)){
//            canEdit = false;
//        }else{
//            canEdit = true;
//        }

    }

    private void initView() {
        mHeadTittle.setText("账户绑定");
        if (!canEdit) {
            gameEdit.setEnabled(false);
            channelEdit.setEnabled(false);
        } else {
            gameEdit.setEnabled(true);
            channelEdit.setEnabled(true);
        }

        mHeadBack.setOnClickListener(this);
        selectGame.setOnClickListener(this);
        selectChannel.setOnClickListener(this);
        submitTv.setOnClickListener(this);

        gameEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString().trim();
//                if(!TextUtils.isEmpty(input)){
//                    mGameWindow.init(getGameName());
//                    mGameWindow.showAsDropDown(selectGame);
//                }
                Log.e("", "输入的游戏：：：" + input);
            }
        });

        channelEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString().trim();
//                if(!TextUtils.isEmpty(input)){
//                    mChannelWindow.init(Integer.parseInt(mGameId));
//                    mChannelWindow.showAsDropDown(selectChannel);
//                }
                Log.e("", "输入的平台：：：" + input);
            }
        });

        Flowable<HttpResultModel<VipGameAccountResults>> fr = DataService.getVipGameAccount();
        RxLoadingUtils.subscribeWithDialog(context, fr, bindToLifecycle(), new Consumer<HttpResultModel<VipGameAccountResults>>() {
            @Override
            public void accept(HttpResultModel<VipGameAccountResults> vipGameAccountResultsHttpResultModel) throws Exception {
                if (vipGameAccountResultsHttpResultModel.isSucceful()) {
                    mAccountNum = vipGameAccountResultsHttpResultModel.data.count;
                    tipsTv.setText(getString(R.string.add_account_tips, mAccountNum));
                }
            }
        });

        toggleButt.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (mAccountNum > 0) {
                    if (on)
                        showVipBindDialog(1, new GXPlayDialog.onDialogActionListner() {
                            @Override
                            public void onCancel() {
                                toggleButt.setToggleOff();
                            }

                            @Override
                            public void onConfirm() {
                            }
                        });
                } else {
                    toggleButt.setToggleOff();
                    showVipBindDialog(2, new GXPlayDialog.onDialogActionListner() {
                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirm() {
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putInt(RechargeFragment.VIP, 3);
                            DetailFragmentsActivity.launch(context, bundle, RechargeFragment.newInstance());
                        }
                    });
                }

            }
        });
    }

    /**
     * 提交，添加账户
     */
    private void addGameAccount(final int game_id, final int channel_id, final String game_account) {
        if (gid == channel_id) {
            showVipBindDialog(0, new GXPlayDialog.onDialogActionListner() {
                @Override
                public void onCancel() {
                    dialog.dismiss();
                }

                @Override
                public void onConfirm() {
                    dialog.dismiss();
                    doAddGameAccount(game_id, channel_id, game_account);
                }
            });
        } else {
            doAddGameAccount(game_id, channel_id, game_account);
        }
    }

    private void doAddGameAccount(int game_id, int channel_id, String game_account) {
        Flowable<HttpResultModel<LogoutResults>> fr = DataService.addGameAccount(new AddGameAccountRequestBody(game_id, 1, channel_id, game_account, toggleButt.isToggleOn()));
        RxLoadingUtils.subscribeWithDialog(context, fr, bindToLifecycle(), new Consumer<HttpResultModel<LogoutResults>>() {
            @Override
            public void accept(HttpResultModel<LogoutResults> recommendResultsHttpResultModel) throws Exception {
                if (recommendResultsHttpResultModel.isSucceful()) {
                    Toast.makeText(AddAccountActivity.this, "添加账户成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddAccountActivity.this, "添加账户失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
//                showError(netError);
            }
        });
    }

    public String getAccountname() {
        return accountEdit.getText().toString().trim();
    }

    public String getGameName() {
        return gameEdit.getText().toString().trim();
    }

    public String getChannelName() {
        return channelEdit.getText().toString().trim();
    }

    //选择游戏
    public void onGameSelected(String name, int game_id) {
        if (null != mGameWindow && mGameWindow.isShowing()) {
            mGameWindow.dismiss();
        }
        gameEdit.setText(name);
        gameEdit.setSelection(getGameName().length());
        this.mGameId = String.valueOf(game_id);
    }

    //选择平台
    public void onChannelSelected(String name, int channel_id) {
        if (null != mChannelWindow && mChannelWindow.isShowing()) {
            mChannelWindow.dismiss();
        }
        channelEdit.setText(name);
        channelEdit.setSelection(getChannelName().length());
        this.mChannelId = String.valueOf(channel_id);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_account;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.action_bar_back:
                onBackPressed();
                break;
            case R.id.selectGame:
//                if(canEdit){
                if (!TextUtils.isEmpty(getGameName())) {
                    mGameWindow.init(getGameName());
                    mGameWindow.showAsDropDown(selectGame);
                }
//                }

                break;
            case R.id.selectChannel:
//                if(canEdit){
                if (!TextUtils.isEmpty(getGameName())) {
                    mChannelWindow.init(Integer.parseInt(mGameId));
                    mChannelWindow.showAsDropDown(selectChannel);
                }
//                }
                break;
            case R.id.submitTv:
                if (TextUtils.isEmpty(getAccountname())) {
                    Toast.makeText(AddAccountActivity.this, "请填写账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getGameName())) {
                    Toast.makeText(AddAccountActivity.this, "请填写游戏名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getChannelName())) {
                    Toast.makeText(AddAccountActivity.this, "请填写平台名称", Toast.LENGTH_SHORT).show();
                    return;
                }

                addGameAccount(Integer.parseInt(mGameId), Integer.parseInt(mChannelId), getAccountname());

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (null != mGameWindow && mGameWindow.isShowing()) {
            mGameWindow.dismiss();
            return;
        }
        if (null != mChannelWindow && mChannelWindow.isShowing()) {
            mChannelWindow.dismiss();
            return;
        }

        super.onBackPressed();

    }

    /**
     * type
     * 0：綁定VIP帳號
     * 1：綁定当乐帳號
     * 2:VIP数量为0
     */
    private void showVipBindDialog(int type, GXPlayDialog.onDialogActionListner listner) {
        String content = "", title = "";
        if (type == 0) {
            title = "帐户绑定提示窗";
            content = "当乐平台帐号必须绑定的是乐号，请谨慎操作！您确定绑定该账号吗？";
        } else if (type == 1) {
            title = "绑定VIP的提示框";
            content = "您将占用一个VIP账号名额，确定此操作吗？";
        } else if (type == 2) {
            title = "绑定VIP的提示框";
            content = "VIP会员剩余名额为0，请升级会员。";
        }
        dialog = null;
        dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Full_Confirm, title, content);
        dialog.addOnDialogActionListner(listner);
        dialog.show(getSupportFragmentManager(), GXPlayDialog.TAG);
    }
}
