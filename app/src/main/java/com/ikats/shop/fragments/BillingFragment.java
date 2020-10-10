package com.ikats.shop.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.ikats.shop.App;
import com.ikats.shop.BuildConfig;
import com.ikats.shop.R;
import com.ikats.shop.adapters.BillingItemAdapter;
import com.ikats.shop.adapters.VipItemAdapter;
import com.ikats.shop.database.OrderTableEntiry;
import com.ikats.shop.database.OrderTableEntiry_;
import com.ikats.shop.database.PrintTableEntiry;
import com.ikats.shop.database.VipTableEntiry;
import com.ikats.shop.database.VipTableEntiry_;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.GoodsBean;
import com.ikats.shop.model.OrderResultBean;
import com.ikats.shop.model.PayResultBean;
import com.ikats.shop.model.PrintBean;
import com.ikats.shop.model.VerifyResultBean;
import com.ikats.shop.model.VipBean;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.net.model.CancelOrderRequestBody;
import com.ikats.shop.net.model.OrderinfoRequestBody;
import com.ikats.shop.services.JWebSocketClientService;
import com.ikats.shop.utils.PlayerHikvision;
import com.ikats.shop.utils.Prints;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.utils.SnowFlake;
import com.ikats.shop.utils.Utils;
import com.ikats.shop.views.X5WebView;
import com.ikats.shop.views.XReloadableListContentLayout;
import com.ikats.shop.views.XReloadableStateContorller;
import com.jaeger.library.StatusBarUtil;
import com.tamsiree.rxfeature.tool.RxQRCode;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxKeyboardTool;
import com.tamsiree.rxkit.view.RxToast;
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

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.objectbox.Box;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ikats.shop.activitys.DetailFragmentsActivity.TAG;

public class BillingFragment extends XBaseFragment {
    @BindView(R.id.billing_verify_by_idcards_iv)
    ImageView idcard_btn;
    @BindView(R.id.billing_verify_by_phone_iv)
    ImageView phone_btn;
    @BindView(R.id.billing_verify_by_wechat_iv)
    ImageView wechat_btn;
    @BindView(R.id.billing_sell_order_tv)
    TextView order_tv;
    @BindView(R.id.billing_title_name_tv)
    TextView name_tv;
    @BindView(R.id.billing_title_loading_iv)
    ImageView loadingIv;
    @BindView(R.id.billing_title_test_tv)
    TextView test_tv;
    @BindView(R.id.billing_enter_url_et)
    EditText enter_url_et;
    @BindView(R.id.billing_goods_buy_rv)
    XRecyclerView billing_rv;
    @BindView(R.id.billing_enter_submit_btn)
    Button submit_btn;
    @BindView(R.id.billing_enter_vip_btn)
    Button vip_btn;
    @BindView(R.id.billing_enter_billin_btn)
    Button billing_btn;
    @BindView(R.id.billing_bottom_amount_tv)
    TextView amount_tv;
    @BindView(R.id.billing_pay_tip_tv)
    TextView verify_tips_tv;
    @BindView(R.id.billing_sufaceview)
    SurfaceView surfaceView;
    @BindView(R.id.billing_xreload_layout)
    XReloadableStateContorller reloadableStateContentLayout;
    @BindView(R.id.billing_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.billing_webview)
    X5WebView wvContent;
    @BindView(R.id.billing_pay_qrcode_iv)
    ImageView qrcode_iv;
    @BindView(R.id.billing_goods_statis_tv_val)
    TextView statis_tv;
    @BindView(R.id.billing_goods_sale_tv_val)
    TextView sale_tv;
    @BindView(R.id.billing_goods_receivable_tv_val)
    TextView receivable_tv;
    @BindView(R.id.billing_goods_pay_tv_val)
    TextView pay_tv;
    @BindView(R.id.billing_bottom_shopping_bag_big_tv)
    TextView bag_big_tv;
    @BindView(R.id.billing_bottom_shopping_bag_small_tv)
    TextView bag_small_tv;
    String cur_product_id = "";
    String startTime, endTime;
    private PlayerHikvision playerHikvision;
    private boolean isMakeVideo;
    private BillingItemAdapter billingItemAdapter;
    private String video_pid, promo_code;
    private VerifyResultBean verifyResultBean;
    //    private SettingBean settingBean;
    private GoodsBean[] beans;
    private Dialog payDialog;
    private PrintBean printBean;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setTranslucent(context, 0);
        beans = new GoodsBean[]{new GoodsBean(), new GoodsBean()};
    }

    boolean isScan;
    private static final int PLAY_HIK_STREAM_CODE = 1001;
    private static final String IP_ADDRESS = "192.168.1.7";//"192.168.0.104";
    private static final int PORT = 8000;
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "ikats903";

    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case 1:
                String idcard = (String) msg.obj;
                ToastUtils.showLong(idcard);
                Flowable<HttpResultModel<VerifyResultBean>> IDCard = DataService.builder().buildReqUrl(App.getSettingBean().shop_url + "api/get/user")
                        .buildReqParams("IDCard", idcard)
                        .buildParseDataClass(VerifyResultBean.class)
                        .request(ApiService.HttpMethod.GET);
                RxLoadingUtils.subscribeWithDialog(context, IDCard, bindToLifecycle(), httpResultModel -> {
                    if (httpResultModel.isSucceful()) {
                        verifyResultBean = httpResultModel.resultData;
                    } else {
                        DialogFragmentHelper.builder(context1 -> new AlertDialog.Builder(context1, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("请登录手机商城，完善收件人身份信息。").setNegativeButton("取消", null)
                                .setPositiveButton("确定", (dialog1, which1) -> {
                                    dialog1.cancel();
                                }).create(), true).show(getChildFragmentManager(), "");
                    }
                    ToastUtils.showLong(httpResultModel.resultContent);
                    ((DialogFragment) getChildFragmentManager().findFragmentByTag("idcard")).dismiss();
                }, netError -> {
                    verifyResultBean = null;
                    ((DialogFragment) getChildFragmentManager().findFragmentByTag("idcard")).dismiss();
                    ToastUtils.showLong("身份证验证失败！");
                });
                break;
            case 0:
                if (BuildConfig.DEBUG)
                    ToastUtils.showLong(msg.obj + "");
                if (msg.obj.equals("远程下载完成")) {
                    isMakeVideo = false;
                    loadingIv.clearAnimation();
                    loadingIv.setVisibility(View.GONE);
                }
                break;
            case -1: {
                if (!TextUtils.isEmpty(order_tv.getText())) {
                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            String action = billing_btn.getText().toString();
//                                        if (action.equals("开单")) {
//                                            action = "结单";
//                                        } else {
                            action = "开单";
//                                        }
                            billing_btn.setText(action);

                            verifyResultBean = null;
                            billingItemAdapter.clearAndRestData();
                            promo_code = "";
                            String sell_id = order_tv.getText().toString();
                            order_tv.setText("");
                            OnBnStop(null);
                            statis_tv.setText("");
                            sale_tv.setText("");
                            receivable_tv.setText("");
                            enter_url_et.setHint("");
                            pay_tv.setText("");
                            verify_tips_tv.setText("");
                            qrcode_iv.setImageResource(0);
                            qrcode_iv.setBackgroundResource(R.drawable.shape_grey_stroke_6_radius_rect);
//                                        handler.removeCallbacksAndMessages(null);
                            endTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

                            String record_url = App.getSettingBean().record_ip.concat(":").concat(App.getSettingBean().record_port);
                            App.getBoxStore().runInTxAsync(() -> {
                                Box<OrderTableEntiry> box = App.getBoxStore().boxFor(OrderTableEntiry.class);
                                OrderTableEntiry orderTableEntiry = box.query().equal(OrderTableEntiry_.sell_id, sell_id).build().findFirst();
                                orderTableEntiry.endTime = endTime;
                                orderTableEntiry.endDate = new Date();
                                box.put(orderTableEntiry);

                                String url = String.format(record_url.concat("/taskkill?ffPid=%s&sellNo=%s&orderNo=%s"), video_pid, sell_id, Kits.Empty.check(orderTableEntiry._order_id) ? "" : orderTableEntiry._order_id);
                                Flowable<HttpResultModel> kill = DataService.builder().buildReqUrl(url)
                                        .buildInterceptconvert(true)
                                        .request(ApiService.HttpMethod.GET).map((Function<ResponseBody, HttpResultModel>) sresponseBody -> {
                                            HttpResultModel resultModel = new HttpResultModel();
                                            resultModel.resultCode = 1;
                                            resultModel.resultData = sresponseBody.string();
                                            return resultModel;
                                        });
                                RxLoadingUtils.subscribe(kill, bindToLifecycle(), null);
                            }, (result, error) -> {
                                if (error == null) {
                                    Log.e("aaa", "end txFinished is ok");
                                }
                            });
                        }
                    };
                    if (msg.obj != null) {
                        context.runOnUiThread(run);
                    } else {
                        DialogFragmentHelper.builder(context1 -> new AlertDialog.Builder(context1, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                .setTitle("提示").setMessage("是否结束交易").setNegativeButton("取消", null)
                                .setPositiveButton("确定", (dialog, which) -> {
                                    dialog.cancel();
                                    context.runOnUiThread(run);
                                }).create(), true).show(getChildFragmentManager(), "");
                    }
                } else {
                    if (Kits.Empty.check(verifyResultBean)) {
                        isScan = false;
                        enter_url_et.setText("");
                        enter_url_et.requestFocus();
                        ToastUtils.showLong("请先验证身份！");
                        return false;
                    }
                    String action = billing_btn.getText().toString();
//                    if (action.equals("开单")) {
                    action = "结单";
//                    } else {
//                        action = "开单";
//                    }
                    billing_btn.setText(action);
                    enter_url_et.requestFocus();

                    video_pid = null;
                    order_tv.setText("" + SnowFlake.getSnowFlake().nextId());
                    endTime = startTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                    loadingIv.clearAnimation();
                    loadingIv.setVisibility(View.GONE);
                    String record_url = App.getSettingBean().record_ip.concat(":").concat(App.getSettingBean().record_port);
                    Flowable<HttpResultModel> flowable = DataService.builder().buildReqUrl(String.format(record_url.concat("/record?sellNo=%s&channel=%s"), order_tv.getText().toString(), App.getSettingBean().record_channel))
                            .request(ApiService.HttpMethod.DOWNLOAD).map((Function<ResponseBody, HttpResultModel>) responseBody -> {
                                String result = responseBody.string();
                                HttpResultModel httpResultModel = new HttpResultModel();
                                httpResultModel.resultCode = 1;
                                httpResultModel.resultData = result;
                                video_pid = result;
                                return httpResultModel;
                            });
                    RxLoadingUtils.subscribe(flowable, bindToLifecycle(), null);
                    OrderTableEntiry orderTableEntiry = new OrderTableEntiry();
                    orderTableEntiry.ip = App.getSettingBean().camera_ip;
                    orderTableEntiry.prot = Integer.parseInt(App.getSettingBean().camera_port);
                    orderTableEntiry.channel = App.getSettingBean().camera_channel;
                    orderTableEntiry.streamType = PlayerHikvision.HIK_SUB_STREAM_CODE;
                    orderTableEntiry.username = App.getSettingBean().camera_user;
                    orderTableEntiry.password = App.getSettingBean().camera_pwd;
                    orderTableEntiry.startTime = startTime;
                    orderTableEntiry.startDate = new Date();
                    orderTableEntiry.endDate = new Date();
                    orderTableEntiry.endTime = endTime;
                    orderTableEntiry.outBizNo = "";
                    orderTableEntiry.payid = "";
                    orderTableEntiry.sell_id = order_tv.getText().toString();
                    orderTableEntiry.record_ip = App.getSettingBean().record_ip;
                    orderTableEntiry.record_prot = App.getSettingBean().record_port;
                    orderTableEntiry.record_channel = App.getSettingBean().record_channel;
                    App.getBoxStore().runInTxAsync(() -> {
                        App.getBoxStore().boxFor(OrderTableEntiry.class).put(orderTableEntiry);
                    }, (result, error) -> {
                        if (error == null) {
                            Log.e("aaa", "start txFinished is ok");
                        }
                    });
                }
            }
            break;
        }
        return false;
    });

    private void calculateAmount() {
        enter_url_et.requestFocus();
        Flowable.just(0f).map((Function<Float, Float>) aFloat -> {
            float account = aFloat;
            for (GoodsBean bean : billingItemAdapter.getDataSource()) {
                bean.amount = bean.count * bean.sell_price;
                account += bean.amount;
            }
            return account;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<Float>) aFloat -> {
            amount_tv.setText(String.format("￥%.2f", aFloat));
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(bag_big_tv.getText());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFF4785F")), 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        bag_big_tv.setText(spannableString);
        spannableString.clear();
        spannableString.append(bag_small_tv.getText());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFF4785F")), 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        bag_small_tv.setText(spannableString);
        billing_rv.verticalLayoutManager(context);
        billingItemAdapter = new BillingItemAdapter(context);
        billing_rv.setAdapter(billingItemAdapter);
        billing_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    enter_url_et.requestFocus();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        billingItemAdapter.setRecItemClick(new RecyclerItemCallback<GoodsBean, BillingItemAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, GoodsBean model, int tag, BillingItemAdapter.ViewHolder holder) {
                enter_url_et.requestFocus();
                RxToast.showToast("on click item");
                super.onItemClick(position, model, tag, holder);
            }
        });
        billingItemAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                calculateAmount();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                calculateAmount();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                calculateAmount();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                calculateAmount();
            }
        });

        reloadableStateContentLayout.setInterceptTouch(true);
        swipeRefreshLayout.setEnabled(false);

        enter_url_et.setOnEditorActionListener((v, actionId, event) ->
                {
                    if (Kits.Empty.check(verifyResultBean)) {
                        isScan = false;
                        enter_url_et.setText("");
                        enter_url_et.requestFocus();
                        v.setTag(null);
                        ToastUtils.showLong("请验证身份！");
                        return false;
                    }
                    if (TextUtils.isEmpty(order_tv.getText())) {
                        isScan = false;
                        enter_url_et.setText("");
                        enter_url_et.requestFocus();
                        v.setTag(null);
                        ToastUtils.showLong("请先生成销售单号！");
                        return false;
                    }
                    if (isScan && null != v.getTag()) return false;
                    if ((event != null && actionId == EditorInfo.IME_ACTION_DONE) || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        switch (event.getAction()) {
                            case KeyEvent.ACTION_DOWN:
                                isScan = true;

                                v.postDelayed(() -> {
                                    v.setTag(1);
                                    String cur_product_id = enter_url_et.getText().toString().trim();
                                    if (App.products.containsKey(cur_product_id)) {
                                        GoodsBean bean = App.products.get(cur_product_id);
                                        if (!billingItemAdapter.addOrupdateData(bean)) {
                                            ToastUtils.showLong("不是同一店铺商品！");
                                            MediaPlayer.create(context, R.raw.error).start();
                                        }
                                        billing_rv.scrollToPosition(0);
                                        enter_url_et.requestFocus();
                                        enter_url_et.setText("");
                                        isScan = false;
                                        enter_url_et.setHint(cur_product_id);
                                        enter_url_et.setHintTextColor(Color.LTGRAY);
                                        BillingFragment.this.cur_product_id = cur_product_id;
                                        v.setTag(null);
                                    } else {
                                        isScan = false;
                                        enter_url_et.setText("");
                                        enter_url_et.requestFocus();
                                        v.setTag(null);
                                        ToastUtils.showLong("无此商品备案");
                                        MediaPlayer.create(context, R.raw.error).start();
                                    }
                                }, 100);
                                return false;///返回true，保留软键盘。false，隐藏软键盘
                        }
                    }
                    return false;
                }
        );

        playerHikvision = new PlayerHikvision(surfaceView, handler);
        Prints.OpenPosCallBack(order_tv);
    }

    @OnClick({R.id.billing_enter_submit_btn, R.id.billing_enter_vip_btn, R.id.billing_sell_order_tv, R.id.billing_title_test_tv,
            R.id.billing_title_name_tv, R.id.billing_enter_url_et, R.id.billing_verify_by_phone_iv, R.id.billing_verify_by_wechat_iv,
            R.id.billing_verify_by_idcards_iv, R.id.billing_bottom_shopping_bag_big_tv, R.id.billing_bottom_shopping_bag_small_tv,
            R.id.billing_enter_billin_btn})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.billing_enter_submit_btn:
                if (Kits.Empty.check(verifyResultBean)) {
                    ToastUtils.showLong("请验证身份！");
                    return;
                }
                if (TextUtils.isEmpty(order_tv.getText())) {
                    ToastUtils.showLong("请先生成销售单号！");
                    return;
                }
                if (Kits.Empty.check(billingItemAdapter.getDataSource())) {
                    ToastUtils.showLong("请先选购商品！");
                    return;
                }
                if (Kits.Empty.check(App.getSettingBean().zipCode)) {
                    ToastUtils.showLong("请先设置店铺邮编!");
                    return;
                }
                if (Kits.Empty.check(App.getSettingBean().shop_address)) {
                    ToastUtils.showLong("请先设置店铺地址!");
                    return;
                }
                if (Kits.Empty.check(App.getSettingBean().shop_area)) {
                    ToastUtils.showLong("请先设置店铺地区!");
                    return;
                }
                if (Kits.Empty.check(App.getSettingBean().shop_name)) {
                    ToastUtils.showLong("请先设置店铺名称!");
                    return;
                }
                if (Kits.Empty.check(App.getSettingBean().shop_code)) {
                    ToastUtils.showLong("请先设置店铺编码!");
                    return;
                }
                printBean = null;
//                AtomicBoolean is_express = new AtomicBoolean(false);
//                DialogFragmentHelper.builder(R.layout.dialog_delivery_method_layout, true).setDialogWindow(dialogWindow -> {
//                    WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
//                    layoutParams.width = Utils.dip2px(context, 800);//WindowManager.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = Utils.dip2px(context, 400);//WindowManager.LayoutParams.MATCH_PARENT;;//
//                    dialogWindow.setAttributes(layoutParams);
//                    return dialogWindow;
//                }).setOnProcessView((dialog, view12) -> {
//                    Button shop_btn = view12.findViewById(R.id.dialog_delivery_method_shop_btn);
//                    Button express_btn = view12.findViewById(R.id.dialog_delivery_method_express_btn);
//                    shop_btn.setOnClickListener(v -> {
//                        is_express.set(false);
//                        dialog.cancel();
//                    });
//                    express_btn.setOnClickListener(v -> {
//                        is_express.set(true);
//                        dialog.cancel();
//                    });
//                }).setCancelListener(() -> {
                Flowable<HttpResultModel<OrderResultBean>> pay = DataService.builder().buildReqUrl(App.getSettingBean().shop_url/*"https://sit.shop.chigoose.com/"*/ + "api/create")
                        .builderRequestBody(OrderinfoRequestBody.newBuilder().setCurrentUser("mobile", verifyResultBean.mobile)
                                .setSkuNos(billingItemAdapter.getDataSource())
                                .setCode(promo_code)
                                .setReceiverId(verifyResultBean.receivers.size() > 0 ? String.valueOf(verifyResultBean.receivers.get(0).id) : "")
                                .setSellCode(order_tv.getText().toString())
                                .setChannelCode(App.getSettingBean().shop_code)
                                .setChannelName(App.getSettingBean().shop_name)
                                .setPosCode(RxDeviceTool.getMacAddress())
                                .setReceiver(false, verifyResultBean.name, App.getSettingBean().shop_area, App.getSettingBean().shop_address, App.getSettingBean().zipCode, verifyResultBean.mobile)
                                .builder())
                        .buildParseDataClass(OrderResultBean.class)
                        .request(ApiService.HttpMethod.POST_JSON);
                RxLoadingUtils.subscribeWithDialog(context, pay, bindToLifecycle(), httpResultModel -> {
                    if (httpResultModel.isSucceful()) {
                        promo_code = "";
                        statis_tv.setText(httpResultModel.resultData.amount);
                        sale_tv.setText(httpResultModel.resultData.couponDiscount);
                        receivable_tv.setText(httpResultModel.resultData.amountPayable);
                        pay_tv.setText(httpResultModel.resultData.amountPayable);
                        Bitmap bmp = RxQRCode.creatQRCode(httpResultModel.resultData.payableCode, BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
                        qrcode_iv.setImageBitmap(bmp);
                        printBean = new PrintBean();
                        printBean.list.addAll(billingItemAdapter.getDataSource());
                        printBean.sell_code = httpResultModel.resultData.orderSns.get(0);
                        printBean.total = httpResultModel.resultData.amount;
                        printBean.discounts = httpResultModel.resultData.couponDiscount;
                        printBean.cope = httpResultModel.resultData.amountPayable;
                        printBean.paid = httpResultModel.resultData.amountPayable;

                        JWebSocketClientService.setOrderSn(printBean.sell_code);
                        DialogFragmentHelper.builder(context1 -> {
                            ImageView qr_iv = new ImageView(context1);
                            qr_iv.setImageBitmap(bmp);
                            return payDialog = new AlertDialog.Builder(context1, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("请扫码支付").setView(qr_iv).setPositiveButton("取消订单", (dialog1, which1) -> {
                                payDialog = null;
                                DialogFragmentHelper.builder(context12 -> new AlertDialog.Builder(context12, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("是否需要取消订单？").setNegativeButton("取消", null).setPositiveButton("确定", (dialog12, which12) -> {
                                    Flowable<HttpResultModel> del_order = DataService.builder().buildReqUrl(App.getSettingBean().shop_url + "api/cancel")
                                            .builderRequestBody(new CancelOrderRequestBody(httpResultModel.resultData.orderSns.get(0)))
                                            .request(ApiService.HttpMethod.POST_JSON);
                                    RxLoadingUtils.subscribeWithDialog(context12, del_order, bindToLifecycle(), httpResultModel1 -> {
                                        if (httpResultModel1.isSucceful()) {
                                            ToastUtils.showLong("订单取消成功！");
                                        } else {
                                            ToastUtils.showLong(httpResultModel1.resultContent);
                                        }
                                    }, netError -> ToastUtils.showLong("订单取消失败，请重试！"), false);
                                }).create(), false).show(getChildFragmentManager(), "");
                            }).create();
                        }, false).setDialogWindow(dialogWindow -> {
                            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                            layoutParams.height = Utils.dip2px(context, 400);
                            layoutParams.width = Utils.dip2px(context, 400);
                            dialogWindow.setAttributes(layoutParams);
                            return dialogWindow;
                        }).show(getChildFragmentManager(), "");
                        App.getBoxStore().runInTxAsync(() -> {
                            Box<OrderTableEntiry> box = App.getBoxStore().boxFor(OrderTableEntiry.class);
                            OrderTableEntiry orderTableEntiry = box.query().equal(OrderTableEntiry_.sell_id, order_tv.getText().toString()).build().findFirst();
                            orderTableEntiry._order_id = httpResultModel.resultData.orderSns.get(0);
                            orderTableEntiry.crateTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                            box.put(orderTableEntiry);
                        }, (result, error) -> {
                            if (error == null) {

                                Log.e("aaa", "txFinished: is ok");
                            }
                        });
                    }
                    ToastUtils.showLong(httpResultModel.resultContent);
                }, netError -> {
                    ToastUtils.showLong(netError.getMessage());
                });
//                }).show(getChildFragmentManager(), "");
                break;
            case R.id.billing_enter_billin_btn:
                handler.sendEmptyMessage(-1);
                break;
            case R.id.billing_enter_vip_btn:
                if (true) {
                    DialogFragmentHelper.builder(context -> {
                        AlertDialog[] alertDialogs = new AlertDialog[1];
                        EditText editText = new EditText(context);
                        editText.setHint("请输入优惠码");
                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        editText.setShowSoftInputOnFocus(false);
                        editText.requestFocus();
                        editText.setSingleLine();
                        editText.postDelayed(() -> {
                            RxKeyboardTool.hideSoftInput(editText);
                        }, 100);
                        editText.setPadding(10, 0, 0, 0);
                        editText.setOnKeyListener((v, keyCode, event) -> {
                            // If the event is a key-down event on the "enter" button
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                    (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                                alertDialogs[0].getButton(BUTTON_POSITIVE).performClick();
                                return false;
                            }
                            return false;
                        });
                        return alertDialogs[0] = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setView(editText)/*.setNegativeButton("取消", (dialog, which) -> dialog.cancel())*/
                                .setPositiveButton("确定", (dialog, which) -> {
                                    dialog.cancel();
                                    promo_code = editText.getText().toString();
                                }).create();
                    }, true)/*.setCancelListener(() -> view.postDelayed(() -> {
                        RxKeyboardTool.hideSoftInput(context);
                    }, 50))*/.show(getChildFragmentManager(), "");
                    return;
                }
                DialogFragmentHelper.builder(R.layout.dialog_search_vip_layout, true).setDialogWindow(dialogWindow -> {
                    WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
                    wlp.width = App.w - 100;
                    wlp.height = App.h - 100;
                    dialogWindow.setAttributes(wlp);
                    dialogWindow.setWindowAnimations(R.style.BottomInAndOutStyle);
                    return dialogWindow;
                }).setOnProcessView((dialog, view1) -> {
                    view1.findViewById(R.id.dialog_vip_close_iv).setOnClickListener(v -> {
                        dialog.cancel();
                    });
                    XReloadableListContentLayout xRecyclerView = view1.findViewById(R.id.dialog_vip_search_reload_list_layout);
                    VipItemAdapter vipItemAdapter = new VipItemAdapter(context);
                    xRecyclerView.getRecyclerView().verticalLayoutManager(context);
                    xRecyclerView.getRecyclerView().setAdapter(vipItemAdapter);
                    xRecyclerView.getRecyclerView().setRefreshEnabled(false);
                    List<VipBean> list = new ArrayList<VipBean>();

                    ((EditText) view1.findViewById(R.id.dialog_vip_search_enter_et)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String phone = TextUtils.isEmpty(s) ? "" : s.toString();
                            if (phone.length() == 11) {
                                list.clear();
                                App.getBoxStore().runInTxAsync(() -> {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        App.getBoxStore().boxFor(VipTableEntiry.class).query().contains(VipTableEntiry_.phone, phone).build().find().forEach(vipTableEntiry -> {
                                            VipBean bean = new VipBean();
                                            bean.name = vipTableEntiry.name;
                                            bean.phone = vipTableEntiry.phone;
                                            bean.level = vipTableEntiry.level;
                                            bean.integral = vipTableEntiry.integtal;
                                            bean.balance = vipTableEntiry.balance;
                                            list.add(bean);
                                        });
                                    }
                                }, (result, error) -> {
                                    if (error == null) {
                                        vipItemAdapter.addData(list);
                                    }
                                });
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    view1.findViewById(R.id.dialog_vip_select_btn).setOnClickListener(v -> {
                        ToastUtils.showLong("" + list.size());
                        dialog.dismiss();
                    });

                }).show(getChildFragmentManager(), "");
                break;
            case R.id.billing_enter_url_et:
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enter_url_et.getWindowToken(), 0);
                disableShowSoftInput(enter_url_et);
                break;
            case R.id.billing_sell_order_tv://
                //            retrycount = 0;
//                getPayResult("");
//            Flowable<HttpResultModel> flowable = DataService.builder().buildReqUrl("http://oms.hbyunjie.com/videofile/uploadvideofile")
//                    .buildReqParams(new File(context.getExternalCacheDir(), "123.mp4")).request(ApiService.HttpMethod.UPLOAD);
//            RxLoadingUtils.subscribeWithDialog(context, flowable, bindToLifecycle(), new Consumer<HttpResultModel>() {
//                @Override
//                public void accept(HttpResultModel httpResultModel) throws Exception {
//
//                }
//            }, false);
//            title_tv.setEnabled(false);
//                PrintBean printBean = new PrintBean();
//                printBean.list = billingItemAdapter.getDataSource();
//                printBean.
//                Prints.PostPrint(context, "");
                break;
            case R.id.billing_title_test_tv:
                OnBnBegin(view);
                //            if (null != mMediaPlayer) {
//                if (mMediaPlayer.isPlaying())
//                    mMediaPlayer.stop();
//                mMediaPlayer.release();
//                mMediaPlayer = null;
//                surfaceView.setVisibility(View.GONE);
//                return;
//            }
//
//            surfaceView.setVisibility(View.VISIBLE);
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            try {
//                mMediaPlayer.setDataSource("http://192.168.0.101:3000/video/" + startTime + ".mp4");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mMediaPlayer.setSurface(surfaceView.getHolder().getSurface());
////            mMediaPlayer.setLooping(true);
//            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mMediaPlayer.start();
//                }
//            });
//            mMediaPlayer.prepareAsync();
//            mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
//                ToastUtils.showLong("");
//                return false;
//            });
                break;
            case R.id.billing_title_name_tv:
                if (null == startTime || startTime.equals(endTime) || playerHikvision.isLive() || isMakeVideo)
                    return;
                if (playerHikvision.isplayback()) {
                    surfaceView.setVisibility(View.GONE);
                    playerHikvision.stopPlayback(playerHikvision.mPlaybackId, playerHikvision.mPort);
                    playerHikvision.refresh();
                } else {
                    String[] begins = startTime.split("_");
                    String[] ends = endTime.split("_");
                    surfaceView.setVisibility(View.VISIBLE);
                    playerHikvision.playback(IP_ADDRESS, PORT, USER_NAME, PASSWORD, 1,
                            Integer.parseInt(begins[0]), Integer.parseInt(begins[1]), Integer.parseInt(begins[2]),
                            Integer.parseInt(begins[3]), Integer.parseInt(begins[4]), Integer.parseInt(begins[5]),
                            Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]),
                            Integer.parseInt(ends[3]), Integer.parseInt(ends[4]), Integer.parseInt(ends[5]));
                }
                break;
            case R.id.billing_verify_by_idcards_iv:
                if (null != verifyResultBean) {
                    ToastUtils.showLong("请先结束交易！");
                    return;
                }
                OnBnBegin(view);
                DialogFragmentHelper.builder(context -> {
                    ImageView dimageView = new ImageView(context);
                    Glide.with(context).asGif().load(R.drawable.qk_qrcode).into(new SimpleTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                            dimageView.setImageDrawable(resource);
                            resource.start();
                        }
                    });
                    dimageView.setBackground(view.getBackground());
                    AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setView(dimageView).create();
                    return alertDialog;
                }, true).setDialogWindow(dialogWindow -> {
                    WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialogWindow.setAttributes(layoutParams);
                    return dialogWindow;
                }).show(getChildFragmentManager(), "idcard");
                break;
            case R.id.billing_verify_by_phone_iv:
                if (null != verifyResultBean) {
                    ToastUtils.showLong("请先结束交易！");
                    return;
                }
                DialogFragmentHelper.builder(context -> {
                    AlertDialog[] alertDialogs = new AlertDialog[1];
                    EditText editText = new EditText(context);
                    editText.setHint("请输入手机号验证");
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                    editText.setShowSoftInputOnFocus(false);
                    editText.requestFocus();
                    editText.setSingleLine();
                    editText.postDelayed(() -> {
                        RxKeyboardTool.hideSoftInput(editText);
                    }, 100);
                    editText.setPadding(10, 0, 0, 0);
                    editText.setOnKeyListener((v, keyCode, event) -> {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                            alertDialogs[0].getButton(BUTTON_POSITIVE).performClick();
                            return false;
                        }
                        return false;
                    });
                    return alertDialogs[0] = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setView(editText)/*.setNegativeButton("取消", (dialog, which) -> dialog.cancel())*/
                            .setPositiveButton("确定", (dialog, which) -> {
                                dialog.cancel();
                                Flowable<HttpResultModel<VerifyResultBean>> phone = DataService.builder().buildReqUrl(App.getSettingBean().shop_url + "api/get/user")
                                        .buildReqParams("mobile", /*"13121528060"*/editText.getText().toString().trim())
                                        .buildParseDataClass(VerifyResultBean.class)
                                        .request(ApiService.HttpMethod.GET);
                                RxLoadingUtils.subscribeWithDialog(context, phone, bindToLifecycle(), httpResultModel -> {
                                    if (httpResultModel.isSucceful()) {
//                                        receivers = String.valueOf(httpResultModel.resultData.receivers.get(0).id);
                                        verifyResultBean = httpResultModel.resultData;
                                        if (Kits.Empty.check(verifyResultBean.name) || Kits.Empty.check(verifyResultBean.idCard)) {
                                            verify_tips_tv.setText("未绑定身份证用户！");
//                                            verifyResultBean = null;
//                                            DialogFragmentHelper.builder(context1 -> new AlertDialog.Builder(context1, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("请登录手机商城，完善收件人身份证信息。").setNegativeButton("取消", null)
//                                                    .setPositiveButton("确定", (dialog1, which1) -> {
//                                                        dialog1.cancel();
//                                                    }).create(), true).show(getChildFragmentManager(), "");
                                        } else {
                                            ToastUtils.showLong(httpResultModel.resultContent);
                                        }
                                    } else {
                                        ToastUtils.showLong(httpResultModel.resultContent);
                                    }
                                }, netError -> {
//                                    receivers = null;
                                    verifyResultBean = null;
                                    ToastUtils.showLong("身份证验证失败！" + netError.getMessage());
                                });
                            }).create();
                }, true)/*.setCancelListener(() -> {
                    view.postDelayed(() -> {
//                        disableShowSoftInput(enter_url_et);
//                        RxKeyboardTool.hideSoftInput(enter_url_et);
                        RxKeyboardTool.hideSoftInput(context);
//                        ToastUtils.showLong("oncancel");
                    }, 50);
                    view.postDelayed(() -> {
                        enter_url_et.setText("");
//                        ToastUtils.showLong("onclear");
                    }, 150);
                })*/.show(getChildFragmentManager(), "");
                break;
            case R.id.billing_verify_by_wechat_iv:
                if (null != verifyResultBean) {
                    ToastUtils.showLong("请先结束交易！");
                    return;
                }
                Flowable<HttpResultModel> wechat = DataService.builder().buildReqUrl("").buildReqParams("", "")
                        .request(ApiService.HttpMethod.POST);
                RxLoadingUtils.subscribeWithDialog(context, wechat, bindToLifecycle(), httpResultModel -> {
                }, netError -> {
                    DialogFragmentHelper.builder(context -> {
//                        ImageView imageView = new ImageView(context);
                        Bitmap bmp = RxQRCode.creatQRCode(App.getSettingBean().shop_url + "member/login", BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
//                        imageView.setImageBitmap(bmp);
                        ImageView qr_iv = new ImageView(context);
//                        qr_iv.setLayoutParams(new RadioGroup.LayoutParams(Utils.dip2px(context, 60), Utils.dip2px(context, 60)));
                        qr_iv.setImageBitmap(bmp);
                        return new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("请扫码登录").setView(qr_iv)
                                .setPositiveButton("取消", (dialog, which) -> dialog.cancel()).create();
                    }, true).setDialogWindow(dialogWindow -> {
                        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                        layoutParams.height = Utils.dip2px(context, 400);
                        layoutParams.width = Utils.dip2px(context, 400);
                        dialogWindow.setAttributes(layoutParams);
                        return dialogWindow;
                    }).show(getChildFragmentManager(), "");
                });
                break;
            case R.id.billing_bottom_shopping_bag_big_tv:
            case R.id.billing_bottom_shopping_bag_small_tv:
                if (Kits.Empty.check(verifyResultBean)) {
                    isScan = false;
                    enter_url_et.setText("");
                    enter_url_et.requestFocus();
                    ToastUtils.showLong("请验证身份！");
                    return;
                }
                if (TextUtils.isEmpty(order_tv.getText())) {
                    ToastUtils.showLong("请先生成销售单号！");
                    return;
                }
                boolean big = (view == bag_big_tv);
                if (big) {
                    BillingFragment.this.cur_product_id = "11111111111111";
                } else {
                    BillingFragment.this.cur_product_id = "00000000000000";
                }

                GoodsBean bean = App.products.get(cur_product_id);
                if (bean != null) {
                    billingItemAdapter.addOrupdateData(bean);
                } else {
                    if (big) {
                        bean = beans[0];
                        bean.name = "大购物袋";
                    } else {
                        bean = beans[1];
                        bean.name = "小购物袋";
                    }
                    bean.productId = bean.barcode = cur_product_id;
                    billingItemAdapter.addOrupdateData(bean);
                    billing_rv.scrollToPosition(0);
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_billing_layout;
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        context.registerReceiver(mUsbReceiver, filter);
        registerMsgReceiver();
        disableShowSoftInput(enter_url_et);
        enter_url_et.requestFocus();
        enter_url_et.postDelayed(() -> {
            RxKeyboardTool.hideSoftInput(context);
        }, 1000);
        super.onResume();
    }

    @Override
    public void onPause() {
        (context).unregisterReceiver(mUsbReceiver);
        (context).unregisterReceiver(payMessageReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        OnBnStop(null);
        Prints.close();
        verifyResultBean = null;
        billingItemAdapter.clearAndRestData();
        IDCardReaderFactory.destroy(idCardReader);
        playerHikvision.cleanup();
        super.onDestroy();
    }

    public static BillingFragment newInstance() {
        return new BillingFragment();
    }

    @Override
    public boolean onBackPress(Activity activity) {
        DialogFragmentHelper.builder(context -> new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("确定退出应用程序吗?")
                .setNegativeButton("取消", null).setPositiveButton("确定", (dialog, which) -> {
                    getActivity().finish();
                }).create(), true).show(getChildFragmentManager(), "");
        return true;
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else {
//    if (android.os.Build.VERSION.SDK_INT <= 10) {
//        editText.setInputType(InputType.TYPE_NULL);
//    }
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
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
//            test_tv.setText("设备已连接");
            ToastUtils.showLong("设备已连接");
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
//                        test_tv.setText("设备发生异常，断开连接！");
                        ToastUtils.showLong("设备发生异常，断开连接！");
                    }
                });
            };
            idCardReader.open(0);
            idCardReader.setIdCardReaderExceptionListener(listener);
            bStoped = false;
            mReadCount = 0;
            writeLogToFile("连接设备成功");
//            test_tv.setText("连接成功");
            ToastUtils.showLong("连接成功");
            bopen = true;
            countdownLatch = new CountDownLatch(1);
            new Thread(() -> {
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
                    } catch (Exception e) {
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
                                    handler.sendMessage(handler.obtainMessage(1, strID));
                                    //有效期限
                                    String strEffext = idCardInfo.getValidityTime();
                                    //签发机关
                                    String strIssueAt = idCardInfo.getDepart();
                                    test_tv.setText("读取次数：" + mReadCount + ",耗时：" + nTickUsed + "毫秒, 卡类型：居民身份证,姓名：" + strName +
                                            "，民族：" + strNation + "，住址：" + strAddr + ",身份证号：" + strID);
                                    if (idCardInfo.getPhotolength() > 0) {
                                        byte[] buf = new byte[WLTService.imgLength];
                                        if (1 == WLTService.wlt2Bmp(idCardInfo.getPhoto(), buf)) {
//                                                test_tv.setBackground(new BitmapDrawable(IDPhotoHelper.Bgr2Bitmap(buf)));
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
                                    handler.sendMessage(handler.obtainMessage(1, strID));
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
//                                                test_tv.setBackground(new BitmapDrawable(IDPhotoHelper.Bgr2Bitmap(buf)));
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
                                    handler.sendMessage(handler.obtainMessage(1, strID));
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
//                                                test_tv.setBackground(new BitmapDrawable(IDPhotoHelper.Bgr2Bitmap(buf)));
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
                countdownLatch.countDown();
            }).start();
        } catch (IDCardReaderException e) {
            writeLogToFile("连接设备失败");
//            test_tv.setText("连接失败");
//            test_tv.setText("开始读卡失败，错误码：" + e.getErrorCode() + "\n错误信息：" + e.getMessage() + "\n内部代码=" + e.getInternalErrorCode());
            ToastUtils.showLong("连接失败" + "开始读卡失败，错误码：" + e.getErrorCode() + "\n错误信息：" + e.getMessage() + "\n内部代码=" + e.getInternalErrorCode());
        }

    }

    public void OnBnBegin(View view) {
        if (bopen) {
//            test_tv.setText("设备已连接");
            ToastUtils.showLong("设备已连接");
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
//        test_tv.setText("设备断开连接");
        ToastUtils.showLong("设备断开连接");
    }

    /**
     * 动态广播接收消息
     */
    private BroadcastReceiver payMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.e(TAG, "-----接收服务端数据" + message);
            try {
                Gson gson = new Gson();
                PayResultBean payResultBean = gson.fromJson((message), PayResultBean.class);
                if (payResultBean.isPaySuccess == (1)) {
                    if (null != payDialog && null != printBean && payResultBean.orderSn.equals(printBean.sell_code)) {
                        ToastUtils.showLong("支付成功！");
                        payDialog.cancel();
                        payDialog = null;
                        App.getBoxStore().runInTxAsync(() -> {
                            PrintTableEntiry printTableEntiry = PrintTableEntiry.builder(printBean);
                            printTableEntiry.is_pay = true;
                            App.getBoxStore().boxFor(PrintTableEntiry.class).put(printTableEntiry);
                            Prints.PostPrint(context, printTableEntiry, order_tv);
                        }, (result, error) -> {
                            if (Kits.Empty.check(error)) {

                            }
                            printBean = null;
                        });
                        handler.sendMessage(handler.obtainMessage(-1, 0));
                        JWebSocketClientService.setOrderSn(null);
                    }
                }
            } catch (Exception e) {

            } finally {
            }
        }
    };

    /**
     * 动态注册广播
     */
    private void registerMsgReceiver() {
        IntentFilter filter = new IntentFilter("com.xxx.servicecallback.content");
        (context).registerReceiver(payMessageReceiver, filter);
    }
}