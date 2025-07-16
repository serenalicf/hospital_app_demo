package com.serena.dataservice.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {
        List<UserData> list = new ArrayList();
        for(int i = 0;i < 10;i++) {
            UserData userData = new UserData();
            userData.setUserId(i);
            userData.setUserName("Lucy" + i);
            list.add(userData);
        }

        String filePath = "C:\\Users\\serenali\\Desktop\\test.xlsx";
        try {
            EasyExcel.write(filePath, UserData.class)
                    .sheet("UserInformation")
                    .doWrite(list);
        } catch(Exception  ex) {
        ex.printStackTrace();
        }

    }
}
