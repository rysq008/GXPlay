package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.adapters.AvailableRedpackAdapter;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class ChoiceRedPackActivity extends XBaseActivity implements View.OnClickListener, AvailableRedpackAdapter.OnItemCheckListener {

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.game_adapter_layout)
    XReloadableRecyclerContentLayout XReloadableRecyclerContentLayout;

    AvailableRedpackAdapter mAdapter;

    private int option_game_id;
    private String totalMoney;
    private AvailableRedpackResultModel.ListBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getIntentData(getIntent());
        initListeners();
        initView();
        fetchAvailableRedpackInfo(1);
    }

    /**
     * 获取可用红包/卡券
     */
    private void fetchAvailableRedpackInfo(int page) {
        Flowable<HttpResultModel<AvailableRedpackResultModel>> flowable = DataService.getRedPackInfo(new AvailableRedpackRequestBody(page, option_game_id, totalMoney));
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<AvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<AvailableRedpackResultModel> generalizeResultsHttpResultModel) throws Exception {
                if (generalizeResultsHttpResultModel.isSucceful()) {
                    showData(generalizeResultsHttpResultModel.current_page, generalizeResultsHttpResultModel.total_page, generalizeResultsHttpResultModel.data.getList());
                }
//                else{
//                    showError(new NetError("",5));
//                }

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
    }

    public void showData(int cur_page, int total_page, List model) {
        if (cur_page > 1) {
            mAdapter.addData(model);
        } else {
            mAdapter.setData(model);
        }
        XReloadableRecyclerContentLayout.getRecyclerView().setPage(cur_page, total_page);
        if (mAdapter.getItemCount() < 1) {
            XReloadableRecyclerContentLayout.showEmpty();
            return;
        } else {
            XReloadableRecyclerContentLayout.showContent();
        }
    }


    public void showError(NetError error) {
        XReloadableRecyclerContentLayout.refreshState(false);
    }

    private void initView() {
        mHeadTittle.setText("可使用卡券");

        XReloadableRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (null == mAdapter) {
            mAdapter = new AvailableRedpackAdapter(context, bean);
            mAdapter.addOnItemCheckListener(this);
        }
        XReloadableRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        XReloadableRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                fetchAvailableRedpackInfo(1);
            }

            @Override
            public void onLoadMore(int page) {
                fetchAvailableRedpackInfo(page);
            }
        });

        XReloadableRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
        XReloadableRecyclerContentLayout.showLoading();
    }

    private void initListeners() {
        mHeadBack.setOnClickListener(this);
    }

    private void getIntentData(Intent intent) {
        option_game_id = intent.getIntExtra(OrderConfirmActivity.OPTION_GAME_ID, 0);
        totalMoney = intent.getStringExtra(OrderConfirmActivity.RED_PACK_LIMIT);
        bean = (AvailableRedpackResultModel.ListBean) intent.getBundleExtra(OrderConfirmActivity.RED_PACK_BEAN).getSerializable(OrderConfirmActivity.RED_PACK_BEAN);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choice_red_pack;
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
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        AvailableRedpackResultModel.ListBean bean = mAdapter.getRecordAccount();
        super.onBackPressed();
    }

    @Override
    public void onItemCheked(AvailableRedpackResultModel.ListBean bean) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        if (bean.isSelect()) {
            bundle.putSerializable(OrderConfirmActivity.RED_PACK_BEAN, bean);
        } else {
            bundle.putSerializable(OrderConfirmActivity.RED_PACK_BEAN, null);
        }
        intent.putExtra(OrderConfirmActivity.RED_PACK_BEAN, bundle);

//        if (null != bean) {
//            intent.putExtra(OrderConfirmActivity.RED_PACK_AMOUNT, bean.getAmount());
//            if (1 == bean.getKind()) {//单发
//                intent.putExtra(OrderConfirmActivity.RED_PACK_TYPE, "1");
//                intent.putExtra(OrderConfirmActivity.RED_PACK_ID, bean.getMy_red_id() + "");
//            } else if (2 == bean.getKind()) {//群发
//                intent.putExtra(OrderConfirmActivity.RED_PACK_TYPE, "2");
//                intent.putExtra(OrderConfirmActivity.RED_PACK_ID, bean.getRed_id() + "");
//            } else {
//                intent.putExtra(OrderConfirmActivity.RED_PACK_TYPE, "0");
//                intent.putExtra(OrderConfirmActivity.RED_PACK_ID, "");
//            }
//        } else {
//            intent.putExtra(OrderConfirmActivity.RED_PACK_AMOUNT, "0.0");
//            intent.putExtra(OrderConfirmActivity.RED_PACK_TYPE, "0");
//            intent.putExtra(OrderConfirmActivity.RED_PACK_ID, "");
//        }
        setResult(RESULT_OK, intent);
        onBackPressed();
    }
}
