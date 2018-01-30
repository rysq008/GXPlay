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
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.VipGameAccountResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SimpleTextWatcher;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.XReloadableStateContorller;
import com.game.helper.views.widget.ChannelPopupWindow;
import com.game.helper.views.widget.GamePopupWindow;
import com.game.helper.views.widget.ToggleButton;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class GameDetailAddAccountActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "GameDetailAddAccountActivity";
    public static final String GAME_MY_ACCOUNT_INFO = "game_my_account_info";


    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.accountEdit_add_account_game_detail)
    EditText accountEdit;
    @BindView(R.id.gameEdit_add_account_game_detail)
    EditText gameEdit;
    @BindView(R.id.selectGame_add_account_game_detail)
    TextView selectGame;
    @BindView(R.id.channelEdit_add_account_game_detail)
    EditText channelEdit;
    @BindView(R.id.selectChannel_add_account_game_detail)
    TextView selectChannel;
    @BindView(R.id.submitTv_add_account_game_detail)
    TextView submitTv;
    @BindView(R.id.xreload_add_account_game_detail)
    XReloadableStateContorller xreload;
    @BindView(R.id.tb_add_account_toggle_game_detail)
    ToggleButton toggleButt;
    @BindView(R.id.add_account_tips_tv_game_detail)
    TextView tipsTv;

    private int mGameId = 16;
    private int mChannelId;
    private boolean canEdit = true;

    private GamePopupWindow mGameWindow;
    private ChannelPopupWindow mChannelWindow;
    private GXPlayDialog dialog;
    private int mAccountNum = 0;
    private int gid = 1;//当乐端ID号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();

        mGameWindow = new GamePopupWindow(GameDetailAddAccountActivity.this);
        mChannelWindow = new ChannelPopupWindow(GameDetailAddAccountActivity.this);
    }

    private void initIntentData(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            xreload.showContent();
            GamePackageInfoResult gameDetailInfo = (GamePackageInfoResult) extras.getSerializable(GAME_MY_ACCOUNT_INFO);
            mGameId = gameDetailInfo.getGame().getId();
            mChannelId = gameDetailInfo.getChannel().getId();
            gameEdit.setText(gameDetailInfo.getGame().getName());
            channelEdit.setText(gameDetailInfo.getChannel().getName());
        } else {
            xreload.showEmpty();
        }

//        if(!TextUtils.isEmpty(mGameId) && !TextUtils.isEmpty(mChannelId)){
//            canEdit = false;
//        }else{
//            canEdit = true;
//        }

    }

    private void initView() {
        mHeadTittle.setText("账户绑定");
        gameEdit.setEnabled(false);
        channelEdit.setEnabled(false);
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
                    Toast.makeText(GameDetailAddAccountActivity.this, "添加账户成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(GameDetailAddAccountActivity.this, "添加账户失败", Toast.LENGTH_SHORT).show();
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
        this.mGameId = game_id;
        Toast.makeText(GameDetailAddAccountActivity.this, "游戏Id：" + game_id, Toast.LENGTH_SHORT).show();
    }

    //选择平台
    public void onChannelSelected(String name, int channel_id) {
        if (null != mChannelWindow && mChannelWindow.isShowing()) {
            mChannelWindow.dismiss();
        }
        channelEdit.setText(name);
        channelEdit.setSelection(getChannelName().length());
        this.mChannelId = channel_id;
        Toast.makeText(GameDetailAddAccountActivity.this, "平台Id：" + channel_id, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_account_game_detail;
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
           /* case R.id.selectGame_add_account_game_detail:
//                if(canEdit){
                if (!TextUtils.isEmpty(getGameName())) {
                    mGameWindow.init(getGameName());
                    mGameWindow.showAsDropDown(selectGame);
                }
//                }

                break;
            case R.id.selectChannel_add_account_game_detail:
//                if(canEdit){
                if (!TextUtils.isEmpty(getGameName())) {
                    mChannelWindow.init(Integer.parseInt(mGameId));
                    mChannelWindow.showAsDropDown(selectChannel);
                }
//                }
                break;*/
            case R.id.submitTv_add_account_game_detail:
                if (TextUtils.isEmpty(getAccountname())) {
                    Toast.makeText(GameDetailAddAccountActivity.this, "请填写账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getGameName())) {
                    Toast.makeText(GameDetailAddAccountActivity.this, "请填写游戏名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getChannelName())) {
                    Toast.makeText(GameDetailAddAccountActivity.this, "请填写平台名称", Toast.LENGTH_SHORT).show();
                    return;
                }

                addGameAccount(mGameId, mChannelId, getAccountname());

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
