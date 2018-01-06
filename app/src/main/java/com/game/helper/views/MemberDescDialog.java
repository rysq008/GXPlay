package com.game.helper.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.MineGiftInfoResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.api.ApiService;
import com.game.helper.net.model.MineGiftInfoRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.ScreenUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by sung on 2017/12/21.
 * 礼包详情
 */

@SuppressLint("ValidFragment")
public class MemberDescDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    public static final String TAG = MemberDescDialog.class.getSimpleName();
    private ImageView icon0,icon1,icon2,icon3;
    private TextView name0,name1,name2,name3;
    private TextView value0,value1,value2,value3;
    private TextView bili0,bili1,bili2,bili3;
    private TextView confirm;

    private VipLevelResults vipList;

    public MemberDescDialog(VipLevelResults vip) {
        this.vipList = vip;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_member_desc, container, false);
        icon0 = view.findViewById(R.id.iv_img_0);
        name0 = view.findViewById(R.id.tv_name_0);
        value0 = view.findViewById(R.id.tv_value_0);
        bili0 = view.findViewById(R.id.tv_bili_0);

        icon1 = view.findViewById(R.id.iv_img_1);
        name1 = view.findViewById(R.id.tv_name_1);
        value1 = view.findViewById(R.id.tv_value_1);
        bili1 = view.findViewById(R.id.tv_bili_1);

        icon2 = view.findViewById(R.id.iv_img_2);
        name2 = view.findViewById(R.id.tv_name_2);
        value2 = view.findViewById(R.id.tv_value_2);
        bili2 = view.findViewById(R.id.tv_bili_2);

        icon3 = view.findViewById(R.id.iv_img_3);
        name3 = view.findViewById(R.id.tv_name_3);
        value3 = view.findViewById(R.id.tv_value_3);
        bili3 = view.findViewById(R.id.tv_bili_3);

        confirm = view.findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    private void setData(){
        if (vipList == null) return;
        //ILFactory.getLoader().loadNet(icon0, Api.API_PAY_OR_IMAGE_URL + vipList.list.get(0).image, ILoader.Options.defaultOptions());
        name0.setText(vipList.list.get(0).name+"会员");
        value0.setText(vipList.list.get(0).vip_re_amount+"元");
        bili0.setText((int)(Float.parseFloat(vipList.list.get(0).no_re_rate) * 100) + "%");

        //ILFactory.getLoader().loadNet(icon1, Api.API_PAY_OR_IMAGE_URL + vipList.list.get(1).image, ILoader.Options.defaultOptions());
        name1.setText(vipList.list.get(1).name+"会员");
        value1.setText(vipList.list.get(1).vip_re_amount+"元");
        bili1.setText((int)(Float.parseFloat(vipList.list.get(1).no_re_rate) * 100) + "%");

        //ILFactory.getLoader().loadNet(icon2, Api.API_PAY_OR_IMAGE_URL + vipList.list.get(2).image, ILoader.Options.defaultOptions());
        name2.setText(vipList.list.get(2).name+"会员");
        value2.setText(vipList.list.get(2).vip_re_amount+"元");
        bili2.setText((int)(Float.parseFloat(vipList.list.get(2).no_re_rate) * 100) + "%");

        //ILFactory.getLoader().loadNet(icon3, Api.API_PAY_OR_IMAGE_URL + vipList.list.get(3).image, ILoader.Options.defaultOptions());
        name3.setText(vipList.list.get(3).name+"会员");
        value3.setText(vipList.list.get(3).vip_re_amount+"元");
        bili3.setText((int)(Float.parseFloat(vipList.list.get(3).no_re_rate) * 100) + "%");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() == null) return;
        if (!getDialog().isShowing()) return;
        getDialog().setCanceledOnTouchOutside(false);
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = ScreenUtils.getScreenWidth(getContext()) / 4 * 3;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        if (v == confirm){
            if (getDialog() != null) getDialog().dismiss();
        }
    }
}
