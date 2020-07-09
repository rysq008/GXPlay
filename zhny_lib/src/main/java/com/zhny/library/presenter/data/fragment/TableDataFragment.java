package com.zhny.library.presenter.data.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.zhny.library.R;
import com.zhny.library.base.BaseFragment;
import com.zhny.library.databinding.FragmentTableDataBinding;
import com.zhny.library.presenter.data.custom.table.ScrollablePanelAdapter;
import com.zhny.library.presenter.data.dialog.CalendarRangeDialog;
import com.zhny.library.presenter.data.dialog.DataDevicePopWin;
import com.zhny.library.presenter.data.dialog.TableDataPieDialog;
import com.zhny.library.presenter.data.listener.OnTableDataListener;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.model.vo.CustomTableData;
import com.zhny.library.presenter.data.model.vo.PieDataVo;
import com.zhny.library.presenter.data.util.TableDataHelper;
import com.zhny.library.presenter.data.viewmodel.TableDataViewModelViewModel;
import com.zhny.library.presenter.playback.customer.SingleBottomPopWin;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

public class TableDataFragment extends BaseFragment implements OnTableDataListener,
        DataDevicePopWin.OnDataAddMachineListener,
        ScrollablePanelAdapter.OnTableRowSelectListener,
        CalendarRangeDialog.OnCalendarRangeFinishListener,
        SingleBottomPopWin.OnSingleBottomPopWinListener {

    private FragmentTableDataBinding binding;
    private TableDataViewModelViewModel viewModel;
    private SingleBottomPopWin singleBottomPopWin;
    private TableDataPieDialog tableDataPieDialog;
    private DataDevicePopWin dataDevicePopWin;
    private CalendarRangeDialog calendarRangeDialog;
    private ScrollablePanelAdapter scrollablePanelAdapter;
    private List<JobReportDto.JobBean> jobBeanList = new ArrayList<>();
    private String startDate, endDate, snList;
    private List<String> sortList;
    private int selectSortIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(TableDataViewModelViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_table_data, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        viewModel.setParams(getContext());
        binding.setOnTableDataListener(this);

        binding.tvSelectDate.setText("选择时间");

        sortList = Arrays.asList(getResources().getStringArray(R.array.data_table_sort));
        binding.tvTableDataSort.setText(sortList.get(0));
        singleBottomPopWin = new SingleBottomPopWin(getContext(), sortList, this);

        dataDevicePopWin = new DataDevicePopWin(getContext(), this);
        tableDataPieDialog = new TableDataPieDialog();
        scrollablePanelAdapter = new ScrollablePanelAdapter(this);

        calendarRangeDialog = new CalendarRangeDialog(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        selectSortIndex = 0;
        snList = null;
        startDate = TimeUtils.getDateStr(new Date(), -7);
        endDate = TimeUtils.getDateStr(new Date(), -1);
        String start2EndTime = startDate.replace("-", ".").concat(" - ").concat(endDate.replace("-", "."));
        binding.tvSelectDate.setText(start2EndTime);
        requestJobReportData(startDate, endDate, snList, selectSortIndex);
    }

    //请求数据
    private void requestJobReportData(String start, String end, String snList, int selectSortIndex) {
        showLoading();
        int sort = selectSortIndex + 1;
        viewModel.getJobReport(start, end, snList, sort).observe(this, baseDto -> {
            if (baseDto.getContent() != null && baseDto.getContent().jobList != null) {
                jobBeanList.clear();
                if (baseDto.getContent().jobList.size() > 10) {
                    jobBeanList.addAll(baseDto.getContent().jobList.subList(0, 10));
                } else {
                    jobBeanList.addAll(baseDto.getContent().jobList);
                }
                updateTableData(baseDto.getContent());
            }
            dismissLoading();
        });
    }

    private void updateTableData(JobReportDto jobReportDto) {
        CustomTableData tableData = TableDataHelper.ecTableData(jobReportDto);
        tableData.topList = Arrays.asList(getResources().getStringArray(R.array.table_data_row));

        scrollablePanelAdapter.refresh(tableData);
        binding.spTable.setPanelAdapter(scrollablePanelAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    @Override
    public void onSelectSortClick() {
        if (!singleBottomPopWin.isShowing()){
            Window window = Objects.requireNonNull(getActivity()).getWindow();
            singleBottomPopWin.show(binding.getRoot(), window, selectSortIndex, 2);
        }
    }

    @Override
    public void onSelectTimeClick() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            calendarRangeDialog.show(activity.getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onAddDevices() {
        //点击添加机具
        if (!dataDevicePopWin.isShowing()) {
            dataDevicePopWin.show(binding.getRoot(), Objects.requireNonNull(getActivity()).getWindow());
        }
        //请求数据
        viewModel.getDataDevices().observe(this, baseDto -> {
            if (baseDto.getContent() == null) {
                return;
            }
            List<DataDeviceDto> deviceDtos = baseDto.getContent();
            List<String> tableSnList = TableDataHelper.getDevicesSnList(jobBeanList);
            boolean hasSelected = false;
            for (DataDeviceDto dto : deviceDtos) {
//                if (snList != null && snList.contains(dto.sn)) {
//                    dto.checkType = 1;
//                }
                if (tableSnList != null && tableSnList.contains(dto.sn)) {
                    dto.checkType = 1;
                    hasSelected = true;
                }
                dto.brandAndModel = dto.productBrandMeaning + "-" + dto.productModel;
            }
            dataDevicePopWin.refreshData(hasSelected, baseDto.getContent());
        });
    }


    @Override
    public void onSingleBottomPopWinClick(int type, int index) {
        selectSortIndex = index;
        binding.tvTableDataSort.setText(sortList.get(index));
        requestJobReportData(startDate, endDate, snList, selectSortIndex);
        if (singleBottomPopWin.isShowing()) {
            singleBottomPopWin.dismiss();
        }
    }

    @Override
    public void onAddMachineListener(String snList) {
        this.snList = snList;
        requestJobReportData(startDate, endDate, snList, selectSortIndex);
    }

    @Override
    public void onTableRowSelect(int index) {
        if (index < 0) return; //点击了总计或平均
        if (jobBeanList.size() > 0) {
            JobReportDto.JobBean jobBean = jobBeanList.get(index);
            PieDataVo vo = new PieDataVo(jobBean.workTime, jobBean.runningTime, jobBean.offLineTime);
            tableDataPieDialog.setParams(jobBean.name, vo);
            tableDataPieDialog.show(getChildFragmentManager(), null);
        }
    }

    @Override
    public void onCalendarRangeFinish(@Nullable String start, @Nullable String end) {
        if (start != null && end != null) {
            String start2EndTime = start + " - " + end;
            binding.tvSelectDate.setText(start2EndTime);
            startDate = start.replace(".", "-");
            endDate = end.replace(".", "-");
            requestJobReportData(startDate, endDate, snList, selectSortIndex);
        }
    }


}
