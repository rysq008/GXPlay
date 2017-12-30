package com.game.helper.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.HotResults;
import com.game.helper.net.api.Api;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.imageloader.LoadCallback;
import jp.shts.android.library.TriangleLabelView;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by zr on 2017-10-13.
 */

public class HotViewHolder extends AbstractViewHolder<HotResults> {

    @BindView(R.id.hot_item_title_tv)
    TextView textView;
    @BindView(R.id.hot_item_body_recycle)
    RecyclerView recyclerView;

    private Context mContext;

    public HotViewHolder(ViewGroup parent) {
        super(parent, R.layout.activity_hot_item_layout);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(final HotResults data) {
        ILFactory.getLoader().loadNet(mContext, "", null, new LoadCallback() {
            @Override
            public void onLoadReady(Bitmap bitmap) {
                textView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new HotAdapter(data));

    }

    public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HViewHolder> {

        HotResults data;

        public HotAdapter(HotResults results) {
            data = results;
        }

        @Override
        public HViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mContext, R.layout.adapter_hot_item_layout, null);
            HViewHolder viewHolder = new HViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(HViewHolder holder, int position) {
//            HotResults.HotItem itemData = data.list.get(position);
//            holder.discount.setText(itemData.game_package.get("zhekou_shouchong").toString());
//            ILFactory.getLoader().loadNet(holder.riv, Api.API_PAY_OR_IMAGE_URL.concat(itemData.logothumb), ILoader.Options.defaultOptions());
        }

        @Override
        public int getItemCount() {
            return data.list.size() * 10;
        }

        class HViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tlv__discount_item_hot)
            TriangleLabelView discount;
            @BindView(R.id.hot_item_iv)
            RoundedImageView riv;

            public HViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
