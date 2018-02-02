package com.game.helper.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.model.MineGiftInfoResults;
import com.game.helper.utils.ScreenUtils;
import com.game.helper.utils.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by sung on 2017/12/21.
 * 礼包详情
 */

@SuppressLint("ValidFragment")
public class GiftDescDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    public static final String TAG = GiftDescDialog.class.getSimpleName();
    private TextView content;
    private TextView time;
    private ImageView close;

    private int id = -1;//礼包id
    private MineGiftInfoResults giftInfo;

    @SuppressLint("ValidFragment")
    public GiftDescDialog(int giftId, MineGiftInfoResults gift) {
        this.id = giftId;
        this.giftInfo = gift;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_gift_desc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        content = view.findViewById(R.id.tv_content);
        time = view.findViewById(R.id.tv_end_time);
        close = view.findViewById(R.id.iv_close);

        close.setOnClickListener(this);
        if (id == -1) return;
        setData();
    }

    private void setData() {
        if (giftInfo == null) {
            Toast.makeText(getContext(), "数据异常！请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        content.setText(giftInfo.gift_content);
        time.setText((StringUtils.isEmpty(giftInfo.start_time) ? "" : giftInfo.start_time)
                + (StringUtils.isEmpty(giftInfo.end_time) ? "" : ("至" + giftInfo.end_time)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() == null) return;
        if (!getDialog().isShowing()) return;
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = ScreenUtils.getScreenWidth(getContext()) / 4 * 3;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
//        mDismissed = false;
//        mShownByMe = true;
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(this, tag);
//        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
//        ft.commitAllowingStateLoss();
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Field shown = DialogFragment.class.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        if (v == close) {
            this.dismiss();
        }
    }
}
