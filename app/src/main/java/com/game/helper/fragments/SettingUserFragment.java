package com.game.helper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.login.ResetPasswdFragment;
import com.game.helper.fragments.wallet.CashFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CheckTradePasswdResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.HeadImageView;
import com.game.helper.views.OptionsPickerView;
import com.game.helper.views.PasswordEditDialog;
import com.game.helper.views.widget.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * 用户个人信息设置
 */
public class SettingUserFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = SettingUserFragment.class.getSimpleName();
    @BindView(R.id.action_bar_back) View mHeadBack;
    @BindView(R.id.action_bar_tittle) TextView mHeadTittle;
    @BindView(R.id.ll_avatar) View mItemAvatar;
    @BindView(R.id.hv_avatar) HeadImageView mAvatar;
    @BindView(R.id.ll_nickname) View mItemNickname;
    @BindView(R.id.tv_nickname) TextView mNickname;
    @BindView(R.id.ll_phone) View mItemPhone;
    @BindView(R.id.tv_phone) TextView mPhone;
    @BindView(R.id.ll_sex) View mItemSex;
    @BindView(R.id.tv_sex) TextView mSex;
    @BindView(R.id.ll_birthday) View mItemBirthday;
    @BindView(R.id.tv_birthday) TextView mBirthday;
    @BindView(R.id.ll_sign) View mItemsign;
    @BindView(R.id.ll_safe_pw) View mItemPassword;
    @BindView(R.id.tv_password_status) TextView mPasswordStatus;
    @BindView(R.id.ll_safe_orderpw) View mItemOrderPassword;
    @BindView(R.id.tv_safe_orderpw_status) TextView mOrderPasswordStatus;
    @BindView(R.id.ll_safe_alipay) View mItemAlipay;
    @BindView(R.id.tv_safe_alipay_status) TextView mAlipayStatus;

    //args
    private MemberInfoResults userInfo;
    private TimePickerView mTimerPicker;
    private OptionsPickerView mGenderPicker;
    private Calendar mCalendar;

    public static SettingUserFragment newInstance(){
        return new SettingUserFragment();
    }

    public SettingUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting_user;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_setting_user_info));
        
        mHeadBack.setOnClickListener(this);
        mItemAvatar.setOnClickListener(this);
        mItemNickname.setOnClickListener(this);
        mItemPhone.setOnClickListener(this);
        mItemSex.setOnClickListener(this);
        mItemsign.setOnClickListener(this);
        mItemBirthday.setOnClickListener(this);
        mItemPassword.setOnClickListener(this);
        mItemOrderPassword.setOnClickListener(this);
        mItemAlipay.setOnClickListener(this);

        pickerInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMemberInfo();
    }

    private void pickerInit(){
        //time picker
        mTimerPicker = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        mCalendar = Calendar.getInstance();
        mTimerPicker.setRange(mCalendar.get(Calendar.YEAR) - 100, mCalendar.get(Calendar.YEAR));
        mTimerPicker.setTime(new Date());
        mTimerPicker.setCyclic(false);
        mTimerPicker.setCancelable(true);
        mTimerPicker.setTitle("出生日期");
        mTimerPicker.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                int year = date.getYear() + 1900;
                int month = date.getMonth() + 1;
                int day = date.getDay();
                int age = mCalendar.get(Calendar.YEAR) - year;
                String dateStr = year+"-"+month+"-"+day;
                mBirthday.setText(dateStr);
            }
        });

        //gender picker
        mGenderPicker = new OptionsPickerView(getContext());
        mGenderPicker.setTitle("性别");
        ArrayList<String> gender = new ArrayList<>();
        gender.add("未知");
        gender.add("男");
        gender.add("女");
        mGenderPicker.setPicker(gender);
        mGenderPicker.setCyclic(false);
        mGenderPicker.setSelectOptions(0);
        mGenderPicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String sex = "保密";
                switch (options1){
                    case 0:
                        sex = "保密";
                        break;
                    case 1:
                        sex = "男";
                        break;
                    case 2:
                        sex = "女";
                        break;
                }
                mSex.setText(String.valueOf(sex));
            }
        });
    }

    private void setUserData(MemberInfoResults userData){
        mNickname.setText(userData.nick_name);
        mPhone.setText(Utils.converterSecretPhone(userData.phone));
        mBirthday.setText(userData.birthday);
        if (!StringUtils.isEmpty(userData.icon)) {
            Glide.with(getContext()).load(userData.icon).into(mAvatar.getAvatarView());
        }

        if(Utils.getLoginInfo(getContext()).has_passwd){
            mPasswordStatus.setText(getResources().getString(R.string.setting_password_status_exist));
            mPasswordStatus.setTextColor(getResources().getColor(R.color.colorShadow));
        }else {
            mPasswordStatus.setText(getResources().getString(R.string.setting_password_status_none));
            mPasswordStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (Utils.getLoginInfo(getContext()).has_trade_passwd) {
            mOrderPasswordStatus.setText(getResources().getString(R.string.setting_password_status_exist));
            mOrderPasswordStatus.setTextColor(getResources().getColor(R.color.colorShadow));
        }else {
            mOrderPasswordStatus.setText(getResources().getString(R.string.setting_password_status_none));
            mOrderPasswordStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (Utils.getLoginInfo(getContext()).has_alipay_account) {
            mAlipayStatus.setText(getResources().getString(R.string.setting_password_status_exist));
            mAlipayStatus.setTextColor(getResources().getColor(R.color.colorShadow));
        }else {
            mAlipayStatus.setText(getResources().getString(R.string.setting_password_status_none));
            mAlipayStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void getMemberInfo() {
        Flowable<HttpResultModel<MemberInfoResults>> fr = DataService.getMemberInfo();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MemberInfoResults>>() {
            @Override
            public void accept(HttpResultModel<MemberInfoResults> memberInfoResultsHttpResultModel) throws Exception {
                if (memberInfoResultsHttpResultModel.isSucceful()) {
                    userInfo = memberInfoResultsHttpResultModel.data;
                    setUserData(memberInfoResultsHttpResultModel.data);
                } else {
                    Toast.makeText(getContext(), memberInfoResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 获取交易密码状态
     * */
    private void ProvingTradePssword(String password){
        Flowable<HttpResultModel<CheckTradePasswdResults>> fr = DataService.checkTradePassword(new CheckTradePasswdRequestBody(password));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CheckTradePasswdResults>>() {
            @Override
            public void accept(HttpResultModel<CheckTradePasswdResults> checkTradePasswdResultsHttpResultModel) throws Exception {
                if (checkTradePasswdResultsHttpResultModel.isSucceful()) goToSetTradePassword();
                else Toast.makeText(getContext(), "交易密码验证失败！", Toast.LENGTH_SHORT).show();
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

    private void goToSetTradePassword(){//跳转设置交易密码
        DetailFragmentsActivity.launch(getContext(),null,UpdateTradePasswordFragment.newInstance());
    }

    private void goToSetAlipay(){//跳转设置支付宝
        UpdateAlipayFragment updateAlipayFragment = UpdateAlipayFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean(UpdateAlipayFragment.TAG,Utils.getLoginInfo(getContext()).has_alipay_account);
        updateAlipayFragment.setArguments(bundle);
        DetailFragmentsActivity.launch(getContext(),bundle,updateAlipayFragment);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mItemPassword){
            DetailFragmentsActivity.launch(getContext(),null, ResetPasswdFragment.newInstance());
        }
        if (v == mItemOrderPassword){
            if (!(Utils.getLoginInfo(getContext()).has_trade_passwd)){
                goToSetTradePassword();
                return;
            }
            final PasswordEditDialog dialog = new PasswordEditDialog();
            dialog.addOnPassWordEditListener(new PasswordEditDialog.OnPassWordEditListener() {
                @Override
                public void onConfirmComplete(String password) {
                    dialog.dismiss();
                    ProvingTradePssword(password);
                }
            });
            dialog.show(getChildFragmentManager(),PasswordEditDialog.TAG);
        }
        if (v == mItemBirthday){
            mTimerPicker.show();
        }
        if (v == mItemSex){
            mGenderPicker.show();
        }
        if (v == mItemNickname){
            DetailFragmentsActivity.launch(getContext(),null,UpdateNicknameFragment.newInstance());
        }
        if (v == mItemPhone){
            DetailFragmentsActivity.launch(getContext(),null,UpdatePhoneFragment.newInstance());
        }
        if (v == mItemsign){
            DetailFragmentsActivity.launch(getContext(),null,UpdateSignFragment.newInstance());
        }
        if (v == mItemAlipay){
            goToSetAlipay();
        }

    }
}
