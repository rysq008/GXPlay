package com.ikats.shop.model;

import java.io.Serializable;

public class SettingBean implements Serializable {
    public String shop_url = "https://shop.hbyunjie.com/";
    public String manage_url = "http://oms.hbyunjie.com/";
    public boolean isAfterPayPrint;
    public boolean isPayPrintCheck;
    public boolean isPreviewOpen;
    public String camera_ip = "192.168.1.7";
    public String camera_port = "8000";
    public int camera_channel = 1;
    public String camera_user = "admin";
    public String camera_pwd = "ikats903";
    public boolean isLiveSuccess;//预览是否成功
    public boolean isRecordOpen;//
    public String record_ip = "http://192.168.1.140";
    public String record_port = "3000";
    public String record_channel = "101";
    public boolean send_by_express;//true：快递，false：自提
    public boolean send_by_self;//true：自提，false：快递
    public String zipCode = "100000";//邮编
    public String shop_area = "";//地区
    public String shop_address = "";//发货地址：门店或者预留地址
    public String shop_name = "";//门店名
    public String shop_code = "";//门店码
    public String shop_cashier = "";//门店收银员
    public int province_index;//省索引
    public int city_index;//市索引
    public int area_index;//区索引
    public String appKey;
    public String security;
    public boolean isPrintSuccess;//打印是否成功
}
