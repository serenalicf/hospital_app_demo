package com.serena.hospitalservice.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.serena.hospitalservice.mapper.HospitalSetMapper;
import com.serena.model.model.hospital.HospitalSetting;
import org.springframework.stereotype.Service;

@Service
public class HospitalSettingServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSetting>implements HospitalSettingService {



}
