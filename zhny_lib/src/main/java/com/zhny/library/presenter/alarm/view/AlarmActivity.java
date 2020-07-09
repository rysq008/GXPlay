package com.zhny.library.presenter.alarm.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.databinding.ActivityAlarmBinding;
import com.zhny.library.presenter.alarm.adapter.AlarmAdapter;
import com.zhny.library.presenter.alarm.dto.AlarmDto;
import com.zhny.library.presenter.alarm.viewmodel.AlarmViewModel;
import com.zhny.library.presenter.work.WorkConstants;
import com.zhny.library.presenter.work.view.SelectWorkActivity;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 农机预警
 */
public class AlarmActivity extends BaseActivity implements OnRefreshListener, AlarmAdapter.OnMsgDetailListener {

    private ActivityAlarmBinding binding;
    private AlarmViewModel viewModel;
    private AlarmAdapter adapter;
    List<AlarmDto>  dataList = new ArrayList<>();

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.alarm_title));
        initAdapter();
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        adapter = new AlarmAdapter(this);
        binding.alarmList.setLayoutManager(gridLayoutManager);
        binding.alarmList.setAdapter(adapter);

        binding.smartRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestData(false);
    }

    private void requestData(boolean isDropDown) {
        if (!isDropDown) {
            //开始加载
            showLoading();
        }
        //请求报警消息数据
        viewModel.getAlarms().observe(this, baseDto -> {
            List<AlarmDto> alarmDtos = baseDto.getContent();
            if (alarmDtos != null && alarmDtos.size() > 0) {
                viewModel.emptyMag.setValue(false);
                Collections.sort(alarmDtos, (o1, o2) -> TimeUtils.compareTime(o1.startDate, o2.startDate));
                dataList.clear();
                dataList.addAll(alarmDtos);
                adapter.updateData(dataList);
            } else {
                viewModel.emptyMag.setValue(true);
            }
            if (isDropDown) {
                binding.smartRefreshLayout.finishRefresh();
            } else {
                //关闭加载
                dismissLoading();
            }
        });
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

    @Override
    public void onMsgDetail(AlarmDto alarmDto) {
        //点击查看详情
        if (TextUtils.isEmpty(alarmDto.fieldCenter) || TextUtils.isEmpty(alarmDto.fieldCoordinates) || TextUtils.isEmpty(alarmDto.fieldCode)) {
            Toast.makeText(this, "缺少跳转参数", Toast.LENGTH_SHORT).show();
            return;
        }
        //设置已读标志
        if(!alarmDto.isRead) {
            viewModel.batchRead(Collections.singletonList(alarmDto.id));
        }
        Bundle bundle = new Bundle();
        bundle.putString(WorkConstants.BUNDLE_FIELD_CODE, alarmDto.fieldCode);
        bundle.putString(WorkConstants.BUNDLE_FIELD_NAME, alarmDto.fieldName);
        bundle.putString(WorkConstants.BUNDLE_FIELD_CENTER, alarmDto.fieldCenter);
        bundle.putString(WorkConstants.BUNDLE_FIELD_COORDINATES, alarmDto.fieldCoordinates);
        startActivity(SelectWorkActivity.class, bundle);
    }
}
