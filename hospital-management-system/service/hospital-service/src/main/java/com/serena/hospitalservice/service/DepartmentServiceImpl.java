package com.serena.hospitalservice.service;

import com.alibaba.fastjson.JSONObject;
import com.serena.hospitalservice.repository.DepartmentRepository;
import com.serena.model.model.hospital.Department;
import com.serena.model.vo.hospital.DepartmentQueryVo;
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
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString, Department.class);

        Department departmentExist = departmentRepository.getDepartmentByHospitalCodeAndDepartmentCode(department.getHospitalCode(), department.getDepartmentCode());

        if(departmentExist != null) {
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }

    }

    @Override
    public Page<Department> findDepartmentList(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //create pageable, set current page and page size, 0 is first page
        Pageable pageable = PageRequest.of(page-1, limit);

        Department department = new Department();
        //convert vo to department
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        //create example
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);

        Page<Department> departmentList = departmentRepository.findAll(example, pageable);
        return departmentList;
    }

    @Override
    public void remove(String hospitalCode, String departmentCode) {
        Department department = departmentRepository.findByHospitalCodeAndDepartmentCode(hospitalCode, departmentCode);

        if(department != null) {
            //department.setIsDeleted(1);
            departmentRepository.deleteById(department.getId());
        }
    }
}
