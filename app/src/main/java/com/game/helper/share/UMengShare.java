package com.game.helper.share;

import android.app.Activity;

import com.game.helper.R;
import com.game.helper.model.CommonShareResults;
import com.game.helper.net.api.Api;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by Administrator on 2017/12/7.
 */

public class UMengShare {

    private Activity mActivity;
    //默认的面板顺序
    private SHARE_MEDIA[] mMedia = new SHARE_MEDIA[]{
            SHARE_MEDIA.QZONE,
            SHARE_MEDIA.QQ,
            SHARE_MEDIA.WEIXIN,
            SHARE_MEDIA.WEIXIN_CIRCLE};

    //使用默认面板顺序
    public UMengShare(Activity activity) {
        this.mActivity = activity;
    }

    //指定默认面板顺序
    public UMengShare(Activity activity, SHARE_MEDIA[] media) {
        this.mActivity = activity;
        this.mMedia = media;
    }

    /*----------------------分享文本   begin---------------------------*/

    /**
     * 分享文本   不使用UMeng默认面板
     * qq不支持单纯分享文本
     * 回调集中处理
     */
    public void shareTextWithOutBoard(SHARE_MEDIA platform, String shareText, UMShareListener umShareListener) {
        new ShareAction(mActivity)
                .setPlatform(platform)//传入平台
                .withText(shareText)//分享内容
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    /**
     * 分享文本     不使用UMeng默认面板
     * qq不支持单纯分享文本
     * 回调单独处理
     */
    public void shareTextWithOutBoard(SHARE_MEDIA platform, String shareText) {
        new ShareAction(mActivity)
                .setPlatform(platform)//传入平台
                .withText(shareText)//分享内容
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })//回调监听器
                .share();
    }
    /*----------------------分享文本   end---------------------------*/


    /*----------------------分享图片   begin---------------------------*/

    /**
     * UMImage的构建有如下几种形式：
     UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
     UMImage image = new UMImage(ShareActivity.this, file);//本地文件
     UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
     UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
     UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
     Tip：推荐使用网络图片和资源文件的方式，平台兼容性更高

     用户设置的图片大小最好不要超过250k，缩略图不要超过18k，
     如果超过太多（最好不要分享1M以上的图片，压缩效率会很低），图片会被压缩。 用户可以设置压缩的方式：
     image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
     image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
     压缩格式设置:
     image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
     */
    /**
     * 分享图片    不使用UMeng默认面板
     * 回调集中处理
     */
    public void shareImgWithOutBoard(SHARE_MEDIA platform, String shareText, UMShareListener umShareListener) {
        new ShareAction(mActivity)
                .setPlatform(platform)//传入平台
                .withText(shareText)//分享内容
                .withMedia(new UMImage(mActivity, R.mipmap.ic_mine_game))
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    /**
     * 分享图片   不使用UMeng默认面板
     * 回调单独处理
     */
    public void shareImgWithOutBoard(SHARE_MEDIA platform, String shareText) {
        new ShareAction(mActivity)
                .setPlatform(platform)//传入平台
                .withText(shareText)//分享内容
                .withMedia(new UMImage(mActivity, R.mipmap.ic_mine_game))
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })//回调监听器
                .share();
    }


    /*----------------------分享图片   end---------------------------*/


    /*----------------------分享链接  begin---------------------------*/

    /**
     * 分享链接   不使用UMeng默认面板
     * 回调集中处理
     */
    public void shareLinkWithOutBoard(SHARE_MEDIA platform, UMShareListener umShareListener) {

        UMWeb web = new UMWeb("http://www.baidu.com");
        web.setTitle("This is music title");//标题
        web.setThumb(new UMImage(mActivity, R.mipmap.ic_mine_game));  //缩略图
        web.setDescription("my description");//描述

        new ShareAction(mActivity)
                .setPlatform(platform)//传入平台
                .withMedia(web)
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    /**
     * 分享链接      不使用UMeng默认面板
     * 回调单独处理
     */
    public void shareLinkWithOutBoard(SHARE_MEDIA platform) {

        UMWeb web = new UMWeb("http://www.baidu.com");
        web.setTitle("This is music title");//标题
        web.setThumb(new UMImage(mActivity, R.mipmap.ic_mine_game));  //缩略图
        web.setDescription("my description");//描述

        new ShareAction(mActivity)
                .setPlatform(platform)//传入平台
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })//回调监听器
                .share();
    }
    /*----------------------分享文本   end---------------------------*/

    /*##########################  以上不使用UMeng自带默认面板分享############################*/

    /**
     * ---------------------------------------分割线---------------------------------------------
     */

    /*##########################  以下使用UMeng自带默认面板分享############################*/


    /*----------------------分享文本   begin---------------------------*/

    /**
     * 分享文本  使用UMeng自带默认面板
     * qq不支持单纯分享文本
     * 回调集中处理
     */
    public void shareTextWithBoard(String shareText, UMShareListener umShareListener) {
        new ShareAction(mActivity)
                .withText(shareText)
                .setDisplayList(mMedia)
                .setCallback(umShareListener)
                .open();
    }

    /**
     * 分享文本  使用UMeng自带默认面板
     * qq不支持单纯分享文本
     * 回调集中处理
     */
    public void shareTextWithBoard(String shareText) {
        new ShareAction(mActivity)
                .withText(shareText)
                .setDisplayList(mMedia)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })
                .open();
    }

    /*----------------------分享文本   end---------------------------*/

    /*----------------------分享图片   begin---------------------------*/

    /**
     * 分享图片  使用UMeng自带默认面板
     * 回调集中处理
     */
    public void shareImgWithBoard(String shareText, UMShareListener umShareListener) {
        new ShareAction(mActivity)
                .withText(shareText)
                .withMedia(new UMImage(mActivity, R.mipmap.ic_mine_game))
                .setDisplayList(mMedia)
                .setCallback(umShareListener)
                .open();
    }

    /**
     * 分享图片  使用UMeng自带默认面板
     * 回调集中处理
     */
    public void shareImgWithBoard(String shareText) {
        new ShareAction(mActivity)
                .withText(shareText)
                .withMedia(new UMImage(mActivity, R.mipmap.ic_mine_game))
                .setDisplayList(mMedia)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })
                .open();
    }

    /*----------------------分享图片   end---------------------------*/

    /*----------------------分享链接   begin---------------------------*/

    /**
     * 分享链接  使用UMeng自带默认面板
     * 回调单独处理
     */
    public void shareLinkWithBoard(CommonShareResults commonShareResults, UMShareListener umShareListener) {

        UMWeb web = new UMWeb(commonShareResults.getUrl());
        web.setTitle(commonShareResults.getTitle());//标题
        web.setThumb(new UMImage(mActivity, Api.API_BASE_URL + commonShareResults.getLogo()));  //缩略图
        web.setDescription(commonShareResults.getDesc());//描述
//        web.setThumb(new UMImage(mActivity,R.mipmap.ic_alipay));  //缩略图

        new ShareAction(mActivity)
                .withMedia(web)
                .setDisplayList(mMedia)
                .setCallback(umShareListener)
                .open();
    }

    /**
     * 分享链接  使用UMeng自带默认面板
     * 回调集中处理
     */
    public void shareLinkWithBoard() {
        UMWeb web = new UMWeb("http://www.baidu.com");
        web.setTitle("This is music title");//标题
        web.setThumb(new UMImage(mActivity, R.mipmap.ic_mine_game));  //缩略图
        web.setDescription("my description");//描述

        new ShareAction(mActivity)
                .withMedia(web)
                .setDisplayList(mMedia)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })
                .open();
    }

    /*----------------------分享文本   end---------------------------*/
}
