package com.zhny.library.base;

import java.io.Serializable;

/**
 * 服务器返回公共实体
 *
 * @param <T>
 * @Time 2019-07-21
 */
public class BaseDto<T> implements Serializable {
    private String msgCode;
    private String msg;
    private T content;
    private String status;

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
