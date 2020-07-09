package com.zhny.library.presenter.data.util;

import android.text.TextUtils;

import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.model.vo.CustomTableData;
import com.zhny.library.presenter.data.model.vo.DataDetailVo;
import com.zhny.library.utils.DataUtil;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by liming
 */
public class TableDataHelper {


    //封装表格数据
    public static CustomTableData ecTableData(JobReportDto jobReportDto) {
        List<CustomTableData.Column> leftList = new ArrayList<>();
        List<List<CustomTableData.Column>> dataList = new ArrayList<>();
        if (jobReportDto == null || jobReportDto.jobList == null) {
            return new CustomTableData(leftList, dataList);
        }
        List<CustomTableData.Column> areaCol  = new ArrayList<>();
        List<CustomTableData.Column> areaProportionCol = new ArrayList<>();
        List<CustomTableData.Column> mileageCol = new ArrayList<>();
        List<CustomTableData.Column> workTimeCol = new ArrayList<>();
        List<CustomTableData.Column> runningTimeCol = new ArrayList<>();
        List<CustomTableData.Column> offLineTimeCol = new ArrayList<>();
        List<CustomTableData.Column> workTimeProportionCol = new ArrayList<>();
        List<CustomTableData.Column> runningTimeProportionCol = new ArrayList<>();
        List<CustomTableData.Column> offLineTimeProportionCol = new ArrayList<>();

        long workTime = 0, runningTime = 0, offLineTime = 0;
        double area = 0, mileage = 0;
        int size = jobReportDto.jobList.size();
        if (size > 10) size = 10;
        for (int i = 0; i < size; i++) {
            JobReportDto.JobBean jobBean = jobReportDto.jobList.get(i);
            //设置第一列数据
            CustomTableData.Column column = new CustomTableData.Column(i, jobBean.name);
            leftList.add(column);
            //设置数据列
            areaCol.add(new CustomTableData.Column(i, DataUtil.get2Point(jobBean.area) + "亩"));//作业面积
            areaProportionCol.add(new CustomTableData.Column(i, DataUtil.get2Point(jobBean.area / jobBean.sumArea * 100) + "%"));//作业面积占比
            mileageCol.add(new CustomTableData.Column(i, DataUtil.get2Point(jobBean.mileage) + "km"));//行驶里程
            workTimeCol.add(new CustomTableData.Column(i, TimeUtils.timeStamp2Hm(jobBean.workTime)));//作业时长
            runningTimeCol.add(new CustomTableData.Column(i, TimeUtils.timeStamp2Hm(jobBean.runningTime)));//转运时长
            offLineTimeCol.add(new CustomTableData.Column(i, TimeUtils.timeStamp2Hm(jobBean.offLineTime)));//离线时长
            workTimeProportionCol.add(new CustomTableData.Column(i, DataUtil.get2Point(jobBean.workTimeProportion * 100) + "%"));//作业时间占比
            runningTimeProportionCol.add(new CustomTableData.Column(i, DataUtil.get2Point(jobBean.runningTimeProportion * 100) + "%"));//转运时间占比
            offLineTimeProportionCol.add(new CustomTableData.Column(i, DataUtil.get2Point(jobBean.offLineTimeProportion * 100) + "%"));//离线时间占比

            workTime += jobBean.workTime;
            runningTime += jobBean.runningTime;
            offLineTime += jobBean.offLineTime;
            area += jobBean.area;
            mileage += jobBean.mileage;
        }
        if (size != 0) {
            long allTime = workTime + runningTime + offLineTime;
            //总计
            areaCol.add(new CustomTableData.Column(-1, DataUtil.get2Point(area) + "亩"));
            areaProportionCol.add(new CustomTableData.Column(-1, "100%"));
            mileageCol.add(new CustomTableData.Column(-1, DataUtil.get2Point(mileage) + "km"));
            workTimeCol.add(new CustomTableData.Column(-1, TimeUtils.timeStamp2Hm(workTime)));
            runningTimeCol.add(new CustomTableData.Column(-1, TimeUtils.timeStamp2Hm(runningTime)));
            offLineTimeCol.add(new CustomTableData.Column(-1, TimeUtils.timeStamp2Hm(offLineTime)));
            workTimeProportionCol.add(new CustomTableData.Column(-1, allTime == 0 ? "0" : DataUtil.get2Point(workTime * 100f / allTime) + "%"));
            runningTimeProportionCol.add(new CustomTableData.Column(-1, allTime == 0 ? "0" : DataUtil.get2Point(runningTime * 100f / allTime) + "%"));
            offLineTimeProportionCol.add(new CustomTableData.Column(-1, allTime == 0 ? "0" : DataUtil.get2Point(offLineTime * 100f / allTime) + "%"));
            leftList.add(new CustomTableData.Column(-1, "总计"));
            //平均
            areaCol.add(new CustomTableData.Column(-2, DataUtil.get2Point(area / size) + "亩"));
            areaProportionCol.add(new CustomTableData.Column(-2, DataUtil.get2Point(area / size / area * 100) + "%"));
            mileageCol.add(new CustomTableData.Column(-2, DataUtil.get2Point(mileage / size) + "km"));
            workTimeCol.add(new CustomTableData.Column(-2, TimeUtils.timeStamp2Hm(workTime / size)));
            runningTimeCol.add(new CustomTableData.Column(-2, TimeUtils.timeStamp2Hm(runningTime / size)));
            offLineTimeCol.add(new CustomTableData.Column(-2, TimeUtils.timeStamp2Hm(offLineTime / size)));
            workTimeProportionCol.add(new CustomTableData.Column(-2, allTime == 0 ? "0" : DataUtil.get2Point(workTime * 100f / allTime) + "%"));
            runningTimeProportionCol.add(new CustomTableData.Column(-2, allTime == 0 ? "0" : DataUtil.get2Point(runningTime * 100f / allTime) + "%"));
            offLineTimeProportionCol.add(new CustomTableData.Column(-2, allTime == 0 ? "0" : DataUtil.get2Point(offLineTime * 100f / allTime) + "%"));
            leftList.add(new CustomTableData.Column(-2, "平均"));
        }

        //添加到数据集合
        dataList.add(areaCol);
        dataList.add(areaProportionCol);
        dataList.add(mileageCol);
        dataList.add(workTimeCol);
        dataList.add(runningTimeCol);
        dataList.add(offLineTimeCol);
        dataList.add(workTimeProportionCol);
        dataList.add(runningTimeProportionCol);
        dataList.add(offLineTimeProportionCol);

        CustomTableData tableData = new CustomTableData();
        tableData.firstColumn = leftList;
        tableData.dataColumn = dataList;
        return tableData;
    }


    //获取sn号
    public static String getDevicesSn(List<DataDeviceDto> data) {
        if (data == null || data.isEmpty()) return null;
        StringBuilder snStr = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            DataDeviceDto dto = data.get(i);
            if (dto.checkType == 1) {
                snStr.append(dto.sn).append(",");
            }
        }
        if (TextUtils.isEmpty(snStr)) return null;
        return snStr.toString().substring(0, snStr.length() - 1);
    }


    //数据转换 -> 数据明细界面
    public static List<DataDetailVo> getDataDetail(List<JobReportDto.JobBean> jobList, boolean isToday) {
        List<DataDetailVo> data = new ArrayList<>(jobList.size());
        for (JobReportDto.JobBean jobBean : jobList) {
            DataDetailVo vo = new DataDetailVo();
            vo.deviceId = jobBean.deviceId;
            vo.name = jobBean.name;
            vo.sn = jobBean.sn;
            vo.productType = jobBean.productType;
            vo.imgUrl = jobBean.imgUrl;
            vo.areaStr = DataUtil.get2Point(jobBean.area) + "亩";
            vo.mileageStr = DataUtil.get2Point(jobBean.mileage) + "km";
            vo.workTime = TimeUtils.timeStamp2Hm(jobBean.workTime);
            vo.runningTime = isToday ? "-" : TimeUtils.timeStamp2Hm(jobBean.runningTime);
            vo.offLineTime = isToday ? "-" : TimeUtils.timeStamp2Hm(jobBean.offLineTime);
            data.add(vo);
        }
        return data;
    }


    //获取snList
    public static List<String> getDevicesSnList(List<JobReportDto.JobBean> jobBeanList) {
        List<String> snList = new ArrayList<>();
        if (jobBeanList == null) return snList;
        for (JobReportDto.JobBean jobBean : jobBeanList) {
            snList.add(jobBean.sn);
        }
        return snList;
    }
}
