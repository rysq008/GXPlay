package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.adapters.AccountManagerAdapter;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.GameAccountRequestBody;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class AccountManageFragment extends XBaseFragment implements View.OnClickListener, AccountManagerAdapter.OnActionListener {
    private static final String TAG = AccountManageFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_account_list)
    XReloadableRecyclerContentLayout mContent;

    private AccountManagerAdapter mAdapter;

    public static AccountManageFragment newInstance() {
        return new AccountManageFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_account_mannager;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_account_mannager));
        mHeadBack.setOnClickListener(this);

        mContent.showLoading();
        initList();
        getDataFromNet(1,true);
        BusProvider.getBus().receive(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
            @Override
            public void accept(MsgEvent msgEvent) throws Exception {
                if (msgEvent.getData() instanceof GameAccountResultModel.ListBean) {
                    mAdapter.setData((GameAccountResultModel.ListBean) msgEvent.getData());
                }
            }
        });
    }

    /**
     * 获取数据
     */
    private void getDataFromNet(int page, boolean isrefresh) {
        getGameAccountInfo(page, isrefresh);
    }

    private void initList() {
        mAdapter = null;
        mAdapter = new AccountManagerAdapter(getContext(), null);
        mAdapter.addOnActionListener(this);
        mContent.getRecyclerView().setHasFixedSize(true);
        mContent.getRecyclerView().verticalLayoutManager(context);
        mContent.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mContent.getRecyclerView().setAdapter(mAdapter);
        mContent.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(1, true);
            }

            @Override
            public void onLoadMore(int page) {
                getDataFromNet(page, false);
            }
        });
        mContent.getRecyclerView().useDefLoadMoreView();
    }

    private void getGameAccountInfo(final int page, boolean isrefresh) {
        Flowable<HttpResultModel<GameAccountResultModel>> fr = DataService.getGameAccountList(new GameAccountRequestBody(page, 1, 0, 0));
        RxLoadingUtils.subscribeWithReload(mContent, fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameAccountResultModel> gameAccountResultModelHttpResultModel) throws Exception {
                notifyData(gameAccountResultModelHttpResultModel.data, page);
                mContent.getRecyclerView().setPage(gameAccountResultModelHttpResultModel.current_page, gameAccountResultModelHttpResultModel.total_page);
            }
        }, null, null, isrefresh);
    }

    private void deleteGameAccountInfo(int id) {
        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.deleteGameAccount(new SingleGameIdRequestBody(id));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
            @Override
            public void accept(HttpResultModel<NotConcernResults> notConcernResultsHttpResultModel) throws Exception {
                Toast.makeText(getContext(), notConcernResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (notConcernResultsHttpResultModel.isSucceful()) getGameAccountInfo(1, true);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notifyData(XBaseModel data, int page) {
        mAdapter.setData(data, page == 1 ? true : false);
        if (mAdapter.getItemCount() < 1) {
            mContent.showEmpty();
            return;
        } else {
            mContent.showContent();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onDelete(final int gameid) {
        final GXPlayDialog dialog = new GXPlayDialog(GXPlayDialog.Ddialog_Without_tittle_Full_Confirm, "", "确定要删除该游戏账号吗？");
        dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm() {
                deleteGameAccountInfo(gameid);
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), GXPlayDialog.TAG);
    }
}
