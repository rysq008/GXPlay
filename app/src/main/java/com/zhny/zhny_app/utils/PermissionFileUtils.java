package com.zhny.zhny_app.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import java.io.File;

/**
 *
 * 时   间：2019/1/21
 * 简   述：<功能简述>
 */

public class PermissionFileUtils {

    //    private static String APP_DIR_NAME = "honjane";
    private static String APP_DIR_NAME = PathUtils.app_base_dir + "/update";
    private static String FILE_DIR_NAME = "files";
    private static String mRootDir;
    private static String mAppRootDir;
    private static String mFileDir;

    public static void init(){

        mRootDir = getRootPath();
        if (mRootDir != null && !"".equals(mRootDir)){
            mAppRootDir = mRootDir + APP_DIR_NAME;
            mFileDir = mAppRootDir + FILE_DIR_NAME;
            File appDir = new File(mAppRootDir);
            if (!appDir.exists()){
                appDir.mkdirs();
            }
            File fileDir = new File(mAppRootDir + "/" + FILE_DIR_NAME);
            if (!fileDir.exists()){
                fileDir.mkdirs();
            }

        }else{
            mRootDir = "";
            mAppRootDir = "";
            mFileDir = "";
        }
    }

    public static String getFileDir(){

        return mFileDir;
    }

    public static String getRootPath(){

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return Environment.getExternalStorageDirectory().getAbsolutePath(); // filePath:  /sdcard/
        }else{
            return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:  /data/data/
        }
    }

    /**
     * 打开文件
     * 兼容7.0
     *
     * @param context activity
     * @param file File
     * @param contentType 文件类型如：文本（text/html）
     * 当手机中没有一个app可以打开file时会抛ActivityNotFoundException
     */
    public static void startActionFile(Context context, File file, String contentType) throws ActivityNotFoundException{

        if (context == null){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
        intent.setDataAndType(getUriForFile(context, file), contentType);
        if (!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 打开相机
     * 兼容7.0
     *
     * @param activity Activity
     * @param file File
     * @param requestCode result requestCode
     */
    public static void startActionCapture(Activity activity, File file, int requestCode){

        if (activity == null){
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, file));
        activity.startActivityForResult(intent, requestCode);
    }

    public static Uri getUriForFile(Context context, File file){

        if (context == null || file == null){
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24){
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.innovationai.verify"+".provider", file);
        }else{
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static Uri getImageContentUri(Context context, File imageFile){

        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            new String[]{MediaStore.Images.Media._ID},
            MediaStore.Images.Media.DATA + "=? ",
            new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()){
            int id = cursor.getInt(cursor
                .getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(Uri.parse("content://media/external/images/media"), "" + id);
        }else{
            if (imageFile.exists()){
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }else{
                return null;
            }
        }
    }
}
