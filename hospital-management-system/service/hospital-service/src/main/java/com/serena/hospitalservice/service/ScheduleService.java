package com.serena.hospitalservice.service;

import com.serena.model.model.hospital.Schedule;
import com.serena.model.vo.hospital.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> paramMap);

    Page<Schedule> findScheduleList(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hospitalCode, String hospitalScheduleId);
}
