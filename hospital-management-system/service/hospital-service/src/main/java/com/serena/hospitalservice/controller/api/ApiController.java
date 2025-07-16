package com.serena.hospitalservice.controller.api;

import com.serena.commonutil.exception.CustomException;
import com.serena.commonutil.result.Result;
import com.serena.commonutil.result.ResultCodeEnum;
import com.serena.hospitalservice.service.DepartmentService;
import com.serena.hospitalservice.service.HospitalService;
import com.serena.hospitalservice.service.HospitalSettingService;
import com.serena.hospitalservice.service.ScheduleService;
import com.serena.model.model.hospital.Department;
import com.serena.model.model.hospital.Hospital;
import com.serena.model.model.hospital.Schedule;
import com.serena.model.vo.hospital.DepartmentQueryVo;
import com.serena.model.vo.hospital.ScheduleQueryVo;
import com.serena.serviceutil.helper.HttpRequestHelper;
import com.serena.serviceutil.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
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

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //upload hospital
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        //retrieve hospital info from response
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        verifySignKey(paramMap, hospitalSettingService);

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

        verifySignKey(paramMap, hospitalSettingService);
        Hospital hospital = hospitalService.getByHospitalCode(hospitalCode);
        return Result.ok(hospital);
    }
    
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        verifySignKey(paramMap, hospitalSettingService);

        departmentService.save(paramMap);
        return Result.ok();
    }

    private static void verifySignKey(Map<String, Object> paramMap, HospitalSettingService hospitalSettingService) {
        String hospitalSign = (String) paramMap.get("sign");

        String hospitalCode = (String) paramMap.get("hospitalCode");

        String signKey = hospitalSettingService.getSignKey(hospitalCode);

        String encryptedKey = MD5.encrypt(signKey);

        if (!hospitalSign.equals(encryptedKey)) {
            throw new CustomException(ResultCodeEnum.SIGN_ERROR);
        }
    }

    @PostMapping("/department/list")
    public Result findDepartment(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hospitalCode = (String) paramMap.get("hospitalCode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        verifySignKey(paramMap, hospitalSettingService);

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHospitalCode(hospitalCode);
        Page<Department> departmentPage = departmentService.findDepartmentList(page, limit, departmentQueryVo);

        return Result.ok(departmentPage);

    }

    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hospitalCode = (String) paramMap.get("hospitalCode");
        String departmentCode = (String) paramMap.get("departmentCode");

        verifySignKey(paramMap, hospitalSettingService);

        departmentService.remove(hospitalCode, departmentCode);

        return Result.ok();
    }

    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        verifySignKey(paramMap, hospitalSettingService);
        scheduleService.save(paramMap);
        return Result.ok();
    }

    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hospitalCode = (String) paramMap.get("hospitalCode");
        String departmentCode = (String) paramMap.get("departmentCode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        verifySignKey(paramMap, hospitalSettingService);

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHospitalCode(hospitalCode);
        scheduleQueryVo.setDepartmentCode(departmentCode);
        Page<Schedule> schedulePage = scheduleService.findScheduleList(page, limit, scheduleQueryVo);

        return Result.ok(schedulePage);
    }

    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hospitalCode = (String) paramMap.get("hospitalCode");
        String hospitalScheduleId = (String) paramMap.get("hospitalScheduleId");

        verifySignKey(paramMap, hospitalSettingService);

        scheduleService.remove(hospitalCode, hospitalScheduleId);

        return Result.ok();
    }

}
