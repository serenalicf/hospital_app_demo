package com.serena.commonutil.exception;

import com.serena.commonutil.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "Custom Global Exception Class")
public class CustomException extends RuntimeException {

    @ApiModelProperty(value = "Exception status code")
    private Integer code;

    /**
     * Create exception object with status code and error message
     * @param message
     * @param code
     */
    public CustomException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * Accept enum type object
     * @param resultCodeEnum
     */
    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}