package com.serena.hospitalservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.serena.commonutil.result.Result;
import com.serena.hospitalservice.service.HospitalSettingService;
import com.serena.model.dto.hospital.HospitalSettingQueryDto;
import com.serena.model.model.hospital.HospitalSetting;
import com.serena.serviceutil.utils.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@Api(tags = "Hospital Setting Management")
@RestController
@RequestMapping("/admin/hospital/hospitalsetting")
@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSettingService hospitalSettingService;

    @ApiOperation(value = "Retrieve list of hospital setting")
    @GetMapping("findAll")
    public Result<List<HospitalSetting>> findAll(){
        List<HospitalSetting> list = hospitalSettingService.list();

        return Result.ok(list);
    }

    @ApiOperation(value = "Logical delete hospital setting")
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable int id){
        boolean isRemoved = hospitalSettingService.removeById(id);
        if(isRemoved){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "Search hospitals based on conditions")
    @PostMapping("searchPage/{currentPage}/{limit}")
    public Result searchPageHospitalSetting(@PathVariable int currentPage,
                                            @PathVariable int limit,
                                            @RequestBody(required = false) HospitalSettingQueryDto hospitalSettingQueryDto) {
        Page<HospitalSetting> page = new Page<>(currentPage, limit);

        QueryWrapper queryWrapper = new QueryWrapper<>();
        String hospitalName = hospitalSettingQueryDto.getHospitalName();
        String hospitalCode = hospitalSettingQueryDto.getHospitalCode();
        if(!StringUtils.isEmpty(hospitalName)){
            queryWrapper.like("hospital_name", hospitalName);

        }
        if(!StringUtils.isEmpty(hospitalCode)){
            queryWrapper.eq("hospital_code", hospitalCode);
        }
        Page<HospitalSetting> resultPage = hospitalSettingService.page(page, queryWrapper);
        return Result.ok(resultPage);
    }

    @ApiOperation(value = "Add hospital setting")
    @PostMapping("add")
    public Result addHospitalSetting(@RequestBody HospitalSetting hospitalSetting){
        hospitalSetting.setStatus(1); //not lock
        Random random = new Random();
        hospitalSetting.setSignKey( MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        boolean isAdded = hospitalSettingService.save(hospitalSetting);
        if(isAdded){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("get/{id}")
    public Result getHospitalSetting(@PathVariable int id){

        HospitalSetting hospitalSetting = hospitalSettingService.getById(id);
        return Result.ok(hospitalSetting);
    }

    @PostMapping("update")
    public Result updateHospitalSetting(@RequestBody HospitalSetting hospitalSetting){
        boolean isUpdated = hospitalSettingService.updateById(hospitalSetting);
        if(isUpdated){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        hospitalSettingService.removeByIds(ids);
        return Result.ok();
    }

    //lock and unlock
    @PutMapping("lock/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id, @PathVariable Integer status){
        HospitalSetting setting = hospitalSettingService.getById(id);
        setting.setStatus(status);
        hospitalSettingService.updateById(setting);
        return Result.ok();
    }

    //signature key
    @PutMapping("sendSignatureKey/{id}")
    public Result sendKey(@PathVariable Long id){
        HospitalSetting setting = hospitalSettingService.getById(id);
        String signatureKey = setting.getSignKey();
        String hospitalCode = setting.getHospitalCode();
        //TODO send sms
        return Result.ok();
    }

}
