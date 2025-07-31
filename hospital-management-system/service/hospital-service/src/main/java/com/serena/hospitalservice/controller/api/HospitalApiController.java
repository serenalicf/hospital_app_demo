package com.serena.hospitalservice.controller.api;

import com.serena.commonutil.result.Result;
import com.serena.hospitalservice.service.DepartmentService;
import com.serena.hospitalservice.service.HospitalService;
import com.serena.model.model.hospital.Hospital;
import com.serena.model.vo.hospital.DepartmentVo;
import com.serena.model.vo.hospital.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hospital/hospital")
public class HospitalApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "query hospital list")
    @GetMapping("findHospitalList/{page}/{limit}")
    public Result findHospitalList(@PathVariable Integer page,
                                   @PathVariable Integer limit,
                                   HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.selectHospital(page, limit, hospitalQueryVo);
        return Result.ok(hospitalPage);
    }

    @ApiOperation(value = "search by hospital name")
    @GetMapping("findByHospitalName/{hospitalName}")
    public Result findByHospitalName(@PathVariable String hospitalName) {
       List<Hospital> list =  hospitalService.findByHospitalName(hospitalName);

       return Result.ok(list);
    }

    @ApiOperation(value = "get department list by hospitalCode")
    @GetMapping("department/{hospitalCode}")
    public Result getDepartmentList(@PathVariable String hospitalCode) {
        List<DepartmentVo> list =  departmentService.findDepartmentTree(hospitalCode);

        return Result.ok(list);
    }

    @ApiOperation(value = "get detail of hospital e.g.appointment")
    @GetMapping("getDetail/{hospitalCode}")
    public Result getAppointmentDetail(@PathVariable String hospitalCode) {
        Map<String, Object> list =  hospitalService.getAppointmentDetail(hospitalCode);

        return Result.ok(list);
    }
}
