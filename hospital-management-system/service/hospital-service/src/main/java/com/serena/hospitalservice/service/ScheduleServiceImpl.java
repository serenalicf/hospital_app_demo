package com.serena.hospitalservice.service;

import com.alibaba.fastjson.JSONObject;
import com.serena.hospitalservice.repository.ScheduleRepository;
import com.serena.model.model.hospital.Schedule;
import com.serena.model.vo.hospital.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String paramMapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramMapString, Schedule.class);

        Schedule scheduleExist = scheduleRepository.getScheduleByHospitalCodeAndHospitalScheduleId(schedule.getHospitalCode(), schedule.getHospitalScheduleId());

        if(scheduleExist != null) {
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(1);
            scheduleRepository.save(scheduleExist);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findScheduleList(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        //create pageable, set current page and page size, 0 is first page
        Pageable pageable = PageRequest.of(page-1, limit);

        Schedule schedule = new Schedule();
        //convert vo to department
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);

        //create example
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matcher);

        Page<Schedule> scheduleList = scheduleRepository.findAll(example, pageable);
        return scheduleList;
    }

    @Override
    public void remove(String hospitalCode, String hospitalScheduleId) {
        Schedule schedule = scheduleRepository.findByHospitalCodeAndHospitalScheduleId(hospitalCode, hospitalScheduleId);

        if(schedule != null) {
            //department.setIsDeleted(1);
            scheduleRepository.deleteById(Integer.parseInt(schedule.getId()));
        }
    }
}
