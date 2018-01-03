package com.game.helper.model.DownLoad;

import android.widget.Button;
import android.widget.TextView;

import com.game.helper.R;

import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

public class DownloadController {
    private TextView mStatus;
    private Button mAction;

    private DownloadState mState;

    public DownloadController(TextView status, Button action) {
        mStatus = status;
        mAction = action;
        setState(new Normal());
    }

    public void setState(DownloadState state) {
        mState = state;
        mState.setText(mStatus, mAction);
    }

    public void setEvent(DownloadEvent event) {
        int flag = event.getFlag();
        switch (flag) {
            case DownloadFlag.NORMAL:
                setState(new DownloadController.Normal());
                break;
            case DownloadFlag.WAITING:
                setState(new DownloadController.Waiting());
                break;
            case DownloadFlag.STARTED:
                setState(new DownloadController.Started());
                break;
            case DownloadFlag.PAUSED:
                setState(new DownloadController.Paused());
                break;
            case DownloadFlag.CANCELED:
                setState(new DownloadController.Canceled());
                break;
            case DownloadFlag.COMPLETED:
                setState(new DownloadController.Completed());
                break;
            case DownloadFlag.FAILED:
                setState(new DownloadController.Failed());
                break;
            case DownloadFlag.DELETED:
                setState(new DownloadController.Deleted());
                break;
            case DownloadFlag.INSTALLED:
                setState(new DownloadController.Open());
                break;
        }
    }

    public void handleClick(Callback callback) {
        mState.handleClick(callback);
    }

    public interface Callback {
        void startDownload();

        void pauseDownload();

        void cancelDownload();

        void installApk();

        void openApp();
    }

    static abstract class DownloadState {

        abstract void setText(TextView status, Button button);

        abstract void handleClick(Callback callback);
    }

    public static class Normal extends DownloadState {

        @Override
        void setText(TextView status, Button button) {
//            button.setText("下载");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_download);
            status.setText("");
        }

        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }

    public static class Waiting extends DownloadState {
        @Override
        void setText(TextView status, Button button) {
//            button.setText("等待中");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_waitting);
            status.setText("等待中...");
        }

        @Override
        void handleClick(Callback callback) {
            callback.cancelDownload();
        }
    }

    public static class Started extends DownloadState {
        @Override
        void setText(TextView status, Button button) {
//            button.setText("暂停");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_pause);
            status.setText("下载中...");
        }

        @Override
        void handleClick(Callback callback) {
            callback.pauseDownload();
        }
    }

    public static class Paused extends DownloadState {
        @Override
        void setText(TextView status, Button button) {
//            button.setText("继续");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_continue);
            status.setText("已暂停");
        }

        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }

    public static class Failed extends DownloadState {
        @Override
        void setText(TextView status, Button button) {
//            button.setText("继续");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_continue);
            status.setText("下载失败");
        }

        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }

    public static class Canceled extends DownloadState {
        @Override
        void setText(TextView status, Button button) {
//            button.setText("下载");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_download);
            status.setText("下载已取消");
        }

        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }

    public static class Completed extends DownloadState {
        @Override
        void setText(TextView status, Button button) {
//            button.setText("安装");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_install);
            status.setText("下载已完成");
        }

        @Override
        void handleClick(Callback callback) {
            callback.installApk();
        }
    }

    public static class Deleted extends DownloadState {

        @Override
        void setText(TextView status, Button button) {
//            button.setText("下载");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_download);
            status.setText("下载已取消");
        }

        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }

    public static class Open extends DownloadState {

        @Override
        void setText(TextView status, Button button) {
//            button.setText("打开");
            button.setBackgroundResource(R.mipmap.bg_game_list_item_open);
            status.setText("安装已完成");
        }

        @Override
        void handleClick(Callback callback) {
            callback.openApp();
        }
    }
}
