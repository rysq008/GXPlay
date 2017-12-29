package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.NotConcernResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.UpdateNicknameRequestBody;
import com.game.helper.net.model.UpdatePhoneRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.utils.RxLoadingUtils;
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
public class UpdatePhoneFragment extends XBaseFragment implements View.OnClickListener,EditInputView.OnEditInputListener{
    public static final String TAG = UpdatePhoneFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.et_phone)
    EditInputView mPhone;
    @BindView(R.id.et_code)
    EditInputView mCode;
    @BindView(R.id.tv_left_time)
    CountDownText mleftTime;
    @BindView(R.id.tv_submit)
    View mSubmit;

    public static UpdatePhoneFragment newInstance(){
        return new UpdatePhoneFragment();
    }

    public UpdatePhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_phone;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_update_phone));
        mPhone.addOnEditInputListener(this);
        mCode.addOnEditInputListener(this);
        mHeadBack.setOnClickListener(this);
        mleftTime.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        //getData();
    }

    private void getData() {
        if (getArguments() == null) return;
        String phone = getArguments().getString(TAG);
        if (phone != null && phone.length() != 0) mPhone.setText(Utils.converterSecretPhone(phone));
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mleftTime){
            getVerify();
        }
        if (v == mSubmit){
            updatePhone();
        }
    }

    private void getVerify(){
        String account = mPhone.getText().toString().trim();

        if (StringUtils.isEmpty(account)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_account), Toast.LENGTH_SHORT).show();
            return;
        }

        mleftTime.setCountDownTimer(60 * 1000,1000);
        mleftTime.startTimer();

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

    private void updatePhone(){
        String phone = mPhone.getText().toString().trim();
        String code = mCode.getText().toString().trim();

        if (StringUtils.isEmpty(phone)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_account), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(code)){
            Toast.makeText(getContext(), getResources().getString(R.string.login_hint_without_code), Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.updatePhone(new UpdatePhoneRequestBody(phone,code));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
            @Override
            public void accept(HttpResultModel<NotConcernResults> notConcernResultsHttpResultModel) throws Exception {
                if (notConcernResultsHttpResultModel.isSucceful()) {
                    getActivity().onBackPressed();
                } else {
                }
                Toast.makeText(getContext(), notConcernResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onTextChange(EditText content) {
        mSubmit.setSelected(content.getText()!= null && content.getText().toString().length()>0 ? true : false);
    }
}
