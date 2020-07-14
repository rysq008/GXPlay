package com.zhny.zhny_app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhny.zhny_app.R;
import com.zhny.zhny_app.activitys.DetailFragmentsActivity;
import com.zhny.zhny_app.dialog.CommonDialogFragment;
import com.zhny.zhny_app.dialog.DialogFragmentHelper;
import com.zhny.zhny_app.fragments.BaseFragment.XBaseFragment;
import com.zhny.zhny_app.model.BaseModel.HttpResultModel;
import com.zhny.zhny_app.model.LoginBean;
import com.zhny.zhny_app.present.FLoginPresenter;
import com.zhny.zhny_app.utils.Utils;
import com.zhny.zhny_app.views.ToastMgr;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;

public class LoginFragment extends XBaseFragment<FLoginPresenter> {

    @BindView(R.id.login_username_et)
    EditText user_et;
    @BindView(R.id.login_user_clear_iv)
    ImageView user_clear_iv;
    @BindView(R.id.login_password_et)
    EditText pwd_et;
    @BindView(R.id.login_password_eye_cb)
    CheckBox eye_cb;
    @BindView(R.id.login_get_code_tv)
    TextView get_code_tv;
    @BindView(R.id.login_forget_pwd_tv)
    TextView forget_pwd_tv;
    @BindView(R.id.login_change_pwd_or_code_ctv)
    CheckedTextView pwd_or_code_ctv;
    @BindView(R.id.login_action_btn)
    Button login_action_btn;
    @BindView(R.id.register_action_tv)
    TextView register_action_tv;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_layout;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        user_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_clear_iv.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        eye_cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                pwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            else
                pwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });
    }


    @Override
    public FLoginPresenter newP() {
        return new FLoginPresenter();
    }

    @OnClick({R.id.rl_common_left, R.id.login_user_clear_iv, R.id.login_password_eye_cb, R.id.login_get_code_tv,
            R.id.login_forget_pwd_tv, R.id.login_change_pwd_or_code_ctv, R.id.login_action_btn,
            R.id.register_action_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_common_left:
                context.finish();
                break;
            case R.id.login_user_clear_iv:
                user_et.setText("");
                user_clear_iv.setVisibility(View.GONE);
                break;
            case R.id.login_password_eye_cb:
//                eye_cb.toggle();
                break;
            case R.id.login_get_code_tv:
                getP().requestGetCode(context, user_et.getText().toString(), msg -> {
                    CountDownTimer cdt = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            HttpResultModel resultModel = (HttpResultModel) msg.obj;
                            pwd_et.setText(resultModel.msg);
                            get_code_tv.setText("重新获取" + (millisUntilFinished / 1000 + 1));
                        }

                        @Override
                        public void onFinish() {
                            get_code_tv.setEnabled(true);
                            get_code_tv.setText("重新获取验证码");
                        }
                    };
                    cdt.start();
                    get_code_tv.setEnabled(false);
                    return false;
                });
                break;
            case R.id.login_forget_pwd_tv:
                DialogFragmentHelper.showBuilderDialog(getFragmentManager(),
                        DialogFragmentHelper.builder(R.layout.dialog_common_notitle_layout, true)
                                .setDialogWindow(dialogWindow -> {
                                    WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
                                    wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    dialogWindow.setAttributes(wlp);
                                    dialogWindow.setGravity(Gravity.BOTTOM);
                                    dialogWindow.setWindowAnimations(R.style.BottomInAndOutStyle);
                                    return null;
                                })
                                .setOnProcessView((dialog, view1) -> {
                                    final Button[] btn = {view1.findViewById(R.id.dialog_ok_btn)};
                                    btn[0].setOnClickListener(v -> {
                                        dialog.cancel();
                                        DialogFragmentHelper.showBuilderDialog(getFragmentManager(),
                                                DialogFragmentHelper.builder(R.layout.dialog_common_reset_pwd_layout, true)
                                                        .setOnProcessView((dialog1, view2) -> {
                                                            TextView tv_title = view2.findViewById(R.id.dialog_title_tv);
                                                            EditText et_input = view2.findViewById(R.id.dialog_input_et);
                                                            btn[0] = view2.findViewById(R.id.dialog_ok_btn);
                                                            tv_title.setText("输入手机号");
                                                            btn[0].setOnClickListener(v1 -> {
                                                                if (!Utils.isMobileNO(et_input.getText().toString())) {
                                                                    ToastMgr.showLongToast("请输入正确的手机号码");
                                                                    return;
                                                                }
                                                                dialog1.cancel();
                                                                getP().requestGetCode(context, et_input.getText().toString(), msg -> {
                                                                    DialogFragmentHelper.showBuilderDialog(getFragmentManager(),
                                                                            DialogFragmentHelper.builder(R.layout.dialog_common_reset_pwd_layout, false)
                                                                                    .setOnProcessView((dialog2, view3) -> {
                                                                                        TextView tv_title1 = view3.findViewById(R.id.dialog_title_tv);
                                                                                        TextView et_input1 = view3.findViewById(R.id.dialog_input_et);
                                                                                        TextView tv_resend = view3.findViewById(R.id.dialog_resend_tv);
                                                                                        TextView tv_tips = view3.findViewById(R.id.dialog_tips_tv);
                                                                                        Button btn1 = view3.findViewById(R.id.dialog_ok_btn);
                                                                                        tv_title1.setText(String.format("已将验证码发到尾号%s手机", et_input.getText().toString().substring(8)));
                                                                                        tv_resend.setVisibility(View.VISIBLE);
                                                                                        tv_tips.setVisibility(View.VISIBLE);
                                                                                        tv_resend.setEnabled(false);
                                                                                        CountDownTimer scdt = new CountDownTimer(30000, 1000) {
                                                                                            @Override
                                                                                            public void onTick(long millisUntilFinished) {
                                                                                                tv_resend.setText(String.format("%dS重发", millisUntilFinished / 1000));
                                                                                            }

                                                                                            @Override
                                                                                            public void onFinish() {
                                                                                                tv_resend.setEnabled(true);
                                                                                                tv_resend.setText("重发");
                                                                                                tv_resend.setOnClickListener(v -> {
                                                                                                    btn[0].performClick();
                                                                                                    dialog2.cancel();
                                                                                                });
                                                                                            }
                                                                                        };
                                                                                        scdt.start();
                                                                                        tv_tips.setOnClickListener(tv -> {
                                                                                            ToastMgr.showShortToast("wait develop !");
                                                                                        });
                                                                                        btn1.setOnClickListener(v2 -> {
                                                                                            if (Kits.Empty.check(et_input1.getText().toString())) {
                                                                                                ToastMgr.showShortToast("请输入验证码");
                                                                                                return;
                                                                                            }
                                                                                            dialog2.cancel();
                                                                                            getP().requestVerifCode(context, et_input1.getText().toString(), msg12 -> {
                                                                                                dialog1.cancel();
                                                                                                DialogFragmentHelper.showBuilderDialog(getFragmentManager(),
                                                                                                        DialogFragmentHelper.builder(R.layout.dialog_common_reset_pwd_layout, false)
                                                                                                                .setOnProcessView((dialog3, view4) -> {
                                                                                                                    TextView tv_title2 = view4.findViewById(R.id.dialog_title_tv);
                                                                                                                    EditText et_input2 = view4.findViewById(R.id.dialog_input_et);
                                                                                                                    CheckBox cb = view4.findViewById(R.id.dialog_eye_cb);
                                                                                                                    view4.findViewById(R.id.dialog_resend_tv).setVisibility(View.GONE);
                                                                                                                    view4.findViewById(R.id.dialog_tips_tv).setVisibility(View.GONE);
                                                                                                                    Button btn2 = view4.findViewById(R.id.dialog_ok_btn);
                                                                                                                    tv_title2.setText("设置新密码");
                                                                                                                    cb.setVisibility(View.VISIBLE);
                                                                                                                    cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                                                                                        if (isChecked)
                                                                                                                            et_input2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                                                                                                        else
                                                                                                                            et_input2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                                                                                                    });
                                                                                                                    cb.performClick();
                                                                                                                    btn2.setOnClickListener(v3 -> {
//                                                                                                                        if (Kits.Empty.check(et_input2.getText().toString())) {
//                                                                                                                            ToastMgr.showShortToast("请输入密码");
//                                                                                                                            return;
//                                                                                                                        }
                                                                                                                        getP().requestResetPwd(context, et_input.getText().toString(), et_input2.getText().toString(), msg1 -> {
                                                                                                                            dialog3.cancel();
                                                                                                                            if (null != msg1)
                                                                                                                                DialogFragmentHelper.showBuilderDialog(getFragmentManager(), DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
                                                                                                                                    @Override
                                                                                                                                    public Dialog getDialog(Context context) {
                                                                                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("提示")
                                                                                                                                                .setMessage("重置成功")
                                                                                                                                                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                                                                        dialog.dismiss();
                                                                                                                                                    }
                                                                                                                                                });
                                                                                                                                        return builder.show();
                                                                                                                                    }
                                                                                                                                }, true), "");
                                                                                                                            return false;
                                                                                                                        });
                                                                                                                    });

                                                                                                                }), "");
                                                                                                return false;
                                                                                            });

                                                                                        });
                                                                                    }), "");
                                                                    return false;
                                                                });
                                                            });
                                                        }), "");
                                    });
                                }), "");
                break;
            case R.id.login_change_pwd_or_code_ctv:
                if (!get_code_tv.isEnabled()) return;
                pwd_or_code_ctv.toggle();
                forget_pwd_tv.setVisibility(pwd_or_code_ctv.isChecked() ? View.VISIBLE : View.GONE);
                eye_cb.setVisibility(pwd_or_code_ctv.isChecked() ? View.VISIBLE : View.GONE);
                get_code_tv.setVisibility(pwd_or_code_ctv.isChecked() ? View.GONE : View.VISIBLE);
                pwd_or_code_ctv.setText(pwd_or_code_ctv.isChecked() ? "验证码登录" : "密码登录");
                break;
            case R.id.login_action_btn:
                getP().requestLogin(context, "18076569075"/*user_et.getText().toString()*/, "test1234"/*pwd_et.getText().toString()*/,/*pwd_or_code_ctv.isChecked()*/true);
                break;
            case R.id.register_action_tv:
                DetailFragmentsActivity.launch(context, null, RegisterFragment.newInstance());
                break;
            default:
                break;
        }
    }

    public void getUserData(LoginBean results) {
        DetailFragmentsActivity.launch(context, null, HomeFragment.newInstance());
        context.finish();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

}
