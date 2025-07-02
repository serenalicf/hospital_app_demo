package com.serena.model.model.hospital;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.serena.model.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "Hospital Setting")
@TableName("hospital_setting")
public class HospitalSetting extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Hospital Name")
	@TableField("hospital_name")
	private String hospitalName;

	@ApiModelProperty(value = "Hospital Code")
	@TableField("hospital_code")
	private String hospitalCode;

	@ApiModelProperty(value = "API Base URL")
	@TableField("api_url")
	private String apiUrl;

	@ApiModelProperty(value = "Signature Key")
	@TableField("sign_key")
	private String signKey;

	@ApiModelProperty(value = "Contact Person Name")
	@TableField("contact_name")
	private String contactName;

	@ApiModelProperty(value = "Contact Person Phone")
	@TableField("contact_phone")
	private String contactPhone;

	@ApiModelProperty(value = "Status")
	@TableField("status")
	private Integer status;
}