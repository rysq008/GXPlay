package com.game.helper.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.ChannelListFragment;
import com.game.helper.model.HotResults;
import com.game.helper.net.api.Api;
import com.game.helper.views.RecommendView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import zlc.season.practicalrecyclerview.ItemType;

import static android.view.View.GONE;

/**
 *
 */
public class SearchListAdapter extends SimpleRecAdapter<ItemType, SearchListAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public SearchListAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_game_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        holder.setDisplay(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });
        holder.launchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("gameId", ((HotResults.HotItem) item).id);
                DetailFragmentsActivity.launch(context, bundle, ChannelListFragment.newInstance());
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.game_item_layout)
        RecommendView recommondLayout;

        @BindView(R.id.recommend_item_icon_iv)
        RoundedImageView roundedIv;
        @BindView(R.id.recommend_item_name_tv)
        TextView nameTv;
        @BindView(R.id.recommend_item_discount_tv)
        TextView discountTv;
        @BindView(R.id.recommend_item_type_tv)
        TextView typeTv;
        @BindView(R.id.recommend_item_size_tv)
        TextView sizeTv;
        @BindView(R.id.recommend_item_desc_tv)
        TextView descTv;
        @BindView(R.id.recommend_item_launch_iv)
        ImageView launchIv;
        @BindView(R.id.recommend_item_activity_discount_tv)
        TextView activityDiscount;
        @BindView(R.id.recommend_item_discount_tv_matching_activity_discount)
        TextView matchingActivityDiscount;
        @BindView(R.id.recommend_item_hand_type_tv)
        TextView handType;


        public void setDisplay(ItemType itemType) {
            HotResults.HotItem data = (HotResults.HotItem) itemType;
            ILFactory.getLoader().loadNet(roundedIv, Api.API_PAY_OR_IMAGE_URL.concat(data.logo), ILoader.Options.defaultOptions());
            nameTv.setText(data.name.replace(" ", ""));

            Float zhekou_shouchong = data.game_package.get("zhekou_shouchong");
            Float discount_activity = data.game_package.get("discount_activity");
            if (discount_activity >0) {
                discountTv.setVisibility(GONE);
                activityDiscount.setVisibility(View.VISIBLE);
                matchingActivityDiscount.setVisibility(View.VISIBLE);
                matchingActivityDiscount.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
                activityDiscount.setText(discount_activity.toString()+"折");
                matchingActivityDiscount.setText(zhekou_shouchong.toString()+"折");
            } else {
                activityDiscount.setVisibility(GONE);
                matchingActivityDiscount.setVisibility(GONE);
                discountTv.setVisibility(View.VISIBLE);
                discountTv.setText(zhekou_shouchong.toString()+"折");
            }
            typeTv.setText(data.type.get("name").replace(" ", ""));
            sizeTv.setText(data.game_package.get("filesize").toString().replace(" ", "")+"M");
            descTv.setText(data.intro.replace(" ", ""));
            handType.setText(data.class_type.get("name"));

        }

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
