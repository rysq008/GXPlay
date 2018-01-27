package com.game.helper.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.WebviewFragment;
import com.game.helper.model.DownLoad.DownloadController;
import com.game.helper.model.GamePackageListResult;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.ReportedRequestBody;
import com.game.helper.utils.DownLoadReceiveUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import zlc.season.practicalrecyclerview.ItemType;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

import static zlc.season.rxdownload2.function.Utils.dispose;

/**
 * Created by Tian on 2017/12/21.
 */

public class ChannelListItemAdapter extends SimpleRecAdapter<ItemType, ChannelListItemAdapter.ViewHolder> {
    public static final int TAG_VIEW = 0;
    private RxPermissions rxPermissions;
    private RxDownload mRxDownload;
    private DataBaseHelper dataBaseHelper;
    private PackageManager pm;

    public void setStandAloneGame(boolean standAloneGame) {
        isStandAloneGame = standAloneGame;
    }

    private boolean isStandAloneGame;

    public ChannelListItemAdapter(Context context, RxPermissions rxPermissions) {
        super(context);
        this.rxPermissions = rxPermissions;
        mRxDownload = RxDownload.getInstance(context)
                .maxDownloadNumber(2);//最大下载数量
        dataBaseHelper = DataBaseHelper.getSingleton(context.getApplicationContext());
        pm = context.getPackageManager();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        final GamePackageListResult.ListBean itemDate = (GamePackageListResult.ListBean) data.get(position);
        holder.setDisplay(position, itemDate);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_channel_list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_channel_list_logothumb)
        ImageView ivLogothumb;
        @BindView(R.id.tv_channel_list_name)
        TextView tvtName;
        @BindView(R.id.tv_channel_list_discount_vip)
        TextView tvDiscountVip;
        @BindView(R.id.tv_channel_list_type_name)
        TextView tvTypeName;
        @BindView(R.id.tv_channel_list_package_filesize)
        TextView tvPackageFilesize;
        @BindView(R.id.tv_channel_list_source)
        TextView tvtSource;
        @BindView(R.id.percent_channel_list_item)
        TextView mPercent;
        @BindView(R.id.size_channel_list_item)
        TextView mSize;
        @BindView(R.id.status_channel_list_item)
        TextView mStatusText;
        @BindView(R.id.pb_channel_list_item)
        ProgressBar pbChannel;
        @BindView(R.id.btn_channel_list_load)
        Button ivChannelListLoad;
        @BindView(R.id.tv_channel_list_package_plat)
        TextView tvChannelPlat;
        @BindView(R.id.tv_channel_list_activity_discount)
        TextView tvActivityDiscount;
        @BindView(R.id.tv_channel_list_matching_activity_discount)
        TextView tvMatchingActivityDiscount;

        private DownloadBean downloadBean;
        private int flag;
        private DownloadController mDownloadController;
        private GamePackageListResult.ListBean mData;
        private Boolean isWebGame;

        public ViewHolder(View itemView) {
            super(itemView);

            KnifeKit.bind(this, itemView);
            mDownloadController = new DownloadController(mStatusText, ivChannelListLoad);
        }

        public void setDisplay(final int position, final GamePackageListResult.ListBean itemDate) {
            mData = itemDate;
            ILFactory.getLoader().loadNet(ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(itemDate.getGame().getLogo()), null);
            Float discount_vip = mData.getDiscount_vip();
            Float discount_activity = mData.getDiscount_activity();
            if (discount_activity > 0) {
                tvDiscountVip.setVisibility(View.GONE);
                tvActivityDiscount.setVisibility(View.VISIBLE);
                tvMatchingActivityDiscount.setVisibility(View.VISIBLE);
                tvMatchingActivityDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tvActivityDiscount.setText(discount_activity.toString() + "折");
                tvMatchingActivityDiscount.setText(discount_vip.toString() + "折");
            } else {
                tvDiscountVip.setVisibility(isStandAloneGame ? View.GONE : View.VISIBLE);
                tvActivityDiscount.setVisibility(View.GONE);
                tvMatchingActivityDiscount.setVisibility(View.GONE);
                tvDiscountVip.setText(discount_vip.toString() + "折");
            }
            String classTypeName = mData.getGame().getClass_type().getName();
            int filesize = (int) mData.getFilesize();
            String packageName = mData.getName_package();
            if ("手机页游".equals(classTypeName) && filesize <= 0 && TextUtils.isEmpty(packageName)) {
                isWebGame = true;
            } else {
                isWebGame = false;
            }
            tvtName.setText(itemDate.getGame().getName());
            tvTypeName.setText(itemDate.getGame().getType().getName());
            tvPackageFilesize.setText(String.valueOf(itemDate.getFilesize()) + "M");
            tvtSource.setText(itemDate.getChannel().getName());
            tvChannelPlat.setText(itemDate.getChannel().getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getRecItemClick() != null) {
                        getRecItemClick().onItemClick(position, itemDate, TAG_VIEW, ViewHolder.this);
                    }
                }
            });


            /*****************************************************************************************************************************/
            /**
             * important!! 如果有订阅没有取消,则取消订阅!防止ViewHolder复用导致界面显示的BUG!
             */
            downloadBean = new DownloadBean
                    .Builder(itemDate.getPath())
                    .setSaveName(null)      //not need.
                    .setSavePath(null)      //not need
                    .setExtra1(itemDate.getGame().getLogo())   //save extra info into database.
                    .setExtra2(itemDate.getGame().getName())  //save extra info into database.
                    .setExtra3(itemDate.getName_package())
                    .build();
//            Utils.dispose(mData.disposable);
//            Observable<DownloadEvent> replayDownloadStatus = mRxDownload.receiveDownloadStatus(itemDate.getPath())
//                    .replay()
//                    .autoConnect();
//            Observable<DownloadEvent> sampled = replayDownloadStatus
//                    .filter(new Predicate<DownloadEvent>() {
//                        @Override
//                        public boolean test(@NonNull DownloadEvent downloadEvent) throws Exception {
//                            return downloadEvent.getFlag() == DownloadFlag.STARTED;
//                        }
//                    })
//                    .throttleFirst(200, TimeUnit.MILLISECONDS);
//            Observable<DownloadEvent> noProgress = replayDownloadStatus
//                    .filter(new Predicate<DownloadEvent>() {
//                        @Override
//                        public boolean test(@NonNull DownloadEvent downloadEvent) throws Exception {
//                            return downloadEvent.getFlag() != DownloadFlag.STARTED;
//                        }
//                    });
//            mData.disposable = Observable.merge(sampled, noProgress)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<DownloadEvent>() {
//                        @Override
//                        public void accept(@NonNull DownloadEvent downloadEvent) throws Exception {
//                            if (flag != downloadEvent.getFlag()) {
//                                flag = downloadEvent.getFlag();
//                                log("all events:" + downloadEvent.getFlag());
//                            }
//                            if (downloadEvent.getFlag() == DownloadFlag.FAILED) {
//                                Throwable throwable = downloadEvent.getError();
//                                Log.w("TAG", throwable);
//                            }
//
//                            mDownloadController.setEvent(downloadEvent);
//                            updateProgressStatus(downloadEvent.getDownloadStatus());
//
//                            if (downloadEvent.getFlag() == DownloadFlag.COMPLETED) {
//                                File[] files = mRxDownload.getRealFiles(itemDate.getPath());
//                                if (files != null) {
//                                    if (files[0].exists()) {
//                                        PackageInfo info = pm.getPackageArchiveInfo(files[0].getPath(), PackageManager.GET_ACTIVITIES);
//                                        if (Utils.isAppInstalled(context,info.applicationInfo.packageName)) {
//                                            downloadEvent.setFlag(DownloadFlag.INSTALLED);
//                                        } else {
//                                            downloadEvent.setFlag(DownloadFlag.COMPLETED);
//                                            dataBaseHelper.updateRecord(itemDate.getPath(), DownloadFlag.INSTALL);
////                                            installApk();
//                                        }
//                                        mDownloadController.setEvent(downloadEvent);
//                                        updateProgressStatus(downloadEvent.getDownloadStatus());
//                                        dispose(mData.disposable);
//                                    } else {
//                                        dataBaseHelper.deleteRecord(itemDate.getPath());
//                                        downloadEvent.setFlag(DownloadFlag.NORMAL);
//                                        mDownloadController.setEvent(downloadEvent);
//                                        downloadEvent.setDownloadStatus(dataBaseHelper.readStatus(itemDate.getPath()));
//                                        updateProgressStatus(downloadEvent.getDownloadStatus());
//                                    }
//                                }
//                            }
//                        }
//                    });
            mData.disposable = DownLoadReceiveUtils.receiveDownloadEvent(context, mData.getPath(), mData.getName_package(), mDownloadController, new DownLoadReceiveUtils.OnDownloadEventReceiveListener() {
                @Override
                public void receiveDownloadEvent(DownloadEvent event, boolean isDisposable) {
                    updateProgressStatus(event);
                    if (isDisposable) {
                        dispose(mData.disposable);
                    }
                }
            });
            mDownloadController.setReportedRequestBody(new ReportedRequestBody(mData.getGame().getId(), mData.getChannel().getId(), 1), ((XActivity) context).bindToLifecycle());
        }

        private void updateProgressStatus(DownloadEvent event) {
            if (isWebGame) {
                event.setFlag(DownloadFlag.INSTALLED);
                mDownloadController.setEvent(event);
                dispose(mData.disposable);
            }
            if (event.getFlag() == DownloadFlag.INSTALLED || event.getFlag() == DownloadFlag.NORMAL) {
                pbChannel.setVisibility(View.GONE);
                mPercent.setVisibility(View.GONE);
                mSize.setVisibility(View.GONE);
                mStatusText.setVisibility(View.GONE);
            } else if (event.getFlag() == DownloadFlag.COMPLETED) {
                pbChannel.setVisibility(View.GONE);
                mPercent.setVisibility(View.GONE);
                mSize.setVisibility(View.GONE);
                mStatusText.setVisibility(View.VISIBLE);
            } else {
                mStatusText.setVisibility(View.VISIBLE);
                pbChannel.setVisibility(View.VISIBLE);
                mPercent.setVisibility(View.VISIBLE);
                mSize.setVisibility(View.VISIBLE);
                pbChannel.setIndeterminate(event.getDownloadStatus().isChunked);
                pbChannel.setMax((int) event.getDownloadStatus().getTotalSize());
                pbChannel.setProgress((int) event.getDownloadStatus().getDownloadSize());
                mPercent.setText(event.getDownloadStatus().getPercent());
                if (event.getDownloadStatus().getDownloadSize() > 0 && event.getDownloadStatus().getTotalSize() > 0)
                    mSize.setText(event.getDownloadStatus().getFormatStatusString());
            }
        }

        @OnClick(R.id.btn_channel_list_load)
        public void onClick() {
            mDownloadController.handleClick(new DownloadController.Callback() {
                @Override
                public void startDownload() {
                    DownLoadReceiveUtils.startDownload(context, rxPermissions, downloadBean);
                }

                @Override
                public void pauseDownload() {
                    DownLoadReceiveUtils.pauseDownload(context, mData.getPath());
                }

                @Override
                public void cancelDownload() {
                }

                @Override
                public void installApk() {
                    DownLoadReceiveUtils.installApk(context, mData.getPath());
                }

                @Override
                public void openApp() {
                    if (isWebGame) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebviewFragment.PARAM_URL, mData.getPath());
                        bundle.putString(WebviewFragment.PARAM_TITLE, mData.getGame().getName());
                        DetailFragmentsActivity.launch(context, bundle, WebviewFragment.newInstance());
                    } else {
                        DownLoadReceiveUtils.openApp(context, mData.getName_package());
                    }

                }
            });


        }

    }
}
