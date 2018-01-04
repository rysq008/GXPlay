/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.game.helper.activitys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.EasemobAccountResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SPUtils;
import com.game.helper.utils.SharedPreUtil;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.helpdesk.easeui.ui.BaseActivity;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;


public class HuanxinKefuLoginActivity extends BaseActivity implements LifecycleProvider<ActivityEvent> {

    public String huanxinName = "tian";
    public String huanxinPassword = "huanxin";
    private static final String TAG = HuanxinKefuLoginActivity.class.getSimpleName();

    private boolean progressShow;
    private ProgressDialog progressDialog;

    @Override
    @CallSuper
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        Intent intent = getIntent();

        if (SharedPreUtil.isLogin()) {
            progressShow = true;
            progressDialog = getProgressDialog();
            progressDialog.setMessage("正在加载数据.");
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            // 可以检测是否已经登录过环信，如果登录过则环信SDK会自动登录，不需要再次调用登录操作
            if (ChatClient.getInstance().isLoggedInBefore()) {
                toChatActivity();
            } else {
                //去服务器获取环信的登录账号，密码,然后不用再登录
                easemobIM();
            }
        } else {
            String tempName = SPUtils.getString(this, SPUtils.TEMP_HUANXIN_NAME, "");
            // Log.d(TAG, "获取SP文件里环信的临时账号：" + tempName);
            if (tempName.length() <= 0) {
                //生成一个随机的环信账号，和密码
                String account = getRandomAccount();
                //Log.d(TAG, "创建SP文件里环信的临时账号：" + account);
                SPUtils.putString(this, SPUtils.TEMP_HUANXIN_NAME, account);
                //创建一个用户并登录环信服务器
                createAccountThenLoginChatServer(account);
            } else {
                login(tempName, huanxinPassword);
            }


        }


    }

    private void easemobIM() {
        Flowable<HttpResultModel<EasemobAccountResults>> fe = DataService.getEasemobIM();
        RxLoadingUtils.subscribe(fe,bindToLifecycle(), new Consumer<HttpResultModel<EasemobAccountResults>>() {
            @Override
            public void accept(HttpResultModel<EasemobAccountResults> easemobAccountResults) throws Exception {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                huanxinName = easemobAccountResults.data.getUser();
                huanxinPassword = easemobAccountResults.data.getPasswd();
                login(huanxinName, huanxinPassword);
            }
        });

    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        UIProvider.getInstance().pushActivity(this);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
        UIProvider.getInstance().popActivity(this);
    }


    private void createAccountThenLoginChatServer(String userName) {
        // 自动生成账号,此处生成一个账号,为了演示.正式应从自己服务器获取账号
        final String account = userName;
        final String userPwd = huanxinPassword;
        progressDialog = getProgressDialog();
        progressDialog.setMessage("System is automatically registered users for you...");
        progressDialog.show();
        // createAccount to huanxin server
        // if you have a account, this step will ignore
        ChatClient.getInstance().register(account, userPwd, new Callback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器
                        login(account, userPwd);
                    }
                });
            }

            @Override
            public void onError(final int errorCode, String error) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (errorCode == Error.NETWORK_ERROR)

                        {
                        } else if (errorCode == Error.USER_ALREADY_EXIST)

                        {
                            login(account, userPwd);
                        } else if (errorCode == Error.USER_AUTHENTICATION_FAILED)

                        {
                            //Toast.makeText(getApplicationContext(), R.string.no_register_authority, Toast.LENGTH_SHORT).show();
                        } else if (errorCode == Error.USER_ILLEGAL_ARGUMENT)

                        {
                            Toast.makeText(getApplicationContext(), "用户名非法", Toast.LENGTH_SHORT).show();
                        } else

                        {
                            //Toast.makeText(getApplicationContext(), R.string.register_user_fail, Toast.LENGTH_SHORT).show();
                        }

                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(HuanxinKefuLoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;
                }
            });
        }
        return progressDialog;
    }

    private void login(final String uname, final String upwd) {
        progressShow = true;
        progressDialog = getProgressDialog();
        progressDialog.setMessage("Being contact customer service, please wait...");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        // login huanxin server
        ChatClient.getInstance().login(uname, upwd, new Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "demo login success!");
                if (!progressShow) {
                    return;
                }
                toChatActivity();
            }

            @Override
            public void onError(int code, final String error) {
                Log.e(TAG, "login fail,code:" + code + ",error:" + error);
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(HuanxinKefuLoginActivity.this,
                                error,
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    private void toChatActivity() {
        if (!HuanxinKefuLoginActivity.this.isFinishing())
            progressDialog.dismiss();

        //此处演示设置技能组,如果后台设置的技能组名称为[shouqian|shouhou],这样指定即分配到技能组中.
        //为null则不按照技能组分配,同理可以设置直接指定客服scheduleAgent
        String queueName = null;
               /* switch (messageToIndex) {
                    case Constant.MESSAGE_TO_AFTER_SALES:
                        queueName = "shouhou";
                        break;
                    case Constant.MESSAGE_TO_PRE_SALES:
                        queueName = "shouqian";
                        break;
                    default:
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, selectedIndex);
                //设置点击通知栏跳转事件
                Conversation conversation = ChatClient.getInstance().chatManager().getConversation(Preferences.getInstance().getCustomerAccount());
                String titleName = null;
                if (conversation.officialAccount() != null) {
                    titleName = conversation.officialAccount().getName();
                }
                // 进入主页面
                Intent intent = new IntentBuilder(LoginActivity.this)
                        .setTargetClass(ChatActivity.class)
                        .setVisitorInfo(DemoMessageHelper.createVisitorInfo())
                        .setServiceIMNumber(Preferences.getInstance().getCustomerAccount())
                        .setScheduleQueue(DemoMessageHelper.createQueueIdentity(queueName))
                        .setTitleName(titleName)
//						.setScheduleAgent(DemoMessageHelper.createAgentIdentity("ceshiok1@qq.com"))
                        .setShowUserNick(true)
                        .setBundle(bundle)
                        .build();
                startActivity(intent);*/
        Intent intent = new IntentBuilder(HuanxinKefuLoginActivity.this)
                .setServiceIMNumber("imkefu")
                .setTitleName("G9游戏客服")
                .build();
        startActivity(intent);
        finish();
    }


    /**
     * 此处随机生成账号
     *
     * @return
     */
    private String getRandomAccount() {
        String val = "s";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {// 字符串
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) {// 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        val += sDateFormat.format(new Date());
        return val.toLowerCase(Locale.getDefault());
    }

    private void logoutTempAccount() {
        ChatClient.getInstance().logout(true, new Callback() {
            @Override
            public void onSuccess() {
                SPUtils.remove(HuanxinKefuLoginActivity.this, SPUtils.TEMP_HUANXIN_NAME);
                Log.d(TAG, "已清除SPUtils.TEMP_HUANXIN_NAME:" + SPUtils.getString(HuanxinKefuLoginActivity.this, SPUtils.TEMP_HUANXIN_NAME, ""));
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }


    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }


    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }
}
