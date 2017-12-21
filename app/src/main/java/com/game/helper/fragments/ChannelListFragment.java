package com.game.helper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.adapters.ChannelListItemAdapter;
import com.game.helper.adapters.SpecialMoreItemAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameListResultModel;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.GamePackageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/21.
 */

public class ChannelListFragment extends XBaseFragment {
    public static final String TAG = SpecialDetailFragment.class.getSimpleName();
    private int gameId;

    public static ChannelListFragment newInstance() {
        return new ChannelListFragment();
    }

    @BindView(R.id.action_bar_back)
    LinearLayout actionBarBack;
    @BindView(R.id.action_bar_tittle)
    TextView actionBarTittle;
    @BindView(R.id.xrcl_channel_list)
    XRecyclerContentLayout xrclChannelList;
    Unbinder unbinder;
    private ChannelListItemAdapter mAdapter;
    private StateView errorView;
    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        gameId = arguments.getInt("gameId");
        initAdapter();
        errorView.setLoadDataType(StateView.REFRESH, 1);
        loadAdapterData(1);
    }

    private void initAdapter() {
        actionBarTittle.setText("渠道列表");
        xrclChannelList.getRecyclerView().verticalLayoutManager(context);
        if (mAdapter == null) {
            mAdapter = new ChannelListItemAdapter(context);
            /*mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, SpecialMoreItemAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, SpecialMoreItemAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    Bundle bundle = new Bundle();
                    bundle.putString("imagerurl", ((SpecialResults.SpecialItem) model).image);
                    bundle.putString("content", ((SpecialResults.SpecialItem) model).content);
                    bundle.putString("name", ((SpecialResults.SpecialItem) model).name);
                    bundle.putInt("id", ((SpecialResults.SpecialItem) model).id);
                    DetailFragmentsActivity.launch(context, bundle, SpecialDetailFragment.newInstance());
                }
            });*/
            mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, ChannelListItemAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, ChannelListItemAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    //游戏详情
                }
            });
        }
        xrclChannelList.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                errorView.setLoadDataType(StateView.REFRESH, 1);
                loadAdapterData(1);
            }

            @Override
            public void onLoadMore(int page) {
                errorView.setLoadDataType(StateView.REFRESH, page);
                loadAdapterData(page);
            }
        });
        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(xrclChannelList.getRecyclerView().getOnRefreshAndLoadMoreListener());
        }

        xrclChannelList.errorView(errorView);
        xrclChannelList.getRecyclerView().useDefLoadMoreView();
        xrclChannelList.loadingView(View.inflate(context, R.layout.view_loading, null));
        xrclChannelList.showLoading();
    }

    private void loadAdapterData(int page) {
        Flowable<HttpResultModel<GameListResultModel>> fr = DataService.getGamePackageList(new GamePackageRequestBody(page, gameId, 1));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GameListResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameListResultModel> specialResultsHttpResultModel) throws Exception {

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {

            }
        });
        RxLoadingUtils.subscribe(fr, this.bindToLifecycle(), new Consumer<HttpResultModel<GameListResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameListResultModel> gameListResultModelHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(gameListResultModelHttpResultModel.data.getList());
                showData(gameListResultModelHttpResultModel.current_page, gameListResultModelHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
    }

    public void showError(NetError error) {
        xrclChannelList.getLoadingView().setVisibility(View.GONE);
        xrclChannelList.refreshState(false);
        xrclChannelList.showError();
    }

    public void showData(int cur_page, int total_page, List model) {
        //mXRv.getLoadingView().setVisibility(View.GONE);
        if (model.size() < 1 || model == null) {
            xrclChannelList.showEmpty();

        } else {
            //mXRv.showContent();
            if (cur_page > 1) {
                mAdapter.addData(model);
            } else {
                mAdapter.setData(model);
            }
            xrclChannelList.getRecyclerView().setPage(cur_page, total_page);
            //mXRv.getSwipeRefreshLayout().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_channel_list;
    }

    @Override
    public Object newP() {
        return null;
    }


    @OnClick(R.id.action_bar_back)
    public void onActionBarBackClicked() {
        getActivity().finish();
    }

}
