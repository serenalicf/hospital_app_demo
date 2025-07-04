package com.serena.model.model.data;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Data
@ApiModel(description = "Data Dictionary")
@TableName("dictionary")
public class Dictionary  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "Create Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "Update Time")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "Logical Delete (1:Deleted, 0:Not Deleted)")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    @ApiModelProperty(value = "Additional Parameters")
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();

    @ApiModelProperty(value = "Parent ID")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "Name")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "Value")
    @TableField("value")
    private String value;

    @ApiModelProperty(value = "Code")
    @TableField("dictionary_code")
    private String dictionaryCode;

    @ApiModelProperty(value = "Has Children")
    @TableField(exist = false) // this field is needed in ElementUi tree component, but it doesn't exist in table which will throw exception, use exist = false
    private boolean hasChildren;

}