package com.game.helper.fragments.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.BuildConfig;
import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.EditInputView;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.widget.CountDownText;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends XBaseFragment implements View.OnClickListener, EditInputView.OnEditInputListener {
    public static final String TAG = LoginFragment.class.getSimpleName();
    public static final int LOGIN_TYPE_MESSAGE = 1;
    public static final int LOGIN_TYPE_PASSWORD = 0;

    //ui
    @BindView(R.id.action_bar_tittle)
    TextView mTittle;
    @BindView(R.id.action_bar_back)
    View mBack;
    @BindView(R.id.action_bar_back_iv)
    ImageView mBackIv;
    @BindView(R.id.tv_tab_message)
    TextView mTabMessage;
    @BindView(R.id.tv_tab_passwd)
    TextView mTabPasswd;
    @BindView(R.id.et_account)
    EditInputView mAccount;
    @BindView(R.id.et_password)
    EditInputView mPassWord;
    @BindView(R.id.tv_forget_passwd)
    View mForgetPasswd;
    @BindView(R.id.tv_login_password)
    View mLoginPassword;
    @BindView(R.id.tv_goto_regist)
    View mGotoRegist;
    @BindView(R.id.tv_debug)
    TextView debugHint;
    @BindView(R.id.tv_left_time)
    CountDownText mCountDownText;
    @BindView(R.id.iv_code)
    ImageView mIvCode;

    //args
    private int Login_Type = 1;
    private onLoginListener mOnLoginListener;
    private GXPlayDialog loginDialog;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTittle.setText(getResources().getString(R.string.login_tittle));
        mAccount.addOnEditInputListener(this);
        mPassWord.addOnEditInputListener(this);
        mBack.setOnClickListener(this);
        mLoginPassword.setOnClickListener(this);
        mCountDownText.setOnClickListener(this);
        mGotoRegist.setOnClickListener(this);
        mForgetPasswd.setOnClickListener(this);
        mTabMessage.setOnClickListener(this);
        mTabPasswd.setOnClickListener(this);

        mCountDownText.setVisibility(View.GONE);
        switchLoginType(LOGIN_TYPE_PASSWORD);
        if (BuildConfig.DEBUG) {
            debugHint.setVisibility(View.VISIBLE);
            debugHint.setText("测试环境默认验证码：9870");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    private void login(int type) {
        String account = mAccount.getText().toString().trim();
        String code = mPassWord.getText().toString().trim();

        if (StringUtils.isEmpty(account)) {
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_account), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(code)) {
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_code), Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<LoginResults>> fr = DataService.login(new LoginRequestBody(account, code, type + "", ""));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<LoginResults>>() {
            @Override
            public void accept(HttpResultModel<LoginResults> loginResultsHttpResultModel) throws Exception {
                if (loginResultsHttpResultModel.isSucceful()) {
                    LoginUserInfo userInfo = new LoginUserInfo(loginResultsHttpResultModel.data);
                    SharedPreUtil.saveLoginUserInfo(userInfo);
                    if (mOnLoginListener != null) {
                        mOnLoginListener.onLoginSuccessful(userInfo);
                    }
                    getActivity().onBackPressed();

                    if (!loginResultsHttpResultModel.data.has_passwd) {
                        if (loginDialog == null) loginDialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Single_Confirm, "温馨提示", getResources().getString(R.string.common_set_password_hint));
                        loginDialog.show(getChildFragmentManager(),GXPlayDialog.TAG);
                        loginDialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
                            @Override
                            public void onCancel() {
                                DetailFragmentsActivity.launch(getContext(), null, SetPasswordFragment.newInstance());
                            }

                            @Override
                            public void onConfirm() {
                                DetailFragmentsActivity.launch(getContext(), null, SetPasswordFragment.newInstance());
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), loginResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void getVerify() {
        String account = mAccount.getText().toString().trim();

        if (StringUtils.isEmpty(account)) {
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_account), Toast.LENGTH_SHORT).show();
            return;
        }

        mCountDownText.setCountDownTimer(60 * 1000, 1000);
        mCountDownText.startTimer();

        Flowable<HttpResultModel<VerifyResults>> fr = DataService.getVerify(new VerifyRequestBody(account, RxConstant.VERIFY_USER_FOR_LOGIN));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<VerifyResults>>() {
            @Override
            public void accept(HttpResultModel<VerifyResults> verifyResultsHttpResultModel) throws Exception {
                if (verifyResultsHttpResultModel.isSucceful()) {
                } else {
                    Toast.makeText(getContext(), verifyResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            getActivity().onBackPressed();
        }
        if (v == mLoginPassword) {
            if (!mLoginPassword.isSelected()) return;
            login(Login_Type);
        }
        if (v == mCountDownText) {
            getVerify();
        }
        if (v == mGotoRegist) {
            DetailFragmentsActivity.launch(getContext(), null, RegistFragment.newInstance());
        }
        if (v == mForgetPasswd){
            DetailFragmentsActivity.launch(getContext(),null,ForgetPasswdFragment.newInstance());
        }
        if (v == mTabMessage) {
            switchLoginType(LOGIN_TYPE_MESSAGE);
        }
        if (v == mTabPasswd) {
            switchLoginType(LOGIN_TYPE_PASSWORD);
        }
    }

    private void switchLoginType(int type) {
        if (Login_Type == type) return;
        boolean message_login = false;
        if (Login_Type == LOGIN_TYPE_MESSAGE) message_login = true;
        mCountDownText.setVisibility(message_login ? View.GONE : View.VISIBLE);
        mPassWord.setInputType(message_login ? EditInputView.Type_Password : EditInputView.Type_Code);
        mPassWord.setHintText(getResources().getString(message_login ? R.string.login_hint_password : R.string.login_hint_code));
        mTabPasswd.setTextColor(getResources().getColor(message_login ? R.color.colorWhite : R.color.colorPrimary));
        mTabMessage.setTextColor(getResources().getColor(message_login ? R.color.colorPrimary : R.color.colorWhite));
        mTabPasswd.setSelected(message_login ? true : false);
        mTabMessage.setSelected(message_login ? false : true);
        mIvCode.setImageResource(message_login ? R.mipmap.login_ic_secret : R.mipmap.login_ic_code);

        Login_Type = type;
        mPassWord.setText("");
        mLoginPassword.setSelected(false);
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

    @Override
    public void onTextChange(EditText content) {
        mLoginPassword.setSelected(content.getText() != null && content.getText().toString().length() > 0 ? true : false);
    }

    public interface onLoginListener {
        void onLoginSuccessful(LoginUserInfo userInfo);
    }

    public void addOnRegistListener(onLoginListener onLoginListener) {
        mOnLoginListener = onLoginListener;
    }
}
