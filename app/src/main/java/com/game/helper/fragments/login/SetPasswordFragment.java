package com.game.helper.fragments.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.VersionInfoFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.SetPasswordRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.StringUtils;
import com.game.helper.views.EditInputView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetPasswordFragment extends XBaseFragment implements View.OnClickListener,EditInputView.OnEditInputListener{
    public static final String TAG = SetPasswordFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.tv_reset_passwd)
    View mResetPasswd;
    @BindView(R.id.et_password)
    EditInputView mPassWord;
    @BindView(R.id.et_password1)
    EditInputView mPassWord1;

    public static SetPasswordFragment newInstance(){
        return new SetPasswordFragment();
    }

    public SetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_set_password;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_set_password));
        mHeadBack.setOnClickListener(this);
        mPassWord.addOnEditInputListener(this);
        mPassWord1.addOnEditInputListener(this);
        mResetPasswd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mResetPasswd){
            if (!mResetPasswd.isSelected()) return;
            resetPassWord();
        }
    }

    private void resetPassWord(){
        String passWord = mPassWord.getText().toString().trim();
        String passWord1 = mPassWord1.getText().toString().trim();

        String errorMsg = null;
        if (StringUtils.isEmpty(passWord)) errorMsg = getResources().getString(R.string.login_hint_without_passwd);
        else if (StringUtils.isEmpty(passWord1)) errorMsg = getResources().getString(R.string.login_hint_without_confirm_passwd);
        else if (passWord != null && passWord1 != null && !passWord.equals(passWord1)) errorMsg = getResources().getString(R.string.login_hint_wrong_notequal_passwd);

        if (errorMsg != null) {
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.setPasswrd(new SetPasswordRequestBody(passWord));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
            @Override
            public void accept(HttpResultModel<NotConcernResults> resetPasswdResultsHttpResultModel ) throws Exception {
                if (resetPasswdResultsHttpResultModel.isSucceful()) {
                    SharedPreUtil.updateUserPasswdStatus(getContext(),true);
                    getActivity().onBackPressed();
                }
                Toast.makeText(getContext(), resetPasswdResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Link Net Error! Error Msg: "+netError.getMessage().trim());
            }
        });
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
