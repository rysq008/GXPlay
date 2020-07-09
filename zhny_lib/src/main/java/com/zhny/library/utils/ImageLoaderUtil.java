package com.zhny.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zhny.library.R;

public class ImageLoaderUtil {
    static ImageLoaderUtil instance;

    public ImageLoaderUtil() {
    }

    public static ImageLoaderUtil getInstance() {
        if (null == instance) {
            synchronized (ImageLoaderUtil.class) {
                if (null == instance) {
                    instance = new ImageLoaderUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 普通加载
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, int placeholderId, int errorImgId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderId)
                .error(errorImgId);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆形带圆边图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleWithFrameImage(Context context, String url, ImageView imageView, int borderWidth, int borderColor) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new GlideCircleTransform(borderWidth, borderColor))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage(Context context, String url, ImageView imageView) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(20))
                .placeholder(R.drawable.default_machine_icon)
                .error(R.drawable.default_machine_icon);

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.default_machine_icon);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage(Context context,
                                      String url,
                                      int roundingRadius,
                                      ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.default_machine_icon).into(imageView);
        } else {
            RequestOptions options = RequestOptions
                    .bitmapTransform(new RoundedCorners(roundingRadius))
                    .placeholder(R.drawable.default_machine_icon)
                    .error(R.drawable.default_machine_icon);
            Glide.with(context).load(url).apply(options).into(imageView);
        }

    }


}
