package com.ikats.shop.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.adapters.AddressAdapter;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.ProvinceBean;
import com.ikats.shop.model.SettingBean;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.utils.PlayerHikvision;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.utils.ShareUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class SettingFragment extends XBaseFragment {

    @BindView(R.id.setting_test_surfaceview)
    SurfaceView _surfaceView;
    @BindView(R.id.setting_title_close_iv)
    ImageView close_iv;
    @BindView(R.id.setting_url_tip_et)
    EditText mall_url_et;
    @BindView(R.id.setting_order_manage_url_et)
    EditText manage_url_et;
    @BindView(R.id.setting_send_by_self_cb)
    CheckBox self_cb;
    @BindView(R.id.setting_send_by_express_cb)
    CheckBox express_cb;
    @BindView(R.id.setting_shop_address_tv)
    TextView shop_adr_tv;
    @BindView(R.id.setting_connect_print_test_tv)
    TextView print_test;
    @BindView(R.id.setting_select_province_spinner)
    Spinner province_sp;
    @BindView(R.id.setting_select_city_spinner)
    Spinner city_sp;
    @BindView(R.id.setting_select_district_spinner)
    Spinner area_sp;
    @BindView(R.id.setting_shop_address_et)
    EditText shop_address_et;
    @BindView(R.id.setting_shop_name_et)
    EditText shop_name_et;
    @BindView(R.id.setting_shop_code_et)
    EditText shop_code_et;
    @BindView(R.id.setting_shop_cashier_et)
    EditText shop_cashier_et;
    @BindView(R.id.setting_video_preview_open_cb)
    CheckBox preview_cb;
    @BindView(R.id.setting_connect_preview_test_tv)
    TextView camera_test;
    @BindView(R.id.setting_video_record_open_cb)
    CheckBox record_cb;
    @BindView(R.id.setting_preview_ip_et)
    EditText preview_ip_et;
    @BindView(R.id.setting_preview_ip_port_et)
    EditText preview_port_et;
    @BindView(R.id.setting_preview_ip_channel_et)
    EditText preview_channel_et;
    @BindView(R.id.setting_preview_username_et)
    EditText preview_user_et;
    @BindView(R.id.setting_preview_password_et)
    EditText preview_pwd_et;
    @BindView(R.id.setting_video_record_ip_et)
    EditText record_ip_et;
    @BindView(R.id.setting_video_record_port_et)
    EditText record_port_et;
    @BindView(R.id.setting_video_record_channel_et)
    EditText record_channel_et;
    SettingBean settingBean;
    SurfaceView surfaceView;
    AddressAdapter province_adp, city_adp, area_adp;
    private PlayerHikvision playerHikvision, dplayerHikvision;
    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case -1: {
                if (playerHikvision.isLive()) {
                    DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
                        @Override
                        public Dialog getDialog(Context context) {
                            return new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                    .setTitle("提示").setMessage("是否停止预览").setNegativeButton("取消", null)
                                    .setPositiveButton("确定", (dialog, which) -> {
                                        dialog.cancel();
                                        playerHikvision.stopLive(playerHikvision.mPlayId, playerHikvision.mPort);
                                        playerHikvision.refresh();
                                    }).create();
                        }
                    }, true).show(getChildFragmentManager(), "");
                } else {
//                    playerHikvision.live(settingBean.camera_ip, Integer.parseInt(settingBean.camera_port), settingBean.camera_user, settingBean.camera_pwd, PlayerHikvision.HIK_SUB_STREAM_CODE, 1);
                    dplayerHikvision.live(settingBean.camera_ip, Integer.parseInt(settingBean.camera_port), settingBean.camera_user, settingBean.camera_pwd, PlayerHikvision.HIK_SUB_STREAM_CODE, 1);
                    DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
                        @Override
                        public Dialog getDialog(Context context) {
                            if (surfaceView.getParent() != null)
                                ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
                            AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setView(surfaceView).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    dplayerHikvision.stopLive(dplayerHikvision.mPlayId, dplayerHikvision.mPort);
                                    dplayerHikvision.refresh();
                                }
                            }).create();
                            return alertDialog;
                        }
                    }, true).setDialogWindow(dialogWindow -> {
                        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                        dialogWindow.setAttributes(layoutParams);
                        return dialogWindow;
                    }).show(getChildFragmentManager(), "");

                }
            }
            break;
        }
        return false;
    });
    private SimpleExoPlayer exoPlayer;
    private Player.DefaultEventListener listener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            // 视频播放状态
            Log.e("TAG", "playbackState = " + playbackState + " playWhenReady = " + playWhenReady);
            switch (playbackState) {
                case Player.STATE_IDLE:
                    // 空闲
                    break;
                case Player.STATE_BUFFERING:
                    // 缓冲中
                    break;
                case Player.STATE_READY:
                    // 准备好
                    break;
                case Player.STATE_ENDED:
                    // 结束
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.e("TAG", "ExoPlaybackException = " + error.getMessage());
            // 报错
            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    // 加载资源时出错
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    // 渲染时出错
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    // 意外的错误
                    break;
            }
        }
    };
    private String province, city, area;


    @Override
    public void initData(Bundle savedInstanceState) {
        // 得到默认合适的带宽
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // 创建跟踪的工厂
        TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        // 创建跟踪器
        DefaultTrackSelector trackSelection = new DefaultTrackSelector(factory);
        // 创建播放器
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelection);

        // 第二种方式 传入了默认的渲染工厂（DefaultRenderersFactory），默认的轨道选择器（DefaultTrackSelector）和默认的加载控制器（DefaultLoadControl），然后把返回的播放器实例
        //SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
        //         new DefaultRenderersFactory(context),
        //         new DefaultTrackSelector(),
        //         new DefaultLoadControl());
        // 生成数据媒体实例，通过该实例加载媒体数据
        settingBean = ShareUtils.getSettingInfo();
        surfaceView = new SurfaceView(context);
        mall_url_et.setText(settingBean.shop_url);
        manage_url_et.setText(settingBean.manage_url);
        self_cb.setChecked(!settingBean.send_by_express);
        express_cb.setChecked(settingBean.send_by_express);
        preview_cb.setChecked(settingBean.isPreviewOpen);
        preview_ip_et.setText(settingBean.camera_ip);
        preview_port_et.setText(settingBean.camera_port);
        preview_channel_et.setText(settingBean.camera_channel + "");
        preview_user_et.setText(settingBean.camera_user);
        preview_pwd_et.setText(settingBean.camera_pwd);
        record_cb.setChecked(settingBean.isRecordOpen);
        record_ip_et.setText(settingBean.record_ip);
        record_port_et.setText(settingBean.record_port);
        record_channel_et.setText(settingBean.record_channel);
        playerHikvision = new PlayerHikvision(_surfaceView, handler);
        dplayerHikvision = new PlayerHikvision(surfaceView, handler);

        province_adp = new AddressAdapter(context);
        province_adp.setData(App.provinceBeans);
        province_sp.setAdapter(province_adp);
        province_sp.setSelection(settingBean.province_index);

        city_adp = new AddressAdapter(context);
        city_adp.setData((App.provinceBeans.get(settingBean.province_index).cityList));
        city_sp.setAdapter(city_adp);
        city_sp.setSelection(settingBean.city_index);

        area_adp = new AddressAdapter(context);
        area_adp.setData((App.provinceBeans.get(settingBean.province_index).cityList.get(settingBean.city_index).areaList));
        area_sp.setAdapter(area_adp);
        area_sp.setSelection(settingBean.area_index);

        shop_address_et.setText(settingBean.shop_address);
        shop_name_et.setText(settingBean.shop_name);
        shop_code_et.setText(settingBean.shop_code);
        shop_cashier_et.setText(settingBean.shop_cashier);

        province_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingBean.province_index = position;
                if (!Kits.Empty.check((App.provinceBeans.get(position).cityList))) {
                    city_adp.setData((App.provinceBeans.get(position).cityList));
                    if (!Kits.Empty.check((App.provinceBeans.get(position).cityList.get(settingBean.city_index).areaList))) {
                        area_adp.setData((App.provinceBeans.get(position).cityList).get(settingBean.city_index).areaList);
                        area_sp.setSelection(settingBean.area_index);
                        area = ((ProvinceBean.AreaListBean) area_sp.getSelectedItem()).name;
                    } else {
                        area = "";
                    }
                    city_sp.setSelection(settingBean.city_index);
                    city = ((ProvinceBean.CityListBean) city_sp.getSelectedItem()).name;
                } else {
                    city_adp.clearData();
                    area_adp.clearData();
                    city = area = "";
                }
                province = ((ProvinceBean) province_sp.getSelectedItem()).name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        city_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingBean.city_index = position;
                if (!Kits.Empty.check(App.provinceBeans.get(province_sp.getSelectedItemPosition()).cityList.get(position).areaList)) {
                    area_adp.setData((App.provinceBeans.get(province_sp.getSelectedItemPosition()).cityList.get(position).areaList));
                    area_sp.setSelection(settingBean.area_index);
                    area = ((ProvinceBean.AreaListBean) area_sp.getSelectedItem()).name;
                } else {
                    area_adp.clearData();
                    area = "";
                }
                city = Kits.Empty.check(city_sp.getSelectedItem()) ? "" : ((ProvinceBean.CityListBean) city_sp.getSelectedItem()).name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        area_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingBean.area_index = position;
                area = Kits.Empty.check(area_sp.getSelectedItem()) ? "" : ((ProvinceBean.AreaListBean) area_sp.getSelectedItem()).name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting_layout;
    }

    @OnClick({R.id.setting_send_by_self_cb, R.id.setting_send_by_express_cb, R.id.setting_video_preview_open_cb,
            R.id.setting_submit_btn, R.id.setting_title_close_iv, R.id.setting_connect_print_test_tv,
            R.id.setting_connect_preview_test_tv, R.id.setting_connect_record_test_tv})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.setting_title_close_iv:
                ToastUtils.showLong(province + "," + city + "," + area);
                break;
            case R.id.setting_send_by_self_cb:
                if (self_cb.isChecked()) {
                    express_cb.setChecked(false);
                }
                setExpressShow(express_cb.isChecked());
                settingBean.send_by_self = self_cb.isChecked();
                break;
            case R.id.setting_send_by_express_cb:
                if (express_cb.isChecked()) {
                    self_cb.setChecked(false);
                }
                setExpressShow(express_cb.isChecked());
                settingBean.send_by_express = express_cb.isChecked();
                break;
            case R.id.setting_connect_print_test_tv:
//                Prints.PostPrint(context, "aaa");
                break;
            case R.id.setting_video_preview_open_cb:
                break;
            case R.id.setting_connect_preview_test_tv:
                handler.sendEmptyMessage(-1);
                break;
            case R.id.setting_video_record_open_cb:
                break;
            case R.id.setting_connect_record_test_tv:
                String record_url = settingBean.record_ip.concat(":").concat(settingBean.record_port);
//                Executors.newSingleThreadExecutor().execute(() -> {
//                    URL url = null;
//                    try {
//                        url = new URL(String.format(record_url.concat("/record?sellNo=%s&channel=%s"), "20200728-003", settingBean.record_channel));
//                        URLConnection urlConnection = url.openConnection();
//                        InputStream inputStream = ((HttpURLConnection) urlConnection).getInputStream();
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                        String url_str = "", cur_str;
//                        while ((cur_str = bufferedReader.readLine()) != null) {
//                            url_str += cur_str;
//                        }
//                        Log.e("TAG", "onViewClick: " + url_str);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                });
//                Flowable.create(emitter -> {
//                    URL url = new URL(String.format(record_url.concat("/record?sellNo=%s&channel=%s"), "20200728-003", settingBean.record_channel));
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream inputStream = ((HttpURLConnection) urlConnection).getInputStream();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    String url_str = "", cur_str;
//                    while ((cur_str = bufferedReader.readLine()) != null) {
//                        url_str += cur_str;
//                    }
//                    Log.e("TAG", "on111111111ViewClick: " + url_str);
//                    emitter.onNext(url_str);
//                    emitter.onComplete();
//                }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
//
//                }, throwable -> {
//
//                });
                String video_name = "20200915";
                Flowable<HttpResultModel> flowable =
                        DataService.builder().buildReqUrl(String.format(record_url.concat("/record?sellNo=%s&channel=%s"), video_name, settingBean.record_channel))
                                .buildInterceptconvert(true)
                                .request(ApiService.HttpMethod.GET).flatMap(new Function<ResponseBody, Flowable<HttpResultModel>>() {
                            @Override
                            public Flowable<HttpResultModel> apply(ResponseBody responseBody) throws Exception {
                                String vpid = responseBody.string();
                                Log.e("TAG", "apply: " + vpid);
                                Thread.currentThread().sleep(15000);
                                return DataService.builder().buildReqUrl(String.format(record_url.concat("/taskkill?ffPid=%s&sellNo=%s&orderNo=%s"), vpid, video_name, ""))
                                        .buildInterceptconvert(true)
                                        .request(ApiService.HttpMethod.GET).map((Function<ResponseBody, HttpResultModel>) sresponseBody -> {
                                            Thread.currentThread().sleep(5000);
                                            HttpResultModel resultModel = new HttpResultModel();
                                            resultModel.resultCode = 1;
                                            resultModel.resultData = sresponseBody.string();
                                            return resultModel;
                                        });
                            }
                        });
                RxLoadingUtils.subscribeWithDialog(context, flowable, bindToLifecycle(), httpResultModel -> {
                    String video_pid = (String) httpResultModel.resultData;
                    ToastUtils.showLong("测试正常");
                    String uri = String.format(record_url + "/video/%s.flv", video_name);

                    Log.e("TAG", "on2222222ViewClick: " + video_pid + "-->" + uri);

                    SimpleExoPlayerView mPlayerView = new SimpleExoPlayerView(context);
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "exoplayerdemo"));
                    // 创建资源
                    Uri suri = Uri.parse(uri/*"http://192.168.1.140:3000/video/test.flv"*/);
                    MediaSource mediaSources = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(suri);
                    // 将播放器附加到view
                    mPlayerView.setPlayer(exoPlayer);
                    // 准备播放
                    exoPlayer.prepare(mediaSources);
                    // 准备好了之后自动播放，如果已经准备好了，调用该方法实现暂停、开始功能
                    exoPlayer.setPlayWhenReady(true);
                    // 添加事件监听
                    exoPlayer.addListener(listener);

                    DialogFragmentHelper.builder(context -> {
                        AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setView(mPlayerView).create();
                        return alertDialog;
                    }, true).setDialogWindow(dialogWindow -> {
                        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                        dialogWindow.setAttributes(layoutParams);
                        return dialogWindow;
                    }).show(getChildFragmentManager(), "");

                }, netError -> {
                    ToastUtils.showLong("测试异常，请检查输入参数是否正确。");
                });
                break;
            case R.id.setting_submit_btn:
                DialogFragmentHelper.builder(context -> new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("是否保存本页面配置").setNegativeButton("取消", null).setPositiveButton("确定", (dialog, which) -> {
                    (settingBean.shop_url) = mall_url_et.getText().toString().trim();
                    (settingBean.manage_url) = manage_url_et.getText().toString().trim();
                    (settingBean.isAfterPayPrint) = self_cb.isChecked();
                    (settingBean.isPayPrintCheck) = express_cb.isChecked();
                    (settingBean.isPreviewOpen) = preview_cb.isChecked();
                    (settingBean.camera_ip) = preview_ip_et.getText().toString().trim();
                    (settingBean.camera_port) = preview_port_et.getText().toString().trim();
                    (settingBean.camera_channel) = Integer.parseInt(preview_channel_et.getText().toString().trim());
                    (settingBean.camera_user) = preview_user_et.getText().toString().trim();
                    (settingBean.camera_pwd) = preview_pwd_et.getText().toString().trim();
                    (settingBean.isRecordOpen) = record_cb.isChecked();
                    (settingBean.record_ip) = record_ip_et.getText().toString().trim();
                    (settingBean.record_port) = record_port_et.getText().toString().trim();
                    (settingBean.record_channel) = record_channel_et.getText().toString().trim();
                    settingBean.send_by_self = self_cb.isChecked();
                    settingBean.send_by_express = express_cb.isChecked();
                    settingBean.shop_area = province + "," + city + "," + area;
                    settingBean.province_index = province_sp.getSelectedItemPosition();
                    settingBean.city_index = city_sp.getSelectedItemPosition();
                    settingBean.area_index = area_sp.getSelectedItemPosition();
                    settingBean.shop_address = shop_address_et.getText().toString().trim();
                    settingBean.shop_name = shop_name_et.getText().toString().trim();
                    settingBean.shop_code = shop_code_et.getText().toString().trim();
                    settingBean.shop_cashier = shop_cashier_et.getText().toString().trim();
                    ShareUtils.saveSettingInfo(settingBean);
                    App.setSettingBean(settingBean);
                }).create(), true).show(getChildFragmentManager(), "");
                break;
        }
    }

    public void setExpressShow(boolean send_by_express) {
        shop_adr_tv.setVisibility(send_by_express ? View.GONE : View.VISIBLE);
        province_sp.setVisibility(send_by_express ? View.GONE : View.VISIBLE);
        city_sp.setVisibility(send_by_express ? View.GONE : View.VISIBLE);
        area_sp.setVisibility(send_by_express ? View.GONE : View.VISIBLE);
        shop_address_et.setVisibility(send_by_express ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        if (null != exoPlayer) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer.removeListener(listener);
            exoPlayer = null;
        }
        // 移除事件监听
        super.onDestroy();
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

}
