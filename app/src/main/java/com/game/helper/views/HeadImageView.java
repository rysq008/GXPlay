package com.game.helper.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.views.widget.TotoroToast;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.LoadCallback;
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
                if (msgEvent.getType() == RxConstant.Head_Image_Change_Type) {
                    ILFactory.getLoader().loadNet(getContext(), msgEvent.getMsg(), null, new LoadCallback() {
                        @Override
                        public void onLoadReady(Bitmap bitmap) {
                            riv.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.getBus().unregister(this);
    }

    @OnClick(R.id.common_head_iv)
    public void onClick() {
        TotoroToast.makeText(getContext(), " on click head view ", 0).show();
    }

}
