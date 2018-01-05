package com.game.helper.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.game.helper.R;
import com.game.helper.model.NoticeResults;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.LoadCallback;
import zlc.season.practicalrecyclerview.AbstractViewHolder;

/**
 * Created by zr on 2017-10-13.
 */

public class NoticeViewHolder extends AbstractViewHolder<NoticeResults> {

    @BindView(R.id.tv_notice_view_item)
    TextView textView;
    @BindView(R.id.activity_notice_item_vflipper)
    ViewFlipper viewFlipper;

    private Context mContext;

    public NoticeViewHolder(ViewGroup parent) {
        super(parent, R.layout.activity_notice_item_layout);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(NoticeResults data) {
        ILFactory.getLoader().loadNet(mContext, "", null, new LoadCallback() {
            @Override
            public void onLoadReady(Bitmap bitmap) {
                textView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        });

//        setInAnimation      设置View进入屏幕时候使用的动画
//        setOutAnimation     设置View退出屏幕时候使用的动画
//        showPrevious        显示ViewFlipper里面的上一个View
//        showNext            显示ViewFlipper里面的下一个View
//        setFlipInterval     设置View之间切换的时间间隔
//        startFlipping       使用setFlipInterval方法设置的时间间隔来开始切换所有的View,切换会循环进行
//        stopFlipping        停止View切换
//        isFlipping          用来判断View切换是否正在进行
//        setDisplayedChild   切换到指定子View
        for (NoticeResults.NoticeItem item : data.list) {
            TextView tv = new TextView(mContext);
            tv.setText(item.content);
            viewFlipper.addView(new TextView(mContext));
        }
        viewFlipper.setInAnimation(mContext, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(mContext, android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();

    }

    @OnClick(R.id.activity_notice_item_vflipper)
    public void onClick() {
    }
}
