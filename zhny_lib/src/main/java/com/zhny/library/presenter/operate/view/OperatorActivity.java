package com.zhny.library.presenter.operate.view;

import android.os.Bundle;

import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.databinding.ActivityOperatorBinding;
import com.zhny.library.presenter.data.view.DataOverviewActivity;
import com.zhny.library.presenter.driver.view.DriverListActivity;
import com.zhny.library.presenter.fence.view.FenceActivity;
import com.zhny.library.presenter.machine.view.MachineListActivity;
import com.zhny.library.presenter.operate.adapter.OperatorAdapter;
import com.zhny.library.presenter.operate.helper.OperatorEnum;
import com.zhny.library.presenter.operate.listener.OnOperatorItemListener;
import com.zhny.library.presenter.playback.view.PlayBackListActivity;
import com.zhny.library.presenter.work.view.SelectLandActivity;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

public class OperatorActivity extends BaseActivity implements OnOperatorItemListener {

    private ActivityOperatorBinding binding;
    private OperatorAdapter operatorAdapter;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_operator);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.operator_title));

        operatorAdapter = new OperatorAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        binding.rvOperate.setLayoutManager(gridLayoutManager);
        binding.rvOperate.setAdapter(operatorAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<OperatorEnum> operatorData = Arrays.asList(OperatorEnum.values());
        operatorAdapter.refreshData(operatorData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    public void onOperateItemClick(OperatorEnum item) {
        switch (item) {
            case OPERATOR_STATISTICS: //数据统计
                startActivity(DataOverviewActivity.class);
                break;
            case OPERATOR_TRACK_PLAYBACK: //轨迹回放
                startActivity(PlayBackListActivity.class);
                break;
            case OPERATOR_OPERATION_QUALITY: //作业质量
                startActivity(SelectLandActivity.class);
                break;
            case OPERATOR_ELECTRIC_FENCE: //电子围栏
                startActivity(FenceActivity.class);
                break;
            case OPERATOR_MACHINE_LIST: //机具列表
                startActivity(MachineListActivity.class);
                break;
            case OPERATOR_MY_PILOT: //我的机手
                startActivity(DriverListActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //设置operator从底部退出
        overridePendingTransition(R.anim.act_slide_in_bottom_return, R.anim.act_slide_out_bottom_return);
    }
}
