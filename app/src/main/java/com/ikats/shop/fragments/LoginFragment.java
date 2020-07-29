package com.ikats.shop.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikats.shop.R;
import com.ikats.shop.activitys.DetailFragmentsActivity;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.LoginBean;
import com.ikats.shop.present.FLoginPresenter;
import com.ikats.shop.utils.CodeUtils;
import com.ikats.shop.utils.Utils;
import com.ikats.shop.views.ToastMgr;

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
    @BindView(R.id.login_show_code_tv)
    TextView show_code_tv;
    @BindView(R.id.login_forget_pwd_tv)
    TextView forget_pwd_tv;
    @BindView(R.id.login_enter_code_et)
    EditText enter_code_et;
    @BindView(R.id.login_change_pwd_or_code_ctv)
    CheckBox pwd_or_code_ctv;
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
        pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eye_cb.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
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
            pwd_et.setSelection(pwd_et.getText().length());
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
                    HttpResultModel resultModel = (HttpResultModel) msg.obj;
                    Bitmap bitmap = (CodeUtils.getInstance().createBitmap(resultModel.resultContent));
                    show_code_tv.setBackground(new BitmapDrawable(bitmap));
                    CountDownTimer cdt = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            pwd_et.setText(resultModel.resultContent);
                            get_code_tv.setText((millisUntilFinished / 1000 + 1) + "S");
                        }

                        @Override
                        public void onFinish() {
                            get_code_tv.setEnabled(true);
                            get_code_tv.setText("");
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
                                                                                                                                    public AlertDialog getDialog(Context context) {
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
            case R.id.login_change_pwd_or_code_ctv://记住我
//                if (!get_code_tv.isEnabled()) return;
//                pwd_or_code_ctv.toggle();
//                forget_pwd_tv.setVisibility(pwd_or_code_ctv.isChecked() ? View.VISIBLE : View.GONE);
//                eye_cb.setVisibility(pwd_or_code_ctv.isChecked() ? View.VISIBLE : View.GONE);
//                get_code_tv.setVisibility(pwd_or_code_ctv.isChecked() ? View.GONE : View.VISIBLE);
//                pwd_or_code_ctv.setText(pwd_or_code_ctv.isChecked() ? "验证码登录" : "密码登录");
                break;
            case R.id.login_action_btn:
//                enter_code_et.getText()
                getP().requestLogin(context, "admin"/*user_et.getText().toString()*/, "admin"/*pwd_et.getText().toString()*/,/*pwd_or_code_ctv.isChecked()*/true);
                break;
            case R.id.register_action_tv:
                DetailFragmentsActivity.launch(context, null, RegisterFragment.newInstance());
                break;
            default:
                break;
        }
    }

    public void showContent(LoginBean results) {
        DetailFragmentsActivity.launch(context, null, HomeFragment.newInstance());
        context.finish();
    }

    @Override
    public boolean onBackPress(Activity activity) {
        Log.e("aaa", "onKeyBackPressed: LoginFragment.java");
        DialogFragmentHelper.builder(context -> new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("提示").setMessage("确定退出应用程序吗?")
                .setNegativeButton("取消", null).setPositiveButton("确定", (dialog, which) -> {
                    getActivity().finish();
                    System.exit(1);
                }).create(), true).show(getChildFragmentManager(), "");
        return true;
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

}
