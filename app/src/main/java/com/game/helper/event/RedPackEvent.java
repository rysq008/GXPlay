package com.game.helper.event;

public class RedPackEvent<T> {

    private T data;

    private int type;
    private int requestCode;

    public RedPackEvent(T data) {
        this.data = data;
    }

    public RedPackEvent(int requestCode, int type, T data) {
        this.type = type;
        this.data = data;
        this.requestCode = requestCode;
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

}