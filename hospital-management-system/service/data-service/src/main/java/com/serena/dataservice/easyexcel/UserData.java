package com.serena.dataservice.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserData {
    @ExcelProperty(value = "user_id", index = 0)
    private int userId;

    @ExcelProperty(value = "user_name", index = 1)
    private String userName;


}
