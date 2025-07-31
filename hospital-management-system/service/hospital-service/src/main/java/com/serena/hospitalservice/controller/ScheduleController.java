package com.serena.hospitalservice.controller;

import com.serena.commonutil.result.Result;
import com.serena.hospitalservice.service.ScheduleService;
import com.serena.model.model.hospital.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hospital/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "Search data of schedule rule with pagination")
    @GetMapping("getScheduleRule/{page}/{limit}/{hospitalCode}/{departmentCode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hospitalCode,
                                  @PathVariable String departmentCode) {


        Map<String, Object> map = scheduleService.getScheduleRule(page, limit, hospitalCode, departmentCode);

        return Result.ok(map);
    }

    // search schedule detailed info based on hospitalCode, departmentCode, workDate
    @ApiOperation(value = "query schedule detail")
    @GetMapping("getScheduleDetail/{hospitalCode}/{departmentCode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hospitalCode,
                                    @PathVariable String departmentCode,
                                    @PathVariable String workDate) {

        List<Schedule> scheduleList = scheduleService.getScheduleDetail(hospitalCode, departmentCode, workDate);
        return Result.ok(scheduleList);
    }


}
