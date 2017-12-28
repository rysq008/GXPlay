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
import com.game.helper.fragments.VersionInfoFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.ResetPasswdResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.ResetPasswdRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.EditInputView;
import com.game.helper.views.widget.CountDownText;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswdFragment extends XBaseFragment implements View.OnClickListener, EditInputView.OnEditInputListener{
    public static final String TAG = ResetPasswdFragment.class.getSimpleName();

    //ui
    @BindView(R.id.action_bar_tittle)
    TextView mTittle;
    @BindView(R.id.action_bar_back)
    View mBack;
    @BindView(R.id.action_bar_back_iv)
    ImageView mBackIv;
    @BindView(R.id.tv_debug)
    TextView debugHint;
    @BindView(R.id.et_account)
    EditInputView mAccount;
    @BindView(R.id.et_password)
    EditInputView mPassWord;
    @BindView(R.id.et_password1)
    EditInputView mPassWord1;
    @BindView(R.id.et_verity)
    EditInputView mVerrity;
    @BindView(R.id.tv_left_time)
    CountDownText mCountDownText;
    @BindView(R.id.tv_reset_passwd)
    View mResetPasswd;
    @BindView(R.id.tv_goto_login)
    View mGotoLogin;

    public static ResetPasswdFragment newInstance(){
        return new ResetPasswdFragment();
    }

    public ResetPasswdFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_reset_password;
    }

    private void initView(){
        mTittle.setText(getResources().getString(R.string.reset_tittle));
        mBack.setOnClickListener(this);
        mResetPasswd.setSelected(false);
        mAccount.setText(Utils.converterSecretPhone(SharedPreUtil.getLoginUserInfo().phone));
        mAccount.setEditAble(false);
        mGotoLogin.setVisibility(View.GONE);
        mPassWord.addOnEditInputListener(this);
        mPassWord1.addOnEditInputListener(this);
        mVerrity.addOnEditInputListener(this);
        mCountDownText.setOnClickListener(this);
        mResetPasswd.setOnClickListener(this);
        mGotoLogin.setOnClickListener(this);
        if (BuildConfig.DEBUG){
            debugHint.setVisibility(View.VISIBLE);
            debugHint.setText("测试环境默认验证码：9870");
        }
    }

    private void resetPassWord(){
        String account = mAccount.getText().toString().trim();
        String passWord = mPassWord.getText().toString().trim();
        String passWord1 = mPassWord1.getText().toString().trim();
        String code = mVerrity.getText().toString().trim();

        String errorMsg = null;
        if (StringUtils.isEmpty(account)) errorMsg = getResources().getString(R.string.login_hint_without_account);
        else if (StringUtils.isEmpty(passWord)) errorMsg = getResources().getString(R.string.login_hint_without_passwd);
        else if (StringUtils.isEmpty(passWord1)) errorMsg = getResources().getString(R.string.login_hint_without_confirm_passwd);
        else if (passWord != null && passWord1 != null && !passWord.equals(passWord1)) errorMsg = getResources().getString(R.string.login_hint_wrong_notequal_passwd);
        else if (StringUtils.isEmpty(code)) errorMsg = getResources().getString(R.string.login_hint_without_code);

        if (errorMsg != null) {
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<ResetPasswdResults>> fr = DataService.resetPassWord(new ResetPasswdRequestBody(passWord, code));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<ResetPasswdResults>>() {
            @Override
            public void accept(HttpResultModel<ResetPasswdResults> resetPasswdResultsHttpResultModel ) throws Exception {
                if (resetPasswdResultsHttpResultModel.isSucceful()) {
                    Toast.makeText(getContext(), "修改密码成功！", Toast.LENGTH_SHORT).show();
                    SharedPreUtil.updateUserPasswdStatus(getContext(),true);
                    getActivity().onBackPressed();
                }else {
                    Toast.makeText(getContext(), resetPasswdResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (v == mBack){
            getActivity().onBackPressed();
        }
        if (v == mCountDownText){
            mCountDownText.setCountDownTimer(60 * 1000,1000);
            mCountDownText.startTimer();
            getVerify();
        }
        if (v == mGotoLogin){
            DetailFragmentsActivity.launch(getContext(),null,LoginFragment.newInstance());
        }
        if (v == mResetPasswd){
            if (!mResetPasswd.isSelected()) return;
            resetPassWord();
        }
    }

    @Override
    public void onTextChange(EditText content) {
        mResetPasswd.setSelected(content.getText()!= null && content.getText().toString().length()>0 ? true : false);
    }

    @Override
    public Object newP() {
        return null;
    }
}
