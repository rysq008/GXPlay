package com.game.helper.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.ChannelListFragment;
import com.game.helper.model.HotResults;
import com.game.helper.net.api.Api;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xrecyclerview.XRecyclerView;
import jp.shts.android.library.TriangleLabelView;

/**
 * Created by zr on 2017-10-13.
 */

public class HotView extends LinearLayout {
    private static final String TAG = HotView.class.getSimpleName();

    @BindView(R.id.hot_item_title_tv)
    TextView textView;
    @BindView(R.id.hot_item_body_recycle)
    XRecyclerView recyclerView;

    public HotView(Context context) {
        super(context);
        setupView(context);
    }

    public HotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public HotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    public void setupView(Context context) {
        inflate(context, R.layout.activity_hot_item_layout, this);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.verticalDivider(R.color.white, R.dimen.dp_20);//设置divide
    }

    public void setData(final HotResults data) {
        recyclerView.setAdapter(new HotAdapter(data));
    }

    public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HViewHolder> {

        HotResults data;

        public HotAdapter(HotResults results) {
            data = results;
        }

        @Override
        public HViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.adapter_hot_item_layout, null);
            HViewHolder viewHolder = new HViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(HViewHolder holder, int position) {
            final HotResults.HotItem itemData = data.list.get(position);
            holder.discount.setPrimaryText(itemData.game_package.get("zhekou_shouchong").toString() + "折");
            holder.name.setText(itemData.name);
            ILFactory.getLoader().loadNet(holder.riv, Api.API_PAY_OR_IMAGE_URL.concat(itemData.logo), null);
            holder.riv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ChannelListFragment.GAME_ID, itemData.id);
                    DetailFragmentsActivity.launch(getContext(), bundle, ChannelListFragment.newInstance());
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.list.size();
        }

        class HViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tlv__discount_item_hot)
            TriangleLabelView discount;
            @BindView(R.id.hot_item_tv_name)
            TextView name;
            @BindView(R.id.hot_item_iv)
            RoundedImageView riv;

            public HViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
