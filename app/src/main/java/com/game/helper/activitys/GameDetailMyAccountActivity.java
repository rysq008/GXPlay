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
import com.game.helper.fragments.GameDetailRechargeFragment;
import com.game.helper.fragments.recharge.RechargeGameFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.GameAccountRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SPUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class GameDetailMyAccountActivity extends XBaseActivity implements View.OnClickListener, MyAccountAdapter.OnItemCheckListener {

    public static final String TAG = "MyAccountActivity";

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

    @Override
    protected void onResume() {
        getGameAccountInfo(1,true);
        super.onResume();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();
        initAdapter();
    }

    private void initIntentData(Intent intent) {
        option_game_id = SPUtils.getInt(context,SPUtils.GAME_ID,0);
        option_channel_id = SPUtils.getInt(context,SPUtils.CHANNEL_ID,0);
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
    }

    private void getGameAccountInfo(int page,boolean showLoading) {
        Flowable<HttpResultModel<GameAccountResultModel>> fr = DataService.getGameAccountList(new GameAccountRequestBody(page, 1, option_game_id, option_channel_id));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameAccountResultModel> recommendResultsHttpResultModel) throws Exception {
                List<GameAccountResultModel.ListBean> list = new ArrayList<>();
                list.addAll(recommendResultsHttpResultModel.data.getList());
                showData(recommendResultsHttpResultModel.current_page, recommendResultsHttpResultModel.total_page, list);
            }
        }, null,null,showLoading);

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
        bundle.putExtra(GameDetailRechargeFragment.TAG, gameBean);
        setResult(GameDetailRechargeFragment.RESULT_CODE, bundle);
        onBackPressed();
    }
}
