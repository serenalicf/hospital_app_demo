package com.serena.hospitalservice.controller;

import com.serena.commonutil.result.Result;
import com.serena.hospitalservice.service.DepartmentService;
import com.serena.model.vo.hospital.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/hospital/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "list all departments by hospital code")
    @GetMapping("getDepartmentList/{hospitalCode}")
    public Result getDepartmentList(@PathVariable String hospitalCode) {
        List<DepartmentVo> departmentList = departmentService.findDepartmentTree(hospitalCode);
        return Result.ok(departmentList);
    }


}
