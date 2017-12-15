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
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.HeadImageView;
import com.game.helper.views.PasswordEditDialog;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

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

    //args
    private MemberInfoResults userInfo;

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

        getMemberInfo();
    }

    private void setUserData(MemberInfoResults userData){
        mNickname.setText(userData.nick_name);
        mPhone.setText(userData.phone);
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

    @Override
    public Object newP() {
        return null;
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
            PasswordEditDialog dialog = new PasswordEditDialog();
            dialog.show(getChildFragmentManager(),PasswordEditDialog.TAG);
        }
        Toast.makeText(getContext(), v.getId()+"", Toast.LENGTH_SHORT).show();
    }
}
