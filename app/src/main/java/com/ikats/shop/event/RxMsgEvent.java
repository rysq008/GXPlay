package com.ikats.shop.event;

public class RxMsgEvent<T> {

    private T data;

    public String getTag() {
        return tag;
    }

    private String tag;

    private String mMsg;
    private int type;
    private int requestCode;

    public RxMsgEvent(T data) {
        this.data = data;
    }

    public RxMsgEvent(int requestCode, int type, String msg) {
        this.type = type;
        this.mMsg = msg;
        this.requestCode = requestCode;
    }

    public RxMsgEvent(int requestCode, String tag, T data) {
        this.tag = tag;
        this.data = data;
        this.requestCode = requestCode;
    }

    public String getMsg() {
        return mMsg;
    }

    public int getType() {
        return type;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MsgEvent{" +
                "data=" + data +
                ", tag='" + tag + '\'' +
                ", mMsg='" + mMsg + '\'' +
                ", types=" + type +
                ", requestCode=" + requestCode +
                '}';
    }
}