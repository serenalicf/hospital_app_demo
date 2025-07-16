package com.serena.hospitalservice.service;

import com.serena.model.model.hospital.Hospital;
import com.serena.model.vo.hospital.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> switchedMap);

    Hospital getByHospitalCode(String hospitalCode);

    Page<Hospital> selectHospital(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> getHospitalById(String id);
}
