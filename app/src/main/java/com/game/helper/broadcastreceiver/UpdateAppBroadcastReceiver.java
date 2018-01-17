package com.game.helper.broadcastreceiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

/**
 * Created by Tian on 2018/1/16.
 */

public class UpdateAppBroadcastReceiver extends BroadcastReceiver {
    public static final String DOWNLOAD_ID = "downloadId";
    @Override
    public void onReceive(Context context, Intent intent) {
        int downloadId = intent.getIntExtra(DOWNLOAD_ID, 0);
        String action = intent.getAction();
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //文件下载成功时
            DownloadManager.Query query = new DownloadManager.Query();
            //通过下载的id查找
            query.setFilterById(downloadId);
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:

                        break;

                }

            }
        }

    }
}
