package com.game.helper.views.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.game.helper.R;
import com.game.helper.model.BannerResults;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import zlc.season.practicalrecyclerview.SectionItem;

public class Header implements SectionItem {
    @BindView(R.id.banner_guide_content)
    BGABanner mBanner;

    BannerResults mBannerResults;

    public Header(BannerResults bannerResults) {
        mBannerResults = bannerResults;
    }

    public Header() {
    }

    @Override
    public View createView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_banner_layout, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onBind() {
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(final BGABanner banner, View view, Object model, int position) {
                final ImageView itemView = (ImageView) view;
                itemView.setImageResource((int) model);
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
        mBanner.setData(Arrays.asList(R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4), null);
    }
}