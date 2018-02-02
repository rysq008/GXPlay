package com.game.helper.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.ChannelListFragment;
import com.game.helper.model.RecommendResults;
import com.game.helper.net.api.Api;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/21.
 */

public class SpecialDetailAdapter extends SimpleRecAdapter<ItemType, SpecialDetailAdapter.ViewHolder> {
    public static final int TAG_VIEW = 0;


    public SpecialDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_special_detail;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        RecommendResults.RecommendItem itemDate = (RecommendResults.RecommendItem) data.get(position);
        Float zhekou_shouchong = 0f;
        Float discount_activity = 0f;
        if (!Kits.Empty.check(itemDate.game_package)) {
            zhekou_shouchong = itemDate.game_package.get("zhekou_shouchong");
            discount_activity = itemDate.game_package.get("discount_activity");
            holder.ivPackageFilesize.setText(String.valueOf(itemDate.game_package.get("filesize").toString().replace(" ", "") + "M"));
        }
        ILFactory.getLoader().loadNet(holder.ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(itemDate.logo), ILoader.Options.defaultOptions());
        holder.ivName.setText(itemDate.name);
        holder.ivTypeName.setText(itemDate.type.get("name").replace(" ", ""));
        holder.ivntro.setText(itemDate.intro);
        if (!Kits.Empty.check(itemDate.class_type)) {
            holder.tvTypeClass.setText(itemDate.class_type.get("name"));
        }
        if (discount_activity > 0) {
            holder.ivDiscountVip.setVisibility(View.GONE);
            holder.tvActivityDiscoun.setVisibility(View.VISIBLE);
            holder.tvMatchingActivityDiscoun.setVisibility(View.VISIBLE);
            holder.tvMatchingActivityDiscoun.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.tvActivityDiscoun.setText(discount_activity.toString() + "折");
            holder.tvMatchingActivityDiscoun.setText(zhekou_shouchong.toString() + "折");
        } else {
            holder.tvActivityDiscoun.setVisibility(View.GONE);
            holder.tvMatchingActivityDiscoun.setVisibility(View.GONE);
            holder.ivDiscountVip.setVisibility(itemDate.isStandAloneGame() ? View.GONE : View.VISIBLE);
            holder.ivDiscountVip.setText(zhekou_shouchong.toString() + "折");
        }
        holder.gameId = itemDate.id;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_detail_special_logothumb)
        RoundedImageView ivLogothumb;
        @BindView(R.id.tv_detail_special_name)
        TextView ivName;
        @BindView(R.id.tv_detail_special_discount_vip)
        TextView ivDiscountVip;
        @BindView(R.id.tv_detail_special_type_name)
        TextView ivTypeName;
        @BindView(R.id.tv_detail_special_package_filesize)
        TextView ivPackageFilesize;
        @BindView(R.id.tv_detail_special_intro)
        TextView ivntro;
        @BindView(R.id.tv_detail_special_activity_discount)
        TextView tvActivityDiscoun;
        @BindView(R.id.tv_detail_special_matching_activity_discount)
        TextView tvMatchingActivityDiscoun;
        @BindView(R.id.tv_detail_special_type_class)
        TextView tvTypeClass;
        public int gameId;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }

        @OnClick(R.id.iv_detail_special_into)
        public void onViewClicked() {
            //跳到详情列表
            Bundle bundle = new Bundle();
            bundle.putInt("gameId", gameId);
            DetailFragmentsActivity.launch(context, bundle, ChannelListFragment.newInstance());
        }
    }
}
