package com.serena.userservice.api;

import com.alibaba.fastjson.JSONObject;
import com.serena.commonutil.helper.JwtHelper;
import com.serena.commonutil.result.Result;
import com.serena.model.model.user.UserInfo;
import com.serena.userservice.service.UserService;
import com.serena.userservice.util.HttpClientUtil;
import com.serena.userservice.util.WeiXinPropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/c")
public class WeixinApiController {

    @Autowired
    private UserService userService;

    //1. generate QR code for wx, return required params for QR code
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result getQrConnectParams() {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("appid", WeiXinPropertiesUtil.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");
            String encodedUrl = URLEncoder.encode(WeiXinPropertiesUtil.WX_OPEN_REDIRECT_URL, "utf-8");
            map.put("redirect_url", encodedUrl);
            map.put("state", System.currentTimeMillis()+"");
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //callback after scanning wx QR code
    @GetMapping("callback")
    public String callback(String code, String state) {
        //obtain temporary code
        System.out.println("code:" + code);

        //request weixin address to get 2 values by code, weixin id, secret key
        // Exchange code for access_token
        //%s = occupyOperator
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(
                baseAccessTokenUrl.toString(),
                WeiXinPropertiesUtil.WX_OPEN_APP_ID,
                WeiXinPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        //use httpclient to call the api
        try {
            String accessTokenInfo = HttpClientUtil.get(accessTokenUrl);
            System.out.println("accessTokenInfo: " + accessTokenInfo);

            //extract openId, accessTokenInfo from accessTokenInfo string
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String accessToken  = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");

            //check if weixin user is saved in DB based on openId
            UserInfo userInfo = userService.selectWxInfoByOpenId(openId);
            if(userInfo == null) {
                //get user info by using weixin address accessToken , openId
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo"+
                        "?access_token=%s"+
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);
                String resultInfo = HttpClientUtil.get(userInfoUrl);
                System.out.println("resultInfo: " + resultInfo);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                String nickname = resultUserInfoJson.getString("nickname");
                String headimgurl = resultUserInfoJson.getString("headimgurl");

                //save user info into db
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openId);
                userInfo.setStatus(1);
                userService.save(userInfo);
            }


            //return name & token
            Map<String, Object> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);

            //for later checking if binding phone in front end,
            // if openId = "", not bind phone, else  bind
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);

            return "redirect:"+ WeiXinPropertiesUtil.WX_OPEN_BASE_URL +
                    "/weixin/callback?token="+map.get("token")+
                    "&openid="+map.get("openid")+"&name="+
                    URLEncoder.encode((String)map.get("name"));


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
