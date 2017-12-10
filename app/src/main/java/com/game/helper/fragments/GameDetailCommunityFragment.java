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
public class GameDetailCommunityFragment extends XBaseFragment {
    private static final String TAG = GameDetailCommunityFragment.class.getSimpleName();

    @BindView(R.id.common_recycler_view_layout)
    RecyclerView mGiftList;

    public static GameDetailCommunityFragment newInstance() {
        GameDetailCommunityFragment fragment = new GameDetailCommunityFragment();
        return fragment;
    }

    public GameDetailCommunityFragment() {
        // Required empty public constructor
    }

    private void setData() {

    }

    private void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mGiftList.setHasFixedSize(true);
        mGiftList.setLayoutManager(manager);
        mGiftList.setItemAnimator(new DefaultItemAnimator());
        mGiftList.setAdapter(new CommunityAdapter());

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


    public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityHolder> {


        @Override
        public CommunityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.game_communication_item_layout, null);
            CommunityHolder viewHolder = new CommunityHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CommunityHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class CommunityHolder extends RecyclerView.ViewHolder {

            public CommunityHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
