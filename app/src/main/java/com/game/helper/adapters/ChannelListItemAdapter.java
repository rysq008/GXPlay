package com.game.helper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.GameListResultModel;
import com.game.helper.model.SpecialDetailResults;
import com.game.helper.net.api.Api;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import zlc.season.practicalrecyclerview.ItemType;

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            final ItemType item = data.get(position);
            gameListResultModelHttpResultModel.ListBean itemDate = (GameListResultModel.) data.get(position);
        ILFactory.getLoader().loadNet(holder.ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(itemDate.logothumb),ILoader.ILoader.Options.defaultOptions());
        holder.tvName.setText(itemDate.name);
        holder.tvDiscountVip.setText(String.valueOf(itemDate.game_package.discount_vip));
        holder.tvTypeName.setText(itemDate.type.name);
        holder.tvPackageFilesize.setText(String.valueOf(itemDate.game_package.filesize));
        //holder.ivntro.setText(itemDate.intro);
            //holder.gameId = itemDate.id;

            @Override
            public void onClick(View v) {
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
        ProgressBar pbChannelList;
        @BindView(R.id.iv_channel_list_load)
        ImageView ivChannelListLoad;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.iv_channel_list_load)
        public void onViewClicked() {
        }
    }
}
