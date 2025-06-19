package com.serena.hospitalmanagementsystem.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

//this help implement Auto-fill fields
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    //when mybatis insert, this method executes
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("version", 1, metaObject);
        this.setFieldValByName("isDeleted", 0, metaObject);
    }

    //when mybatis update, this method executes
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
