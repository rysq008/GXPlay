package com.game.helper.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.SettingUserFragment;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.net.api.Api;
import com.game.helper.utils.SharedPreUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.kit.Kits;
import io.reactivex.functions.Consumer;

/**
 * Created by zr on 2017-10-13.
 */

public class HeadImageView extends FrameLayout {

    @BindView(R.id.common_head_iv)
    RoundedImageView riv;

    public HeadImageView(Context context) {
        super(context);
        setupView(context);
    }

    public HeadImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public HeadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        inflate(context, R.layout.common_head_layout, this);
        ButterKnife.bind(this);
        invalidate();
        BusProvider.getBus().receive(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
            @Override
            public void accept(MsgEvent msgEvent) throws Exception {
                if (((Activity) getContext()).isFinishing())
                    return;
                if (msgEvent.getType() == RxConstant.Head_Image_Change_Type) {
                    if (Kits.Empty.check(msgEvent.getMsg())) {
                        ILFactory.getLoader().loadResource(riv, R.mipmap.ic_default_avatar_circle, null);
                    } else {
                        ILFactory.getLoader().loadNet(riv, Api.API_BASE_URL.concat(msgEvent.getMsg()), null);
                    }
//                    ILFactory.getLoader().loadNet(getContext(), Api.API_BASE_URL.concat(msgEvent.getMsg()), null, new LoadCallback() {
//                        @Override
//                        public void onLoadReady(Bitmap bitmap) {
//                            riv.setImageBitmap(bitmap);
//                        }
//                    });
                }
            }
        });
    }

    public RoundedImageView getAvatarView() {
        return riv;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        BusProvider.getBus().unregister(this);
    }

    @OnClick({R.id.common_head_iv})
    public void onClick(View view) {
//        TotoroToast.makeText(getContext(), " on click head view ", 0).show();
        switch (view.getId()) {
            case R.id.common_head_iv:
                LoginUserInfo info = SharedPreUtil.getLoginUserInfo();
                BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, info == null ? "" : info.icon));
                Fragment fragment = DetailFragmentsActivity.getCurrentFragment();
                if (null != fragment && fragment instanceof SettingUserFragment) {
//                    Toast.makeText(getContext(), "ssss", Toast.LENGTH_SHORT).show();
                } else {
                    DetailFragmentsActivity.launch(getContext(), null, SettingUserFragment.newInstance());
                }
                break;
        }
    }

}
