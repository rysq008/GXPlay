package com.zhny.library.presenter.work.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.databinding.ActivitySelectWorkBinding;
import com.zhny.library.presenter.machine.util.MachineUtil;
import com.zhny.library.presenter.work.WorkConstants;
import com.zhny.library.presenter.work.adapter.FristWorkAdapter;
import com.zhny.library.presenter.work.adapter.SecWorkAdapter;
import com.zhny.library.presenter.work.adapter.YearAdapter;
import com.zhny.library.presenter.work.dto.WorkDto;
import com.zhny.library.presenter.work.helper.YearInfo;
import com.zhny.library.presenter.work.util.WorkUtil;
import com.zhny.library.presenter.work.viewmodel.SelectWorkViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 选择作业季
 */
public class SelectWorkActivity extends BaseActivity implements YearAdapter.YearItemClickListener
        , FristWorkAdapter.WorkItemClickListener, OnRefreshListener {
    private static final String TAG = "SelectWorkActivity";

    private ActivitySelectWorkBinding binding;

    private SelectWorkViewModel viewModel;


    private YearAdapter yearAdapter;

    List<WorkDto> dataList = new ArrayList<>();;
    List<WorkDto> oneYeardataList = new ArrayList<>();

    List<String> yearList ;
    List<YearInfo> yearInfoList ;

    List<Integer> fristJobTypeList = new ArrayList<>(Arrays.asList(0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 17, 20));
    List<WorkDto> fristWorkList ; //上方作业列表:点击可以跳转至 查看作业质量页面
    List<WorkDto> secWorkList ; //下方作业列表：不可点击

    private FristWorkAdapter fristWorkAdapter;
    private SecWorkAdapter secWorkAdapter;
    private String fieldName;
    private String fieldCode;
    private String fieldCenter;
    private String fieldCoordinates;

    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            fieldCode = params.getString(WorkConstants.BUNDLE_FIELD_CODE);
            fieldName = params.getString(WorkConstants.BUNDLE_FIELD_NAME);
            fieldCenter = params.getString(WorkConstants.BUNDLE_FIELD_CENTER);
            fieldCoordinates = params.getString(WorkConstants.BUNDLE_FIELD_COORDINATES);
        }
    }


    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(SelectWorkViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_work);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        setToolBarTitle(getString(R.string.select_work));
        viewModel.landName.setValue(fieldName);
        binding.setLifecycleOwner(this);

        initAdapter();
    }

    private void initAdapter() {
        //year
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        yearAdapter = new YearAdapter(this);
        binding.listYear.setLayoutManager(gridLayoutManager);
        binding.listYear.setAdapter(yearAdapter);

        //fristWorkAdapter
        GridLayoutManager g1 = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        fristWorkAdapter = new FristWorkAdapter(this);
        binding.listFristWork.setLayoutManager(g1);
        binding.listFristWork.setAdapter(fristWorkAdapter);

        //secWorkAdapter
        GridLayoutManager g2 = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        secWorkAdapter = new SecWorkAdapter();
        binding.listSecWork.setLayoutManager(g2);
        binding.listSecWork.setAdapter(secWorkAdapter);

        binding.smoothRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData(true);
    }

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //请求作业季数据
        if (TextUtils.isEmpty(fieldCode)) return;
        if (dataList.size() == 0) {
            requestData(false);
        }

    }

    private void requestData(boolean isDropDown) {
        if (!isDropDown) {
            //开始加载
            showLoading();
        }
        viewModel.getWorkData(fieldCode).observe(this, baseDto -> {
            if (baseDto.getContent() != null) {
                dataList.clear();
                dataList.addAll(baseDto.getContent());
                refreshYearData();
            }
            if (isDropDown) {
                binding.smoothRefreshLayout.finishRefresh();
            } else {
                //关闭加载
                dismissLoading();
            }
        });
    }


    /**
     * 刷新年份数据
     */
    private void refreshYearData() {
        if (dataList.size() == 0) return;
        yearList = new ArrayList<>();
        yearInfoList = new ArrayList<>();

        for (WorkDto workDto : dataList) {
            //year
            if (!TextUtils.isEmpty(workDto.startTime) && workDto.startTime.length() >= 4) {
                String year = !TextUtils.isEmpty(workDto.startTime.substring(0, 4)) ?
                        workDto.startTime.substring(0, 4) : "";

                workDto.startYear = year;
                yearList.add(year);
            }
        }

        //year
        yearList = new ArrayList<>(new HashSet<>(yearList));//去重后的集合
        Collections.sort(yearList, Collections.reverseOrder()); //从大到小排序

        //yearInfo
        if (yearList != null && yearList.size() != 0) {
            for (int i = 0; i < yearList.size(); i++) {
                YearInfo yearInfo;
                if (i == 0) {
                    yearInfo = new YearInfo(yearList.get(i), true);
                } else {
                    yearInfo = new YearInfo(yearList.get(i), false);
                }
                yearInfoList.add(yearInfo);
            }
            getOneYearWorkData(yearInfoList.get(0).year);
            yearAdapter.updateData(yearInfoList);
        }


    }

    /**
     * @param year 获取某一年的作业列表
     */
    private void getOneYearWorkData(String year) {
        Map<String, List<WorkDto>> map = WorkUtil.groupListByYear(dataList);
        oneYeardataList.clear();
        oneYeardataList.addAll(map.get(year));
        if (oneYeardataList == null || oneYeardataList.size() == 0) return;
        refreshOneYearWorkData();
    }

    /**
     * 刷新某一年的作业列表
     */
    private void refreshOneYearWorkData() {
        fristWorkList= new ArrayList<>();
        secWorkList = new ArrayList<>();
        for (WorkDto workDto : oneYeardataList) {
            String startTime = MachineUtil.getYMD(workDto.startTime);
            String endTime = MachineUtil.getYMD(workDto.endTime);

            workDto.startTime2EndTime = startTime + " - " + endTime;
            //fristWorkList secWorkList

            if (fristJobTypeList.contains(workDto.jobType)) {
                fristWorkList.add(workDto);
            } else {
                secWorkList.add(workDto);
            }
        }

        //fristWorkList
        fristWorkList = WorkUtil.sortWorkByStartTime(fristWorkList);
        boolean isShowFristWorkList = fristWorkList != null && fristWorkList.size() != 0;
        viewModel.showFristWorkList.setValue(isShowFristWorkList);
        if (isShowFristWorkList) {
            fristWorkAdapter.updateData(fristWorkList);
        }

        //secWorkList
        secWorkList = WorkUtil.sortWorkByStartTime(secWorkList);
        boolean isShowSecWorkList = secWorkList != null && secWorkList.size() != 0;
        viewModel.showSecWorkList.setValue(isShowSecWorkList);
        if (isShowSecWorkList) {
            secWorkAdapter.updateData(secWorkList);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }


    /**
     * 点击 上方 作业列表某项
     *
     * @param workDto workDto
     */
    @Override
    public void onWorkItemClick(WorkDto workDto) {

        //Go WorkTrackActivity
        Bundle bundle = new Bundle();

        bundle.putString(WorkConstants.BUNDLE_FIELD_NAME, fieldName);
        bundle.putString(WorkConstants.BUNDLE_FIELD_CODE, fieldCode);
        bundle.putString(WorkConstants.BUNDLE_FIELD_CENTER, fieldCenter);
        bundle.putString(WorkConstants.BUNDLE_FIELD_COORDINATES, fieldCoordinates);

        bundle.putSerializable(WorkConstants.BUNDLE_WORKDTO, workDto);

        startActivity(WorkTrackActivity.class, bundle);


    }

    /**
     * 点击年份列表某项
     *
     * @param yearInfo yearInfo
     * @param position 点击位置
     */
    @Override
    public void onYearItemClick(YearInfo yearInfo, int position) {

        for (int i = 0; i < yearInfoList.size(); i++) {
            if (i == position) {
                yearInfoList.get(i).isSelect = true;
            } else {
                yearInfoList.get(i).isSelect = false;
            }

        }
        getOneYearWorkData(yearInfoList.get(position).year);
        yearAdapter.updateData(yearInfoList);
    }


}
