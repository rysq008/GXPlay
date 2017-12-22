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
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.ResetAlipayResults;
import com.game.helper.model.ResetTradeResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.ResetAlipayRequestBody;
import com.game.helper.net.model.ResetTradeRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.utils.RxLoadingUtils;
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
public class UpdateAlipayFragment extends XBaseFragment implements View.OnClickListener,EditInputView.OnEditInputListener{
    public static final String TAG = UpdateAlipayFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @BindView(R.id.tv_debug)
    TextView debugHint;
    @BindView(R.id.tv_account)
    TextView mAccount;
    @BindView(R.id.type_trde_old_account)
    EditInputView mOldAlipay;
    @BindView(R.id.type_trde_new_account)
    EditInputView mAlipayAccount;
    @BindView(R.id.type_trde_new_account1)
    EditInputView mAlipayAccount1;
    @BindView(R.id.et_verity)
    EditInputView mVerrity;
    @BindView(R.id.tv_left_time)
    CountDownText mCountDownText;
    @BindView(R.id.et_identy)
    EditInputView mIdenty;
    @BindView(R.id.et_real_name)
    EditInputView mName;
    @BindView(R.id.tv_reset_passwd)
    View mResetPasswd;
    @BindView(R.id.ll_old_alipay)
    View mItemOldAlipay;

    private boolean is_new = true;

    public static UpdateAlipayFragment newInstance(){
        return new UpdateAlipayFragment();
    }

    public UpdateAlipayFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_alipay;
    }

    private void initView(){
        Bundle arguments = getArguments();
        is_new = (arguments == null) ? true : !(arguments.getBoolean(UpdateAlipayFragment.TAG));
        mHeadTittle.setText(is_new ? getResources().getString(R.string.common_set_alipay) : getResources().getString(R.string.common_update_alipay));
        mHeadBack.setOnClickListener(this);

        mResetPasswd.setSelected(false);
        mAccount.setText(Utils.converterSecretPhone(Utils.getLoginInfo(getContext()).phone));
        mOldAlipay.addOnEditInputListener(this);
        mAlipayAccount.addOnEditInputListener(this);
        mAlipayAccount1.addOnEditInputListener(this);
        mVerrity.addOnEditInputListener(this);
        mCountDownText.setOnClickListener(this);
        mResetPasswd.setOnClickListener(this);
        if (is_new){
            mItemOldAlipay.setVisibility(View.GONE);
        }
        if (BuildConfig.DEBUG){
            debugHint.setVisibility(View.VISIBLE);
            debugHint.setText("测试环境默认验证码：9870");
        }
    }

    private void resetAlipayAccount(){
        String account = Utils.getLoginInfo(getContext()).phone;
        String oldAlipay = mOldAlipay.getText().toString().trim();
        String passWord = mAlipayAccount.getText().toString().trim();
        String passWord1 = mAlipayAccount1.getText().toString().trim();
        String code = mVerrity.getText().toString().trim();
        String identy = mIdenty.getText().toString().trim();
        String name = mName.getText().toString().trim();
        if (is_new) oldAlipay = "";

        String errorMsg = null;
        if (StringUtils.isEmpty(account)) errorMsg = getResources().getString(R.string.login_hint_without_account);
        else if (StringUtils.isEmpty(oldAlipay) && !is_new) errorMsg = getResources().getString(R.string.login_hint_without_alipay);
        else if (StringUtils.isEmpty(passWord)) errorMsg = getResources().getString(R.string.login_hint_without_alipay);
        else if (StringUtils.isEmpty(passWord1)) errorMsg = getResources().getString(R.string.login_hint_without_confirm_alipay);
        else if (passWord != null && passWord1 != null && !passWord.equals(passWord1)) errorMsg = getResources().getString(R.string.login_hint_wrong_notequal_alipay);
        else if (StringUtils.isEmpty(code)) errorMsg = getResources().getString(R.string.login_hint_without_code);
        else if (StringUtils.isEmpty(identy)) errorMsg = getResources().getString(R.string.login_hint_without_identy);
        else if (StringUtils.isEmpty(name)) errorMsg = getResources().getString(R.string.login_hint_without_name);

        if (errorMsg != null) {
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<ResetAlipayResults>> fr = DataService.resetAlipayAccount(new ResetAlipayRequestBody(passWord,name,identy,code,oldAlipay));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<ResetAlipayResults>>() {
            @Override
            public void accept(HttpResultModel<ResetAlipayResults> resetAlipayResultsHttpResultModel) throws Exception {
                String hint = "修改支付宝账号失败！请重试";
                if (resetAlipayResultsHttpResultModel.isSucceful()) {
                    hint = "修改支付宝账号成功！";
                    Utils.updateUserAlipayStatus(getContext(),true);
                }
                final GXPlayDialog dialog = new GXPlayDialog(GXPlayDialog.Ddialog_Without_tittle_Single_Confirm,"",hint);
                dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onConfirm() {
                        dialog.dismiss();
                        getActivity().onBackPressed();
                    }
                });
                dialog.show(getChildFragmentManager(),GXPlayDialog.TAG);
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
        String account = Utils.getLoginInfo(getContext()).phone;

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
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mCountDownText){
            String account = Utils.getLoginInfo(getContext()).phone;
            if (StringUtils.isEmpty(account)) return;
            mCountDownText.setCountDownTimer(60 * 1000,1000);
            mCountDownText.startTimer();
            getVerify();
        }
        if (v == mResetPasswd){
            if (!mResetPasswd.isSelected()) return;
            resetAlipayAccount();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onTextChange(EditText content) {
        mResetPasswd.setSelected(content.getText()!= null && content.getText().toString().length()>0 ? true : false);
    }
}
