package com.game.helper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.AboutUsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.RegistResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.RegistRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * 系统相关设置
 */
public class SettingSystemFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = SettingSystemFragment.class.getSimpleName();
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.ll_about_us)
    View mAboutUs;
    @BindView(R.id.tv_exit_login)
    View mExit;

    public static SettingSystemFragment newInstance(){
        return new SettingSystemFragment();
    }

    public SettingSystemFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting_system;
    }


    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_setting_system));
        mHeadBack.setOnClickListener(this);

        mAboutUs.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }

    private void loginOut(){

        Flowable<HttpResultModel<LogoutResults>> fr = DataService.logout();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<LogoutResults>>() {
            @Override
            public void accept(HttpResultModel<LogoutResults> logoutResultsHttpResultModel) throws Exception {
                if (logoutResultsHttpResultModel.isSucceful()) {

                }else {
                    //Toast.makeText(getContext(), logoutResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: "+netError.getMessage().trim());
            }
        });

        Utils.clearLoginInfo(getContext());
        getActivity().onBackPressed();
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mExit){
            loginOut();
        }
        if (v == mAboutUs){
            startActivity(new Intent(getContext(), AboutUsActivity.class));
        }
    }
}
