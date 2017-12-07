package com.game.helper.views.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;


/**
 * Created by 神荼 on 2017/8/7.
 */

public class CountDownText extends AppCompatTextView {
    private CountDownTimer mCountDownTimer;
    private onTimeEndListener onTimeEndListener;

    public CountDownText(Context context) {
        super(context);
    }

    public CountDownText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCountDownTimer(long millis, long interval) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(millis, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("tick", "onTick: "+millisUntilFinished );
                setText(millis2FitTimeSpan(Math.abs(System.currentTimeMillis() - System.currentTimeMillis() + millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                setText("获取验证码");
                setClickable(true);
            }
        };
    }

    public void startTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer.start();
            setClickable(false);
        }
    }

    public void stopTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            setText("获取验证码");
        }
    }

    public void destroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (onTimeEndListener != null) {
            onTimeEndListener = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private String millis2FitTimeSpan(long millis) {
        return millis / 1000 + "\tS";
    }

    public void addOnTimeEndListener(onTimeEndListener onTimeEndListener) {
        this.onTimeEndListener = onTimeEndListener;
    }

    public interface onTimeEndListener {
        void onTimeEnd();
    }
}
