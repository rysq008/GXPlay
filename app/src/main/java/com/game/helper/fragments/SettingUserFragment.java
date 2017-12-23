package com.game.helper.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.BuildConfig;
import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.login.ResetPasswdFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CheckTradePasswdResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
import com.game.helper.net.model.UpdateBirthdayRequestBody;
import com.game.helper.net.model.UpdateGenderRequestBody;
import com.game.helper.utils.FileUtils;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.UploadUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.AvatarEditDialog;
import com.game.helper.views.HeadImageView;
import com.game.helper.views.OptionsPickerView;
import com.game.helper.views.widget.TimePickerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * 用户个人信息设置
 */
public class SettingUserFragment extends XBaseFragment implements View.OnClickListener, AvatarEditDialog.onDialogActionListner {
    public static final String TAG = SettingUserFragment.class.getSimpleName();
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.ll_avatar)
    View mItemAvatar;
    @BindView(R.id.hv_avatar)
    HeadImageView mAvatar;
    @BindView(R.id.ll_nickname)
    View mItemNickname;
    @BindView(R.id.tv_nickname)
    TextView mNickname;
    @BindView(R.id.ll_phone)
    View mItemPhone;
    @BindView(R.id.tv_phone)
    TextView mPhone;
    @BindView(R.id.ll_sex)
    View mItemSex;
    @BindView(R.id.tv_sex)
    TextView mSex;
    @BindView(R.id.ll_birthday)
    View mItemBirthday;
    @BindView(R.id.tv_birthday)
    TextView mBirthday;
    @BindView(R.id.ll_sign)
    View mItemsign;
    @BindView(R.id.ll_safe_pw)
    View mItemPassword;
    @BindView(R.id.tv_password_status)
    TextView mPasswordStatus;
    @BindView(R.id.ll_safe_orderpw)
    View mItemOrderPassword;
    @BindView(R.id.tv_safe_orderpw_status)
    TextView mOrderPasswordStatus;
    @BindView(R.id.ll_safe_alipay)
    View mItemAlipay;
    @BindView(R.id.tv_safe_alipay_status)
    TextView mAlipayStatus;

    //args
    private MemberInfoResults userInfo;
    private TimePickerView mTimerPicker;
    private OptionsPickerView mGenderPicker;
    private Calendar mCalendar;

    //avatar
    private AvatarEditDialog dialog;
    private static final int CAPTURE_IMAGE_CAMERA = 100;
    private static final int IMAGE_STORE = 200;
    private static final int CROP_CHOOSE = 10;
    private Uri mIconUrl;
    private Uri mIconCrop;

    public static SettingUserFragment newInstance() {
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

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_setting_user_info));
        pickerInit();

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
    }

    @Override
    public void onResume() {
        super.onResume();
        getMemberInfo();
    }

    /**
     * 初始化性别日期选择框
     */
    private void pickerInit() {
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = simpleDateFormat.format(date);
                mBirthday.setText(dateStr);
                updateBirthday(dateStr);
            }
        });

        //gender picker
        mGenderPicker = new OptionsPickerView(getContext());
        mGenderPicker.setTitle("性别");
        ArrayList<String> gender = new ArrayList<>();
        gender.add("女");
        gender.add("男");
        gender.add("保密");
        mGenderPicker.setPicker(gender);
        mGenderPicker.setCyclic(false);
        mGenderPicker.setSelectOptions(0);
        mGenderPicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String sex = "未知";
                switch (options1) {
                    case 0:
                        sex = "女";
                        break;
                    case 1:
                        sex = "男";
                        break;
                    case 2:
                        sex = "未知";
                        break;
                }
                mSex.setText(String.valueOf(sex));
                updateGender(options1 + "");
            }
        });
    }

    private void setUserData(MemberInfoResults userData) {
        mNickname.setText(userData.nick_name);
        mPhone.setText(Utils.converterSecretPhone(userData.phone));
        mBirthday.setText(userData.birthday);
        if (userData.gender.equals("2")) mSex.setText("保密");
        else if (userData.gender.equals("0")) mSex.setText("女");
        else if (userData.gender.equals("1")) mSex.setText("男");
        if (!StringUtils.isEmpty(userData.icon_thumb)) {
//            Glide.with(getContext()).load(userData.icon).into(mAvatar.getAvatarView());

//            ILFactory.getLoader().loadNet(mAvatar.getAvatarView(), Api.API_BASE_URL.concat(userData.icon),null);
            BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, userData.icon_thumb));
        }


        //安全管理三项状态设置
        if (Utils.getLoginInfo(getContext()).has_passwd) {
            mPasswordStatus.setText(getResources().getString(R.string.setting_password_status_exist));
            mPasswordStatus.setTextColor(getResources().getColor(R.color.colorShadow));
        } else {
            mPasswordStatus.setText(getResources().getString(R.string.setting_password_status_none));
            mPasswordStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (Utils.getLoginInfo(getContext()).has_trade_passwd) {
            mOrderPasswordStatus.setText(getResources().getString(R.string.setting_password_status_exist));
            mOrderPasswordStatus.setTextColor(getResources().getColor(R.color.colorShadow));
        } else {
            mOrderPasswordStatus.setText(getResources().getString(R.string.setting_password_status_none));
            mOrderPasswordStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (Utils.getLoginInfo(getContext()).has_alipay_account) {
            mAlipayStatus.setText(getResources().getString(R.string.setting_password_status_exist));
            mAlipayStatus.setTextColor(getResources().getColor(R.color.colorShadow));
        } else {
            mAlipayStatus.setText(getResources().getString(R.string.setting_password_status_none));
            mAlipayStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
        if (v == mItemAvatar) {
            if (dialog == null) {
                dialog = new AvatarEditDialog();
                dialog.addOnDialogActionListner(this);
            }
            dialog.show(getChildFragmentManager(), AvatarEditDialog.TAG);
        }
        if (v == mItemPassword) {
            DetailFragmentsActivity.launch(getContext(), null, ResetPasswdFragment.newInstance());
        }
        if (v == mItemOrderPassword) {
            goToSetTradePassword(true);
            /*if (!(Utils.getLoginInfo(getContext()).has_trade_passwd)) {
                goToSetTradePassword(true);
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
            dialog.show(getChildFragmentManager(), PasswordEditDialog.TAG);*/
        }
        if (v == mItemBirthday) {
            mTimerPicker.show();
        }
        if (v == mItemSex) {
            mGenderPicker.show();
        }
        if (v == mItemNickname) {
            UpdateNicknameFragment updateNicknameFragment = UpdateNicknameFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(UpdateNicknameFragment.TAG, userInfo.nick_name);
            updateNicknameFragment.setArguments(bundle);
            DetailFragmentsActivity.launch(getContext(), bundle, updateNicknameFragment);
        }
        if (v == mItemPhone) {
            UpdatePhoneFragment updatePhoneFragment = UpdatePhoneFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(UpdatePhoneFragment.TAG, userInfo.phone);
            updatePhoneFragment.setArguments(bundle);
            DetailFragmentsActivity.launch(getContext(), bundle, updatePhoneFragment);
        }
        if (v == mItemsign) {
            UpdateSignFragment updateSignFragment = UpdateSignFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(UpdateSignFragment.TAG, userInfo.signature);
            updateSignFragment.setArguments(bundle);
            DetailFragmentsActivity.launch(getContext(), bundle, updateSignFragment.newInstance());
        }
        if (v == mItemAlipay) {
            goToSetAlipay();
        }

    }

    /**************************         safe mannage         ***************************/

    /**
     * 获取交易密码状态
     */
    private void ProvingTradePssword(String password) {
        Flowable<HttpResultModel<CheckTradePasswdResults>> fr = DataService.checkTradePassword(new CheckTradePasswdRequestBody(password));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CheckTradePasswdResults>>() {
            @Override
            public void accept(HttpResultModel<CheckTradePasswdResults> checkTradePasswdResultsHttpResultModel) throws Exception {
                if (checkTradePasswdResultsHttpResultModel.isSucceful())
                    goToSetTradePassword(false);
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

    /**
     * 跳转设置交易密码
     */
    private void goToSetTradePassword(boolean isSetTrade) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(UpdateTradePasswordFragment.TAG, isSetTrade);
        UpdateTradePasswordFragment updateTradePasswordFragment = UpdateTradePasswordFragment.newInstance();
        DetailFragmentsActivity.launch(getContext(), bundle, updateTradePasswordFragment);
    }

    /**
     * 跳转设置支付宝
     */
    private void goToSetAlipay() {
        UpdateAlipayFragment updateAlipayFragment = UpdateAlipayFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean(UpdateAlipayFragment.TAG, Utils.getLoginInfo(getContext()).has_alipay_account);
        updateAlipayFragment.setArguments(bundle);
        DetailFragmentsActivity.launch(getContext(), bundle, updateAlipayFragment);
    }
    /**************************         safe mannage end         ***************************/

    /**************************         avatar edit about         ***************************/
    @Override
    public void onCancel() {
        if (dialog != null) dialog.dismiss();
    }

    /**
     * 拍摄
     */
    @Override
    public void onTakePhoto() {
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mIconUrl = createCoverUri("_icon");
        intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, mIconUrl);
        startActivityForResult(intent_photo, CAPTURE_IMAGE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_CAMERA:
            case IMAGE_STORE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (null == data) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case CROP_CHOOSE:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getContext(), "数据错误，请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mIconCrop != null) {
                    Log.d(TAG, "avatar uri: " + mIconCrop.toString());
                    mAvatar.getAvatarView().setImageURI(mIconCrop);
                    updateAvatar(FileUtils.getFileByPath(mIconCrop.getPath()));
                }
                break;
        }
    }

    /**
     * 选择相册图片
     */
    @Override
    public void onChoosePic() {
        mIconUrl = createCoverUri("_select_icon");
        Intent intent_album = new Intent(Intent.ACTION_PICK);
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    /**
     * 创建img uri
     */
    private Uri createCoverUri(String type) {
        String filename = Utils.getLoginInfo(getContext()).member_id + type + ".jpg";
        File outputImage = new File(Utils.getRootDir(), filename);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RxConstant.WRITE_PERMISSION_REQ_CODE);
            return null;
        }
        FileUtils.createFileByDeleteOldFile(outputImage);
        Uri uri = Uri.fromFile(outputImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !type.contains("crop")) {
            uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", outputImage);
            getContext().grantUriPermission(BuildConfig.APPLICATION_ID, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        return uri;
    }

    /**
     * 缩放
     */
    public void startPhotoZoom(Uri uri) {
        mIconCrop = createCoverUri("_icon_crop");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 30);
        intent.putExtra("aspectY", 30);
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mIconCrop);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_CHOOSE);
    }
    /**************************         avatar edit about end        ***************************/


    /**************************         setting internet       ***************************/
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
     * 更新头像
     */
    private void updateAvatar(File avatar) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("");
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);


        Flowable<HttpResultModel<Object>> ff = DataService.setApiUserIcon(avatar, new UploadUtils.FileUploadProgress() {
            @Override
            public void onProgress(final int progress) {
                dialog.setProgress(progress);
            }
        });


        RxLoadingUtils.subscribeWithDialog(dialog, ff, this.bindToLifecycle(), new Consumer<HttpResultModel<Object>>() {
            @Override
            public void accept(HttpResultModel<Object> httpResultModel) throws Exception {
                if (httpResultModel.isSucceful()) {
                    String icon = ((Map<String, String>) httpResultModel.data).get("image");
                    Toast.makeText(getContext(), icon, Toast.LENGTH_SHORT).show();
                    LoginUserInfo info = SharedPreUtil.getLoginUserInfo();
                    info.icon = icon;
                    SharedPreUtil.saveLoginUserInfo(info);
                    BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, ((Map<String, String>) httpResultModel.data).get("image")));
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "accept: " + netError.getMessage());
            }
        }, null, false);
    }

    /**
     * 更新生日
     */
    private void updateBirthday(String birthday) {
        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.updateBirthday(new UpdateBirthdayRequestBody(birthday));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
            @Override
            public void accept(HttpResultModel<NotConcernResults> notConcernResultsHttpResultModel) throws Exception {
                if (notConcernResultsHttpResultModel.isSucceful()) {
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

    /**
     * 更新性别
     */
    private void updateGender(String gender) {
        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.updateGender(new UpdateGenderRequestBody(gender));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
            @Override
            public void accept(HttpResultModel<NotConcernResults> notConcernResultsHttpResultModel) throws Exception {
                if (notConcernResultsHttpResultModel.isSucceful()) {
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
    /**************************         setting internet end       ***************************/


}
