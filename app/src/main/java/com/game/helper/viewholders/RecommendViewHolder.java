package com.game.helper.viewholders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.RecommendResults;
import com.game.helper.net.api.Api;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by zr on 2017-10-13.
 */

public class RecommendViewHolder extends AbstractViewHolder<RecommendResults.RecommendItem> {

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
    @BindView(R.id.recommend_item_launch_btn)
    Button launchBtn;

    private Context mContext;

    public RecommendViewHolder(ViewGroup parent) {
        super(parent, R.layout.activity_recommend_item_layout);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }


    @OnClick(R.id.recommend_item_launch_btn)
    public void onClick() {
    }

    @Override
    public void setData(RecommendResults.RecommendItem data) {

        ILFactory.getLoader().loadNet(roundedIv, Api.API_PAY_OR_IMAGE_URL.concat(data.logothumb), ILoader.Options.defaultOptions());
        nameTv.setText(data.name);
        discountTv.setText(data.game_package.get("zhekou_shouchong").toString());
        typeTv.setText(data.type.get("name"));
        sizeTv.setText(data.game_package.get("filesize").toString());
        descTv.setText(data.intro);
    }
}
