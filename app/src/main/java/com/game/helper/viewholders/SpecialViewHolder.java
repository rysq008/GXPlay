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
import com.game.helper.model.SpecialResults;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.LoadCallback;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by zr on 2017-10-13.
 */

public class SpecialViewHolder extends AbstractViewHolder<SpecialResults> {

    @BindView(R.id.special_item_tv)
    TextView textView;
    @BindView(R.id.special_item_recycle)
    RecyclerView recyclerView;

    private Context mContext;

    public SpecialViewHolder(ViewGroup parent) {
        super(parent, R.layout.activity_special_item_layout);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(final SpecialResults data) {
        ILFactory.getLoader().loadNet(mContext, "", null, new LoadCallback() {
            @Override
            public void onLoadReady(Bitmap bitmap) {
                textView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);

        ILFactory.getLoader().loadNet(mContext, "", null, new LoadCallback() {
            @Override
            public void onLoadReady(Bitmap bitmap) {
                textView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        });
        recyclerView.setAdapter(new SpecialAdapter(data));

    }

    public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.SViewHolder> {

        SpecialResults data;

        public SpecialAdapter(SpecialResults data) {
            this.data = data;
        }

        @Override
        public SViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mContext, R.layout.adapter_special_item_layout, null);
            SViewHolder viewHolder = new SViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SViewHolder holder, int position) {
//            SpecialResults.SpecialItem itemData = data.list.get(position);
//            ILFactory.getLoader().loadNet(holder.riv, Api.API_PAY_OR_IMAGE_URL.concat(itemData.image), ILoader.Options.defaultOptions());
        }

        @Override
        public int getItemCount() {
            return data.list.size() * 10;
        }

        class SViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.special_item_iv)
            RoundedImageView riv;

            public SViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    @OnClick(R.id.special_item_tv)
    public void onClick() {
    }
}
