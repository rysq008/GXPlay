package com.game.helper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameCommentListResult;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.GameInfoCommentListRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.CircleImageView;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;


public class GameDetailCommunityFragment extends XBaseFragment {
    private static final String TAG = GameDetailCommunityFragment.class.getSimpleName();

    @BindView(R.id.common_recycler_view_layout)
    XReloadableRecyclerContentLayout xRecyclerContentLayout;
    private CommunityAdapter mAdapter;
    private int gameId;

    public static GameDetailCommunityFragment newInstance() {
        GameDetailCommunityFragment fragment = new GameDetailCommunityFragment();
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Log.d(TAG, "----------------==================3");
        initAdapter();
        Bundle arguments = getArguments();
        if (arguments != null) {
            gameId = arguments.getInt("gameId");
            if (gameId > 0) {
                loadAdapterData(1, gameId, true);
            } else {
                xRecyclerContentLayout.showEmpty();
            }
        }
    }

    private void initAdapter() {
        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (mAdapter == null) {
            mAdapter = new CommunityAdapter(context);
            mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, CommunityAdapter.CommunityHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, CommunityAdapter.CommunityHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                }
            });
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadAdapterData(1, gameId, false);
            }

            @Override
            public void onLoadMore(int page) {
                loadAdapterData(page, gameId, false);
            }
        });
    }

    public void loadAdapterData(int page, int gameId, boolean showLoading) {
        Flowable<HttpResultModel<GameCommentListResult>> fr = DataService.getGameCommentList(new GameInfoCommentListRequestBody(page, gameId));
        RxLoadingUtils.subscribeWithReload(xRecyclerContentLayout, fr, this.bindToLifecycle(), new Consumer<HttpResultModel<GameCommentListResult>>() {
            @Override
            public void accept(HttpResultModel<GameCommentListResult> gameListResultModelHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(gameListResultModelHttpResultModel.data.getList());
                showData(gameListResultModelHttpResultModel.current_page, gameListResultModelHttpResultModel.total_page, list);
            }
        }, null, null, showLoading);
    }

    public void showData(int cur_page, int total_page, List model) {
        if (model.size() < 1 || model == null) {
            xRecyclerContentLayout.showEmpty();
        } else {
            if (cur_page > 1) {
                mAdapter.addData(model);
            } else {
                mAdapter.setData(model);
            }
            xRecyclerContentLayout.getRecyclerView().setPage(cur_page, total_page);
        }
    }





    @Override
    public int getLayoutId() {
        return R.layout.common_game_detail_recycler_layout;
    }

    @Override
    public Object newP() {
        return null;
    }


    public class CommunityAdapter extends SimpleRecAdapter<ItemType, CommunityAdapter.CommunityHolder> {


        public CommunityAdapter(Context context) {
            super(context);
        }

        @Override
        public CommunityHolder newViewHolder(View itemView) {
            return new CommunityHolder(itemView);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_game_communication_layout;
        }

        @Override
        public void onBindViewHolder(CommunityHolder holder, int position) {
            final ItemType item = data.get(position);
            final GameCommentListResult.ListBean itemDate = (GameCommentListResult.ListBean) data.get(position);
            if (itemDate.getMember().getIcon().length() > 0) {
                ILFactory.getLoader().loadNet(holder.ivAvatar, Api.API_BASE_URL.concat(itemDate.getMember().getIcon_thumb()), ILoader.Options.defaultOptions());
            }else{
                holder.ivAvatar.setImageResource(R.mipmap.ic_default_avatar_circle);
            }
            holder.tvMemberName.setText(itemDate.getMember().getNick_name());
            holder.tvContent.setText(itemDate.getContent());
            holder.tvTime.setText(String.valueOf(itemDate.getCreate_time()));


        }

        class CommunityHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_avatar_game_communication)
            CircleImageView ivAvatar;
            @BindView(R.id.tv_member_name_game_communication)
            TextView tvMemberName;
            @BindView(R.id.tv_content_game_communication)
            TextView tvContent;
            @BindView(R.id.tv_time_game_communication)
            TextView tvTime;

            public CommunityHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    /*@Override
    public void onPause() {
        super.onPause();
        if (errorView != null) {
            xRecyclerContentLayout.removeView(errorView);
            xRecyclerContentLayout.removeView(loadView);
        }
    }*/


}
