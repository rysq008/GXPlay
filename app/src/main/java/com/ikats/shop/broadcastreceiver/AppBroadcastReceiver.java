package com.ikats.shop.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ikats.shop.App;
import com.ikats.shop.activitys.BaseActivity.XBaseActivity;
import com.tamsiree.rxkit.RxNetTool;

public class AppBroadcastReceiver extends BroadcastReceiver {
    public static final String DOWNLOAD_ID = "downloadId";

    @Override
    public void onReceive(Context context, Intent intent) {
//        int downloadId = intent.getIntExtra(DOWNLOAD_ID, 0);
//        String action = intent.getAction();
//        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
//            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            //文件下载成功时
//            DownloadManager.Query query = new DownloadManager.Query();
//            //通过下载的id查找
//            query.setFilterById(downloadId);
//            Cursor c = downloadManager.query(query);
//            if (c.moveToFirst()) {
//                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
//                switch (status) {
//                    //下载完成
//                    case DownloadManager.STATUS_SUCCESSFUL:
//
//                        break;
//
//                }
//
//            }
//        }
        if (RxNetTool.isConnected(context)) {
        } else {
            if (App.getActivity() instanceof XBaseActivity) {
//                FragmentManager fragmentManager = ((XBaseActivity) App.getActivity()).getSupportFragmentManager();
//                DialogFragmentHelper.builder(context1 -> new AlertDialog.Builder(context1, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("请检测网络设置！")
//                        .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                RxNetTool.openWirelessSettings(context1);
//                            }
//                        }).create(), true).show(fragmentManager, "");
            }
        }
    }
}
