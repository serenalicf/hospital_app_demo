package com.serena.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.serena.commonutil.exception.CustomException;
import com.serena.commonutil.helper.JwtHelper;
import com.serena.commonutil.result.ResultCodeEnum;
import com.serena.model.model.acl.User;
import com.serena.model.model.user.UserInfo;
import com.serena.model.vo.user.LoginVo;
import com.serena.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo> implements UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        //get mobile no and otp from loginVo
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        // mobile no and otp empty?
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)){
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }

        //TODO consistent otp between sms and input otp
//        String redisCode = redisTemplate.opsForValue().get(phone);
//        if(!code.equals(redisCode)) {
//            throw new CustomException(ResultCodeEnum.CODE_ERROR);
//        }

        //using weixin login and binding phone number
        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoByOpenId(loginVo.getOpenid());
            if(null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new CustomException(ResultCodeEnum.DATA_ERROR);
            }
        }

        //if userInfo = null, normal phone login
        if(userInfo == null){
            //first time login by checking mobile no
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);

             userInfo = baseMapper.selectOne(queryWrapper);

            // if not first time, login
            if(userInfo == null){ //register
                userInfo = new UserInfo();
                userInfo.setPhone(phone);
                userInfo.setName("");
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
            if(userInfo.getStatus() == 0){
                throw new CustomException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
            }
        }

        //return username, JWT token
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)){
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)){
            name = userInfo.getPhone();
        }
        map.put("name", name);
        //TODO token
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);

        return map;
    }

    @Override
    public UserInfo selectWxInfoByOpenId(String openId) {
       QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("openid", openId);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        return userInfo;
    }
    
    @Override
    public boolean save(UserInfo userInfo) {
        return super.save(userInfo);
    }
}
