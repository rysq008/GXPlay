package com.zhny.library.presenter.monitor.websocket;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * created by liming
 */
public class WebSocketManager {
    private static final String TAG = "WebSocketManager";

    private final static int MAX_NUM = 10; // 最大重连数
    private final static int MILLIS = 5000;// 重连间隔时间，毫秒
    private volatile static WebSocketManager manager;

    private OkHttpClient client;
    private Request request;
    private IReceiveMessage receiveMessage;
    private WebSocket mWebSocket;

    private boolean isConnect = false;
    private boolean reConnect = false;
    private int connectNum = 0;


    private WebSocketManager() {
    }

    public static WebSocketManager getInstance() {
        if (manager == null) {
            synchronized (WebSocketManager.class) {
                if (manager == null) {
                    manager = new WebSocketManager();
                }
            }
        }
        return manager;
    }

    public void initConnect(String url, IReceiveMessage message) {
        client = new OkHttpClient.Builder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        request = new Request.Builder().url(url).build();
        receiveMessage = message;
        connect();
    }

    /**
     * 连接
     */
    private void connect() {
        if (isConnect()) {
            close();
        }
        reConnect = true;
        client.newWebSocket(request, createListener());
    }

    /**
     * 重连
     */
    private void reconnect() {
        if (connectNum <= MAX_NUM) {
            try {
                Thread.sleep(MILLIS);
                connect();
                connectNum++;
                Log.e(TAG, "reconnect num ==> " + connectNum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            receiveMessage.onConnectFailedOver();
            Log.e(TAG, "reconnect over , +lease check url or network" + MAX_NUM);
        }
    }

    /**
     * 是否连接
     */
    private boolean isConnect() {
        return mWebSocket != null && isConnect;
    }

    /**
     * 发送消息
     *
     * @param text 字符串
     * @return boolean
     */
    public boolean sendMessage(String text) {
        if (!isConnect()) return false;
        return mWebSocket.send(text);
    }

    /**
     * 发送消息
     *
     * @param byteString 字符集
     * @return boolean
     */
    public boolean sendMessage(ByteString byteString) {
        if (!isConnect()) return false;
        return mWebSocket.send(byteString);
    }

    /**
     * 关闭连接
     */
    public void close() {
        reConnect = false;
        if (isConnect()) {
            mWebSocket.cancel();
            mWebSocket.close(1001, "客户端主动关闭连接");
        }
    }

    private WebSocketListener createListener() {
        return new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.d(TAG, "open:" + response.toString());
                mWebSocket = webSocket;
                isConnect = response.code() == 101;
                if (!isConnect && reConnect) {
                    reconnect();
                } else {
                    Log.d(TAG, "connect success.");
                    if (receiveMessage != null) {
                        receiveMessage.onConnectSuccess();
                    }
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                if (receiveMessage != null) {
                    receiveMessage.onMessage(text);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                if (receiveMessage != null) {
                    receiveMessage.onMessage(bytes.base64());
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                mWebSocket = null;
                isConnect = false;
                if (receiveMessage != null) {
                    receiveMessage.onClose();
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d(TAG, "connect onClosed：" + reason);
                mWebSocket = null;
                isConnect = false;
                if (receiveMessage != null) {
                    receiveMessage.onClose();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                if (response != null) {
                    Log.d(TAG, "connect failed：" + response.message());
                }
                isConnect = false;
                if (receiveMessage != null) {
                    receiveMessage.onConnectFailed();
                }
                //java.net.SocketException: Socket closed
                Log.d(TAG, "connect failed throwable" + t.getMessage());
                if (reConnect) {
                    reconnect();
                }

            }
        };
    }
}
