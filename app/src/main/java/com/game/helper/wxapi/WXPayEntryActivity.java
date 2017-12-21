package com.game.helper.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.RedPackEvent;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements
        IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: WXPayEntryActivity");
        api = WXAPIFactory.createWXAPI(this, RxConstant.ThirdPartKey.WeixinId);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onCreate: onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("","BaseReq"+req.checkArgs());
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("","onResp");
        String tipStr = "";
        switch (resp.errCode) {
            case 0:
                tipStr = "支付成功";
                break;
            case -1:
                tipStr = "支付遇到错误";
                break;
            case -2:
                tipStr = "用户取消";
                break;
        }
        BusProvider.getBus().post(new RedPackEvent(0, RxConstant.WX_PAY, resp.errCode));
        Toast.makeText(this,tipStr,Toast.LENGTH_SHORT).show();
        finish();
    }
}
