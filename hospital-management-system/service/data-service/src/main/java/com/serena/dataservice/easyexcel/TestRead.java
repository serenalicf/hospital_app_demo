package com.serena.dataservice.easyexcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {

    public static void main(String[] args) {
        String fileDestination = "C:\\Users\\serenali\\Desktop\\test.xlsx";
        EasyExcel.read(fileDestination, UserData.class, new ExcelListener()).sheet().doRead();
    }
}
