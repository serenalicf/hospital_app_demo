package com.serena.hospitalservice.service;

import com.serena.model.model.hospital.Hospital;

import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> switchedMap);

    Hospital getByHospitalCode(String hospitalCode);
}
