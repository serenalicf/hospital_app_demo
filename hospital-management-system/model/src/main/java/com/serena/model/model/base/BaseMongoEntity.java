package com.serena.model.model.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseMongoEntity implements Serializable {

    @ApiModelProperty(value = "ID")
    @Id
    private String id;

    @ApiModelProperty(value = "Create Time")
    private Date createTime;

    @ApiModelProperty(value = "Update Time")
    private Date updateTime;

    @ApiModelProperty(value = "Logical Delete (1:Deleted, 0:Not Deleted)")
    private Integer isDeleted;

    @ApiModelProperty(value = "Additional Parameters")
    @Transient //Fields with this annotation will not be stored in the database. Only used as regular javaBean properties
    private Map<String,Object> param = new HashMap<>();
}