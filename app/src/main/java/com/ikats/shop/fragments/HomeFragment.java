package com.ikats.shop.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.adapters.TicketItemAdapter;
import com.ikats.shop.database.PrintTableEntiry;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.services.JWebSocketClientService;
import com.ikats.shop.utils.JWebSocketClient;
import com.ikats.shop.utils.Prints;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.utils.UploadUtils;
import com.ikats.shop.views.GlobalStateView;
import com.ikats.shop.views.XReloadableListContentLayout;
import com.jaeger.library.StatusBarUtil;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.COMIO;
import com.tamsiree.rxkit.RxAppTool;
import com.tamsiree.rxkit.RxKeyboardTool;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.enums.EMoveType;
import com.yhao.floatwindow.enums.EScreen;
import com.yhao.floatwindow.interfaces.BaseFloatWindow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

public class HomeFragment extends XBaseFragment {
    @BindView(R.id.home_gs_tv)
    ImageView customer_icon_iv;
    @BindView(R.id.home_radioGroup)
    RadioGroup rg;
    @BindView(R.id.home_rl_user)
    RelativeLayout rl_user;
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
    private BaseFloatWindow mStatusWindow;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setTranslucent(context, 0);
        user_tv.setText(App.getSettingBean().shop_cashier);

//        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        cm.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onLost(Network network) {
//                super.onLost(network);
//                ///网络不可用的情况下的方法
//                net_iv.post(() -> {
//                    net_iv.setVisibility(View.VISIBLE);
//                    Glide.with(context).asGif().load(R.drawable.net).into(new SimpleTarget<GifDrawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
//                            net_iv.setImageDrawable(resource);
//                            resource.start();
//                        }
//                    });
//                });
//                RxMsgEvent msgEvent = new RxMsgEvent(0,"",false);
//                RxBusProvider.getBus().postEvent(msgEvent);
//            }
//
//            @Override
//            public void onAvailable(Network network) {
//                super.onAvailable(network);
//                ///网络可用的情况下的方法
//                net_iv.post(() -> {
//                    net_iv.setVisibility(View.GONE);
//                });
//                RxMsgEvent msgEvent = new RxMsgEvent(0,"",true);
//                RxBusProvider.getBus().postEvent(msgEvent);
//            }
//        });

        GlobalStateView globalStateView = new GlobalStateView(context);
        mStatusWindow = FloatWindow.get("StatusWindow");
        // 初始化展示
        // 效果图1
        if (mStatusWindow != null) {
        } else {
            FloatWindow.with(App.getApp()).setView(globalStateView).setWidth(EScreen.WIDTH, 0.07f)
                    .setHeight(EScreen.WIDTH, 0.2f).setX(EScreen.WIDTH, 0.95f).setY(EScreen.HEIGHT, 0.5f)
                    .setMoveType(EMoveType.SLIDE).setMoveStyle(500, new BounceInterpolator())
                    .setTag("StatusWindow").build();
        }
        FloatWindow.get("StatusWindow").show();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Glide.with(this).asBitmap().load(App.getSettingBean().custom_icon_res)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        customer_icon_iv.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        customer_icon_iv.setImageResource(R.drawable.chigoose);
                    }
                });
        customer_icon_iv.setBackgroundColor(App.getSettingBean().colorPrimary);
        rg.setBackgroundColor(App.getSettingBean().colorPrimary);
        rl_user.setBackgroundColor(App.getSettingBean().colorPrimary);

        mFragment.add(CashierFragment.newInstance());
        mFragment.add(BackstageFragment.newInstance());
//        mFragment.add(CashierFragment.newInstance());

        viewPager.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragment, null));

        viewPager.setOffscreenPageLimit(2);
        mpos.Set(mcom);

//        viewPager.postDelayed(() -> {
        checkVersion();
//        }, 1100);
    }

    private void checkVersion() {
        Flowable<HttpResultModel> check = DataService.builder().buildReqUrl("http://www.ikats.com/download/pos/version.json")
                .buildInterceptconvert(true)
                .request(ApiService.HttpMethod.GET)
                .map((Function<ResponseBody, HttpResultModel>) responseBody -> {
                    String res = responseBody.string();
                    Map<String, String> map = new Gson().fromJson(res, Map.class);
                    HttpResultModel httpResultModel = new HttpResultModel();
                    int rcode = Integer.valueOf(map.get("version")) > RxAppTool.getAppVersionCode(context) ? 1 : 0;
                    httpResultModel.resultCode = rcode;
                    httpResultModel.resultData = map.get("url");
                    return httpResultModel;
                });
        RxLoadingUtils.subscribeWithDialog(context, check, bindToLifecycle(), httpResultModel -> {
            if (httpResultModel.isSucceful()) {
                DialogFragmentHelper.builder(context -> new AlertDialog.Builder(context, THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示")
                        .setMessage("发现新版本，是否立即更新？").setNegativeButton("忽略", null)
                        .setPositiveButton("更新", (dialog, which) -> {
                            ProgressDialog progressDialog = new ProgressDialog(context, THEME_DEVICE_DEFAULT_LIGHT);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            Flowable<HttpResultModel> download = DataService.builder().buildReqUrl(httpResultModel.resultData.toString())
                                    .buildFileDownloadProgress(new UploadUtils.FileDownloadProgress() {
                                        @Override
                                        public void onProgress(int progress) {
                                            progressDialog.setProgress(progress);
                                        }
                                    }).request(ApiService.HttpMethod.DOWNLOAD).map((Function<UploadUtils.DownloadFileRequestBody, HttpResultModel>) responseBody -> {

                                        InputStream inputStream = responseBody.byteStream();
                                        File app = new File(context.getExternalCacheDir(), "ikats.apk");
                                        app.createNewFile();
                                        FileOutputStream fos = new FileOutputStream(app);
                                        byte[] bytes = new byte[1024 * 10];
                                        int count;
                                        while ((count = inputStream.read(bytes)) != -1) {
                                            fos.write(bytes, 0, count);
                                        }
                                        fos.close();
                                        inputStream.close();
                                        HttpResultModel httpResultModel1 = new HttpResultModel();
                                        httpResultModel1.resultCode = 1;
                                        httpResultModel1.resultContent = app.getAbsolutePath();
                                        return httpResultModel1;
                                    });
                            RxLoadingUtils.subscribeWithDialog(progressDialog, download, bindToLifecycle(), httpResultModel1 -> {
                                if (httpResultModel1.isSucceful())
                                    RxAppTool.installApp(context, httpResultModel1.resultContent);
                            }, netError -> {
                                ToastUtils.showLong(netError.getMessage());
                            }, () -> {

                            }, false);

                        }).create(), true).show(getChildFragmentManager(), "");
            }
        });

    }

    @OnClick({R.id.home_rb_cashier, R.id.home_rb_server, R.id.home_tv_user})
    public void OnViewClick(View view) {
        RxKeyboardTool.hideSoftInput(context);
        switch (view.getId()) {
            case R.id.home_rb_cashier:
                viewPager.setCurrentItem(0);
                break;
            case R.id.home_rb_server:
                viewPager.setCurrentItem(1);
                break;
            case R.id.home_tv_user:
                DialogFragmentHelper.builder(context -> {
                    Dialog[] dialog = new Dialog[1];
                    View vv = View.inflate(context, R.layout.fragment_home_menu, null);
                    vv.setBackgroundColor(App.getSettingBean().colorPrimary);
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
                            case R.id.home_menu_print_tv:
                                DialogFragmentHelper.builder(R.layout.dialog_unprint_ticket_layout, true).setDialogWindow(dialogWindow -> {
                                    WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
                                    wlp.width = App.w - 100;
                                    wlp.height = App.h - 100;
                                    dialogWindow.setAttributes(wlp);
                                    dialogWindow.setWindowAnimations(R.style.BottomInAndOutStyle);
                                    return dialogWindow;
                                }).setOnProcessView((sdialog, view1) -> {
                                    view1.findViewById(R.id.dialog_ticket_close_iv).setOnClickListener(sv -> {
                                        sdialog.cancel();
                                    });
                                    XReloadableListContentLayout xRecyclerView = view1.findViewById(R.id.dialog_ticket_unprint_list_layout);
                                    TicketItemAdapter ticketItemAdapter = new TicketItemAdapter(context);
                                    xRecyclerView.getRecyclerView().verticalLayoutManager(context);
                                    xRecyclerView.getRecyclerView().setAdapter(ticketItemAdapter);
                                    xRecyclerView.getRecyclerView().setRefreshEnabled(false);
                                    App.getBoxStore().runInTxAsync(() -> {
                                        ticketItemAdapter.addData(App.getBoxStore().boxFor(PrintTableEntiry.class).getAll());
                                    }, (result, error) -> {
                                        xRecyclerView.postDelayed(() -> {
                                            xRecyclerView.getRecyclerView().setPage(0, 0);
                                        }, 100);
                                    });

                                    view1.findViewById(R.id.dialog_ticket_print_select_btn).setOnClickListener(sv -> {
//                                        ToastUtils.showLong("" + ticketItemAdapter.getSelect_list().size());
                                        sdialog.cancel();
                                        for (PrintTableEntiry printTableEntiry : ticketItemAdapter.getSelect_list())
                                            Prints.PostPrint(context, printTableEntiry, null);
                                    });
                                }).show(getChildFragmentManager(), "");
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
                    vv.findViewById(R.id.home_menu_print_tv).setOnClickListener(onClickListener);
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
        if (null != mStatusWindow)
            mStatusWindow.destory();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


}
