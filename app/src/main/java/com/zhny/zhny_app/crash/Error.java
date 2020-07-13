package com.zhny.zhny_app.crash;

public class Error {

    /*未知数据数据*/
    public static final int DATA_UNDEFINED_EXCEPTION = 1000;
    /*数据类型不匹配*/
    public static final int DATA_TYPE_NOT_MATCH = 1001;

    /*服务器未响应*/
    public static final int SERVICE_NOT_RESPONSE = 2001;
    /*服务器状态异常 */
    public static final int SERVICE_WRONG_RESPONSE = 2002;

    /*数据解析异常*/
    public static final int JSON_PARSER_EXCEPTION = 9001;

    public static String getMessage(int code){
        switch (code){
            case DATA_UNDEFINED_EXCEPTION:
                return "未知数据错误";
            case DATA_TYPE_NOT_MATCH:
                return "类型不匹配";
            case SERVICE_NOT_RESPONSE:
                return "服务器未响应";
            case SERVICE_WRONG_RESPONSE:
                return "状态异常";
            case JSON_PARSER_EXCEPTION:
                return "解析异常";
            default:
                return "未知错误";
        }
    }
}
