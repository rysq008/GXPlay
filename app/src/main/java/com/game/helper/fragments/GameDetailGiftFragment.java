package com.game.helper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameGiftListResult;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.GameInfoGiftListRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameDetailGiftFragment extends XBaseFragment {
    private static final String TAG = GameDetailGiftFragment.class.getSimpleName();

    @BindView(R.id.gift_game_detail_recycler_view_layout)
    XRecyclerContentLayout xRecyclerContentLayout;
    private GiftAdapter mAdapter;

    public static GameDetailGiftFragment newInstance() {
        GameDetailGiftFragment fragment = new GameDetailGiftFragment();
        return fragment;
    }

    private void initAdapter() {
        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (mAdapter == null) {
            mAdapter = new GiftAdapter(context);
            mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, GiftAdapter.GiftHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, GiftAdapter.GiftHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                }
            });
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        /*xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
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
        });*/

    }

    private void loadAdapterData(int page, int gameId,boolean showLoading) {
        Flowable<HttpResultModel<GameGiftListResult>> fr = DataService.getGameGiftList(new GameInfoGiftListRequestBody(page, gameId, 1));
        RxLoadingUtils.subscribe(fr, this.bindToLifecycle(), new Consumer<HttpResultModel<GameGiftListResult>>() {
            @Override
            public void accept(HttpResultModel<GameGiftListResult> gameListResultModelHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(gameListResultModelHttpResultModel.data.getList());
                showData(gameListResultModelHttpResultModel.current_page, gameListResultModelHttpResultModel.total_page, list);
            }
        }, null,null,showLoading);
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
    public void initData(Bundle savedInstanceState) {
        Log.d(TAG, "----------------======================2");
        initAdapter();
        Bundle arguments = getArguments();
        if (arguments != null) {
            int gameId = arguments.getInt("gameId");
            loadAdapterData(1, gameId,true);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_game_detail;
    }

    @Override
    public Object newP() {
        return null;
    }


    public class GiftAdapter extends SimpleRecAdapter<ItemType, GiftAdapter.GiftHolder> {




        public GiftAdapter(Context context) {
            super(context);
        }

        @Override
        public GiftHolder newViewHolder(View itemView) {
            return new GiftHolder(itemView);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_game_detail_gift;
        }

        @Override
        public void onBindViewHolder(GiftHolder holder, int position) {
            final ItemType item = data.get(position);
            final GameGiftListResult.ListBean itemDate = (GameGiftListResult.ListBean) data.get(position);
            ILFactory.getLoader().loadNet(holder.ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(itemDate.getGame().getLogo()), ILoader.Options.defaultOptions());
            holder.tvName.setText(itemDate.getGiftname());
            holder.tvContent.setText(itemDate.getGift_content());
            holder.tvSurplus.setText(String.valueOf(itemDate.getRemain_num()));
            //holder.ivntro.setText(itemDate.intro);
            //holder.gameId = itemDate.id;
            holder.tvGameGiftGain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Bundle bundle = new Bundle();
                    bundle.putInt("gamepackeId", itemDate.getId());
                    bundle.putInt("gameId", itemDate.getGame().getId());
                    DetailFragmentsActivity.launch(context, bundle, GameDetailFragment.newInstance());*/
                }
            });
        }


        class GiftHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_game_gift_logothumb)
            ImageView ivLogothumb;
            @BindView(R.id.tv_game_gift_name)
            TextView tvName;
            @BindView(R.id.tv_game_gift_surplus)
            TextView tvSurplus;
            @BindView(R.id.tv_game_gift_content)
            TextView tvContent;
            @BindView(R.id.tv_game_gift_gain)
            ImageView tvGameGiftGain;

            public GiftHolder(View itemView) {
                super(itemView);
                KnifeKit.bind(this,itemView);
            }

        }

    }
}
