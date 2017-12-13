package com.game.helper.utils;

import com.game.helper.data.RxConstant;
import com.game.helper.model.WxPayInfoBean;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * 微信支付utils
 */
public class WXPayUtils {


    /**
     * 微信支付调用
     */
    public static PayReq weChatPay(WxPayInfoBean wxpayBean) {
        PayReq req = new PayReq();
        req.appId = RxConstant.ThirdPartKey.WeixinId;
        req.partnerId = wxpayBean.getPartnerid();
        req.prepayId = wxpayBean.getPrepayid();
        req.packageValue = wxpayBean.getPackagestr();
        req.nonceStr = wxpayBean.getNoncestr();
        req.timeStamp = wxpayBean.getTimestamp();
        req.sign = wxpayBean.getSign();

        return req;
    }


}
