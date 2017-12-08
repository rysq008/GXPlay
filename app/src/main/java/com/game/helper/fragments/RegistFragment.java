package com.game.helper.fragments;

import android.os.Bundle;
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
import com.game.helper.model.RegistResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.DataService;
import com.game.helper.net.StateCode;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.RegistRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.widget.CountDownText;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = RegistFragment.class.getSimpleName();

    //ui
    @BindView(R.id.et_account)
    EditText mAccount;
    @BindView(R.id.et_password)
    EditText mPassWord;
    @BindView(R.id.et_invatation)
    EditText mInvatation;
    @BindView(R.id.tv_regist)
    View mRegist;
    @BindView(R.id.tv_debug)
    TextView debugHint;
    @BindView(R.id.tv_left_time)
    CountDownText mCountDownText;

    //args
    private onRegistListener mOnRegistListener;

    public static RegistFragment newInstance(){
        return new RegistFragment();
    }

    public RegistFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mRegist.setOnClickListener(this);
        if (BuildConfig.Debug){
            debugHint.setVisibility(View.VISIBLE);
            debugHint.setText("测试环境默认验证码：9870");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_regist;
    }


    private void regist(){
        String account = mAccount.getText().toString().trim();
        String code = mPassWord.getText().toString().trim();
        String marketNum = mInvatation.getText().toString().trim();

        if (StringUtils.isEmpty(account)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_account), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(code)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_code), Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<RegistResults>> fr = DataService.regist(new RegistRequestBody(account,code,marketNum));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<RegistResults>>() {
            @Override
            public void accept(HttpResultModel<RegistResults> registResultsHttpResultModel) throws Exception {
                if (registResultsHttpResultModel.isSucceful()) {
                    LoginUserInfo userInfo = new LoginUserInfo(
                            registResultsHttpResultModel.data.phone,registResultsHttpResultModel.data.member_id);
                    Utils.writeLoginInfo(getContext(),userInfo);
                    if (mOnRegistListener != null){
                        mOnRegistListener.onRegistSuccessful(userInfo);
                    }
                    getActivity().onBackPressed();
                }else {
                    Toast.makeText(getContext(), registResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
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

        Flowable<HttpResultModel<VerifyResults>> fr = DataService.getVerify(new VerifyRequestBody(account, RxConstant.VERIFY_USER_FOR_REGIST));
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
        if (v == mRegist){
            regist();
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

    public interface onRegistListener{
        void onRegistSuccessful(LoginUserInfo userInfo);
    }

    public void addOnRegistListener(onRegistListener onRegistListener){
        mOnRegistListener = onRegistListener;
    }
}
