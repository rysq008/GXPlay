package com.game.helper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.api.Api;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/20.
 */

public class SpecialMoreItemAdapter extends SimpleRecAdapter<ItemType,SpecialMoreItemAdapter.ViewHolder> {
    public static final int TAG_VIEW = 0;

    public SpecialMoreItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_special_more;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        SpecialResults.SpecialItem itemDate = (SpecialResults.SpecialItem) data.get(position);
        ILFactory.getLoader().loadNet(holder.itemIv, Api.API_PAY_OR_IMAGE_URL.concat(itemDate.image), ILoader.Options.defaultOptions());
        holder.itemNameTv.setText(itemDate.name);
        holder.itemContentTv.setText(itemDate.content);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_special_more_iv)
        ImageView itemIv;
        @BindView(R.id.item_special_more_name_tv)
        TextView itemNameTv;
        @BindView(R.id.item_special_more_content_tv)
        TextView itemContentTv;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }

    }
}
