package com.serena.hospitalservice.controller;

import com.serena.commonutil.result.Result;
import com.serena.hospitalservice.service.HospitalService;
import com.serena.model.model.hospital.Hospital;
import com.serena.model.vo.hospital.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/hospital/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //conditional query
    @GetMapping("list/{page}/{limit}")
    public Result listHospital(@PathVariable Integer page, @PathVariable Integer limit, HospitalQueryVo hospitalQueryVo) {

        Page<Hospital> hospitalPage = hospitalService.selectHospital(page, limit, hospitalQueryVo);
        return Result.ok(hospitalPage);
    }

    @ApiOperation(value = "update status of hospitaal - online/ offline")
    @GetMapping("updateHospitalStatus/{id}/{status}")
    public Result updateHospitalStatus(@PathVariable String id, @PathVariable Integer status) {
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation(value = "Hospitail detailed information")
    @GetMapping("showHospitalDetail/{id}")
    public Result showHospital(@PathVariable String id) {
        Map<String, Object> map = hospitalService.getHospitalById(id);
        return Result.ok(map);
    }

}
