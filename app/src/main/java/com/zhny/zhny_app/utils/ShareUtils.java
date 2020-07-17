package com.zhny.zhny_app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.google.gson.Gson;
import com.zhny.zhny_app.R;
import com.zhny.zhny_app.model.LoginBean;
import com.zhny.zhny_app.net.api.Api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ShareUtils {
    private static final String KEY_MAC = "mac";
    private static final String KEY_FIRST_OPEN_APP = "first_open_app";
    private static final String SharedPreference_SessionId = "sessionId";
    private static final String KEY_COOKIES = "cookies";
    private static final String USER_INFO = "user_info";
    private static final String MEMBER_INFO = "member_info";
    private static final String SEARCH_HISTORY_LIST = "search_history_word";

    private static final String KEY_FIRST_ENTER_RECHARGE = "first_open_recharge";


    private static SharedPreferences preferences;

    public static final void init(Context context) {
        if (null == preferences)
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static final String getHost(String key) {
        return null == preferences ? Api.API_BASE_URL : preferences.getString(key, Api.API_BASE_URL);
    }

    public static final boolean saveHost(String key, String val) {
        return preferences.edit().putString(key, val).commit();
    }

    public static final void saveString(String key, String val) {
        preferences.edit().putString(key, val).apply();
    }

    public static final void saveInt(String key, Integer val) {
        preferences.edit().putInt(key, val).apply();
    }

    public static final void saveBool(String key, Boolean val) {
        preferences.edit().putBoolean(key, val).apply();
    }

    public static final <T> T getObject(String key, Class<T> tClass) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String str_out_obj = preferences.getString(key, "");
        if (TextUtils.isEmpty(str_out_obj)) {
            return null;
        }
        byte[] bytes = Base64.decode(str_out_obj, Base64.DEFAULT);
        return new Gson().fromJson(new String(bytes), tClass);
    }

    public static final String getString(String key) {
        return preferences.getString(key, "");
    }

    public static final int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public static final boolean getBool(String key) {
        return preferences.getBoolean(key, false);
    }

    public static final List<String> getIPList() {
        List<String> list = new ArrayList<>();
        try {
            Map<String, Object> map = (Map<String, Object>) preferences.getAll();
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue().equals("ip")) {
                    list.add(entry.getKey().toString());
                }
            }
        } catch (Exception e) {

        }
        return list;
    }

    public static final void setUpGlobalHost(Context ct, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            final static int COUNTS = 5;//点击次数
            final static long DURATION = 1 * 1000;//规定有效时间
            long[] mHits = new long[COUNTS];

            @Override
            public void onClick(View v) {
                /**
                 * 实现双击方法
                 * src 拷贝的源数组
                 * srcPos 从源数组的那个位置开始拷贝.
                 * dst 目标数组
                 * dstPos 从目标数组的那个位子开始写数据
                 * length 拷贝的元素的个数
                 */
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
//                    String tips = "您已在[" + DURATION + "]ms内连续点击【" + mHits.length + "】次了！！！";
//                    Toast.makeText(ct, tips, Toast.LENGTH_SHORT).show();
                    final EditText edittext = new EditText(ct);

                    new AlertDialog.Builder(ct).setTitle("提示").setMessage("请输入6位数密码").
                            setView(edittext).setCancelable(false).
                            setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    String pwd = edittext.getText().toString().trim();
                                    if (pwd.equals("321")) {

                                        EditText et = new EditText(ct);
                                        et.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_launcher, 0);
                                        List<String> slist = new ArrayList<>();
                                        final String[] list = {Api.API_BASE_URL, Api.API_BASE_URL_TEST};//要填充的数据
                                        slist.addAll(Arrays.asList(list));
                                        slist.addAll(ShareUtils.getIPList());
                                        AlertDialog enterDialog = new AlertDialog.Builder(ct).setTitle("提示").setMessage("请转入地址")
                                                .setView(et).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                        String str = et.getText().toString().trim();
                                                        if (TextUtils.isEmpty(str)) {
                                                            return;
                                                        }
                                                        if (!slist.contains(str)) {
                                                            ShareUtils.saveString(et.getText().toString().trim(), "ip");
                                                        }
                                                        if (ShareUtils.saveHost("host", str)) {
                                                            Api.resetIp(ShareUtils.getHost("host"));
                                                            ((Activity) ct).finish();
                                                            Intent it = ct.getPackageManager().getLaunchIntentForPackage(ct.getPackageName());
                                                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                            ct.startActivity(it);
                                                        }
                                                    }
                                                }).setNegativeButton("取消", null).show();


                                        et.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                Drawable drawable = et.getCompoundDrawables()[2];
                                                if (drawable == null) {
                                                    return false;
                                                }
                                                //drawleft 是 小于 ,drawright 是 大于
                                                //左右上下分别对应 0  1  2  3
                                                if (event.getX() > et.getWidth() - et.getCompoundDrawables()[2].getBounds().width()) {
                                                    //点击之后执行的事件
//                                                    Toast.makeText(ct, "右边的drawright被点击了", Toast.LENGTH_SHORT).show();
                                                    //textView.setText("我被点击了");
                                                    final ListPopupWindow listPopupWindow;
                                                    listPopupWindow = new ListPopupWindow(ct);
                                                    listPopupWindow.setAdapter(new ArrayAdapter<String>(ct, android.R.layout.simple_list_item_1, slist));//用android内置布局，或设计自己的样式
                                                    listPopupWindow.setAnchorView(et);//以哪个控件为基准，在该处以mEditText为基准
                                                    listPopupWindow.setModal(true);

                                                    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                            String str = adapterView.getAdapter().getItem(i).toString();
                                                            et.setText(str);//把选择的选项内容展示在EditText上
                                                            listPopupWindow.dismiss();//如果已经选择了，隐藏起来
                                                            enterDialog.cancel();
                                                            if (ShareUtils.saveHost("host", str)) {
                                                                Api.resetIp(ShareUtils.getHost("host"));
                                                                ((Activity) ct).finish();
                                                                Intent it = ct.getPackageManager().getLaunchIntentForPackage(ct.getPackageName());
                                                                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                ct.startActivity(it);
                                                            }
                                                        }
                                                    });
                                                    listPopupWindow.show();//把ListPopWindow展示出来
                                                    return false;
                                                }
                                                return false;
                                            }
                                        });


                                    }
                                }
                            }).
                            setNegativeButton("cancel", null).show();
                }
            }
        });
    }

    public static void saveObject(String key, Object object) {
        if ((null == object) || !(object instanceof Serializable)) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oosw = new ObjectOutputStream(baos);
            oosw.writeObject(object);
            String objstr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            preferences.edit().putString(key, objstr).commit();
            baos.close();
            oosw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveLoginInfo(LoginBean bean) {
        saveObject(USER_INFO, bean);
    }

    public static void clearLoginInfo() {
        remove(USER_INFO);
    }

    public static LoginBean getLoginInfo() {
        LoginBean loginBean = getObject(USER_INFO);
        return loginBean == null ? new LoginBean() : loginBean;
    }

    public static <T> T getObject(String key) {
        T t = null;
        String objstr = preferences.getString(key, "");
        if (TextUtils.isEmpty(objstr)) {
            return t;
        }
        try {
            byte[] bytes = Base64.decode(objstr, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            t = (T) ois.readObject();
            bais.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getSessionId() {
        return preferences.getString(SharedPreference_SessionId, "");
    }

    public static void saveSessionId(String toString) {
        preferences.edit().putString(SharedPreference_SessionId, toString).commit();
    }
}
