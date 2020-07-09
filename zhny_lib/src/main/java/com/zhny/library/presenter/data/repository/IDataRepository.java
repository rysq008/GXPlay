package com.zhny.library.presenter.data.repository;


import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.model.dto.DataStatisticsDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;

import java.util.List;

import androidx.lifecycle.LiveData;


public interface IDataRepository {

    /**
     * 查询统计数据
     */
    LiveData<BaseDto<DataStatisticsDto>> getDataStatistics(String organizationId);


    /**
     * 查询作业信息汇总报表
     *
     * @param deviceFlag        是否添加设备
     * @param sortRule          排序规则  1 作业时长  2作业面积   3 转运时长   4 离线时长
     * @return                  data
     */
    LiveData<BaseDto<JobReportDto>> getJobReport(String organizationId,
                                                 String userId, String startDate,
                                                 String endDate, String snList,
                                                 boolean deviceFlag, int sortRule);

    /**
     * 获取设备数据（不分页）
     */
    LiveData<BaseDto<List<DataDeviceDto>>> getDataDevices(String organizationId, String userId);

    /**
     * 获取一段时间有作业的日期组
     */
    LiveData<BaseDto<List<String>>> getWorkDate(String organizationId,
                                                String startDate,
                                                String endDate);
}
