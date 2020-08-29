package com.ikats.shop.utils;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

public class ScannerKeyEventHelper {

    private StringBuffer mStringBufferResult;           //扫描内容
    private boolean mCaps;                              //大小写
    private final Handler mHandler;
    private final Runnable mScanningFinishedRunnable;
    private OnScanSuccessListener mOnScanSuccessListener;

    public ScannerKeyEventHelper(OnScanSuccessListener onScanSuccessListener) {
        mOnScanSuccessListener = onScanSuccessListener;
        mStringBufferResult = new StringBuffer();
        mHandler = new Handler();
        mScanningFinishedRunnable = new Runnable() {
            @Override
            public void run() {
                performScanSuccess();
            }
        };
    }

    /**
     * 扫描成功结果
     */
    private void performScanSuccess() {
        String barcode = mStringBufferResult.toString();
        if (mOnScanSuccessListener != null)
            mOnScanSuccessListener.onScanSuccess(barcode);
        mStringBufferResult.setLength(0);
    }

    /**
     * 扫描事件解析
     *
     * @param event
     */
    public void analysisKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        //判断字母大小写
        checkLetterStatus(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char aChar = getInputCode(event);
            if (aChar != 0) {
                mStringBufferResult.append(aChar);
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //回车键 返回
                mHandler.removeCallbacks(mScanningFinishedRunnable);
                mHandler.post(mScanningFinishedRunnable);
            }

        }
    }

    /**
     * shift键
     *
     * @param keyEvent
     */
    private void checkLetterStatus(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                //按住shift键 大写
                mCaps = true;
            } else {
                //小写
                mCaps = false;
            }
        }
    }

    /**
     * 获取扫描内容
     *
     * @param keyEvent
     * @return
     */
    private char getInputCode(KeyEvent keyEvent) {
        char aChar;
        int keyCode = keyEvent.getKeyCode();
        Log.i("TAGKEYCODE", keyCode + "");
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= keyEvent.KEYCODE_Z)//29< keycode <54
        {
            //字母
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);//
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //数字
            if (mCaps)//是否按住了shift键
            {
                //按住了 需要将数字转换为对应的字符
                switch (keyCode) {
                    case KeyEvent.KEYCODE_0:
                        aChar = ')';
                        break;
                    case KeyEvent.KEYCODE_1:
                        aChar = '!';
                        break;
                    case KeyEvent.KEYCODE_2:
                        aChar = '@';
                        break;
                    case KeyEvent.KEYCODE_3:
                        aChar = '#';
                        break;
                    case KeyEvent.KEYCODE_4:
                        aChar = '$';
                        break;
                    case KeyEvent.KEYCODE_5:
                        aChar = '%';
                        break;
                    case KeyEvent.KEYCODE_6:
                        aChar = '^';
                        break;
                    case KeyEvent.KEYCODE_7:
                        aChar = '&';
                        break;
                    case KeyEvent.KEYCODE_8:
                        aChar = '*';
                        break;
                    case KeyEvent.KEYCODE_9:
                        aChar = '(';
                        break;
                    default:
                        aChar = ' ';
                        break;
                }
            } else {
                aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
            }

        } else {
            //其他符号
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_STAR:
                    aChar = '*';
                    break;
                case KeyEvent.KEYCODE_POUND:
                    aChar = '#';
                    break;
                case KeyEvent.KEYCODE_SEMICOLON:
                    aChar = mCaps ? ':' : ';';
                    break;
                case KeyEvent.KEYCODE_AT:
                    aChar = '@';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = ' ';
                    break;
            }
        }

        return aChar;
    }

    public interface OnScanSuccessListener {
        void onScanSuccess(String barcode);
    }

    public void onDestroy() {
        mHandler.removeCallbacks(mScanningFinishedRunnable);
        mOnScanSuccessListener = null;
    }

}
