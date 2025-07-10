package com.serena.hospitalservice.controller.api;

import com.serena.commonutil.exception.CustomException;
import com.serena.commonutil.result.Result;
import com.serena.commonutil.result.ResultCodeEnum;
import com.serena.hospitalservice.service.HospitalService;
import com.serena.hospitalservice.service.HospitalSettingService;
import com.serena.model.model.hospital.Hospital;
import com.serena.serviceutil.helper.HttpRequestHelper;
import com.serena.serviceutil.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/hospital")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSettingService hospitalSettingService;

    //upload hospital
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        //retrieve hospital info from response
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hospitalSign = (String) paramMap.get("sign"); //M5 encrypted

        String hospitalCode = (String) paramMap.get("hospitalCode");

        String signKey = hospitalSettingService.getSignKey(hospitalCode);

        String encryptedKey = MD5.encrypt(signKey);

        if (!hospitalSign.equals(encryptedKey)) {
            throw new CustomException(ResultCodeEnum.SIGN_ERROR);
        }

        // logo img is encoded by base 64, + is converted to space, need to convert back
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

        hospitalService.save(paramMap);
        return Result.ok();

    }

    @PostMapping("show")
    public Result getHospital(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hospitalCode = (String) paramMap.get("hospitalCode");

        String hospitalSign = (String) paramMap.get("sign"); //M5 encrypted
        String signKey = hospitalSettingService.getSignKey(hospitalCode);

        String encryptedKey = MD5.encrypt(signKey);

        if (!hospitalSign.equals(encryptedKey)) {
            throw new CustomException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getByHospitalCode(hospitalCode);
        return Result.ok(hospital);
    }

}
