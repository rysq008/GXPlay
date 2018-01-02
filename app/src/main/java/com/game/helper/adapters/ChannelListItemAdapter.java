package com.game.helper.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.GameDetailFragment;
import com.game.helper.model.DownLoad.DownloadController;
import com.game.helper.model.GamePackageListResult;
import com.game.helper.net.api.Api;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import zlc.season.practicalrecyclerview.ItemType;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadStatus;
import zlc.season.rxdownload2.function.Utils;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static zlc.season.rxdownload2.function.Utils.dispose;
import static zlc.season.rxdownload2.function.Utils.log;

/**
 * Created by Tian on 2017/12/21.
 */

public class ChannelListItemAdapter extends SimpleRecAdapter<ItemType, ChannelListItemAdapter.ViewHolder> {
    public static final int TAG_VIEW = 0;
    private RxPermissions rxPermissions;
    private RxDownload mRxDownload;
    private DataBaseHelper dataBaseHelper;
    private PackageManager pm;

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
        @BindView(R.id.percent)
        TextView mPercent;
        @BindView(R.id.size)
        TextView mSize;
        @BindView(R.id.status)
        TextView mStatusText;
        @BindView(R.id.pb_channel_list)
        ProgressBar pbChannel;
        @BindView(R.id.iv_channel_list_load)
        Button ivChannelListLoad;

        private DownloadBean downloadBean;
        private int flag;
        private DownloadController mDownloadController;
        private GamePackageListResult.ListBean mData;

        public ViewHolder(View itemView) {
            super(itemView);

            KnifeKit.bind(this, itemView);
            mDownloadController = new DownloadController(mStatusText, ivChannelListLoad);
        }

        public void setDisplay(final int position, final GamePackageListResult.ListBean itemDate) {
            mData = itemDate;
            ILFactory.getLoader().loadNet(ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(itemDate.getGame().getLogo()), ILoader.Options.defaultOptions());
            tvtName.setText(itemDate.getGame().getName());
            tvDiscountVip.setText(String.valueOf(itemDate.getDiscount_vip()));
            tvTypeName.setText(itemDate.getGame().getType().getName());
            tvPackageFilesize.setText(String.valueOf(itemDate.getFilesize()) + "M");
            tvtSource.setText(itemDate.getChannel().getName());
            //holder.ivntro.setText(itemDate.intro);
            //holder.gameId = itemDate.id;
            ivChannelListLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("gamepackeId", itemDate.getId());
                    bundle.putInt("gameId", itemDate.getGame().getId());
                    bundle.putInt("channelId", itemDate.getChannel().getId());
                    DetailFragmentsActivity.launch(context, bundle, GameDetailFragment.newInstance());
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
//                .setExtra3("com.tencent.mobileqq")
                    .build();
            Utils.dispose(mData.disposable);
            Observable<DownloadEvent> replayDownloadStatus = mRxDownload.receiveDownloadStatus(itemDate.getPath())
                    .replay()
                    .autoConnect();
            Observable<DownloadEvent> sampled = replayDownloadStatus
                    .filter(new Predicate<DownloadEvent>() {
                        @Override
                        public boolean test(@NonNull DownloadEvent downloadEvent) throws Exception {
                            return downloadEvent.getFlag() == DownloadFlag.STARTED;
                        }
                    })
                    .throttleFirst(200, TimeUnit.MILLISECONDS);
            Observable<DownloadEvent> noProgress = replayDownloadStatus
                    .filter(new Predicate<DownloadEvent>() {
                        @Override
                        public boolean test(@NonNull DownloadEvent downloadEvent) throws Exception {
                            return downloadEvent.getFlag() != DownloadFlag.STARTED;
                        }
                    });
            mData.disposable = Observable.merge(sampled, noProgress)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DownloadEvent>() {
                        @Override
                        public void accept(@NonNull DownloadEvent downloadEvent) throws Exception {
                            if (flag != downloadEvent.getFlag()) {
                                flag = downloadEvent.getFlag();
                                log("all events:" + downloadEvent.getFlag());
                            }
                            if (downloadEvent.getFlag() == DownloadFlag.FAILED) {
                                Throwable throwable = downloadEvent.getError();
                                Log.w("TAG", throwable);
                            }

                            mDownloadController.setEvent(downloadEvent);
                            updateProgressStatus(downloadEvent.getDownloadStatus());

                            if (downloadEvent.getFlag() == DownloadFlag.COMPLETED) {
                                File[] files = mRxDownload.getRealFiles(itemDate.getPath());
                                if (files != null) {
                                    if (files[0].exists()) {
                                        PackageInfo info = pm.getPackageArchiveInfo(files[0].getPath(), PackageManager.GET_ACTIVITIES);
                                        if (isAppInstalled(info.applicationInfo.packageName)) {
                                            downloadEvent.setFlag(DownloadFlag.INSTALLED);
                                        } else {
                                            downloadEvent.setFlag(DownloadFlag.COMPLETED);
                                            dataBaseHelper.updateRecord(itemDate.getPath(), DownloadFlag.INSTALL);
//                                            installApk();
                                        }
                                        mDownloadController.setEvent(downloadEvent);
                                        updateProgressStatus(downloadEvent.getDownloadStatus());
                                        dispose(mData.disposable);
                                    } else {
                                        dataBaseHelper.deleteRecord(itemDate.getPath());
                                        downloadEvent.setFlag(DownloadFlag.NORMAL);
                                        mDownloadController.setEvent(downloadEvent);
                                        downloadEvent.setDownloadStatus(dataBaseHelper.readStatus(itemDate.getPath()));
                                        updateProgressStatus(downloadEvent.getDownloadStatus());
                                    }
                                }
                            }
                        }
                    });
        }

        private boolean isAppInstalled(String uri) {
            boolean installed;
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                installed = true;
            } catch (PackageManager.NameNotFoundException e) {
                installed = false;
            }
            return installed;
        }

        private void updateProgressStatus(DownloadStatus status) {
            pbChannel.setIndeterminate(status.isChunked);
            pbChannel.setMax((int) status.getTotalSize());
            pbChannel.setProgress((int) status.getDownloadSize());
            mPercent.setText(status.getPercent());
            mSize.setText(status.getFormatStatusString());
        }

        @OnClick(R.id.iv_channel_list_load)
        public void onClick() {
            mDownloadController.handleClick(new DownloadController.Callback() {
                @Override
                public void startDownload() {
                    start();
                }

                @Override
                public void pauseDownload() {
                    pause();
                }

                @Override
                public void cancelDownload() {
                }

                @Override
                public void installApk() {
                    install();
                }

                @Override
                public void openApp() {
                    //Intent intent = context.getPackageManager().getLaunchIntentForPackage()
                    open();
                }
            });
        }

        private void start() {
            rxPermissions
                    .request(WRITE_EXTERNAL_STORAGE)
                    .doOnNext(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (!granted) {
                                throw new RuntimeException("no permission");
                            }
                        }
                    })
                    .compose(mRxDownload.<Boolean>transformService(downloadBean))
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Toast.makeText(context, "下载开始", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void pause() {
            mRxDownload.pauseServiceDownload(mData.getPath()).subscribe();
        }

        private void install() {
            File[] files = mRxDownload.getRealFiles(mData.getPath());
            if (files != null) {
                Uri uri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(context, context.getApplicationInfo().packageName + ".provider", files[0]);
                } else {
                    uri = Uri.fromFile(files[0]);
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "File not exists", Toast.LENGTH_SHORT).show();
            }
        }

        private void open() {
            Toast.makeText(context, "open", Toast.LENGTH_LONG).show();
        }
    }
}
