package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.HotResults;
import com.game.helper.views.RecommendView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameDetailGiftFragment extends XBaseFragment {
    private static final String TAG = GameDetailGiftFragment.class.getSimpleName();

    @BindView(R.id.common_recycler_view_layout)
    RecyclerView mGiftList;

    public static GameDetailGiftFragment newInstance() {
        GameDetailGiftFragment fragment = new GameDetailGiftFragment();
        return fragment;
    }

    private void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mGiftList.setHasFixedSize(true);
        mGiftList.setLayoutManager(manager);
        mGiftList.setItemAnimator(new DefaultItemAnimator());
        mGiftList.setAdapter(new GiftAdapter());

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_game_detail_recycler_layout;
    }

    @Override
    public Object newP() {
        return null;
    }


    public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftHolder> {

        HotResults data;


        @Override
        public GiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = View.inflate(getContext(), R.layout.activity_recommend_item_layout, null);
            RecommendView recommendView = new RecommendView(context);
            GiftHolder viewHolder = new GiftHolder(recommendView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(GiftHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class GiftHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.recommend_item_discount_tv)
            TextView discount;
            @BindView(R.id.recommend_item_name_tv)
            TextView name;
            @BindView(R.id.recommend_item_icon_iv)
            RoundedImageView riv;

            public GiftHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
