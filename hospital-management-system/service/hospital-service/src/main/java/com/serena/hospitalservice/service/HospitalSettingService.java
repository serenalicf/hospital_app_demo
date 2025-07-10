package com.serena.hospitalservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.serena.model.model.hospital.HospitalSetting;

public interface HospitalSettingService extends IService<HospitalSetting> {
    String getSignKey(String hospitalCode);
}

