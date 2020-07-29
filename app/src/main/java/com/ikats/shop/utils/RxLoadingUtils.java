package com.ikats.shop.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.ikats.shop.event.RxBusProvider;
import com.ikats.shop.event.RxMsgEvent;
import com.ikats.shop.views.XReloadableListContentLayout;
import com.ikats.shop.views.XReloadableStateContorller;

import org.json.JSONException;

import java.net.UnknownHostException;

import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.IModel;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceMaybeObserver;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

public class RxLoadingUtils {
    public static <T> void subscribeWithReloadTwo(final XReloadableListContentLayout reloadableFrameLayout,
                                                  final Observable<T> observable, final ObservableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                  final Action onComplete, final boolean showloading) {
        if (reloadableFrameLayout == null) return;

        if (showloading)
            reloadableFrameLayout.showContent();
        reloadableFrameLayout.setOnReloadListener(reloadableFrameLayout1 -> subscribeWithReloadTwo(reloadableFrameLayout1, observable, transformer, onNext, onError, onComplete, showloading));
        final boolean[] finishReload = new boolean[]{false};
        observable
                .compose(transformer)
                .compose(upstream -> upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(new ResourceObserver<T>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            finishReload[0] = true;
                        }
                        reloadableFrameLayout.refreshState(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        NetError error = null;
                        if (e != null) {
                            if (!(e instanceof NetError)) {
                                if (e instanceof UnknownHostException) {
                                    error = new NetError(e, NetError.NoConnectError);
                                } else if (e instanceof JSONException
                                        || e instanceof JsonParseException
                                        || e instanceof JsonSyntaxException) {
                                    error = new NetError(e, NetError.ParseError);
                                } else {
                                    error = new NetError(e, NetError.OtherError);
                                }
                            } else {
                                error = (NetError) e;
                            }

                            if (XApi.getCommonProvider() != null) {
                                if (XApi.getCommonProvider().handleError(error)) {        //使用通用异常处理
                                    return;
                                }
                            }
                        }
                        RxBusProvider.getBus().postEvent(error);
                        saveErrorMessage(error);
                        if (onError != null) {
                            try {
                                onError.accept(error);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        reloadableFrameLayout.refreshState(false);
                        if (!finishReload[0]) {
                            reloadableFrameLayout.showError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                        }
                    }
                });
    }

    public static <T extends IModel> void subscribeWithReloadTwo(final XReloadableListContentLayout reloadableFrameLayout,
                                                                 Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext) {
        subscribeWithReloadTwo(reloadableFrameLayout, Flowable, transformer, onNext, null, null, false);
    }

    public static <T extends IModel> void subscribeWithDialog(final ProgressDialog progressDialog,
                                                              Flowable<T> tFlowable, FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                              final Action onComplete, final boolean isCancel/*是否可以中断请求*/) {
        if (transformer != null) {
            tFlowable = tFlowable.compose(transformer);
        }
        tFlowable.compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .subscribe(new ApiSubscriber<T>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        MainThreadPostUtils.post(() -> progressDialog.show());

                        if (isCancel) {
                            progressDialog.setCancelable(true);
                            progressDialog.setOnCancelListener(dialog -> {
                                dispose();
                                RxBusProvider.getBus().postEvent(new RxMsgEvent<String>("cancel_request"));
                            });
                        }
                    }

                    @Override
                    public void onNext(T t) {
                        MainThreadPostUtils.post(() -> {
                            if (onNext != null) {
                                try {
                                    onNext.accept(t);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        MainThreadPostUtils.post(() -> progressDialog.dismiss());
                    }

                    @Override
                    protected void onFail(NetError error) {
                        RxBusProvider.getBus().postEvent(error);
                        saveErrorMessage(error);

                        MainThreadPostUtils.post(() -> {
                            progressDialog.dismiss();
                            if (onError != null) {
                                try {
                                    onError.accept(error);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();

                        MainThreadPostUtils.post(() -> {
                            progressDialog.dismiss();
                            if (onComplete != null) {
                                try {
                                    onComplete.run();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, String dialogText) {
        subscribeWithDialog(getDefaultProgressDialog(context, dialogText), Flowable, transformer, onNext, null,
                null, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, final Action onComplete, String dialogText) {
        subscribeWithDialog(getDefaultProgressDialog(context, dialogText), Flowable, transformer, onNext, onError,
                onComplete, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, String dialogText) {
        subscribeWithDialog(getDefaultProgressDialog(context, dialogText), Flowable, transformer, onNext, onError,
                null, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, boolean canCancel) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, onError, null,
                canCancel);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, final Action onComplete) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, onError, onComplete, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, onError, null,
                false);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, boolean canCancel) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, null, null,
                canCancel);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, null, null, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              boolean canCancel) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, null, null, null,
                canCancel);
    }

    private static ProgressDialog getDefaultProgressDialog(Context context) {
        return getDefaultProgressDialog(context, "数据处理中...");
    }

    public static ProgressDialog getDefaultProgressDialog(Context context, String text) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("");
        dialog.setMessage(text);
        dialog.setCancelable(false);
        return dialog;
    }

    public static <T extends IModel> void subscribe(Flowable<T> tFlowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    final Consumer<NetError> onError,
                                                    final Action onComplete, final boolean toastError) {
        if (transformer != null) {
            tFlowable = tFlowable.compose(transformer);
        }
        tFlowable.compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .subscribe(new ApiSubscriber<T>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(T t) {
                        MainThreadPostUtils.post(() -> {
                            if (onNext != null) {
                                try {
                                    onNext.accept(t);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    protected void onFail(NetError error) {
                        RxBusProvider.getBus().postEvent(error);
                        saveErrorMessage(error);
                        MainThreadPostUtils.post(() -> {
                            if (onError != null) {
                                try {
                                    onError.accept(error);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (toastError) {
                                //show Toast Tips
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        MainThreadPostUtils.post(() -> {
                            if (onComplete != null) {
                                try {
                                    onComplete.run();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                });
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    final Consumer<NetError> onError) {
        subscribe(Flowable, transformer, onNext, onError, null, false);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    final Consumer<NetError> onError, boolean toastError) {
        subscribe(Flowable, transformer, onNext, onError, null, toastError);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext) {
        subscribe(Flowable, transformer, onNext, null, null, true);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    boolean toastError) {
        subscribe(Flowable, transformer, onNext, null, null, toastError);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer) {
        subscribe(Flowable, transformer, null, null, true);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, boolean toastError) {
        subscribe(Flowable, transformer, null, null, toastError);
    }

    private static String getDisplayMessage(Throwable throwable, boolean isSub) {
        return throwable instanceof RuntimeException
                ? getThrowableStackTrace(throwable, isSub)
                : throwable.getMessage();
    }

    private static final int MAX_STACK_TRACE_LENGTH = 500;

    private static String getThrowableStackTrace(Throwable throwable, boolean isSub) {
        String stackTrace = throwable.getMessage() + ":" + Log.getStackTraceString(throwable);
        if (isSub && stackTrace.length() > MAX_STACK_TRACE_LENGTH) {
            stackTrace = stackTrace.substring(0, MAX_STACK_TRACE_LENGTH);
        }
        return stackTrace;
    }


    public static <T extends IModel> void subscribeWithReloadThird(final XReloadableStateContorller reloadableFrameLayout,
                                                                   final Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                                   final Action onComplete, final boolean showLoading) {
        if (reloadableFrameLayout == null) return;
        if (showLoading) {
            reloadableFrameLayout.showLoading();
        }
        reloadableFrameLayout.setOnReloadListener(reloadableFrameLayout1 -> subscribeWithReloadThird(reloadableFrameLayout1, Flowable, transformer, onNext, onError, onComplete,
                showLoading));

        final boolean[] finishReload = new boolean[]{false};

        Flowable
                .compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .compose(transformer)
                .subscribe(new ApiSubscriber<T>() {

                    @Override
                    protected void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            finishReload[0] = true;
                        }
                    }

                    @Override
                    protected void onFail(NetError error) {
                        RxBusProvider.getBus().postEvent(error);
                        saveErrorMessage(error);
                        if (onError != null) {
                            try {
                                onError.accept(error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            reloadableFrameLayout.showError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                        }
                    }
                });
    }

    public static <T extends IModel> void subscribeWithReloadTwo(final XReloadableListContentLayout reloadableLayout,
                                                                 final Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                                 final Action onComplete, final boolean showLoading) {
        if (reloadableLayout == null) return;

        if (showLoading) {
            reloadableLayout.showLoading();
        }
        reloadableLayout.setOnReloadListener(reloadableFrameLayout -> subscribeWithReloadTwo(reloadableFrameLayout, Flowable, transformer, onNext, onError, onComplete,
                showLoading));

        final boolean[] finishReload = new boolean[]{false};

        Flowable
                .compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .compose(transformer)
                .subscribe(new ApiSubscriber<T>() {

                    @Override
                    protected void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            finishReload[0] = true;
                        }
                        reloadableLayout.refreshState(false);
                    }

                    @Override
                    protected void onFail(NetError error) {
                        RxBusProvider.getBus().postEvent(error);
                        saveErrorMessage(error);

                        if (!finishReload[0]) {
                            reloadableLayout.showError();
                        }
                        reloadableLayout.refreshState(false);

                        if (onError != null) {
                            try {
                                onError.accept(error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                        }
                    }
                });
    }

    public static <T> void subscribeWithReloadTwo(final XReloadableListContentLayout reloadableLayout,
                                                  final Maybe<T> Mayte, final Consumer<T> onNext, final Consumer<Throwable> onError,
                                                  final Action onComplete, final boolean finishWhenFirstOnNext) {
        if (reloadableLayout == null) return;

        if (finishWhenFirstOnNext) {
            reloadableLayout.showLoading();
        }
        reloadableLayout.setOnReloadListener(reloadableFrameLayout -> subscribeWithReloadTwo(reloadableFrameLayout, Mayte, onNext, onError, onComplete,
                finishWhenFirstOnNext));

        final boolean[] finishReload = new boolean[]{false};
        Mayte
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceMaybeObserver<T>() {
                    @Override
                    public void onSuccess(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            finishReload[0] = true;
                        }
                        reloadableLayout.refreshState(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onError != null) {
                            try {
                                onError.accept(e);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            reloadableLayout.showError();
                        }
                        reloadableLayout.refreshState(false);
                    }

                    @Override
                    public void onComplete() {
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                        }
                    }
                });
    }

    public static void saveErrorMessage(NetError e) {
//         测试 SDK 是否正常工作的代码
//        AVObject avObject = new AVObject("Android_phone");
////        avObject.put("Cookie", ShareUtils.getSessionId());
//        avObject.put("uId", ShareUtils.getString("uId", ""));
//        avObject.put("enId", ShareUtils.getString("enId", ""));
//        avObject.put("brand", SystemUtil.getDeviceBrand());
//        avObject.put("model", SystemUtil.getSystemModel());
//        avObject.put("systemversion", SystemUtil.getSystemVersion());
//        avObject.put("sdkVersion", SystemUtil.getSDKVersion());
//        avObject.put("versionName", SystemUtil.getLocalVersionName());
////        avObject.put("registrationid", JPushInterface.getRegistrationID(App.getInstance()));
//        avObject.put("plam", System.getProperty("os.name"));
//
//        StringBuilder sb = new StringBuilder();
//
//        Writer writer = new StringWriter();
//        PrintWriter pw = new PrintWriter(writer);
//        e.printStackTrace(pw);
////        Throwable cause = e.getCause();
////        // 循环取出Cause
////        while (cause != null) {
////            cause.printStackTrace(pw);
////            cause = e.getCause();
////        }
//        pw.close();
//        String result = writer.toString();
//        sb.append(result);
//        avObject.put("error", e.toString() + "--＞" + sb.toString());
//
//        avObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e == null) {
//                    Log.d("saved", "success!");
//                }
//            }
//        });
    }
}
