package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.adapters.MyAccountAdapter;
import com.game.helper.fragments.recharge.RechargeGameFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.GameAccountRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class MyAccountActivity extends XBaseActivity implements View.OnClickListener, MyAccountAdapter.OnItemCheckListener {

    public static final String TAG = "MyAccountActivity";

    public static final String OPTION_GAME_ID = "option_game_id";
    public static final String OPTION_CHANNEL_ID = "option_channel_id";

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.game_adapter_layout)
    XRecyclerContentLayout xRecyclerContentLayout;
    @BindView(R.id.addAccount)
    ImageView addAccount;


    MyAccountAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    public int option_game_id;
    public int option_channel_id;

    @Override
    protected void onResume() {
        getGameAccountInfo(1);
        super.onResume();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();
        initAdapter();
        errorView.setLoadDataType(StateView.REFRESH, 1);
    }

    private void initIntentData(Intent intent) {
        option_game_id = intent.getIntExtra(OPTION_GAME_ID, 0);
        option_channel_id = intent.getIntExtra(OPTION_CHANNEL_ID, 0);
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
        xRecyclerContentLayout.getRecyclerView().setRefreshEnabled(false);

        if (errorView == null) {
            errorView = new StateView(context);
        }
        if (null != errorView.getParent()) {
            ((ViewGroup) errorView.getParent()).removeView(errorView);
        }
        if (loadingView == null) {
            loadingView = View.inflate(context, R.layout.view_loading, null);
        }
        if (null != loadingView.getParent()) {
            ((ViewGroup) loadingView.getParent()).removeView(loadingView);
        }
        xRecyclerContentLayout.errorView(errorView);

        xRecyclerContentLayout.loadingView(loadingView);
        xRecyclerContentLayout.showLoading();

    }

    private void getGameAccountInfo(int page) {
        Flowable<HttpResultModel<GameAccountResultModel>> fr = DataService.getGameAccountList(new GameAccountRequestBody(page, 1, option_game_id, option_channel_id));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameAccountResultModel> recommendResultsHttpResultModel) throws Exception {
                List<GameAccountResultModel.ListBean> list = new ArrayList<>();
                list.addAll(recommendResultsHttpResultModel.data.getList());
                showData(recommendResultsHttpResultModel.current_page, recommendResultsHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });

    }

    public void showData(int cur_page, int total_page, List model) {
        mAdapter.setData(model);
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        if (mAdapter.getItemCount() < 1) {
            xRecyclerContentLayout.showEmpty();
            return;
        } else {
            xRecyclerContentLayout.showContent();
            xRecyclerContentLayout.getSwipeRefreshLayout().setVisibility(View.VISIBLE);
            return;
        }
    }

    public void showError(NetError error) {
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        xRecyclerContentLayout.showError();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_account;
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
            case R.id.addAccount://添加账户
                Intent intent = new Intent(MyAccountActivity.this, AddAccountActivity.class);
                intent.putExtra(AddAccountActivity.GAME_ID,option_game_id);
                intent.putExtra(AddAccountActivity.CHANNEL_ID,option_channel_id);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemCheked(GameAccountResultModel.ListBean gameBean) {
        Intent bundle = new Intent();
        bundle.putExtra(RechargeGameFragment.TAG, gameBean);
        setResult(RechargeGameFragment.RESULT_CODE, bundle);
        onBackPressed();
    }
}
