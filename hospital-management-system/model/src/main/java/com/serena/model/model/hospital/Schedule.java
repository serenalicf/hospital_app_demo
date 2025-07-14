package com.serena.model.model.hospital;

import com.serena.model.model.base.BaseMongoEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel(description = "Schedule")
@Document("Schedule")
public class Schedule extends BaseMongoEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Hospital Code")
	@Indexed // Regular index
	private String hospitalCode;

	@ApiModelProperty(value = "Department Code")
	@Indexed // Regular index
	private String departmentCode;

	@ApiModelProperty(value = "Title")
	private String title;

	@ApiModelProperty(value = "Doctor Name")
	private String doctorName;

	@ApiModelProperty(value = "Expertise")
	private String skill;

	@ApiModelProperty(value = "Work Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date workDate;

	@ApiModelProperty(value = "Work Time (0: Morning 1: Afternoon)")
	private Integer workTime;

	@ApiModelProperty(value = "Reserved Number")
	private Integer reservedNumber;

	@ApiModelProperty(value = "Available Number")
	private Integer availableNumber;

	@ApiModelProperty(value = "Registration Fee")
	private BigDecimal amount;

	@ApiModelProperty(value = "Schedule Status (-1: Cancelled 0: Closed 1: Available)")
	private Integer status;

	@ApiModelProperty(value = "Schedule ID (Hospital's own schedule primary key)")
	@Indexed // Regular index
	private String hospitalScheduleId;
}