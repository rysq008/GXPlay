package com.game.helper.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.adapters.SearchListAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.HotResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by zr on 2017-10-13.
 */

public class GameListFragment extends XBaseFragment {

    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.game_list_layout)
    XReloadableRecyclerContentLayout xRecyclerContentLayout;
    @BindView(R.id.game_list_move_top_iv)
    ImageView ivMoveTop;

    SearchListAdapter mAdapter;
    int classical_type = 0, common_type = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classical_type = getArguments().getInt("classical", 0);
        common_type = getArguments().getInt("common", 0);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        actionBar.setVisibility((classical_type == 0 && common_type == 0) ? View.VISIBLE : View.GONE);
        if (actionBar.getVisibility() == View.VISIBLE) {
            mHeadTittle.setText("热门推荐");
            mHeadBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
        initAdapter();
        loadGmaeAdapterData(true, 1, classical_type, common_type);
    }

    private void initAdapter() {
        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (null == mAdapter) {
            mAdapter = new SearchListAdapter(context);
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        xRecyclerContentLayout.getRecyclerView().setVerticalScrollBarEnabled(false);
        xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadGmaeAdapterData(false, 1, classical_type, common_type);
            }

            @Override
            public void onLoadMore(int page) {
                loadGmaeAdapterData(false, page, classical_type, common_type);
            }
        });

        xRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
//        xRecyclerContentLayout.showLoading();

    }

    public void showError(NetError error) {
        xRecyclerContentLayout.refreshState(false);
    }

    public void showData(int cur_page, int total_page, List model) {
        if (cur_page > 1) {
            mAdapter.addData(model);
        } else {
            mAdapter.setData(model);
        }
        xRecyclerContentLayout.getRecyclerView().setPage(cur_page, total_page);
        if (mAdapter.getItemCount() < 1) {
            xRecyclerContentLayout.showEmpty();
        } else {
            xRecyclerContentLayout.showContent();
        }
    }

    public void loadGmaeAdapterData(boolean showloading, int page, int class_type_id, int type_id) {
        Flowable<HttpResultModel<HotResults>> fr = DataService.getHomeHot(new RecommendRequestBody(page, class_type_id, type_id));
        RxLoadingUtils.subscribeWithReload(xRecyclerContentLayout, fr, bindToLifecycle(), new Consumer<HttpResultModel<com.game.helper.model.HotResults>>() {
            @Override
            public void accept(HttpResultModel<HotResults> hotResultsHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(hotResultsHttpResultModel.data.list);
                showData(hotResultsHttpResultModel.current_page, hotResultsHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        }, null, showloading);
    }

    public static GameListFragment newInstance(int classical_type, int common_type) {
        GameListFragment fragment = new GameListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("classical", classical_type);
        bundle.putInt("common", common_type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_list_layout;
    }

    @Override
    public Object newP() {
        return null;
    }

    @OnClick(R.id.game_list_move_top_iv)
    public void OnClick() {
        if (mAdapter.getItemCount() > 0) {
            xRecyclerContentLayout.getRecyclerView().smoothScrollToPosition(0);
        }
    }

}
