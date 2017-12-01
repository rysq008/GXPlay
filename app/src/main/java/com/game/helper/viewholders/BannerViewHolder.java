package com.game.helper.viewholders;

import android.view.View;

import com.game.helper.model.BannerResults;

import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by zr on 2017-10-13.
 */

public class BannerViewHolder extends AbstractViewHolder<BannerResults> {
    public BannerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(BannerResults data) {

    }

//    @BindView(R.id.activity_notice_item_tv)
//    TextView textView;
//    @BindView(R.id.activity_notice_item_vflipper)
//    ViewFlipper viewFlipper;
//
//    private Context mContext;
//
//    public BannerView(ViewGroup parent) {
//        super(parent, R.layout.activity_notice_item_layout);
//        ButterKnife.bind(this, itemView);
//        mContext = parent.getContext();
//    }
//
//    @Override
//    public void setData(BannerResults data) {
//        ILFactory.getLoader().loadNet(mContext, "", null, new LoadCallback() {
//            @Override
//            public void onLoadReady(Bitmap bitmap) {
//                textView.setBackgroundDrawable(new BitmapDrawable(bitmap));
//            }
//        });
//
//    }

}
