package com.serena.model.dto.hospital;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HospitalSettingQueryDto {

    @ApiModelProperty(value = "Hospital Name")
    private String hospitalName;

    @ApiModelProperty(value = "Hospital Code")
    private String hospitalCode;
}