package com.game.helper.activitys.BaseActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.share.PermissionUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class ShareActivity extends Activity implements View.OnClickListener{

    public static final int CODE = 4545;
    @BindView(R.id.ll_cancel)
    LinearLayout llCancel;
    @BindView(R.id.tv_wechat_friend)
    LinearLayout tvWechatFriend;
    @BindView(R.id.tv_wechat_moment)
    LinearLayout tvWechatMoment;
    @BindView(R.id.qq_share_ll)
    LinearLayout qqShareLl;
    @BindView(R.id.qzone_share_ll)
    LinearLayout qzoneShareLl;

    private SHARE_MEDIA mPlatformName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        initListeners();
    }

    protected  void initListeners(){
        llCancel.setOnClickListener(this);
        tvWechatFriend.setOnClickListener(this);
        tvWechatMoment.setOnClickListener(this);
        qqShareLl.setOnClickListener(this);
        qzoneShareLl.setOnClickListener(this);
    }

    /**
     *  Android6.0权限适配
     */
    private void permisson() {
        if(PermissionUtils.PermissionCheck(this)){
//            String[] mPermissionList = new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this,mPermissionList,CODE);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE);
            }
        }else{
            onShare(mPlatformName);
        }

    }

    // 调用requestPermissions会弹出对话框，用户做出选择之后的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE) {
            for(int i = 0;i<grantResults.length;i++){
                int writeResult = grantResults[i];
                //判断是否授权，也就是用户点击的是拒绝还是接受
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;
                if (writeGranted) {
                    //用户点击了接受，可以进行相应处理
                    onShare(mPlatformName);
                } else {
                    //用户点击了拒绝，可以进行相应处理
                    cancelShare();
                    Toast.makeText(this, "拒绝了授权，取消分享", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_wechat_friend://微信分享
                mPlatformName = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.tv_wechat_moment://微信分享朋友圈
                mPlatformName = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case R.id.qq_share_ll://qq分享
                mPlatformName = SHARE_MEDIA.QQ;
                break;
            case R.id.qzone_share_ll://qqzone
                mPlatformName = SHARE_MEDIA.QZONE;
                break;
            case R.id.ll_cancel://取消
                cancelShare();
                return;
        }
        permisson();
        onShare(mPlatformName);
    }

    /**
     * 所有的平台分享执行方法
     *
     */
    protected abstract void onShare(SHARE_MEDIA platForm);

    /**
     * 取消分享动作执行方法
     */
    protected abstract void cancelShare();

    /**
     * 注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，
     * 需要在fragment依赖的Activity中实现
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
