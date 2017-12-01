package com.game.helper.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.game.helper.R;
import com.game.helper.model.NoticeResults;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by zr on 2017-10-13.
 */

public class NoticeView extends LinearLayout {

    @BindView(R.id.activity_notice_item_vflipper)
    ViewFlipper viewFlipper;

    private Context mContext;

    public NoticeView(Context context) {
        super(context);
        setupView(context);
    }

    public NoticeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public NoticeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        inflate(context, R.layout.activity_notice_item_layout, this);
        KnifeKit.bind(this);
    }

    public void setData(NoticeResults data) {
    }

    @OnClick(R.id.activity_notice_item_vflipper)
    public void onClick() {
    }
}
