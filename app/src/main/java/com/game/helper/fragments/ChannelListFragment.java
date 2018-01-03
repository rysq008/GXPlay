package com.game.helper.fragments;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.adapters.ChannelListItemAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GamePackageListResult;
import com.game.helper.net.DataService;
import com.game.helper.net.model.GamePackageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/21.
 */

public class ChannelListFragment extends XBaseFragment {
    public static final String TAG = SpecialDetailFragment.class.getSimpleName();
    public static final String GAME_ID = "gameId";
    private int gameId;

    public static ChannelListFragment newInstance() {
        return new ChannelListFragment();
    }

    @BindView(R.id.action_bar_back)
    LinearLayout actionBarBack;
    @BindView(R.id.action_bar_tittle)
    TextView actionBarTittle;
    @BindView(R.id.xrcl_channel_list)
    XReloadableRecyclerContentLayout xrclChannelList;
    private ChannelListItemAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        gameId = arguments.getInt(GAME_ID);
        initAdapter();
        loadAdapterData(1, true);
    }

    private void initAdapter() {
        actionBarTittle.setText("渠道列表");
        xrclChannelList.getRecyclerView().verticalLayoutManager(context);
        if (mAdapter == null) {
            mAdapter = new ChannelListItemAdapter(context, getRxPermissions());
            mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, ChannelListItemAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, ChannelListItemAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    //游戏详情

                }
            });
        }
        xrclChannelList.getRecyclerView().setAdapter(mAdapter);
        xrclChannelList.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadAdapterData(1, false);
            }

            @Override
            public void onLoadMore(int page) {
                loadAdapterData(page, false);
            }
        });
    }

    private void loadAdapterData(int page, boolean showLoading) {
        Flowable<HttpResultModel<GamePackageListResult>> fr = DataService.getGamePackageList(new GamePackageRequestBody(page, gameId, 1));
        RxLoadingUtils.subscribeWithReload(xrclChannelList, fr, this.bindToLifecycle(), new Consumer<HttpResultModel<GamePackageListResult>>() {
            @Override
            public void accept(HttpResultModel<GamePackageListResult> gameListResultModelHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(gameListResultModelHttpResultModel.data.getList());
                showData(gameListResultModelHttpResultModel.current_page, gameListResultModelHttpResultModel.total_page, list);
            }
        }, null, null, showLoading);
    }

    public void showData(int cur_page, int total_page, List model) {
        if (model.size() < 1 || model == null) {
            xrclChannelList.showEmpty();

        } else {
            if (cur_page > 1) {
                mAdapter.addData(model);
            } else {
                mAdapter.setData(model);
            }
            xrclChannelList.getRecyclerView().setPage(cur_page, total_page);
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
