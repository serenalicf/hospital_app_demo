package com.serena.hospitalservice.service;

import com.serena.model.model.hospital.Department;
import com.serena.model.vo.hospital.DepartmentQueryVo;
import com.serena.model.vo.hospital.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    Page<Department> findDepartmentList(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hospitalCode, String departmentCode);

    List<DepartmentVo> findDepartmentTree(String hospitalCode);
}
