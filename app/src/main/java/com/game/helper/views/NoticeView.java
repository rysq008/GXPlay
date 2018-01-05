package com.game.helper.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.MessageFragment;
import com.game.helper.fragments.WebviewFragment;
import com.game.helper.fragments.recharge.RechargeFragment;
import com.game.helper.model.H5Results;
import com.game.helper.model.PlatformMessageResults;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

import static com.game.helper.fragments.WebviewFragment.PARAM_URL;

/**
 * Created by zr on 2017-10-13.
 */

public class NoticeView extends LinearLayout {

    @BindView(R.id.activity_notice_item_vflipper)
    ViewFlipper viewFlipper;

    private Context mContext;
    private H5Results h5Results;

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

    public void setData(PlatformMessageResults data) {
        h5Results = data.h5Results;
        viewFlipper.removeAllViews();
        for (PlatformMessageResults.PlatformMessageItem noticeItem : data.list) {
            TextView tvNotice = (TextView) inflate(getContext(), R.layout.item_text_notice_view, null);
            tvNotice.setText(noticeItem.title);
            viewFlipper.addView(tvNotice);
        }
        viewFlipper.startFlipping();
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setInAnimation(getContext(), R.anim.animation_notice_in_up);
        viewFlipper.setOutAnimation(getContext(), R.anim.animation_notice_out_bottom);
        //viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        //viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }

    @OnClick({R.id.activity_notice_item_vflipper, R.id.notice_vip_layout, R.id.notice_rechange_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notice_rechange_layout:
                DetailFragmentsActivity.launch(getContext(), null, RechargeFragment.newInstance());
                break;
            case R.id.notice_vip_layout:
                Bundle bundle = new Bundle();
                WebviewFragment.PARAM_VIP = h5Results.vip_url;
                bundle.putString(PARAM_URL, WebviewFragment.PARAM_VIP);
                DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
                break;
            case R.id.activity_notice_item_vflipper:
                DetailFragmentsActivity.launch(getContext(), null, MessageFragment.newInstance());
                break;
        }
    }
}
