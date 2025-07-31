package com.serena.smsservice.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.utils.StringUtils;
import com.serena.smsservice.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public boolean send(String phone, String code) {
        //check phone is valid
        if(StringUtils.isEmpty(phone)) {
            return false;
        }
        //integrate aliyun sms service
        //set params
        DefaultProfile profile = DefaultProfile.getProfile(PropertiesUtil.REGION_ID,
                PropertiesUtil.ACCESS_KEY_ID, PropertiesUtil.SECRET);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();

        //fixed config for aliyun
        request.setMethod(MethodType.POST);
        //request.setProtocol(ProtocolType.HTTPS);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        //copy from your aliyun sms
        request.putQueryParameter("SignName", "hospitalSms");
        request.putQueryParameter("TemplateCode", "from aliyun");
        //otp code using Json format {"code":"123456"}
        Map<String, String> params = new HashMap();
        params.put("code", code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(params));

        //call method to send sms
        try {
            CommonResponse response = acsClient.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        }catch (ServerException se) {
            se.printStackTrace();
        }catch (ClientException ce) {
            ce.printStackTrace();
        }
        return false;
    }
}
