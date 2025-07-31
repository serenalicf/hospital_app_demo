package com.serena.smsservice.controller;

import com.serena.commonutil.result.Result;
import com.serena.smsservice.service.SmsService;
import com.serena.smsservice.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
public class SmsApiController {
    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable("phone") String phone) {
        //check code in Redis
        String code = redisTemplate.opsForValue().get(phone);

        if(!StringUtils.isEmpty(code)) {
            return Result.ok();
        }

        //if not exist in redis, generate new code,
        code = RandomUtil.getSixBitRandom();

        // send to aliyun and it will send sms
        boolean isSend = smsService.send(phone, code);

        //store new code in redis, set expired time
        if(isSend) {
            redisTemplate.opsForValue().set(phone, code, 2, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail().message("Sms sent fail");
        }

    }
}
