package com.game.helper.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.GameDetailFragment;
import com.game.helper.model.GamePackageListResult;
import com.game.helper.net.api.Api;

import org.reactivestreams.Subscription;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import zlc.season.practicalrecyclerview.ItemType;
import zlc.season.rxdownload2.RxDownload;

/**
 * Created by Tian on 2017/12/21.
 */

public class ChannelListItemAdapter extends SimpleRecAdapter<ItemType, ChannelListItemAdapter.ViewHolder> {
    public static final int TAG_VIEW = 0;


    public ChannelListItemAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        final GamePackageListResult.ListBean itemDate = (GamePackageListResult.ListBean) data.get(position);
        ILFactory.getLoader().loadNet(holder.ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(itemDate.getGame().getLogo()),ILoader.Options.defaultOptions());
        holder.tvtName.setText(itemDate.getGame().getName());
        holder.tvDiscountVip.setText(String.valueOf(itemDate.getDiscount_vip()));
        holder.tvTypeName.setText(itemDate.getGame().getType().getName());
        holder.tvPackageFilesize.setText(String.valueOf(itemDate.getFilesize())+"M");
        holder.tvtSource.setText(itemDate.getChannel().getName());
        //holder.ivntro.setText(itemDate.intro);
        //holder.gameId = itemDate.id;
        holder.ivChannelListLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("gamepackeId",itemDate.getId());
                bundle.putInt("gameId",itemDate.getGame().getId());
                bundle.putInt("channelId",itemDate.getChannel().getId());
                DetailFragmentsActivity.launch(context,bundle, GameDetailFragment.newInstance());
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });


    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_channel_list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_channel_list_logothumb)
        ImageView ivLogothumb;
        @BindView(R.id.tv_channel_list_name)
        TextView tvtName;
        @BindView(R.id.tv_channel_list_discount_vip)
        TextView tvDiscountVip;
        @BindView(R.id.tv_channel_list_type_name)
        TextView tvTypeName;
        @BindView(R.id.tv_channel_list_package_filesize)
        TextView tvPackageFilesize;
        @BindView(R.id.tv_channel_list_source)
        TextView tvtSource;
        @BindView(R.id.pb_channel_list)
        ProgressBar pbChannel;
        @BindView(R.id.iv_channel_list_load)
        Button ivChannelListLoad;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this,itemView);
        }

    }
}
