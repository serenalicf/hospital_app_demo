package com.serena.hospitalservice.service;

import com.alibaba.fastjson.JSONObject;
import com.serena.dataclientservice.feign.DictionaryFeignClient;
import com.serena.hospitalservice.repository.HospitalRepository;
import com.serena.model.model.hospital.Hospital;
import com.serena.model.vo.hospital.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictionaryFeignClient dictionaryFeignClient;

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

    @Override
    public Page<Hospital> selectHospital(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page-1, limit);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        Example<Hospital> example = Example.of(hospital, matcher);

        Page<Hospital> hospitalPages = hospitalRepository.findAll(example, pageable);

        hospitalPages.getContent().stream()
                .forEach(item -> {
                    this.setHospitalInfo(item);
                });

        return hospitalPages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> getHospitalById(String id) {
        Hospital hospital = this.setHospitalInfo(hospitalRepository.findById(id).get());
        Map<String, Object> result = new HashMap<>();
    //hospital basic infor (include hospital grade)
        result.put("hospital", hospital);
        result.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return result;
    }

    private Hospital setHospitalInfo(Hospital hospital) {
        //get hospitalType based on dictionaryCode & value
        String hospitalType = dictionaryFeignClient.getName("HospitalType", hospital.getHospitalType());

        //find province , city , district
        String province = dictionaryFeignClient.getName(hospital.getProvinceCode());
        String city = dictionaryFeignClient.getName(hospital.getCityCode());
        String district = dictionaryFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hospitalType", hospitalType);
        hospital.getParam().put("address", province + ", " + city + ", " + district);

        return hospital;
    }
}
