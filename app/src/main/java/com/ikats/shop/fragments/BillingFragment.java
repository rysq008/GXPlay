package com.ikats.shop.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.internal.LinkedTreeMap;
import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.database.OrderTableEntiry;
import com.ikats.shop.database.OrderTableEntiry_;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.GoodsBean;
import com.ikats.shop.model.LoginBean;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.net.model.PayinfoRequestBody;
import com.ikats.shop.utils.PlayerHikvision;
import com.ikats.shop.utils.Prints;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.utils.ShareUtils;
import com.ikats.shop.views.X5WebView;
import com.ikats.shop.views.XReloadableStateContorller;
import com.jaeger.library.StatusBarUtil;
import com.lvrenyang.io.base.IOCallBack;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zkteco.android.IDReader.IDPhotoHelper;
import com.zkteco.android.IDReader.WLTService;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderExceptionListener;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;
import com.zkteco.android.biometric.module.idcard.meta.IDPRPCardInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import io.objectbox.Box;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ikats.shop.fragments.HomeFragment.mcom;
import static com.ikats.shop.fragments.HomeFragment.mpos;

public class BillingFragment extends XBaseFragment {
    @BindView(R.id.billing_title_tv)
    TextView title_tv;
    @BindView(R.id.billing_title_name_tv)
    TextView name_tv;
    @BindView(R.id.billing_title_test_tv)
    TextView test_tv;
    @BindView(R.id.billing_enter_url_et)
    EditText enter_url_et;
    @BindView(R.id.billing_enter_submit_btn)
    Button submit_btn;
    @BindView(R.id.billing_bottom_amount_big_tv)
    TextView amout_bit_tv;
    @BindView(R.id.billing_bottom_amount_small_tv)
    TextView amout_small_tv;
    @BindView(R.id.billing_bottom_amount_tv)
    TextView amount_tv;
    @BindView(R.id.billing_sufaceview)
    SurfaceView surfaceView;
    @BindView(R.id.billing_sufaceview_back)
    SurfaceView surfaceViewback;
    @BindView(R.id.billing_xreload_layout)
    XReloadableStateContorller reloadableStateContentLayout;
    @BindView(R.id.billing_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.billing_webview)
    X5WebView wvContent;
    String mUrl = "";
    ArrayMap<String, GoodsBean> arrayMap = new ArrayMap();
    String cur_product_id = "";
    String startTime, endTime;
    private PlayerHikvision playerHikvision;
    private String outBizNo;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setTranslucent(context, 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        context.registerReceiver(mUsbReceiver, filter);
    }

    boolean isScan;
    private static final int PLAY_HIK_STREAM_CODE = 1001;
    private static final String IP_ADDRESS = "192.168.1.7";
    private static final int PORT = 8000;
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "ikats903";

    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case 0:
                ToastUtils.showLong(msg.obj + "");
                break;
            case -1: {
                if (playerHikvision.isplayback())
                    return false;
                if (playerHikvision.isLive()) {
                    DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
                        @Override
                        public Dialog getDialog(Context context) {
                            return new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                    .setTitle("提示").setMessage("是否结束交易").setNegativeButton("取消", null)
                                    .setPositiveButton("确定", (dialog, which) -> {
                                        dialog.cancel();
                                        surfaceView.setVisibility(View.GONE);
                                        handler.removeCallbacksAndMessages(null);
                                        playerHikvision.stopLive(playerHikvision.mPlayId, playerHikvision.mPort);
                                        endTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                                        playerHikvision.cleanup();
                                        App.getBoxStore().runInTxAsync(() -> {
                                            Box<OrderTableEntiry> box = App.getBoxStore().boxFor(OrderTableEntiry.class);
                                            OrderTableEntiry orderTableEntiry = box.query().equal(OrderTableEntiry_.startTime, startTime).build().findFirst();
                                            orderTableEntiry.endTime = endTime;
                                            orderTableEntiry.outBizNo = outBizNo;
                                            box.put(orderTableEntiry);
                                        }, (result, error) -> {
                                            if (error == null) {
                                                Log.e("aaa", "txFinished: is ok");
                                            }
                                        });

                                    }).create();
                        }
                    }, true).show(getChildFragmentManager(), "");
                } else {
                    surfaceView.setVisibility(View.VISIBLE);
                    endTime = startTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                    playerHikvision.live(IP_ADDRESS, PORT, USER_NAME, PASSWORD, PlayerHikvision.HIK_SUB_STREAM_CODE, 1);
                    OrderTableEntiry orderTableEntiry = new OrderTableEntiry();
                    orderTableEntiry.ip = IP_ADDRESS;
                    orderTableEntiry.prot = PORT;
                    orderTableEntiry.channel = 1;
                    orderTableEntiry.streamType = PlayerHikvision.HIK_SUB_STREAM_CODE;
                    orderTableEntiry.username = USER_NAME;
                    orderTableEntiry.password = PASSWORD;
                    orderTableEntiry.startTime = startTime;
                    orderTableEntiry.endTime = endTime;
                    orderTableEntiry.outBizNo = "";
                    App.getBoxStore().boxFor(OrderTableEntiry.class).put(orderTableEntiry);
                }
            }
            break;
        }
        return false;
    });

    @Override
    public void initData(Bundle savedInstanceState) {
        mUrl = "http://www.baidu.com";
        mUrl = "https://shop45833283.m.youzan.com/wscgoods/detail/276jakb39jlnf?scan=1&activity=none&from=kdt&qr=directgoods_638272694";
//        url = "https://h5.m.taobao.com/need/weex/container.html?_wx_tpl=https://owl.alicdn.com/mupp-dy/develop/taobao/need/weex/bpu/entry.js&itemId=597185727259&bpuId=1451437069&spm=a21to.8287046.AI.0&_wx_appbar=true";
//        url = "https://shop45833283.m.youzan.com/wscgoods/detail/276jakb39jlnf?scan=1&activity=none&from=kdt&qr=directgoods_638272694&showsku=true";
//青岛
// url = "https://shop90485387.m.youzan.com/wscgoods/detail/2fudlnlx0a8n7?scan=1&activity=none&from=kdt";
        mUrl = "https://shop90485387.m.youzan.com/wscgoods/detail/2g0jo1ifn7ftv?scan=1&activity=none&from=kdt&qr=directgoods_633465213";
        initWebview(mUrl);

        reloadableStateContentLayout.setInterceptTouch(true);
        swipeRefreshLayout.setOnRefreshListener(() -> wvContent.reload());
        reloadableStateContentLayout.setOnReloadListener(reloadableFrameLayout -> wvContent.reload());

        submit_btn.setOnClickListener(v -> {
            enter_url_et.requestFocus();
            if (Kits.Empty.check(cur_product_id) && Kits.Empty.check(enter_url_et.getText())) {
                ToastUtils.showLong("请输入商品编号!");
                return;
            }
            mUrl = !Kits.Empty.check(mUrl) ? mUrl : "https://shop90485387.m.youzan.com/wscgoods/detail/2g0jo1ifn7ftv?scan=1&activity=none&from=kdt&qr=directgoods_633465213";
            wvContent.loadUrl(mUrl);
            enter_url_et.setText("");
            isScan = false;
            //                wvContent.loadUrl("https://cashier.gaohuitong.com/assets/crossborder/payresult?prepay_id=PT1674348616220693");
        });

        enter_url_et.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(enter_url_et.getWindowToken(), 0);
            disableShowSoftInput(enter_url_et);
        });
        enter_url_et.setOnEditorActionListener((v, actionId, event) -> {
            Log.i("aaa", actionId + "," + "event.getAction()" + "---> onEditorAction: ----->" + v.getText().toString());
            if (isScan) return false;
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        isScan = true;
                        v.postDelayed(() -> {
                            String cur_product_id = enter_url_et.getText().toString();
                            if (App.products.containsKey(cur_product_id)) {
                                if (arrayMap.containsKey(cur_product_id)) {
                                    arrayMap.get(cur_product_id).count++;
                                } else {
                                    GoodsBean bean = App.products.get(cur_product_id);
                                    bean.count++;
                                    arrayMap.put(cur_product_id, bean);
                                }
                                mUrl = arrayMap.get(cur_product_id).url;
                                Log.i("aaa", "onEditorAction: +++++++++>" + arrayMap.get(cur_product_id).count);
                                submit_btn.performClick();
//                                enter_url_et.setHint(cur_product_id);
//                                enter_url_et.setHintTextColor(Color.CYAN);
                                this.cur_product_id = cur_product_id;
                            } else {
                                isScan = false;
                                enter_url_et.setText("");
                                enter_url_et.requestFocus();
                                ToastUtils.showLong("无此商品");
                            }
                        }, 1000);
                        return false;///返回true，保留软键盘。false，隐藏软键盘
                }
            }
            return false;
        });

        playerHikvision = new PlayerHikvision(surfaceView, handler);

        name_tv.setOnClickListener(v -> {
            if (null == startTime || startTime.equals(endTime) || playerHikvision.isLive())
                return;
//            endTime = startTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            String[] begins = startTime.split("_");
            String[] ends = endTime.split("_");
            if (playerHikvision.isplayback()) {
                surfaceView.setVisibility(View.GONE);
                playerHikvision.stopPlayback(playerHikvision.mPlaybackId, playerHikvision.mPort);
                playerHikvision.cleanup();
            } else {
                surfaceView.setVisibility(View.VISIBLE);
                playerHikvision.playback(IP_ADDRESS, PORT, USER_NAME, PASSWORD, 1,
                        Integer.parseInt(begins[0]), Integer.parseInt(begins[1]), Integer.parseInt(begins[2]),
                        Integer.parseInt(begins[3]), Integer.parseInt(begins[4]), Integer.parseInt(begins[5]),
                        Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]),
                        Integer.parseInt(ends[3]), Integer.parseInt(ends[4]), Integer.parseInt(ends[5]));
            }
        });
        test_tv.setOnClickListener(v -> {
            OnBnBegin(v);
        });
        title_tv.setOnClickListener(v -> {
//            retrycount = 0;
//                getPayResult("");
            title_tv.setEnabled(false);
            try {
                Executors.newSingleThreadExecutor().execute(() -> {
                    mcom.Open("/dev/ttyS4", 9600, 0);
                });
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mcom.SetCallBack(new IOCallBack() {
            @Override
            public void OnOpen() {
                title_tv.post(() -> {
                    ToastUtils.showLong("Connected success !");
                    title_tv.setEnabled(false);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        final int bPrintResult = Prints.PrintTicket(
                                App.getApp(), mpos, 384, false, false, true, 1, 1, 0);
                        final boolean bIsOpened = mpos.GetIO().IsOpened();

                        title_tv.post(() -> {
                            // TODO Auto-generated method stub
                            Toast.makeText(
                                    App.getApp(),
                                    (bPrintResult >= 0) ? " printsuccess" + Prints.ResultCodeToString(bPrintResult) : ("printfailed")
                                            + " "
                                            + Prints.ResultCodeToString(bPrintResult),
                                    Toast.LENGTH_SHORT).show();
                            title_tv.setEnabled(bIsOpened);
                        });

                    });
                });
            }

            @Override
            public void OnOpenFailed() {
                title_tv.post(() -> {
                    ToastUtils.showLong("Connected failed !");
                    title_tv.setEnabled(true);
                });
            }

            @Override
            public void OnClose() {
                title_tv.post(() -> {
                    ToastUtils.showLong("Connected close !");
                    title_tv.setEnabled(true);
                });
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_billing_layout;
    }

    @Override
    public void onPause() {
        arrayMap.clear();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        wvContent.clearAllData(context, true);
        IDCardReaderFactory.destroy(idCardReader);
        context.unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    public static BillingFragment newInstance() {
        return new BillingFragment();
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(webView.getContext());
            dialog.create();
            ToastUtils.showLong("onJsAlert");

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //设置响应js 的Confirm()函数
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            ToastUtils.showLong("onJsConfirm");
            return super.onJsConfirm(view, url, message, result);
        }

        //设置响应js 的Prompt()函数
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            ToastUtils.showLong("onJsPrompt");
            return onJsPrompt(view, url, message, defaultValue, result);
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i == 100) {
//                ToastUtils.showLong("page load 100%");
            }
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.i("aaa:", "js console log: " + cm.message()); // cm.message()为js日志
            return true;
        }

    };

    // 隐藏底部栏方法
    private void showBottom() {
        try {
            //定义javaScript方法
            String javascript = "javascript:function showBottom() { "
                    + "console.log('show bottom');"  // 看方法是否执行
                    + "console.log(document.getElementsByClassName('van-button van-button--default van-button--large van-button--square van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice').length);" // 打印出数组的长度
                    + "console.log(document.getElementsByClassName('van-stepper__input').length);" // 打印出数组的长度
                    + "console.log(document.getElementsByClassName('van-stepper__plus').length);" // 打印出数组的长度
//                    + "console.log(document.getElementsByClassName('van-button van-button--primary van-button--normal van-button--disabled van-button--block').length);"
//                    + "document.getElementsByClassName('van-stepper__input')[0].item('value').value='2'"
//                    + "document.getElementsByClassName('van-button van-button--default van-button--large van-button--square van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice')[0].click();"
//                    + "document.getElementsByClassName('van-stepper__plus')[0].click();"
                    + "window.android.collectBtnComplete(document.getElementsByClassName('van-button van-button--default van-button--large van-button--square van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice').length>'0'" +
                    "||document.getElementsByClassName('van-button van-button--default van-button--large van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice').length>'0');"
                    + "window.android.JsToJavaInterface(document.getElementsByClassName('van-stepper__plus').length>'0');"
                    + "}";
//            String strJS = String.format("javascript:document.getElementsByClassName('phone').placeholder='%s';", 5);
//            wvContent.evaluateJavascript(strJS, null); // null 这里传监听方法
            //加载方法
            wvContent.loadUrl(javascript);
            //执行方法
            wvContent.loadUrl("javascript:showBottom();");

//            wvContent.postDelayed(() -> {
//                String js1 = "javascript:function check(){window.android.JsToJavaInterface(document.getElementsByClassName('van-stepper__plus').length>'0')}";
//                wvContent.loadUrl(js1);
//                wvContent.loadUrl("javascript:check();");
//            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCashierData(String url) {
        if (isScan) return;
        isScan = true;
//        ToastUtils.showLong("getCashierData");
        String js = "javascript:function fetch(){console.log('cashier');" +
                "console.log(document.getElementsByClassName('zan-cashier__a').length);" +
                "window.android.getCashierData(document.getElementsByClassName('zan-cashier__a').length>'0')}";
        wvContent.loadUrl(js);
        wvContent.loadUrl("javascript:fetch();");

        this.mUrl = url;
    }

    public void enterBankNo(String bankNo) {
        String js = String.format("javascript:function input(){console.log('bankno');" +
                "console.log(document.getElementsByClassName('van-field__control').length);" +
                "window.android.enterBankNo(document.getElementsByClassName('van-field__control').length>'0','%s')}", bankNo);
        wvContent.loadUrl(js);
        wvContent.loadUrl("javascript:input()");
    }

    int retrycount = 0;
    ProgressDialog progressDialog = null;

    public void getPayResult(String url) {
//        String js = "javascript:function pay(){console.log('pay');" +
//                "console.log(document.getElementsByClassName('price__currency').length);) }";
//        wvContent.loadUrl(js);

        handler.removeCallbacksAndMessages(null);
        wvContent.clearAllData(context, true);
        arrayMap.clear();
        Flowable<HttpResultModel> f_token = DataService.builder().buildReqUrl("http://oms.hbyunjie.com/login/getToken")
                .buildReqParams("appKey", "POS")
                .buildReqParams("security", "81014bf5f79050e6a85739320d8c6540")
                .request(ApiService.HttpMethod.POST).flatMap((Function<HttpResultModel, Flowable<HttpResultModel>>) httpResultModel -> {
                    LoginBean loginBean = new LoginBean();
                    loginBean.access_token = (String) ((LinkedTreeMap) httpResultModel.resultData).get("token");
                    ShareUtils.saveLoginInfo(loginBean);
                    Document doc = Jsoup.connect(url).get();
                    String payinfo = doc.head().childNode(33).childNode(0).attr("data");
                    Log.e("aaa", "getPayResult: " + payinfo);
                    outBizNo = "";
                    if (payinfo != null) {
//                        {"payStatus":"success","kdtId":"45641115","goodsName":"测试商品哈哈dog","authUserTrueName":"李**","mchName":"云海宠物","payAmount":10,"acquireNo":"200807145432000112","outBizNo":"E20200807145431003304201","payFinishTime":"2020-08-10 09:40:53"}
                        JSONObject jsonObject = new JSONObject(payinfo.replace("window._global =", ""));
                        outBizNo = jsonObject.optString("outBizNo");
                    }
                    return DataService.builder().buildReqUrl("http://oms.hbyunjie.com/yz/getOrdersV2")
                            .builderRequestBody(new PayinfoRequestBody("PIB000513", outBizNo))
                            .request(ApiService.HttpMethod.POST_JSON);
                });
        if (retrycount == 0 && progressDialog == null){
            progressDialog = ProgressDialog.show(context, "retry:" + retrycount, "数据处理中。。。");
        }
        RxLoadingUtils.subscribe(f_token, bindToLifecycle(), httpResultModel -> {
                    arrayMap.clear();
                    Log.e("aaa", url + " , getPayResult: ---> " + httpResultModel.resultContent + ", " + httpResultModel.resultCode);
                    if (httpResultModel.isSucceful()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                        ToastUtils.showLong("数据处理成功！");
                    } else {
                        if (retrycount < 10) {
                            wvContent.postDelayed(() -> {
                                getPayResult(url);
                            }, 3000);
                            retrycount++;
                        } else {
                            progressDialog.dismiss();
                            progressDialog = null;
                            ToastUtils.showLong("数据处理失败！");
                        }

                    }
                    Log.i("aaa", "getPayResult: " + httpResultModel.resultContent);
                }, netError -> {
                    if (retrycount < 10) {
                        wvContent.postDelayed(() -> {
                            getPayResult(url);
                        }, 3000);
                        retrycount++;
                    } else {
                        progressDialog.dismiss();
                        progressDialog = null;
                        ToastUtils.showLong("数据处理失败！");
                    }
                    Log.i("aaa", "getPayResult: " + netError.getMessage());
                }, true
        );
    }

    private void initWebview(String url) {

//        WebSettings settings = wvContent.getSettings();
//        settings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
//        //支持获取手势焦点
//        wvContent.requestFocusFromTouch();
        wvContent.setWebChromeClient(webChromeClient);
        wvContent.setWebViewClient(webViewClient);
        wvContent.setDownloadListener(new MyWebViewDownLoadListener());
        wvContent.addJavascriptInterface(new Android2Js(), "android");
//        wvContent.setDrawListener(surl -> {
//            if (surl.startsWith("https://cashier.youzan.com/pay/wsctrade_buy")) {
//                getCashierData();
//            } else if (surl.startsWith("")) {
//                showBottom();
//            }
//        });
//        //开启无痕enable：false，关闭无痕开启enable：true
//        wvContent.getSettingsExtension().setShouldTrackVisitedLinks(false);
        wvContent.loadUrl(url);

    }


    private WebViewClient webViewClient = new WebViewClient() {

        /**
         * 防止加载网页时调起系统浏览器
         */
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest webResourceRequest) {
//            String url = webResourceRequest.getUrl().toString();
//            if (url.startsWith("http:") || url.startsWith("https:")) {
//                view.loadUrl(url);
//
//                Map<String, String> extraHeaders = new HashMap<String, String>();
//                extraHeaders.put("Referer", "");//这里可换成自己域名 不写也没事
//                view.loadUrl(url, extraHeaders);
//                return false;
//            } else {
//                if (url.startsWith("weixin://wap/pay?")) {//这里拦截了支付 不跳转支付页面直接打开支付功能
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    context.startActivity(intent);
//                }
//                return true;
//            }
//        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            reloadableStateContentLayout.showLoading();
        }

        /**
         * 断网状态下回调，回调2次，onPageFinished前后
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            reloadableStateContentLayout.showError();
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            sslErrorHandler.proceed();// 接受所有网站的证书
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            ToastUtils.showLong(url + "<======================>onPageFinished");
            reloadableStateContentLayout.showContent();
            swipeRefreshLayout.setRefreshing(false);
            Log.i("aaa", "onPageFinished: --->" + url);
            isScan = false;

            if (url.startsWith("https://cashier.youzan.com/pay/wsctrade_") || url.contains("/wsctrade/cart?kdt_id")) {
                getCashierData(url);
            } else if (url.startsWith("https://shop")) {
                showBottom();
            } else if (url.startsWith("https://cashier.gaohuitong.com/assets/crossborder/payresult")) {
                retrycount = 0;
                getPayResult(url);
            } else if (url.startsWith("https://cashier.youzan.com/assets/bankcard")) {//login page
                final EditText editText = new EditText(context);
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        Log.i("aaa", actionId + "," + "event.getAction()" + "---> onEditorAction: ----->" + v.getText().toString());
                        if (isScan) return false;
                        if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            switch (event.getAction()) {
                                case KeyEvent.ACTION_DOWN:
                                    isScan = true;
                                    v.postDelayed(() -> {
                                        String bankNo = v.getText().toString();
                                        enterBankNo(bankNo);
                                    }, 1500);
                                    return false;///返回true，保留软键盘。false，隐藏软键盘
                            }
                        }
                        return false;
                    }
                });
//                DialogFragmentHelper.builder(new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示")
//                        .setView(editText).create(), false).show(getChildFragmentManager(), "");
            }
//            view.loadUrl("javascript:document.getElementsByClassName(\"van-stepper__input\").send_keys(\"Selenium\")\n");
//            view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (wvContent.getContentHeight() > 0) {
//                        view.postDelayed(() -> {
//                            hideBottom();
//                        }, 2000);
//
//                    } else {
//                        view.postDelayed(this, 1000);
//                    }
//                }
//            }, 1000);

        }
    };

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    @Override
    public boolean onBackPress(Activity activity) {
        if (wvContent.canGoBack()) {
/*            if (wvContent.getUrl().equals(mUrl)) {

            } else */
            {
                wvContent.post(() -> {
                    wvContent.goBack();
                });
            }
        } else {
            DialogFragmentHelper.builder(context -> new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("确定退出应用程序吗?")
                    .setNegativeButton("取消", null).setPositiveButton("确定", (dialog, which) -> {
                        getActivity().finish();
                    }).create(), true).show(getChildFragmentManager(), "");
        }
        return true;
    }

    public class Android2Js extends Object {
        @JavascriptInterface
        public void enterBankNo(boolean caninput, String bankNo) {
            if (caninput) {
                handler.postDelayed(() -> {
                    String js = String.format("javascript:document.getElementsByClassName('van-field__control').value='%s'", bankNo);
                    wvContent.evaluateJavascript(js, null);
                }, 1);
            } else {
                handler.postDelayed(() -> {
                    Log.e("aaa", "enterBankNo: false");
                    String js = String.format("javascript:function input(){" +
                            "window.android.enterBankNo(document.getElementsByClassName('van-field__control').length>'0','%s')}", bankNo);
                    wvContent.loadUrl(js);
                    wvContent.loadUrl("javascript:input()");
                }, 1);
            }
        }

        @JavascriptInterface
        public void JsToJavaInterface(final boolean param) {
//            ToastUtils.showLong(String.valueOf(param));
//            if (!wvContent.getUrl().startsWith("https://shop")) {
//                return;
//            }
            if (param) {
                handler.postDelayed(() -> {
                    int count = arrayMap.get(cur_product_id) == null ? 0 : arrayMap.get(cur_product_id).count;
                    Log.i("aaa", "JsToJavaInterface: count===" + count);
                    String js = String.format("javascript:function add(){console.log('add');" +
                            "console.log(document.getElementsByClassName('van-stepper__plus').length);" +
                            "for(var i=0;i<'%d';++i){document.getElementsByClassName('van-stepper__plus')[0].click();}}", count - 1);
                    wvContent.loadUrl(js);
                    wvContent.loadUrl("javascript:add();");
                }, 1);
            } else {
                handler.postDelayed(() -> {
                    Log.i("aaa", "JsToJavaInterface: check()");
                    String js1 = "javascript:function check(){window.android.JsToJavaInterface(document.getElementsByClassName('van-stepper__plus').length>'0')}";
                    wvContent.loadUrl(js1);
                    wvContent.loadUrl("javascript:check();");
                }, 1);
            }
        }

        @JavascriptInterface
        public void collectBtnComplete(boolean canpopup) {
//            if (!wvContent.getUrl().startsWith("https://shop")) {
//                return;
//            }
            if (canpopup) {
                handler.postDelayed(() -> {
                    Log.i("aaa", "collectBtnComplete: is true");
                    wvContent.evaluateJavascript("document.getElementsByClassName('van-button van-button--default van-button--large van-button--square van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice')[0].click();", null);
                    wvContent.evaluateJavascript("document.getElementsByClassName('van-button van-button--default van-button--large van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice')[0].click();", null);
                }, 1);
            } else {
                handler.postDelayed(() -> {
                    Log.i("aaa", "collectBtnComplete: is false");
                    String js = "javascript:function check(){window.android.collectBtnComplete(document.getElementsByClassName('van-button van-button--default van-button--large van-button--square van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice').length>'0'" +
                            "||document.getElementsByClassName('van-button van-button--default van-button--large van-goods-action-button van-goods-action-button--first goods-buttons__big theme__button--vice').length>'0');};";
                    wvContent.loadUrl(js);
                    wvContent.loadUrl("javascript:check();");
                }, 1);
            }
        }

        @JavascriptInterface
        public void getCashierData(boolean ispay) {
            if (ispay) {
                handler.postDelayed(() -> {
                    String url = wvContent.getUrl();
                    Log.i("aaa", "getpayinfo: is true--->" + url.equals(mUrl));
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {

                            Document doc = Jsoup.connect(BillingFragment.this.mUrl).get();
                            Log.e("aaa", "::getpayinfo: " + doc.toString());
//                            String payinfo = doc.head().childNode(33).toString();
//                            Log.e("aaa", "getPayResult: " + payinfo);
//                            String outBizNo = "";
//                            if (!Kits.Empty.check(payinfo)) {
                            JSONObject jsonObject = new JSONObject("");
//                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            isScan = false;
                        }
                    });
                }, 1);
            } else {
                handler.postDelayed(() -> {
                    Log.i("aaa", "getpayinfo: is false");
                    String js = "javascript:function fetch(){console.log('payinfo is false');" +
                            "console.log(document.getElementsByClassName('zan-cashier__a').length);" +
///                            "console.log(document.getElementsByClassName('zan-cashier__a')[0].style.display=='display');" +
                            "window.android.getCashierData(document.getElementsByClassName('zan-cashier__a').length>'0')}";
                    wvContent.loadUrl(js);
                    wvContent.loadUrl("javascript:fetch();");
                }, 2000);
            }
        }
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
        }
    }

    private static final int VID = 1024;    //IDR VID
    private static final int PID = 50010;     //IDR PID
    private IDCardReader idCardReader = null;
    private boolean bopen = false;
    private boolean bStoped = false;
    private int mReadCount = 0;
    private CountDownLatch countdownLatch = null;

    private UsbManager musbManager = null;
    private final String ACTION_USB_PERMISSION = "com.example.scarx.idcardreader.USB_PERMISSION";

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        OpenDevice();
                    } else {
                        Toast.makeText(context, "USB未授权", Toast.LENGTH_SHORT).show();
                        //mTxtReport.setText("USB未授权");
                    }
                }
            }
        }
    };

    private void RequestDevicePermission() {
        musbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        for (UsbDevice device : musbManager.getDeviceList().values()) {
            if (device.getVendorId() == VID && device.getProductId() == PID) {
                Intent intent = new Intent(ACTION_USB_PERMISSION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                musbManager.requestPermission(device, pendingIntent);
            }
        }
    }

    private void startIDCardReader() {
        if (null != idCardReader) {
            IDCardReaderFactory.destroy(idCardReader);
            idCardReader = null;
        }
        // Define output log level
        LogHelper.setLevel(Log.VERBOSE);
        // Start fingerprint sensor
        Map idrparams = new HashMap();
        idrparams.put(ParameterHelper.PARAM_KEY_VID, VID);
        idrparams.put(ParameterHelper.PARAM_KEY_PID, PID);
        idCardReader = IDCardReaderFactory.createIDCardReader(context, TransportType.USB, idrparams);
    }

    public void writeLogToFile(String log) {
        try {
            File dirFile = context.getExternalCacheDir();  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
//            String path = "/sdcard/zkteco/idrlog.txt";
            File file = new File(dirFile, "idrlog.txt");
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file, true);
            log += "\r\n";
            outStream.write(log.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenDevice() {
        if (bopen) {
            test_tv.setText("设备已连接");
            return;
        }
        try {
            startIDCardReader();
            IDCardReaderExceptionListener listener = () -> {
                //出现异常，关闭设备
                CloseDevice();
                //当前线程为工作线程，若需操作界面，请在UI线程处理
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        test_tv.setText("设备发生异常，断开连接！");
                    }
                });
            };
            idCardReader.open(0);
            idCardReader.setIdCardReaderExceptionListener(listener);
            bStoped = false;
            mReadCount = 0;
            writeLogToFile("连接设备成功");
            test_tv.setText("连接成功");
            bopen = true;
            countdownLatch = new CountDownLatch(1);
            new Thread(new Runnable() {
                public void run() {
                    while (!bStoped) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        boolean ret = false;
                        final long nTickstart = System.currentTimeMillis();
                        try {
                            idCardReader.findCard(0);
                            idCardReader.selectCard(0);
                        } catch (IDCardReaderException e) {
                            //continue;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int retType = 0;
                        try {
                            retType = idCardReader.readCardEx(0, 0);
                        } catch (IDCardReaderException e) {
                            writeLogToFile("读卡失败，错误信息：" + e.getMessage());
                        }
                        if (retType == 1 || retType == 2 || retType == 3) {
                            final long nTickUsed = (System.currentTimeMillis() - nTickstart);
                            final int final_retType = retType;
                            writeLogToFile("读卡成功：" + (++mReadCount) + "次" + "，耗时：" + nTickUsed + "毫秒");
                            context.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (final_retType == 1) {
                                        final IDCardInfo idCardInfo = idCardReader.getLastIDCardInfo();
                                        //姓名adb
                                        String strName = idCardInfo.getName();
                                        //民族
                                        String strNation = idCardInfo.getNation();
                                        //出生日期
                                        String strBorn = idCardInfo.getBirth();
                                        //住址
                                        String strAddr = idCardInfo.getAddress();
                                        //身份证号
                                        String strID = idCardInfo.getId();
                                        //有效期限
                                        String strEffext = idCardInfo.getValidityTime();
                                        //签发机关
                                        String strIssueAt = idCardInfo.getDepart();
                                        test_tv.setText("读取次数：" + mReadCount + ",耗时：" + nTickUsed + "毫秒, 卡类型：居民身份证,姓名：" + strName +
                                                "，民族：" + strNation + "，住址：" + strAddr + ",身份证号：" + strID);
                                        if (idCardInfo.getPhotolength() > 0) {
                                            byte[] buf = new byte[WLTService.imgLength];
                                            if (1 == WLTService.wlt2Bmp(idCardInfo.getPhoto(), buf)) {
                                                test_tv.setBackground(new BitmapDrawable(IDPhotoHelper.Bgr2Bitmap(buf)));
                                            }
                                        }
                                    } else if (final_retType == 2) {
                                        final IDPRPCardInfo idprpCardInfo = idCardReader.getLastPRPIDCardInfo();
                                        //中文名
                                        String strCnName = idprpCardInfo.getCnName();
                                        //英文名
                                        String strEnName = idprpCardInfo.getEnName();
                                        //国家/国家地区代码
                                        String strCountry = idprpCardInfo.getCountry() + "/" + idprpCardInfo.getCountryCode();//国家/国家地区代码
                                        //出生日期
                                        String strBorn = idprpCardInfo.getBirth();
                                        //身份证号
                                        String strID = idprpCardInfo.getId();
                                        //有效期限
                                        String strEffext = idprpCardInfo.getValidityTime();
                                        //签发机关
                                        String strIssueAt = "公安部";
                                        test_tv.setText("读取次数：" + mReadCount + ",耗时：" + nTickUsed + "毫秒, 卡类型：外国人永居证,中文名：" + strCnName + ",英文名：" +
                                                strEnName + "，国家：" + strCountry + ",证件号：" + strID);
                                        if (idprpCardInfo.getPhotolength() > 0) {
                                            byte[] buf = new byte[WLTService.imgLength];
                                            if (1 == WLTService.wlt2Bmp(idprpCardInfo.getPhoto(), buf)) {
//                                                imageView.setImageBitmap(IDPhotoHelper.Bgr2Bitmap(buf));
                                                test_tv.setBackground(new BitmapDrawable(IDPhotoHelper.Bgr2Bitmap(buf)));
                                            }
                                        }
                                    } else {
                                        final IDCardInfo idCardInfo = idCardReader.getLastIDCardInfo();
                                        //姓名
                                        String strName = idCardInfo.getName();
                                        //民族,港澳台不支持该项
                                        String strNation = "";
                                        //出生日期
                                        String strBorn = idCardInfo.getBirth();
                                        //住址
                                        String strAddr = idCardInfo.getAddress();
                                        //身份证号
                                        String strID = idCardInfo.getId();
                                        //有效期限
                                        String strEffext = idCardInfo.getValidityTime();
                                        //签发机关
                                        String strIssueAt = idCardInfo.getDepart();
                                        //通行证号
                                        String strPassNum = idCardInfo.getPassNum();
                                        //签证次数
                                        int visaTimes = idCardInfo.getVisaTimes();
                                        test_tv.setText("读取次数：" + mReadCount + ",耗时：" + nTickUsed + "毫秒, 卡类型：港澳台居住证,姓名：" + strName +
                                                "，住址：" + strAddr + ",身份证号：" + strID + "，通行证号码：" + strPassNum +
                                                ",签证次数：" + visaTimes);
                                        if (idCardInfo.getPhotolength() > 0) {
                                            byte[] buf = new byte[WLTService.imgLength];
                                            if (1 == WLTService.wlt2Bmp(idCardInfo.getPhoto(), buf)) {
//                                                imageView.setImageBitmap(IDPhotoHelper.Bgr2Bitmap(buf));
                                                test_tv.setBackground(new BitmapDrawable(IDPhotoHelper.Bgr2Bitmap(buf)));
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                    countdownLatch.countDown();
                }
            }).start();
        } catch (IDCardReaderException e) {
            writeLogToFile("连接设备失败");
            test_tv.setText("连接失败");
            test_tv.setText("开始读卡失败，错误码：" + e.getErrorCode() + "\n错误信息：" + e.getMessage() + "\n内部代码=" + e.getInternalErrorCode());
        }

    }

    public void OnBnBegin(View view) {
        if (bopen) {
            test_tv.setText("设备已连接");
            return;
        }
        RequestDevicePermission();

    }

    private void CloseDevice() {
        if (!bopen) {
            return;
        }
        bStoped = true;
        mReadCount = 0;
        if (null != countdownLatch) {
            try {
                countdownLatch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            idCardReader.close(0);
        } catch (IDCardReaderException e) {
            e.printStackTrace();
        }
        bopen = false;
    }


    public void OnBnStop(View view) {
        if (!bopen) {
            return;
        }
        CloseDevice();
        test_tv.setText("设备断开连接");

    }

}
