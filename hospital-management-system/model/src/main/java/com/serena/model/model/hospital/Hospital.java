package com.serena.model.model.hospital;

import com.alibaba.fastjson.JSONObject;
import com.serena.model.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@ApiModel(description = "Hospital")
@Document("Hospital")
public class Hospital extends BaseMongoEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Hospital Code")
	@Indexed(unique = true) // Unique index
	private String hospitalCode;

	@ApiModelProperty(value = "Hospital Name")
	@Indexed // Regular index
	private String hospitalName;

	@ApiModelProperty(value = "Hospital Type")
	private String hospitalType;

	@ApiModelProperty(value = "Province Code")
	private String provinceCode;

	@ApiModelProperty(value = "City Code")
	private String cityCode;

	@ApiModelProperty(value = "District Code")
	private String districtCode;

	@ApiModelProperty(value = "Detailed Address")
	private String address;

	@ApiModelProperty(value = "Hospital Logo")
	private String logoData;

	@ApiModelProperty(value = "Hospital Introduction")
	private String introduction;

	@ApiModelProperty(value = "Transportation Route")
	private String route;

	@ApiModelProperty(value = "Status 0: Offline 1: Online")
	private Integer status;

	// Booking Rule
	@ApiModelProperty(value = "Booking Rule")
	private BookingRule bookingRule;

	public void setBookingRule(String bookingRule) {
		this.bookingRule = JSONObject.parseObject(bookingRule, BookingRule.class);
	}
}