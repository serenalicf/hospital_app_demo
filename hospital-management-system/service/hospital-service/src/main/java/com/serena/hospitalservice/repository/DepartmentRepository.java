package com.serena.hospitalservice.repository;

import com.serena.model.model.hospital.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    Department getDepartmentByHospitalCodeAndDepartmentCode(String hospitalCode, String departmentCode);

    Department findByHospitalCodeAndDepartmentCode(String hospitalCode, String departmentCode);
}
