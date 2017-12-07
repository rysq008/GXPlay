package com.game.helper.fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.game.helper.BuildConfig;
import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.DataService;
import com.game.helper.net.StateCode;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.widget.CountDownText;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = LoginFragment.class.getSimpleName();
    public static final int LOGIN_TYPE_MESSAGE = 1;
    public static final int LOGIN_TYPE_PASSWORD = 0;

    //ui
    @BindView(R.id.et_account)
    EditText mAccount;
    @BindView(R.id.et_password)
    EditText mPassWord;
    @BindView(R.id.ll_login_message)
    View mLoginMessage;
    @BindView(R.id.tv_login_password)
    View mLoginPassword;
    @BindView(R.id.tv_debug)
    TextView debugHint;
    @BindView(R.id.tv_left_time)
    CountDownText mCountDownText;
    
    //args
    private onLoginListener mOnLoginListener;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mLoginPassword.setOnClickListener(this);
        mLoginMessage.setOnClickListener(this);
        mCountDownText.setOnClickListener(this);
        if (BuildConfig.Debug){
            debugHint.setVisibility(View.VISIBLE);
            debugHint.setText("测试环境默认验证码：9870");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    private void login(int type){
        String account = mAccount.getText().toString().trim();
        String code = mPassWord.getText().toString().trim();

        if (StringUtils.isEmpty(account)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_account), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(code)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_code), Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<LoginResults>> fr = DataService.login(new LoginRequestBody(account,code,type+"",""));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<LoginResults>>() {
            @Override
            public void accept(HttpResultModel<LoginResults> loginResultsHttpResultModel) throws Exception {
                if (loginResultsHttpResultModel.isSucceful()) {
                    LoginUserInfo userInfo = new LoginUserInfo(
                            loginResultsHttpResultModel.data.phone,loginResultsHttpResultModel.data.member_id);
                    Utils.writeLoginInfo(getContext(),userInfo);
                    if (mOnLoginListener != null){
                        mOnLoginListener.onLoginSuccessful(userInfo);
                    }
                    getActivity().onBackPressed();
                }else {
                    Toast.makeText(getContext(), loginResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: "+netError.getMessage().trim());
            }
        });
    }

    private void getVerify(){
        String account = mAccount.getText().toString().trim();

        if (StringUtils.isEmpty(account)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_account), Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<VerifyResults>> fr = DataService.getVerify(new VerifyRequestBody(account, RxConstant.VERIFY_USER_FOR_LOGIN));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<VerifyResults>>() {
            @Override
            public void accept(HttpResultModel<VerifyResults> verifyResultsHttpResultModel ) throws Exception {
                if (verifyResultsHttpResultModel.isSucceful()) {
                }else {
                    Toast.makeText(getContext(), verifyResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: "+netError.getMessage().trim());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginMessage){
            login(LOGIN_TYPE_MESSAGE);
        }
        if (v == mLoginPassword){
            login(LOGIN_TYPE_PASSWORD);
        }
        if (v == mCountDownText){
            mCountDownText.setCountDownTimer(60 * 1000,1000);
            mCountDownText.startTimer();
//            getVerify();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownText.destroy();
    }

    public interface onLoginListener{
        void onLoginSuccessful(LoginUserInfo userInfo);
    }

    public void addOnRegistListener(onLoginListener onLoginListener){
        mOnLoginListener = onLoginListener;
    }
}
