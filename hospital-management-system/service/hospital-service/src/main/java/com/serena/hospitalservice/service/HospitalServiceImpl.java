package com.serena.hospitalservice.service;

import com.alibaba.fastjson.JSONObject;
import com.serena.hospitalservice.repository.HospitalRepository;
import com.serena.model.model.hospital.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //convert param map to Hospital entity
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // check if exist in MongoDb
        String hospitalCode = hospital.getHospitalCode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHospitalCode(hospitalCode);

        //if not exist, save in db
        if(hospitalExist != null) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(hospitalExist.getUpdateTime());
            hospital.setIsDeleted(0);

        } else {
            //if exist, update it
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }
        hospitalRepository.save(hospital);

    }

    @Override
    public Hospital getByHospitalCode(String hospitalCode) {
        Hospital hospital = hospitalRepository.getHospitalByHospitalCode(hospitalCode);
        return hospital;
    }
}
