package com.game.helper.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;

import com.game.helper.activitys.BaseActivity.XDetailBaseActivity;
import com.game.helper.model.CommonShareResults;
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.share.UMengShare;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class DetailFragmentsActivity extends XDetailBaseActivity {
    public static final String TAG = "DetailFragmentsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
                .setSwipeBackEnable(true);
    }

    private static Fragment currentFragment;

    public interface OnKeyDownListener {
        boolean onKeyDown(int keyCode, KeyEvent keyEvent);
    }

    public static void launch(Context context, Bundle bundle, final Fragment fra) {
//        Router.newIntent((Activity) context).to(DetailFragmentsActivity.class).data(bundle).launch();
        Intent intent = new Intent(context, DetailFragmentsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentFragment = fra;
        currentFragment.setArguments(bundle);
        context.startActivity(intent);
    }

    @Override
    public Fragment getStartFragment() {
        return currentFragment;
    }

    public static Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (currentFragment instanceof OnKeyDownListener) {
            return ((OnKeyDownListener) currentFragment).onKeyDown(keyCode, keyEvent);
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentFragment = null;
        //内存泄露处理方法
        UMShareAPI.get(this).release();
    }

    public void umShare(GamePackageInfoResult packageInfo) {
        if (null == packageInfo) {
            return;
        }

        UMengShare share = new UMengShare(this);
        share.shareLinkWithBoard(parseToShareModel(packageInfo), new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e(TAG, "onStart: umShare");
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Log.e(TAG, "onResult: umShare");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Log.e(TAG, "onError: umShare");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Log.e(TAG, "onCancel: umShare");
            }
        });
    }

    private CommonShareResults parseToShareModel(GamePackageInfoResult packageInfo) {
        CommonShareResults model = new CommonShareResults();
        model.setContent(packageInfo.getGame().getIntro());
        model.setLogo(packageInfo.getGame().getLogo());
        model.setTitle(packageInfo.getGame().getName());
        model.setUrl(packageInfo.getGame().getUrl());
        return model;
    }

    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
