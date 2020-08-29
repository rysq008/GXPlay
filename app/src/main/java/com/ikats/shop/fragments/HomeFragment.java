package com.ikats.shop.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.services.JWebSocketClientService;
import com.ikats.shop.utils.JWebSocketClient;
import com.jaeger.library.StatusBarUtil;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.COMIO;
import com.tamsiree.rxkit.RxKeyboardTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;

public class HomeFragment extends XBaseFragment {
    @BindView(R.id.home_viewpager)
    ViewPager viewPager;
    @BindView(R.id.home_rb_cashier)
    RadioButton cashier_rb;
    @BindView(R.id.home_rb_server)
    RadioButton server_rb;
    @BindView(R.id.home_tv_user)
    TextView user_tv;
    @BindView(R.id.home_netstatus_iv)
    ImageView net_iv;

    private List<Fragment> mFragment = new ArrayList<>();
    private XFragmentAdapter mAdapter;
    public static Pos mpos = new Pos();
    public static COMIO mcom = new COMIO();
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private JWebSocketClient client;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setTranslucent(context, 0);
        user_tv.setText(App.getSettingBean().shop_cashier);

//        Flowable<HttpResultModel> update = DataService.builder().buildReqUrl("").request(ApiService.HttpMethod.GET);
//        RxLoadingUtils.subscribeWithDialog(null, update, bindToLifecycle(), new Consumer<HttpResultModel>() {
//            @Override
//            public void accept(HttpResultModel httpResultModel) throws Exception {
//
//            }
//        }, new Consumer<NetError>() {
//            @Override
//            public void accept(NetError netError) throws Exception {
//
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//
//            }
//        }, true);
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                super.onLost(network);
                ///网络不可用的情况下的方法
                net_iv.post(() -> {
                    net_iv.setVisibility(View.VISIBLE);
                    Glide.with(context).asGif().load(R.drawable.net).into(new SimpleTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                            net_iv.setImageDrawable(resource);
                            resource.start();
                        }
                    });
                });
            }

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                ///网络可用的情况下的方法
                net_iv.post(() -> {
                    net_iv.setVisibility(View.GONE);
                });
            }
        });

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mFragment.add(CashierFragment.newInstance());
        mFragment.add(BackstageFragment.newInstance());
//        mFragment.add(CashierFragment.newInstance());

        viewPager.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragment, null));

        viewPager.setOffscreenPageLimit(2);
        mpos.Set(mcom);
    }

    @OnClick({R.id.home_rb_cashier, R.id.home_rb_server, R.id.home_tv_user})
    public void OnViewClick(View view) {
        RxKeyboardTool.hideSoftInput(context);
        switch (view.getId()) {
            case R.id.home_rb_cashier:
//                Bitmap bmp = RxQRCode.creatQRCode("httpResultModel.resultData.payableCode", BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
////                qrcode_iv.setImageBitmap(bmp);
//                DialogFragmentHelper.builder(context -> {
//                    ImageView qr_iv = new ImageView(context);
//                    qr_iv.setLayoutParams(new RadioGroup.LayoutParams(Utils.dip2px(context, 60), Utils.dip2px(context, 60)));
//                    qr_iv.setImageBitmap(bmp);
//                    return new android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("请扫码支付").setView(qr_iv).setPositiveButton("取消", (dialog, which) -> {
//                        DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
//                            @Override
//                            public Dialog getDialog(Context context) {
//                                return new android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("是否需要取消订单？").setNegativeButton("取消", null).setPositiveButton("确定", (dialog12, which12) -> {
//                                    dialog12.cancel();
//                                    Flowable<HttpResultModel> del_order = DataService.builder().buildReqUrl(App.getSettingBean().shop_url + "api/cancel")
//                                            .buildReqParams("orderSn", "202009161018888")
//                                            .request(ApiService.HttpMethod.POST);
//                                    RxLoadingUtils.subscribeWithDialog(context, del_order, bindToLifecycle(), httpResultModel1 -> {
//                                        dialog.cancel();
//                                        if (httpResultModel1.isSucceful()) {
//                                            ToastUtils.showLong("订单取消成功！");
//                                        } else {
//                                            ToastUtils.showLong(httpResultModel1.resultContent);
//                                        }
//                                    }, netError -> ToastUtils.showLong("订单取消失败，请重试！"), true);
//                                }).create();
//                            }
//                        }, false).show(getChildFragmentManager(), "");
//                    }).create();
//                }, false).show(getChildFragmentManager(), "");

//                ProgressDialog progressDialog = new ProgressDialog(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                Flowable<HttpResultModel> download = DataService.builder().buildReqUrl("http://sjws.ssl.qihucdn.com/mobile/shouji360/360safesis/20200527-1626/360MobileSafe_8.6.2.1002.apk").buildProgress(new ProgressListener() {
//                    @Override
//                    public void onProgress(long soFarBytes, long totalBytes) {
//                        progressDialog.setMax((int) totalBytes);
//                        progressDialog.setProgress((int) soFarBytes);
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        progressDialog.cancel();
//                        ToastUtils.showLong(throwable.getMessage());
//                    }
//                }).request(ApiService.HttpMethod.DOWNLOAD).map((Function<ResponseBody, HttpResultModel>) responseBody -> {
//
//                    InputStream inputStream = responseBody.byteStream();
//                    File app = new File(context.getExternalCacheDir(), "ikats.apk");
//                    app.createNewFile();
//                    FileOutputStream fos = new FileOutputStream(app);
//                    byte[] bytes = new byte[1024 * 10];
//                    int count;
//                    while ((count = inputStream.read(bytes)) != -1) {
//                        fos.write(bytes, 0, count);
//                    }
//                    fos.close();
//                    inputStream.close();
//                    HttpResultModel httpResultModel = new HttpResultModel();
//                    httpResultModel.resultCode = 1;
//                    httpResultModel.resultContent = app.getAbsolutePath();
//                    return httpResultModel;
//                });
//                RxLoadingUtils.subscribeWithDialog(progressDialog, download, bindToLifecycle(), httpResultModel -> {
//                    if (httpResultModel.isSucceful())
//                        RxAppTool.installApp(context, httpResultModel.resultContent);
//                }, netError -> {
//                    ToastUtils.showLong(netError.getMessage());
//                }, () -> {
//
//                }, false);

                viewPager.setCurrentItem(0);
                break;
            case R.id.home_rb_server:
                viewPager.setCurrentItem(1);
                break;
            case R.id.home_tv_user:
                DialogFragmentHelper.builder(context -> {
                    Dialog[] dialog = new Dialog[1];
                    View vv = View.inflate(context, R.layout.fragment_home_menu, null);
                    View.OnClickListener onClickListener = v -> {
                        switch (v.getId()) {
                            case R.id.home_menu_billing_tv:
                                if (viewPager.getCurrentItem() == 0) {
                                    ((CashierFragment) mFragment.get(0)).viewPager.setCurrentItem(0);
                                } else {
                                    viewPager.setCurrentItem(0);
                                }
                                dialog[0].cancel();
                                break;
                            case R.id.home_menu_search_tv:
                                if (viewPager.getCurrentItem() == 0) {
                                    ((CashierFragment) mFragment.get(0)).viewPager.setCurrentItem(1);
                                } else {
                                    viewPager.setCurrentItem(0);
                                }
                                dialog[0].cancel();
                                break;
                            case R.id.home_menu_statistics_tv:
                                if (viewPager.getCurrentItem() == 0) {
                                    ((CashierFragment) mFragment.get(0)).viewPager.setCurrentItem(2);
                                } else {
                                    viewPager.setCurrentItem(0);
                                }
                                dialog[0].cancel();
                                break;
                            case R.id.home_menu_setting_tv:
                                if (viewPager.getCurrentItem() == 0) {
                                    ((CashierFragment) mFragment.get(0)).viewPager.setCurrentItem(3);
                                } else {
                                    viewPager.setCurrentItem(0);
                                }
                                dialog[0].cancel();
                                break;
                            case R.id.home_menu_exit_tv:
                                dialog[0].cancel();
                                getActivity().finish();
                                System.exit(0);
                                break;
                        }
                    };
                    vv.findViewById(R.id.home_menu_billing_tv).setOnClickListener(onClickListener);
                    vv.findViewById(R.id.home_menu_search_tv).setOnClickListener(onClickListener);
                    vv.findViewById(R.id.home_menu_statistics_tv).setOnClickListener(onClickListener);
                    vv.findViewById(R.id.home_menu_setting_tv).setOnClickListener(onClickListener);
                    vv.findViewById(R.id.home_menu_exit_tv).setOnClickListener(onClickListener);
                    dialog[0] = new AlertDialog.Builder(context).setView(vv).create();
                    return dialog[0];
                }, true).setDialogWindow(dialogWindow -> {
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    lp.x = App.w - 200;
                    lp.y = location[1] * 3;
                    lp.width = 200;
                    dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
                    dialogWindow.setBackgroundDrawableResource(R.color.white);
                    dialogWindow.setDimAmount(0f);
                    dialogWindow.setAttributes(lp);
                    return dialogWindow;
                }).show(getChildFragmentManager(), "");
                break;
        }
    }

    @Override
    public boolean onBackPress(Activity activity) {
        return ((XBaseFragment) mFragment.get(viewPager.getCurrentItem())).onBackPress(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onResume() {
        super.onResume();
        JWebSocketClientService.bindService(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        JWebSocketClientService.unbindService(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mcom.Close();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


}
