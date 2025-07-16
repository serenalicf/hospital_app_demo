package com.serena.hospitalservice.service;

import com.alibaba.fastjson.JSONObject;
import com.serena.hospitalservice.repository.DepartmentRepository;
import com.serena.model.model.hospital.Department;
import com.serena.model.vo.hospital.DepartmentQueryVo;
import com.serena.model.vo.hospital.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<DepartmentVo> findDepartmentTree(String hospitalCode) {
        List<DepartmentVo> departmentVoList = new ArrayList<>();
        Department departmentQuery = new Department();
        departmentQuery.setHospitalCode(hospitalCode);
        Example<Department> example = Example.of(departmentQuery);
        List<Department> departmentList = departmentRepository.findAll(example);

        //categorize by majorDepartmentCode, then get its child departments
        Map<String, List<Department>> departmentMap = departmentList.stream().collect(Collectors.groupingBy(Department::getMajorDepartmentCode));
        //traverse departmentMap
        for(Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            String majorDepartmentCode = entry.getKey();
            List<Department> departmentList1 = entry.getValue();

            //handle major department
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepartmentCode(majorDepartmentCode);
            departmentVo.setDepartmentName(departmentList1.get(0).getDepartmentName());

            //handle child department
            List<DepartmentVo> children = new ArrayList<>();
            for(Department department : departmentList1) {
                DepartmentVo departmentVo1 = new DepartmentVo();
                departmentVo1.setDepartmentCode(department.getDepartmentCode());
                departmentVo1.setDepartmentName(department.getDepartmentName());
                children.add(departmentVo1);
            }
            //add children to major department
            departmentVo.setChildren(children);
            departmentVoList.add(departmentVo);
        }

        return departmentVoList;
    }
}
