package com.game.helper.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.game.helper.model.DownLoad.DownloadController;
import com.game.helper.model.DownLoad.DownloadItem;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.droidlover.xdroidmvp.kit.Kits;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static zlc.season.rxdownload2.function.Utils.dispose;

public class DownLoadReceiveUtils {

    public interface OnDownloadEventReceiveListener {
        public void receiveDownloadEvent(DownloadEvent event, boolean isDisposable);
    }

    public static void receiveDownloadEvent(final Context context, final String url, Disposable disposable, final DownloadController controller, final OnDownloadEventReceiveListener receive) {

        Observable<DownloadEvent> replayDownloadStatus = RxDownload.getInstance(context).receiveDownloadStatus(url)
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
        disposable = Observable.merge(sampled, noProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(@NonNull DownloadEvent downloadEvent) throws Exception {
                        if (downloadEvent.getFlag() == DownloadFlag.FAILED) {
                            Throwable throwable = downloadEvent.getError();
                            Log.w("TAG", throwable);
                        }
                        controller.setEvent(downloadEvent);
                        if (null != receive) {
                            receive.receiveDownloadEvent(downloadEvent, false);
//                            updateProgressStatus(downloadEvent.getDownloadStatus());
                        }

                        if (downloadEvent.getFlag() == DownloadFlag.COMPLETED) {
                            File[] files = RxDownload.getInstance(context).getRealFiles(url);
                            if (files != null) {
                                if (files[0].exists()) {
                                    PackageInfo info = context.getPackageManager().getPackageArchiveInfo(files[0].getPath(), PackageManager.GET_ACTIVITIES);
                                    if (Utils.isAppInstalled(context, info.applicationInfo.packageName)) {
                                        downloadEvent.setFlag(DownloadFlag.INSTALLED);
                                    } else {
                                        downloadEvent.setFlag(DownloadFlag.COMPLETED);
//                                        DataBaseHelper.getSingleton(context).updateRecord(url, DownloadFlag.INSTALL);
//                                            installApk();
                                    }
                                    controller.setEvent(downloadEvent);
                                    if (null != receive) {
                                        receive.receiveDownloadEvent(downloadEvent, true);
//                                        updateProgressStatus(downloadEvent.getDownloadStatus());
                                    }
//                                    dispose(disposable);
                                } else {
                                    DataBaseHelper.getSingleton(context).deleteRecord(url);
                                    downloadEvent.setFlag(DownloadFlag.NORMAL);
                                    controller.setEvent(downloadEvent);
                                    downloadEvent.setDownloadStatus(DataBaseHelper.getSingleton(context).readStatus(url));
                                    if (null != receive) {
                                        receive.receiveDownloadEvent(downloadEvent, false);
//                                        updateProgressStatus(downloadEvent.getDownloadStatus());
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public static void installApk(Context context, RxDownload rxDownload, String url) {
        if (Kits.Empty.check(context) || Kits.Empty.check(rxDownload) || Kits.Empty.check(url))
            return;
        File[] files = rxDownload.getRealFiles(url);
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

    public static void startDownload(final Context context, RxDownload rxDownload, RxPermissions rxPermissions, DownloadBean downloadBean) {
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
                .compose(rxDownload.<Boolean>transformService(downloadBean))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(context, "下载开始", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void startDownload(final Context context, RxDownload rxDownload, RxPermissions rxPermissions, DownloadItem downloadItem) {
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
                .compose(rxDownload.<Boolean>transformService(downloadItem.record.getUrl()))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(context, "下载开始", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static void pauseDownload(RxDownload rxDownload, String url) {
        rxDownload.pauseServiceDownload(url).subscribe();
    }

    public static void deleteDownload(RxDownload rxDownload, Disposable disposable, String url, final OnDownloadEventReceiveListener listener) {
        dispose(disposable);
        rxDownload.deleteServiceDownload(url, true)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
//                        mAdapter.remove(getAdapterPosition());
                        if (null != listener) {
                            listener.receiveDownloadEvent(null, false);
                        }
                    }
                })
                .subscribe();

    }

    public static void showPopUpWindow(Context mContext, View view) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);
        listPopupWindow.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1,
                new String[]{"删除"}));
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
//                    delete();
                    listPopupWindow.dismiss();
                }
            }
        });
        listPopupWindow.setWidth(200);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setModal(false);
        listPopupWindow.show();
    }
}