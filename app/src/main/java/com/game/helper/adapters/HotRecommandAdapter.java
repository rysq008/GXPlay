package com.game.helper.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.GameDetailFragment;
import com.game.helper.model.HotResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.net.api.Api;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * 热门推荐adapter
 */
public class HotRecommandAdapter extends SimpleRecAdapter<ItemType, HotRecommandAdapter.ViewHolder> {

    private Activity mActivity;

    public HotRecommandAdapter(Activity context) {
        super(context);
        mActivity = context;
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        holder.setDisplay(item, mActivity, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailFragmentsActivity.launch(context,null, GameDetailFragment.newInstance());
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recommend_item_layout;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recommend_item_icon_iv)
        RoundedImageView recommendItemIconIv;
        @BindView(R.id.recommend_item_name_tv)
        TextView recommendItemNameTv;
        @BindView(R.id.recommend_item_discount_tv)
        TextView recommendItemDiscountTv;
        @BindView(R.id.recommend_item_type_tv)
        TextView recommendItemTypeTv;
        @BindView(R.id.recommend_item_size_tv)
        TextView recommendItemSizeTv;
        @BindView(R.id.recommend_item_desc_tv)
        TextView recommendItemDescTv;
        @BindView(R.id.recommend_item_launch_iv)
        ImageView recommendItemLaunchIv;
        @BindView(R.id.root_view)
        RelativeLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity, final int position) {
            final HotResults.HotItem data = (HotResults.HotItem) itemType;

            ILFactory.getLoader().loadNet(recommendItemIconIv, Api.API_PAY_OR_IMAGE_URL.concat(data.logo), ILoader.Options.defaultOptions());
            recommendItemNameTv.setText(data.name.replace(" ", ""));
            recommendItemDiscountTv.setText(data.game_package.get("zhekou_shouchong").toString().replace(" ", ""));
            recommendItemTypeTv.setText(data.type.get("name").replace(" ", ""));
            recommendItemSizeTv.setText(data.game_package.get("filesize").toString().replace(" ", ""));
            recommendItemDescTv.setText(data.intro.replace(" ", ""));

        }
    }

}
