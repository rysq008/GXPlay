package com.game.helper.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.ChannelListFragment;
import com.game.helper.fragments.GameDetailFragment;
import com.game.helper.model.RecommendResults;
import com.game.helper.net.api.Api;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by zr on 2017-10-13.
 */

public class RecommendView extends LinearLayout {

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

    private Context mContext;

    public RecommendView(Context context) {
        super(context);
        setupView(context);
    }

    public RecommendView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public RecommendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    public void setupView(Context context) {
        inflate(context, R.layout.activity_recommend_item_layout, this);
        ButterKnife.bind(this);
    }


    public void setData(final RecommendResults.RecommendItem data) {

        ILFactory.getLoader().loadNet(roundedIv, Api.API_PAY_OR_IMAGE_URL.concat(data.logo), ILoader.Options.defaultOptions());
        nameTv.setText(data.name.replace(" ", ""));
        discountTv.setText(data.game_package.get("zhekou_shouchong").toString().replace(" ", ""));
        typeTv.setText(data.type.get("name").replace(" ", ""));
        sizeTv.setText(data.game_package.get("filesize").toString().replace(" ", ""));
        descTv.setText(data.intro.replace(" ", ""));
        launchIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("gameId",data.id);
                DetailFragmentsActivity.launch(getContext(),bundle, ChannelListFragment.newInstance());
            }
        });
    }
}