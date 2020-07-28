package com.know_action.foresight.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.know_action.foresight.R;
import com.know_action.foresight.fragments.BaseFragment.XBaseFragment;
import com.know_action.foresight.model.BaseModel.HttpResultModel;
import com.know_action.foresight.model.RegisterBean;
import com.know_action.foresight.present.FRegisterPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends XBaseFragment<FRegisterPresenter> {

    @BindView(R.id.tv_common_middle_title)
    TextView title_tv;
    @BindView(R.id.tv_common_right)
    TextView title_right_tv;
    @BindView(R.id.register_user_et)
    EditText user_et;
    @BindView(R.id.register_user_clear_iv)
    ImageView user_clear_iv;
    @BindView(R.id.register_code_et)
    EditText code_et;
    @BindView(R.id.register_get_code_tv)
    TextView get_code_tv;
    @BindView(R.id.register_pwd_et)
    EditText pwd_et;
    @BindView(R.id.register_pwd_eye_cb)
    CheckBox pwd_eye_cb;
    @BindView(R.id.register_repwd_et)
    EditText repwd_et;
    @BindView(R.id.register_commit_btn)
    Button commit_btn;
    @BindView(R.id.register_tips_tv)
    TextView tips_tv;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register_layout;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        title_tv.setText("注册");
        title_right_tv.setText("登陆");
        SpannableStringBuilder ssb = new SpannableStringBuilder("注册代表您同意《服务条款》和《隐私协议》");
        ssb.setSpan(new URLSpan("http://www.baidu.com"), 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new URLSpan("http://www.qq.com"), 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#F9993F")), 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#F9993F")), 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 7, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 14, 20, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        tips_tv.setText(ssb);
        tips_tv.setMovementMethod(LinkMovementMethod.getInstance());

        user_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_clear_iv.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
                commit_btn.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pwd_eye_cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                pwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            else
                pwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pwd_et.setSelection(pwd_et.getText().length());
        });
    }


    @Override
    public FRegisterPresenter newP() {
        return new FRegisterPresenter();
    }

    final CountDownTimer[] cdt = new CountDownTimer[1];

    @OnClick({R.id.rl_common_left, R.id.rl_common_right, R.id.register_user_clear_iv, R.id.register_pwd_eye_cb,
            R.id.register_get_code_tv, R.id.register_commit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_common_left:
                context.finish();
                break;
            case R.id.rl_common_right:
                context.finish();
                break;
            case R.id.register_user_clear_iv:
                user_et.setText("");
                user_clear_iv.setVisibility(View.GONE);
                break;
            case R.id.register_get_code_tv:
                getP().requestGetCode(context, user_et.getText().toString(), msg -> {
                    cdt[0] = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            HttpResultModel resultModel = (HttpResultModel) msg.obj;
                            code_et.setText(resultModel.msg);
                            get_code_tv.setText(String.format("重新获取(%d)", (millisUntilFinished / 1000)));
                        }

                        @Override
                        public void onFinish() {
                            get_code_tv.setEnabled(true);
                            get_code_tv.setText("重新获取");
                        }
                    };
                    cdt[0].start();
                    get_code_tv.setEnabled(false);
                    return false;
                });
                break;
            case R.id.register_commit_btn:
                if (null != cdt[0]) {
                    cdt[0].cancel();
                    cdt[0].onFinish();
                }
                getP().requestRegister(context, user_et.getText().toString(), code_et.getText().toString(), pwd_et.getText().toString(), repwd_et.getText().toString());
                break;
            default:
                break;
        }
    }

    public void getUserData(RegisterBean results) {
//        DetailFragmentsActivity.launch(context,null, LoginFragment.newInstance());
        context.finish();
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

}
