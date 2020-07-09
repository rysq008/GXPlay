package com.zhny.library.presenter.work.util;

import com.zhny.library.presenter.work.dto.WorkDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkUtil {

    public static Map<String, List<WorkDto>> groupListByYear(List<WorkDto> WorkDtos) {
        Map<String, List<WorkDto>> map = new HashMap<>();
        for (WorkDto WorkDto : WorkDtos) {
            List<WorkDto> tmpList = map.get(WorkDto.startYear);
            if (tmpList == null) {
                tmpList = new ArrayList<>();
                tmpList.add(WorkDto);
                map.put(WorkDto.startYear, tmpList);
            } else {
                tmpList.add(WorkDto);
            }
        }
        return map;
    }

    public static List<WorkDto> sortWorkByStartTime(List<WorkDto> workDtos) {
        if (workDtos == null || workDtos.size() == 0) {
            return null;
        }
        Collections.sort(workDtos, new Comparator<WorkDto>() {
            @Override
            public int compare(WorkDto o1, WorkDto o2) {
                //降序
                return o2.startTime.compareTo(o1.startTime);
            }
        });
        return workDtos;
    }




}
