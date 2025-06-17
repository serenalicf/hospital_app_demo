package com.serena.hospitalmanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.serena.hospitalmanagementsystem.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}