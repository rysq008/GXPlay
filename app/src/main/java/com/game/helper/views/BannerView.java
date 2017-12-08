package com.game.helper.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.game.helper.R;
import com.game.helper.model.BannerResults;
import com.game.helper.net.api.Api;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by zr on 2017-10-13.
 */

public class BannerView extends LinearLayout {

    @BindView(R.id.banner_guide_content)
    BGABanner mBanner;

    public BannerView(Context context) {
        super(context);
        setupView(context);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        inflate(context, R.layout.common_banner_layout, this);
        KnifeKit.bind(this);

    }

    public void setData(final BannerResults data) {
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(final BGABanner banner, View view, Object model, int position) {
                final ImageView itemView = (ImageView) view;
                BannerResults.BannerItem item = (BannerResults.BannerItem) model;
//                itemView.setImageResource((int) model);
                String image = Kits.Empty.check(item.image) ? "" : item.image;
                ILFactory.getLoader().loadNet(itemView, Api.API_PAY_OR_IMAGE_URL.concat(image), null);
//                Glide.with(banner.getContext())
//                        .load(model)
////                            .placeholder(R.mipmap.ic_launcher)
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                if (itemView == null) {
//                                    return false;
//                                }
//                                if (itemView.getScaleType() != ImageView.ScaleType.FIT_XY) {
//                                    itemView.setScaleType(ImageView.ScaleType.FIT_XY);
//                                }
//                                ViewGroup.LayoutParams params = itemView.getLayoutParams();
//                                int vw = itemView.getWidth() - itemView.getPaddingLeft() - itemView.getPaddingRight();
//                                float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                                int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                                params.height = vh + itemView.getPaddingTop() + itemView.getPaddingBottom();
//                                System.out.println("image height:" + params.height);
//                                System.out.println("image width:" + params.width);
//                                itemView.setLayoutParams(params);
//                                ViewGroup.LayoutParams banner_params = banner.getLayoutParams();
//                                banner_params.width = params.width;
//                                banner_params.height = params.height;
//                                banner.setLayoutParams(banner_params);
//                                return false;
//                            }
//                        }).into(itemView);
            }
        });
        mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
            }
        });
//        mBanner.setData(Arrays.asList(R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4), null);
        mBanner.setData(data.list, null);
    }


}
