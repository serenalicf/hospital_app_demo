package com.serena.userservice.service;

import com.serena.model.model.user.UserInfo;
import com.serena.model.vo.user.LoginVo;

import java.util.Map;

public interface UserService {
    Map<String, Object> login(LoginVo loginVo);

    UserInfo selectWxInfoByOpenId(String openId);
    
    boolean save(UserInfo userInfo);
}
