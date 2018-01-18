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
import com.game.helper.model.LogoutResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SimpleTextWatcher;
import com.game.helper.views.widget.ChannelPopupWindow;
import com.game.helper.views.widget.GamePopupWindow;

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
    @BindView(R.id.submitTv)
    TextView submitTv;

    private String mGameId = "16";
    private String mChannelId;
    private boolean canEdit = true;

    private GamePopupWindow mGameWindow;
    private ChannelPopupWindow mChannelWindow;

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

    }

    /**
     * 提交，添加账户
     */
    private void addGameAccount(int game_id, int channel_id, String game_account) {
        Flowable<HttpResultModel<LogoutResults>> fr = DataService.addGameAccount(new AddGameAccountRequestBody(game_id, 1, channel_id, game_account));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<LogoutResults>>() {
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
}
