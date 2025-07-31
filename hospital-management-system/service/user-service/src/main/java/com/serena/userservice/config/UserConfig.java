package com.serena.userservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.serena.userservice.mapper")
public class UserConfig {
}
