package com.zhny.library.presenter.monitor.websocket;

/**
 * created by liming
 */
public interface IReceiveMessage {

    // 连接成功
    void onConnectSuccess();

    // 连接失败
    void onConnectFailed();

    // 10次连接失败
    void onConnectFailedOver();

    // 关闭
    void onClose();

    void onMessage(String msg);
}
