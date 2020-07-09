package com.zhny.library.base;

import android.os.CountDownTimer;

import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.util.Objects;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public LoadingDialog loadingDialog = null;

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(Objects.requireNonNull(getContext()));
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dismissLoading () {
        new MyTimer(500, 100).start();
    }

    private class MyTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            dismissRealLoading();
        }
    }

    public void dismissRealLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null)  loadingDialog.dismiss();
    }
}

