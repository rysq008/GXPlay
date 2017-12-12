package com.game.helper.utils;

import android.app.Activity;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ali支付utils
 */
public class AliPayUtils {

    private static void AliPay(final Activity activity,final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
//                String info = "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2016101802218278&biz_content=%7B%22total_amount%22%3A%220.01%22%2C%22body%22%3A%22G9%E6%B8%B8%E6%88%8F-%E6%B8%B8%E6%88%8F%E5%85%85%E5%80%BC%22%2C%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%B8%B8%E6%88%8F%E5%85%85%E5%80%BC%22%2C%22out_trade_no%22%3A%22GR2017121210525979834%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F60.205.204.218%3A8080%2FG9game%2FpaymentController.do%3FalipayReturn&sign=Q1AHxKGI3O0j7sDX80ZVmnh3C%2Fy%2Bq9%2FWc4LPE%2BI8%2FoM3D8CL2eDalR8qEuHYPA6FjWymrsR3q2AA%2F2SG5HK9IV25LK%2BqmS7OpbRVGxgpGZ5bsIw9o%2BWWDZ5GRPJmNJ%2FltOdI9Ra0rUxaj9LOJB5Sa4yGutMWyRUikcNNeww3FPwGcSURuJded%2BAyktskGXF79e7CpXChK2LQLsM9GF6u0a85BpvTdfuBYcWW0eQLizRodwEb265%2BvUzTvC3As6h9NKZVbCxE0CdifJ575kQVWTNSa9IpmUkFwAyBpb%2BLwlXamIHDIslo0eYk2CK79jJiPPB7imDpIEDJhWZUeDaQ2w%3D%3D&sign_type=RSA2&timestamp=2017-12-12+10%3A52%3A59&version=1.0";
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

//                        Message msg = new Message();
//                        msg.what = 1;
//                        msg.obj = result;
//                        mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 构造支付订单参数列表
     * @param app_id
     * @param rsa2
     * @return
     */
    public static Map<String, String> buildOrderParamMap(String app_id, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put("app_id", app_id);
        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + "" +  "\"}");//getOutTradeNo()
        keyValues.put("charset", "utf-8");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");
        keyValues.put("timestamp", "2016-07-29 16:55:53");
        keyValues.put("version", "1.0");
        return keyValues;
    }


    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }


    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map
     *            待签名授权信息
     *
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

}
