package com.example.zr.gxapplication.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.avos.avoscloud.AVObject;
import com.shandianshua.base.utils.MainThreadPostUtils;
import com.shandianshua.base.utils.NetUtils;
import com.shandianshua.ui.R;
import com.shandianshua.ui.utils.DialogUtils;
import com.shandianshua.ui.view.ReloadableFrameLayout;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zhoulei date: 15/12/2.
 */
public class RxLoadingUtils {
  private static final int MAX_STACK_TRACE_LENGTH = 500;
  private static final int UPLOAD_ERROR_TIME = 10;

  public static <T> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
      final Observable<T> observable, final Action1<T> onNext, final Action1<Throwable> onError,
      final Action0 onComplete, final boolean finishWhenFirstOnNext) {
    if (reloadableFrameLayout == null) return;

    reloadableFrameLayout.showLoadingView();
    reloadableFrameLayout.setOnReloadListener(new ReloadableFrameLayout.OnReloadListener() {
      @Override
      public void onReload(ReloadableFrameLayout reloadableFrameLayout) {
        subscribeWithReload(reloadableFrameLayout, observable, onNext, onError, onComplete,
            finishWhenFirstOnNext);
      }
    });

    final boolean[] finishReload = new boolean[] {false};

    observable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<T>() {
          @Override
          public void call(T t) {
            if (onNext != null) {
              onNext.call(t);
            }
            if (finishWhenFirstOnNext && !finishReload[0]) {
              reloadableFrameLayout.finishReload();
              finishReload[0] = true;
            }
          }
        },
            new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                if (onError != null) {
                  onError.call(throwable);
                }
                if (!finishReload[0]) {
                  reloadableFrameLayout.needReload(getDisplayMessage(throwable, true));
                }
              }
            },
            new Action0() {
              @Override
              public void call() {
                if (onComplete != null) {
                  onComplete.call();
                }
                if (!finishReload[0]) {
                  reloadableFrameLayout.finishReload();
                }
              }
            });
  }

  public static <T> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
      Observable<T> observable, final Action1<T> onNext, final Action1<Throwable> onError) {
    subscribeWithReload(reloadableFrameLayout, observable, onNext, onError, null, false);
  }

  public static <T> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
      Observable<T> observable, final Action1<T> onNext, final Action1<Throwable> onError,
      boolean finishWhenFirstOnNext) {
    subscribeWithReload(reloadableFrameLayout, observable, onNext, onError, null,
        finishWhenFirstOnNext);
  }

  public static <T> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
      Observable<T> observable, final Action1<T> onNext) {
    subscribeWithReload(reloadableFrameLayout, observable, onNext, null, null, false);
  }

  public static <T> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
      Observable<T> observable, final Action1<T> onNext,
      boolean finishWhenFirstOnNext) {
    subscribeWithReload(reloadableFrameLayout, observable, onNext, null, null,
        finishWhenFirstOnNext);
  }

  public static <T> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
      Observable<T> observable) {
    subscribeWithReload(reloadableFrameLayout, observable, null, null, null, false);
  }

  public static <T> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
      Observable<T> observable, boolean finishWhenFirstOnNext) {
    subscribeWithReload(reloadableFrameLayout, observable, null, null, null, finishWhenFirstOnNext);
  }

  public static <T> void subscribeWithDialog(final ProgressDialog progressDialog,
      Observable<T> observable, final Action1<T> onNext, final Action1<Throwable> onError,
      final Action0 onComplete, final boolean toastErrorMeg) {
    bindDialog(progressDialog, observable, false)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<T>() {
          @Override
          public void call(T t) {
            if (onNext != null) {
              onNext.call(t);
            }
          }
        },
            new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                if (onError != null) {
                  onError.call(throwable);
                }
                if (toastErrorMeg) {
                  showError(throwable);
                }
              }
            },
            new Action0() {
              @Override
              public void call() {
                if (onComplete != null) {
                  onComplete.call();
                }
              }
            });
  }

  public static <T> void subscribeWithDialog(Context context, Observable<T> observable,
                                             final Action1<T> onNext, String dialogText) {
    subscribeWithDialog(getDefaultProgressDialog(context, dialogText), observable, onNext, null,
        null, true);
  }

  public static <T> void subscribeWithDialog(Context context, Observable<T> observable,
                                             final Action1<T> onNext, final Action1<Throwable> onError, String dialogText) {
    subscribeWithDialog(getDefaultProgressDialog(context, dialogText), observable, onNext, onError,
        null, true);
  }

  public static <T> void subscribeWithDialog(Context context, Observable<T> observable,
                                             final Action1<T> onNext, final Action1<Throwable> onError, boolean toastErrorMsg) {
    subscribeWithDialog(getDefaultProgressDialog(context), observable, onNext, onError, null,
        toastErrorMsg);
  }

  public static <T> void subscribeWithDialog(Context context, Observable<T> observable,
                                             final Action1<T> onNext, final Action1<Throwable> onError) {
    subscribeWithDialog(getDefaultProgressDialog(context), observable, onNext, onError, null,
        false);
  }

  public static <T> void subscribeWithDialog(Context context, Observable<T> observable,
                                             final Action1<T> onNext, boolean toastErrorMsg) {
    subscribeWithDialog(getDefaultProgressDialog(context), observable, onNext, null, null,
        toastErrorMsg);
  }

  public static <T> void subscribeWithDialog(Context context, Observable<T> observable,
                                             final Action1<T> onNext) {
    subscribeWithDialog(getDefaultProgressDialog(context), observable, onNext, null, null, true);
  }

  public static <T> void subscribeWithDialog(Context context, Observable<T> observable,
                                             boolean toastErrorMsg) {
    subscribeWithDialog(getDefaultProgressDialog(context), observable, null, null, null,
        toastErrorMsg);
  }

  private static ProgressDialog getDefaultProgressDialog(Context context) {
    return getDefaultProgressDialog(context,
        context.getString(R.string.sds_base_progress_dialog_message));
  }

  private static ProgressDialog getDefaultProgressDialog(Context context, String text) {
    ProgressDialog dialog = new ProgressDialog(context);
    dialog.setTitle("");
    dialog.setMessage(text);
    dialog.setCancelable(false);
    return dialog;
  }


  public static <T> void subscribe(Observable<T> observable, final Action1<T> onNext,
      final Action1<Throwable> onError,
      final Action0 onComplete, final boolean toastError) {
    observable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<T>() {
          @Override
          public void call(T t) {
            if (onNext != null) {
              onNext.call(t);
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            if (onError != null) {
              onError.call(throwable);
            }
            if (toastError) {
              showError(throwable);
            }
          }
        }, new Action0() {
          @Override
          public void call() {
            if (onComplete != null) {
              onComplete.call();
            }
          }
        });
  }

  public static <T> void subscribe(Observable<T> observable, final Action1<T> onNext,
      final Action1<Throwable> onError) {
    subscribe(observable, onNext, onError, null, false);
  }

  public static <T> void subscribe(Observable<T> observable, final Action1<T> onNext,
                                   final Action1<Throwable> onError, boolean toastError) {
    subscribe(observable, onNext, onError, null, toastError);
  }

  public static <T> void subscribe(Observable<T> observable, final Action1<T> onNext) {
    subscribe(observable, onNext, null, null, true);
  }

  public static <T> void subscribe(Observable<T> observable, final Action1<T> onNext,
      boolean toastError) {
    subscribe(observable, onNext, null, null, toastError);
  }

  public static <T> void subscribe(Observable<T> observable) {
    subscribe(observable, null, null, null, true);
  }

  public static <T> void subscribe(Observable<T> observable, boolean toastError) {
    subscribe(observable, null, null, null, toastError);
  }

  public static <T> Observable<T> bindDialog(final Context context, Observable<T> observable) {
    return bindDialog(context, observable, false);
  }

  public static <T> Observable<T> bindDialog(final Context context, Observable<T> observable,
                                             boolean cancelable) {
    return bindDialog(getDefaultProgressDialog(context), observable, cancelable);
  }

  public static <T> Observable<T> bindDialog(final ProgressDialog progressDialog,
      final Observable<T> observable, final boolean cancelable) {
    if (progressDialog == null) {
      return observable;
    }
    return observable.lift(new Observable.Operator<T, T>() {
      @Override
      public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>() {
          @Override
          public void onStart() {
            MainThreadPostUtils.post(new Runnable() {
              @Override
              public void run() {
                DialogUtils.safeShowDialog(progressDialog);
              }
            });
            if (cancelable) {
              progressDialog.setCancelable(true);
              progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                  unsubscribe();
                }
              });
            }
          }

          @Override
          public void onCompleted() {
            MainThreadPostUtils.post(new Runnable() {
              @Override
              public void run() {
                DialogUtils.safeDismissDialog(progressDialog);
              }
            });
            subscriber.onCompleted();
          }

          @Override
          public void onError(Throwable e) {
            MainThreadPostUtils.post(new Runnable() {
              @Override
              public void run() {
                DialogUtils.safeDismissDialog(progressDialog);
              }
            });
            subscriber.onError(e);
          }

          @Override
          public void onNext(T t) {
            subscriber.onNext(t);
          }
        };
      }
    });
  }

  private static void showError(final Throwable throwable) {
    final String msgError = getDisplayMessage(throwable, false);
    if (msgError.length() >= MAX_STACK_TRACE_LENGTH) {
      uploadLeancloud(msgError);
    } else {
      MainThreadPostUtils.toastLong(msgError);
    }
  }

  private static String getDisplayMessage(Throwable throwable, boolean isSub) {
    return throwable instanceof RuntimeException
        ? getThrowableStackTrace(throwable, isSub)
        : throwable.getMessage();
  }

  private static String getThrowableStackTrace(Throwable throwable, boolean isSub) {
    String stackTrace = throwable.getMessage() + ":" + Log.getStackTraceString(throwable);
    if (isSub && stackTrace.length() > MAX_STACK_TRACE_LENGTH) {
      stackTrace = stackTrace.substring(0, MAX_STACK_TRACE_LENGTH);
    }
    return stackTrace;
  }
}
