package com.zhny.library.presenter.data.util;

import com.amap.api.maps.model.LatLng;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.monitor.model.dto.SocketTrack;
import com.zhny.library.presenter.work.dto.PictureDto;

import java.util.ArrayList;
import java.util.List;

/**
 * created by liming
 */
public class TestData {


    //模拟统计数据
    @Deprecated
    public static JobReportDto testData(int count) {
        List<JobReportDto.JobBean> data = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            JobReportDto.JobBean bean = new JobReportDto.JobBean();
            bean.deviceId = i;
            bean.sn = "2020SN_" + i;
            bean.name = "测试数据_" + i;
            bean.area = 100.4 + 2 * i;
            bean.areaProportion = 0.3323f;
            bean.mileage = 100.432;
            bean.workTime = 1000 * 60 * i - i * 80 + 1000 * 60 * 90;
            bean.runningTime = 1000 * 60 * i - i * 80 + 1000 * 60 * 490;
            bean.offLineTime = 1000 * 60 * i - i * 80 + 1000 * 60 * 2000;
            bean.offLineTimeProportion = 0.3443f;
            bean.workTimeProportion = 0.34223f;
            bean.runningTimeProportion = 0.323443f;
            data.add(bean);
        }
        JobReportDto jobReportDto = new JobReportDto();
        jobReportDto.jobList = data;

        return jobReportDto;
    }

    //模拟websocket数据
    @Deprecated
    public static SocketTrack getSocketTrackData(int num, LatLng lastLatLng) {
        SocketTrack socketTrack = new SocketTrack();
        socketTrack.ifaceId = "1003";
        SocketTrack.ReportedBean reportedBean = new SocketTrack.ReportedBean();
        List<SocketTrack.ReportedBean.PosListBean> listBeans = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            SocketTrack.ReportedBean.PosListBean pos = new SocketTrack.ReportedBean.PosListBean();
            if (lastLatLng != null) {
                pos.latitude = "" + (lastLatLng.latitude + i *  0.00001);
                pos.longitude = "" + (lastLatLng.longitude + i * ( i > 15 ? 0.00001 : -0.00001));
            } else {
                pos.latitude = "" + (48.49059454101023 + i *  0.00001);
                pos.longitude = "" + (129.97174350168244 + i * ( i > 15 ? 0.00001 : -0.00001));
            }
            pos.speed = "" + (int) (100 * Math.random());
            listBeans.add(pos);
        }
        reportedBean.width = "" + (int) (100 * Math.random());
        reportedBean.posList = listBeans;
        socketTrack.reported = reportedBean;
        return socketTrack;
    }

    //模拟照片数据
    @Deprecated
    public static List<PictureDto> getPicture(int num) {
        List<PictureDto> data = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            PictureDto pictureDto = new PictureDto();
            List<PictureDto.ImgBean> imgBeans = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                PictureDto.ImgBean imgBean = new PictureDto.ImgBean();
                imgBean.url = "http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg";
                imgBean.latitude = 48.49059454101023 + i + j;
                imgBean.longitude = 129.97174350168244 + i + j;
                imgBeans.add(imgBean);
            }
            pictureDto.imgs = imgBeans;
            data.add(pictureDto);
        }
        return data;
    }
}
