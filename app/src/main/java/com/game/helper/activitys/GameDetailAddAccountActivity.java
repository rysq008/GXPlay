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
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.model.LogoutResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SimpleTextWatcher;
import com.game.helper.views.XReloadableStateContorller;
import com.game.helper.views.widget.ChannelPopupWindow;
import com.game.helper.views.widget.GamePopupWindow;

import butterknife.BindView;
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

    private int mGameId = 16;
    private int mChannelId;
    private boolean canEdit = true;

    private GamePopupWindow mGameWindow;
    private ChannelPopupWindow mChannelWindow;

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
        if(extras != null){
            xreload.showContent();
            GamePackageInfoResult gameDetailInfo = (GamePackageInfoResult) extras.getSerializable(GAME_MY_ACCOUNT_INFO);
            mGameId = gameDetailInfo.getGame().getId();
            mChannelId = gameDetailInfo.getChannel().getId();
            gameEdit.setText(gameDetailInfo.getGame().getName());
            channelEdit.setText(gameDetailInfo.getChannel().getName());
        }else{
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

    }

    /**
     * 提交，添加账户
     */
    private void addGameAccount(int game_id, int channel_id, String game_account,Boolean showLoading) {
        Flowable<HttpResultModel<LogoutResults>> fr = DataService.addGameAccount(new AddGameAccountRequestBody(game_id, 1, channel_id, game_account));
        RxLoadingUtils.subscribeWithReload(xreload,fr, bindToLifecycle(), new Consumer<HttpResultModel<LogoutResults>>() {
            @Override
            public void accept(HttpResultModel<LogoutResults> recommendResultsHttpResultModel) throws Exception {
                if (recommendResultsHttpResultModel.isSucceful()) {
                    Toast.makeText(GameDetailAddAccountActivity.this, "添加账户成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(GameDetailAddAccountActivity.this, "添加账户失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, null,null,showLoading);

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

                addGameAccount(mGameId, mChannelId, getAccountname(),true);

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
}
