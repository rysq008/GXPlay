package com.zhny.zhny_app.views.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import cn.droidlover.xdroidmvp.kit.Kits;


public class TotoroToast implements IToast {

    private static Handler mHandler = new Handler();

    /**
     * 维护toast的队列
     */
    private static BlockingQueue<TotoroToast> mQueue = new LinkedBlockingQueue<TotoroToast>();

    /**
     * 原子操作：判断当前是否在读取{@linkplain #mQueue 队列}来显示toast
     */
    protected static AtomicInteger mAtomicInteger = new AtomicInteger(0);

    private long mDurationMillis;

    private Context mContext;

    private Toast toast;
    private Object mTN;
    private Method show;
    private Method hide;

    public static IToast makeText(Context context, String text, long duration) {
        return new TotoroToast(context).setText(text).setDuration(duration)
                .setGravity(Gravity.BOTTOM, 0, (int) Kits.Dimens.dpToPx(context, 64));
    }

    public TotoroToast(Context context) {
        if (context == null) {
            return;
        }

        toast = new Toast(context);
        mContext = context;
    }

    /**
     * Set the location at which the notification should appear on the screen.
     *
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public IToast setGravity(int gravity, int xOffset, int yOffset) {

        // We can resolve the Gravity here by using the Locale for getting
        // the layout direction
        return this;
    }

    @Override
    public IToast setDuration(long durationMillis) {
        if (durationMillis < 0) {
            mDurationMillis = 0;
        }
        if (durationMillis == Toast.LENGTH_SHORT) {
            mDurationMillis = 2000;
        } else if (durationMillis == Toast.LENGTH_LONG) {
            mDurationMillis = 3500;
        } else {
            mDurationMillis = durationMillis;
        }
        return this;
    }

    /**
     * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param view 传入view
     * @return 自身对象
     */
    @Override
    public IToast setView(View view) {
        toast.setView(view);
        return this;
    }

    @Override
    public IToast setMargin(float horizontalMargin, float verticalMargin) {
        return this;
    }

    /**
     * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param text 字符串
     * @return 自身对象
     */
    @Override
    public IToast setText(String text) {

        // 模拟Toast的布局文件 com.android.internal.R.layout.transient_notification
        // 虽然可以手动用java写，但是不同厂商系统，这个布局的设置好像是不同的，因此我们自己获取原生Toast的view进行配置

        View view = Toast.makeText(mContext, text, Toast.LENGTH_SHORT).getView();
        if (view != null) {
            TextView tv = (TextView) view.findViewById(android.R.id.message);
            tv.setText(text);
            setView(view);
        }

        return this;
    }

    @Override
    public void show() {
        // 1. 将本次需要显示的toast加入到队列中
        mQueue.offer(this);

        // 2. 如果队列还没有激活，就激活队列，依次展示队列中的toast
        if (0 == mAtomicInteger.get()) {
            mAtomicInteger.incrementAndGet();
            mHandler.post(mActivite);
        }
    }

    @Override
    public void cancel() {
        // 1. 如果队列已经处于非激活状态或者队列没有toast了，就表示队列没有toast正在展示了，直接return
        if (0 == mAtomicInteger.get() && mQueue.isEmpty()) {
            return;
        }

        // 2. 当前显示的toast是否为本次要取消的toast，如果是的话
        // 2.1 先移除之前的队列逻辑
        // 2.2 立即暂停当前显示的toast
        // 2.3 重新激活队列
        if (this.equals(mQueue.peek())) {
            mHandler.removeCallbacks(mActivite);
            mHandler.post(mHide);
            mHandler.post(mActivite);
        }
    }

    private void handleShow() {
        initTN();
        try {
            if (show == null || mTN == null) {
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            show.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void handleHide() {
        try {
            if (null != hide) {
                hide.invoke(mTN);
                mQueue.poll();
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void activeQueue() {
        TotoroToast totoroToast = mQueue.peek();
        if (totoroToast == null) {
            // 如果不能从队列中获取到toast的话，那么就表示已经暂时完所有的toast了
            // 这个时候需要标记队列状态为：非激活读取中
            mAtomicInteger.decrementAndGet();
        } else {

            // 如果还能从队列中获取到toast的话，那么就表示还有toast没有展示
            // 1. 展示队首的toast
            // 2. 设置一定时间后主动采取toast消失措施
            // 3. 设置展示完毕之后再次执行本逻辑，以展示下一个toast
            mHandler.post(totoroToast.mShow);
            mHandler.postDelayed(totoroToast.mHide, totoroToast.mDurationMillis);
            mHandler.postDelayed(mActivite, totoroToast.mDurationMillis);
        }
    }

    private final Runnable mShow = new Runnable() {
        @Override
        public void run() {
            handleShow();
        }
    };

    private final Runnable mHide = new Runnable() {
        @Override
        public void run() {
            handleHide();
        }
    };

    private final static Runnable mActivite = new Runnable() {
        @Override
        public void run() {
            activeQueue();
        }
    };

    public static int getAbsoluteGravity(int gravity, int layoutDirection) {
        int result = gravity;
        // If layout is script specific and gravity is horizontal relative (START or END)
        if ((result & Gravity.RELATIVE_LAYOUT_DIRECTION) > 0) {
            if ((result & Gravity.START) == Gravity.START) {
                // Remove the START bit
                result &= ~Gravity.START;
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                    // Set the RIGHT bit
                    result |= Gravity.RIGHT;
                } else {
                    // Set the LEFT bit
                    result |= Gravity.LEFT;
                }
            } else if ((result & Gravity.END) == Gravity.END) {
                // Remove the END bit
                result &= ~Gravity.END;
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                    // Set the LEFT bit
                    result |= Gravity.LEFT;
                } else {
                    // Set the RIGHT bit
                    result |= Gravity.RIGHT;
                }
            }
            // Don't need the script specific bit any more, so remove it as we are converting to
            // absolute values (LEFT or RIGHT)
            result &= ~Gravity.RELATIVE_LAYOUT_DIRECTION;
        }
        return result;
    }

    private void initTN() {
        try {
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(toast);
            show = mTN.getClass().getMethod("show");
            hide = mTN.getClass().getMethod("hide");

            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
