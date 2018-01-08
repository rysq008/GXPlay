package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.adapters.MyAccountAdapter;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.GameDetailRechargeFragment;
import com.game.helper.fragments.login.LoginFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.net.DataService;
import com.game.helper.net.model.GameAccountRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class GameDetailMyAccountActivity extends XBaseActivity implements View.OnClickListener, MyAccountAdapter.OnItemCheckListener {

    public static final String TAG = "MyAccountActivity";
    public static final String GAME_RECHARGE_INFO = "game_recharge_info";

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.game_adapter_layout_game_detail)
    XReloadableRecyclerContentLayout xRecyclerContentLayout;
    @BindView(R.id.addAccount_game_detail)
    ImageView addAccount;


    MyAccountAdapter mAdapter;

    public int option_game_id;
    public int option_channel_id;
    private GamePackageInfoResult gameDetailInfo;
    public static boolean needClose = false;

    @Override
    protected void onResume() {
        super.onResume();
//        if (needClose) {
//            onBackPressed();
//            needClose = false;
//        }
        BusProvider.getBus().receive(MsgEvent.class).doOnNext(new Consumer<MsgEvent>() {
            @Override
            public void accept(MsgEvent msgEvent) throws Exception {
                if (msgEvent.getData() instanceof LoginFragment) {
                    if (SharedPreUtil.isLogin()) {
                        getGameAccountInfo(1, true);
                    } else {
                        onBackPressed();
                    }
                }
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();
        initAdapter();
        getGameAccountInfo(1, true);
    }

    private void initIntentData(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            gameDetailInfo = (GamePackageInfoResult) extras.getSerializable(GAME_RECHARGE_INFO);
            option_game_id = gameDetailInfo.getGame().getId();
            option_channel_id = gameDetailInfo.getChannel().getId();
        } else {
            xRecyclerContentLayout.showEmpty();
        }
    }

    private void initView() {
        mHeadTittle.setText("选择账号");
        mHeadBack.setOnClickListener(this);
        addAccount.setOnClickListener(this);
    }

    private void initAdapter() {
        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (null == mAdapter) {
            mAdapter = new MyAccountAdapter(context);
            mAdapter.addOnItemCheckListener(this);
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        xRecyclerContentLayout.refreshEnabled(false);
    }

    private void getGameAccountInfo(int page, boolean showLoading) {

        Flowable<HttpResultModel<GameAccountResultModel>> fr = DataService.getGameAccountList(new GameAccountRequestBody(page, 1, option_game_id, option_channel_id));
        RxLoadingUtils.subscribeWithReload(xRecyclerContentLayout, fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameAccountResultModel> recommendResultsHttpResultModel) throws Exception {
                List<GameAccountResultModel.ListBean> list = new ArrayList<>();
                list.addAll(recommendResultsHttpResultModel.data.getList());
                showData(recommendResultsHttpResultModel.current_page, recommendResultsHttpResultModel.total_page, list);
            }
        }, null, null, showLoading);

    }

    public void showData(int cur_page, int total_page, List model) {
        mAdapter.setData(model);
        if (mAdapter.getItemCount() < 1) {
            xRecyclerContentLayout.showEmpty();
        } else {
            xRecyclerContentLayout.showContent();
            xRecyclerContentLayout.getSwipeRefreshLayout().setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_game_detail_my_account;
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
            case R.id.addAccount_game_detail://添加账户
                Intent intent = new Intent(GameDetailMyAccountActivity.this, GameDetailAddAccountActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GameDetailAddAccountActivity.GAME_MY_ACCOUNT_INFO, gameDetailInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemCheked(GameAccountResultModel.ListBean gameBean) {
        Intent bundle = new Intent();
        bundle.putExtra(GameDetailRechargeFragment.TAG, gameBean);
        setResult(GameDetailRechargeFragment.RESULT_CODE, bundle);
        onBackPressed();
    }
}
