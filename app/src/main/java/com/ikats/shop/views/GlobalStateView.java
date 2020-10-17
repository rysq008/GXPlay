package com.ikats.shop.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.event.RxBusProvider;
import com.ikats.shop.event.RxMsgEvent;
import com.tamsiree.rxkit.RxNetTool;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

public class GlobalStateView extends LinearLayout {
    public static final String TAG = GlobalStateView.class.getCanonicalName();

    @BindView(R.id.global_net_status_cb)
    CheckBox net_cb;
    @BindView(R.id.global_socket_status_cb)
    CheckBox socket_cb;
    @BindView(R.id.global_print_status_cb)
    CheckBox print_cb;
    @BindView(R.id.global_hikvision_status_cb)
    CheckBox hikvision_cb;

    public GlobalStateView(Context context) {
        super(context);
        setupView(context);
    }

    public GlobalStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public GlobalStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        inflate(context, R.layout.global_view_state, this);
        KnifeKit.bind(this, this);
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                super.onLost(network);
                ///网络不可用的情况下的方法
                RxMsgEvent msgEvent = new RxMsgEvent(NET_CODE, GlobalStateView.TAG, false);
                RxBusProvider.getBus().postEvent(msgEvent);
            }

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                ///网络可用的情况下的方法
                RxMsgEvent msgEvent = new RxMsgEvent(NET_CODE, GlobalStateView.TAG, true);
                RxBusProvider.getBus().postEvent(msgEvent);
            }
        });
        RxBusProvider.getBus().receiveEvent(RxMsgEvent.class).subscribe(rxMsgEvent -> updateStatus(rxMsgEvent));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("add", "onAttachedToWindow: ");
        RxMsgEvent msgEvent = new RxMsgEvent(HIKVIS_CODE, GlobalStateView.TAG, App.getSettingBean().isLiveSuccess);
        RxBusProvider.getBus().postEvent(msgEvent);
        RxMsgEvent smsgEvent = new RxMsgEvent(NET_CODE, GlobalStateView.TAG, RxNetTool.isConnected(getContext()));
        RxBusProvider.getBus().postEvent(smsgEvent);
    }

    private void updateStatus(RxMsgEvent msgEvent) {
        Log.e("sss", "updateStatus: " + msgEvent.getRequestCode() + "," + msgEvent.getData());
        if (!Kits.Empty.check(msgEvent.getTag()) && TAG.equals(msgEvent.getTag()) && msgEvent.getData() instanceof Boolean) {
            boolean is_status_ok = (boolean) msgEvent.getData();
            switch (msgEvent.getRequestCode()) {
                case NET_CODE:
                    net_cb.setChecked(is_status_ok);
                    break;
                case SOCKET_CODE:
                    socket_cb.setChecked(is_status_ok);
                    break;
                case PRINT_CODE:
                    print_cb.setChecked(is_status_ok);
                    break;
                case HIKVIS_CODE:
                    hikvision_cb.setChecked(is_status_ok);
                    break;
            }
        }
    }

    public static final int NET_CODE = 100;
    public static final int SOCKET_CODE = 101;
    public static final int PRINT_CODE = 102;
    public static final int HIKVIS_CODE = 103;
}
