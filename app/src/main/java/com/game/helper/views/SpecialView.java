package com.game.helper.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.SpecialDetailFragment;
import com.game.helper.fragments.SpecialMoreFragment;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.api.Api;
import com.leochuan.CenterScrollListener;
import com.leochuan.ScaleLayoutManager;
import com.leochuan.ViewPagerLayoutManager;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.LoadCallback;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by zr on 2017-10-13.
 */

public class SpecialView extends LinearLayout {

    @BindView(R.id.special_item_tv)
    TextView textView;
    @BindView(R.id.special_item_recycle)
    RecyclerView bannerViewPager;

    public SpecialView(Context context) {
        super(context);
        setupView(context);
    }

    public SpecialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public SpecialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    public void setupView(Context context) {
        inflate(context, R.layout.activity_special_item_layout, this);
        KnifeKit.bind(this);
    }

    public void setData(final SpecialResults data) {
        bannerViewPager.setLayoutManager(new ScaleLayoutManager(2, ViewPagerLayoutManager.HORIZONTAL));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        bannerViewPager.setHasFixedSize(true);
        bannerViewPager.setAdapter(new SpecialAdapter(data));
//        bannerViewPager.addOnScrollListener(new CenterScrollListener());
    }

    public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.SViewHolder> {

        SpecialResults data;
        public SpecialAdapter(SpecialResults data) {
            this.data = data;
        }

        @Override
        public SViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.adapter_special_item_layout, null);
            SViewHolder viewHolder = new SViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SViewHolder holder, int position) {
            SpecialResults.SpecialItem itemData = data.list.get(position);
            holder.setDisplay(itemData);
        }

        @Override
        public int getItemCount() {
            return data.list.size();
        }

        class SViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.special_item_iv)
            RoundedImageView riv;
            SpecialResults.SpecialItem item;

            public SViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void setDisplay(SpecialResults.SpecialItem itemData) {
                ILFactory.getLoader().loadNet(riv, Api.API_PAY_OR_IMAGE_URL.concat(itemData.image), null);
                item = itemData;
            }

            @OnClick(R.id.special_item_iv)
            public void onclick() {
                Bundle bundle = new Bundle();
                bundle.putString("imagerurl", (item.image));
                bundle.putString("content", (item.content));
                bundle.putString("name", (item.name));
                bundle.putInt("id", (item.id));
                DetailFragmentsActivity.launch(getContext(), bundle, SpecialDetailFragment.newInstance());
            }

        }
    }

    @OnClick(R.id.special_item_tv)
    public void onClick() {
        DetailFragmentsActivity.launch(getContext(), null, SpecialMoreFragment.newInstance());
    }
}
