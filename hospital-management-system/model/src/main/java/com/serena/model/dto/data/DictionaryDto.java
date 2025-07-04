package com.serena.model.dto.data;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * <p>
 * Dict
 * </p>
 *
 * @author qy
 */
@Data
public class DictionaryDto {

	@ExcelProperty(value = "id" ,index = 0)
	private Long id;

	@ExcelProperty(value = "parent id" ,index = 1)
	private Long parentId;

	@ExcelProperty(value = "name" ,index = 2)
	private String name;

	@ExcelProperty(value = "value" ,index = 3)
	private String value;

	@ExcelProperty(value = "dictionary code" ,index = 4)
	private String dictionaryCode;

}

