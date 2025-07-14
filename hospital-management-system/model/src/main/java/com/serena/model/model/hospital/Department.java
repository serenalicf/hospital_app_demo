package com.serena.model.model.hospital;

import com.serena.model.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@ApiModel(description = "Department")
@Document("Department")
public class Department extends BaseMongoEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Hospital Code")
	@Indexed // Regular index
	private String hospitalCode;

	@ApiModelProperty(value = "Department Code")
	@Indexed(unique = true) // Unique index
	private String departmentCode;

	@ApiModelProperty(value = "Department Name")
	private String departmentName;

	@ApiModelProperty(value = "Department Description")
	private String introduction;

	@ApiModelProperty(value = "Major Department Code")
	private String majorDepartmentCode;

	@ApiModelProperty(value = "Major Department Name")
	private String majorDepartmentName;
}