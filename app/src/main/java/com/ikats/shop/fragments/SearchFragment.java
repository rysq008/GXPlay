package com.ikats.shop.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

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
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.adapters.SearchItemAdapter;
import com.ikats.shop.database.OrderTableEntiry;
import com.ikats.shop.database.OrderTableEntiry_;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.SearchItemBean;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.views.XReloadableListContentLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.objectbox.Box;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class SearchFragment extends XBaseFragment {

    @BindView(R.id.search_enter_et)
    EditText search_et;
    @BindView(R.id.search_enter_start_time_tv)
    TextView start_et;
    @BindView(R.id.search_enter_end_time_tv)
    TextView end_et;
    @BindView(R.id.search_order_count_tv)
    TextView count_tv;
    @BindView(R.id.search_order_amount_tv)
    TextView amount_tv;
    @BindView(R.id.search_reload_list_layout)
    XReloadableListContentLayout xReloadableListContentLayout;
    SearchItemAdapter searchItemAdapter;
    private String start = "";
    private String end = "";
    //    SettingBean settingBean;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initData(Bundle savedInstanceState) {
//        settingBean = ShareUtils.getSettingInfo();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        //Create the player
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        xReloadableListContentLayout.getRecyclerView().verticalLayoutManager(context);
        searchItemAdapter = new SearchItemAdapter(context);
        searchItemAdapter.setRecItemClick(new RecyclerItemCallback<SearchItemBean, SearchItemAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, SearchItemBean model, int tag, SearchItemAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
                    @Override
                    public Dialog getDialog(Context context) {
                        return new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setMessage("点你妹！有没有数据心里没点逼数！")
                                .setPositiveButton("确定", (dialog, which) -> {
                                    dialog.dismiss();
                                    //initiate Player
                                    // Create a default TrackSelector
                                    String record_url = App.getSettingBean().record_ip.concat(":").concat(App.getSettingBean().record_port);
                                    String uri = String.format(record_url + "/video/%s.flv", Kits.Empty.check(model.order) ? model.sellid : model.order);
                                    SimpleExoPlayerView mPlayerView = new SimpleExoPlayerView(context);
                                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "exoplayerdemo"));
                                    // 创建资源
                                    Uri suri = Uri.parse(uri/*"http://192.168.1.140:3000/video/20200728-003.flv"*/);
                                    MediaSource mediaSources = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(suri);
                                    // 将播放器附加到view
                                    mPlayerView.setPlayer(exoPlayer);
                                    // 准备播放
                                    exoPlayer.prepare(mediaSources);
                                    // 准备好了之后自动播放，如果已经准备好了，调用该方法实现暂停、开始功能
                                    exoPlayer.setPlayWhenReady(true);
                                    // 添加事件监听
                                    exoPlayer.addListener(listener);
                                    DialogFragmentHelper.builder(context1 -> {
                                        AlertDialog alertDialog = new AlertDialog.Builder(context1, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setView(mPlayerView).create();
                                        return alertDialog;
                                    }, true).setDialogWindow(dialogWindow -> {
                                        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                                        dialogWindow.setAttributes(layoutParams);
                                        return dialogWindow;
                                    }).show(getChildFragmentManager(), "");

                                }).create();
                    }
                }, true).show(getChildFragmentManager(), "");
            }
        });
        xReloadableListContentLayout.setInterceptTouch(true);
        xReloadableListContentLayout.getRecyclerView().setAdapter(searchItemAdapter);
        xReloadableListContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getLocalData("", "");
                xReloadableListContentLayout.postDelayed(() -> {
                    xReloadableListContentLayout.refreshState(false);
                }, 1000);
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
        getLocalData("", "");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.search_enter_start_time_tv, R.id.search_enter_end_time_tv, R.id.search_commit_btn})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.search_enter_start_time_tv:
            case R.id.search_enter_end_time_tv:
                DialogFragmentHelper.builder(context -> {
                    DatePickerDialog pickerDialog = new DatePickerDialog(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    Locale locale = getResources().getConfiguration().locale;
                    Locale.setDefault(locale);
                    pickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                        month++;
                        ((TextView) view).setText(year + "年" + month + "月" + dayOfMonth + "日");
                        if (view == start_et)
                            start = String.format("%d_%02d_%02d", year, month, dayOfMonth);
                        else if (view == end_et)
                            end = String.format("%d_%02d_%02d", year, month, dayOfMonth);
                        pickerDialog.cancel();
                    });
                    return pickerDialog;
                }, true).show(getChildFragmentManager(), "");
                break;
            case R.id.search_commit_btn:
                int st = Kits.Empty.check(start) ? 0 : Integer.parseInt((start.replace("_", "")));
                int et = Kits.Empty.check(end) ? 0 : Integer.parseInt((end.replace("_", "")));
                if (et > 0 && st > et) {
                    ToastUtils.showLong("开始时间必须小于结束时间");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(simpleDateFormat.parse(end));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
                    Date date = calendar.getTime();
                    System.out.println("获取当前时间未来的第1天：" + date);
                    String send = simpleDateFormat.format(date);
                    System.out.println("格式化获取当前时间未来的第1天：" + end);
                    getLocalData(start, send);
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    calendar.setTime(new Date());
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getLocalData(String beginTime, String endTime) {
        Flowable<HttpResultModel<List<SearchItemBean>>> flowable = Flowable.create(emitter -> {
            List<SearchItemBean> beanList = new ArrayList<>();
            Box<OrderTableEntiry> box = App.getBoxStore().boxFor(OrderTableEntiry.class);
            /*List<OrderTableEntiry> entiryList = */
            java.util.function.Consumer<OrderTableEntiry> queryConsumer = entiry -> {
                SearchItemBean bean = new SearchItemBean();
                bean.index = (int) entiry._id;
                bean.order = entiry._order_id;
                bean.sellid = entiry.sell_id;
                bean.count = String.valueOf(entiry.count);
                bean.amount = entiry.amount;
                bean.person = entiry.purchaser;
                bean.phone = entiry.phone;
                bean.status = entiry.status;
                bean.createtime = entiry.crateTime;
                bean.action = entiry.action;
                bean.starttime = entiry.startTime;
                bean.endtime = entiry.endTime;
                beanList.add(bean);

            };
            if (Kits.Empty.check(beginTime) || Kits.Empty.check(endTime))
                box.query().build().find().forEach(queryConsumer);
            else
                box.query().between(OrderTableEntiry_.startDate, simpleDateFormat.parse(beginTime), simpleDateFormat.parse(endTime)).build().find().forEach(queryConsumer);
            HttpResultModel resultModel = new HttpResultModel();
            resultModel.resultData = beanList;
            resultModel.resultCode = 1;
            emitter.onNext(resultModel);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
        RxLoadingUtils.subscribeWithDialog(context, flowable, bindToLifecycle(), listHttpResultModel -> searchItemAdapter.setData(listHttpResultModel.resultData), false);
    }

    private void getData(String key, String begin, String end, int page) {
        Flowable<HttpResultModel> search = DataService.builder().buildReqUrl("")
                .buildReqParams("", key).buildReqParams("", begin).buildReqParams("", end)
                .buildReqParams("", page).request(ApiService.HttpMethod.POST);
        RxLoadingUtils.subscribeWithDialog(context, search, bindToLifecycle(), new Consumer<HttpResultModel>() {
            @Override
            public void accept(HttpResultModel httpResultModel) throws Exception {

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {

            }
        });//商城URL;;订单管理平台URL
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_layout;
    }

    @Override
    public void onDestroy() {
        if (null != exoPlayer) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer.removeListener(listener);
            exoPlayer = null;
        }
        super.onDestroy();
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


}
