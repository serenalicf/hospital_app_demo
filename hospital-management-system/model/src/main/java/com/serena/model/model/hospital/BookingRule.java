package com.serena.model.model.hospital;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.List;


@Data
@ApiModel(description = "Booking Rule")
@Document("BookingRule")
public class BookingRule {

	@ApiModelProperty(value = "Appointment Cycle")
	private Integer cycle;

	@ApiModelProperty(value = "Release Time")
	private String releaseTime;

	@ApiModelProperty(value = "Stop Time")
	private String stopTime;

	@ApiModelProperty(value = "Cancellation Deadline Days (e.g.: -1 for one day before appointment, 0 for same day)")
	private Integer quitDay;

	@ApiModelProperty(value = "Cancellation Time")
	private String quitTime;

	@ApiModelProperty(value = "Booking Rules")
	private List<String> rule;

	/**
	 *
	 * @param rule
	 */
	public void setRule(String rule) {
		if(!StringUtils.isEmpty(rule)) {
			this.rule = JSONArray.parseArray(rule, String.class);
		}
	}
}