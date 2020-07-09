package com.zhny.library.presenter.work.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityTrackSettingBinding;
import com.zhny.library.presenter.work.custom.BottomPopWin;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 参数设置
 */
public class TrackSettingActivity extends BaseActivity implements BottomPopWin.OnBottomPopWinListener {


    private ActivityTrackSettingBinding binding;
    private BottomPopWin bottomPopWin;
    private Integer standardValue, floatValue, invalidValue;

    @Override
    public void initParams(Bundle params) {
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_setting);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.track_setting));
        bottomPopWin = new BottomPopWin(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        standardValue = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.SP.WORK_TRACK_SETTING_PLOWING_STANDARD_VALUE
                , Constant.SP.WORK_TRACK_SETTING_PLOWING_STANDARD);

        floatValue = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.SP.WORK_TRACK_SETTING_PLOWING_FLOAT_VALUE
                , Constant.SP.WORK_TRACK_SETTING_PLOWING_FLOAT);

        invalidValue = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.SP.WORK_INVALID_VALUE
                , Constant.SP.WORK_INVALID);


        refreshViewData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保存数据到sp
        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.WORK_TRACK_SETTING_PLOWING_STANDARD_VALUE
                , standardValue);

        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.WORK_TRACK_SETTING_PLOWING_FLOAT_VALUE
                , floatValue);

        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.WORK_INVALID_VALUE
                , invalidValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    //设置犁地作业标准值
    public void setPlowingStandardValue(View view) {
        bottomPopWin.show(binding.getRoot(), getWindow(), standardValue, 1);
    }

    //设置犁地作业浮动值
    public void setPlowingFloatValue(View view) {
        bottomPopWin.show(binding.getRoot(), getWindow(), floatValue, 2);
    }

    //设置浮动值
    public void setInvalidFloatValue(View view) {
        bottomPopWin.show(binding.getRoot(), getWindow(), invalidValue, 3);
    }

    @Override
    public void onBottomPopWinClick(int type, int value) {
        if (type == 1) {
            standardValue = value;
        } else if (type == 2) {
            floatValue = value;
        } else if (type == 3) {
            invalidValue = value;
        }
        if (invalidValue >= standardValue - floatValue) {
            Toast.makeText(this, "无效值不能大于等于标准值和浮动值的差！", Toast.LENGTH_SHORT).show();
            return;
        }
        bottomPopWin.dismiss();
        refreshViewData();
    }

    private void refreshViewData() {
        binding.tvWorkTrackSettingPlowingOperationStandardValue.setText((standardValue + "CM"));
        binding.tvWorkTrackSettingPlowingOperationFloatValue.setText((floatValue + "CM"));
        binding.tvWorkInvalidFloatValue.setText((invalidValue + "CM"));
    }


}
