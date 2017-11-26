package com.game.helper.views.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.event.BusProvider;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.functions.Consumer;


public class StateView extends LinearLayout {

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    public StateView(Context context) {
        super(context);
        setupView(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        inflate(context, R.layout.common_view_state, this);
        KnifeKit.bind(this);
        tvMsg = findViewById(R.id.tv_msg);
        BusProvider.getBus().register(NetError.class).subscribe(new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
    }

    public void setMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (null != tvMsg) {
                tvMsg.setText(msg);
            }
        }
    }

    public void showError(NetError error) {
        if (error != null) {
            switch (error.getType()) {
                case NetError.ParseError:
                    setMsg("数据解析异常:".concat(error.getMessage()));
                    break;

                case NetError.AuthError:
                    setMsg("身份验证异常:".concat(error.getMessage()));
                    break;

                case NetError.BusinessError:
                    setMsg("业务异常:".concat(error.getMessage()));
                    break;

                case NetError.NoConnectError:
                    setMsg("网络无连接:".concat(error.getMessage()));
                    break;

                case NetError.NoDataError:
                    setMsg("数据为空:".concat(error.getMessage()));
                    break;

                case NetError.OtherError:
                    setMsg("其他异常:".concat(error.getMessage()));
                    break;
            }
        }
    }
}
