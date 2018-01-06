package com.game.helper.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import cn.droidlover.xdroidmvp.router.Router;

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

    public static void launchForResult(Activity context, Fragment fra, int flag, Bundle bundle, int requestCode) {
        currentFragment = fra;
        currentFragment.setArguments(bundle);
        Router.newIntent(context).to(DetailFragmentsActivity.class).addFlags(flag).data(bundle).requestCode(requestCode).launch();
    }

    /**
     * 使用startActivityForResult的时候，requestCode一定不要大于0xffff(65535)。
     * 如果希望在Fragment的onActivityResult接收数据，就要调用Fragment.startActivityForResult，而不是Fragment.getActivity().startActivityForResult。
     *
     * @param from
     * @param to
     * @param flag
     * @param bundle
     * @param requestCode
     */
    public static void launchForResult(Fragment from, Fragment to, int flag, Bundle bundle, int requestCode) {
        currentFragment = to;
        currentFragment.setArguments(bundle);
//        .to(DetailFragmentsActivity.class).addFlags(flag).data(bundle).requestCode(requestCode).launch();
        Intent it = new Intent(from.getActivity(), DetailFragmentsActivity.class);
        it.setFlags(flag);
        from.startActivityForResult(it, requestCode, bundle);
    }

    public static void launch(Context context, Bundle bundle, final Fragment fra) {
//        Router.newIntent((Activity) context).to(DetailFragmentsActivity.class).data(bundle).launch();
        launch(context, bundle, Intent.FLAG_ACTIVITY_NEW_TASK, fra);
    }

    public static void launch(Context context, Bundle bundle, int flag, final Fragment fra) {
//        Router.newIntent((Activity) context).to(DetailFragmentsActivity.class).data(bundle).launch();
        Intent intent = new Intent(context, DetailFragmentsActivity.class);
        intent.addFlags(flag);
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
        model.setTitle(packageInfo.getGame().getName() + " 最低" + packageInfo.getDiscount_vip() + "折");
        model.setUrl(packageInfo.getGame().getUrl());
        return model;
    }

    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
