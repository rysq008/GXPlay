package com.ikats.shop.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tamsiree.rxkit.RxDeviceTool;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class SocketClient extends WebSocketClient {
    public static final String ACTION_RECEIVE_MESSAGE = "com.jinuo.mhwang.servermanager";
    public static final String KEY_RECEIVED_DATA = "data";
    private static SocketClient mWebClient;
    private Context mContext;
    /**
     * 路径为ws+服务器地址+服务器端设置的子路径+参数（这里对应服务器端机器编号为参数）
     * 如果服务器端为https的，则前缀的ws则变为wss
     */
    private static final String mAddress = "wss://shop.hbyunjie.com/websocket/order/%s";//"ws://服务器地址：端口/mhwang7758/websocket/";

    private void showLog(String msg) {
        Log.d("WebClient---->", msg);
    }

    private SocketClient(URI serverUri, Context context) {
        super(serverUri, new Draft_6455());
        mContext = context;
        showLog("WebClient");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        showLog("open->" + handshakedata.toString());
//        mWebClient.send("aaa");
    }

    @Override
    public void onMessage(String message) {
        showLog("onMessage->" + message);
        sendMessageBroadcast(message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        showLog("onClose->" + reason);
    }

    @Override
    public void onError(Exception ex) {
        showLog("onError->" + ex.toString());
    }

    /**
     * 初始化
     *
     * @param vmc_no
     */
    public static void initWebSocket(final Context context, final long vmc_no) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mWebClient = new SocketClient(new URI(String.format(mAddress, RxDeviceTool.getMacAddress())), context);
                    try {
                        mWebClient.connectBlocking();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送消息广播
     *
     * @param message
     */
    private void sendMessageBroadcast(String message) {
        if (!message.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(ACTION_RECEIVE_MESSAGE);
            intent.putExtra(KEY_RECEIVED_DATA, message);
            showLog("发送收到的消息");
            mContext.sendBroadcast(intent);
        }
    }

}
