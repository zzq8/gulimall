package com.zzq.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzq.common.utils.HttpUtils;
import com.zzq.common.utils.R;
import com.zzq.common.vo.MemberResponseVo;
import com.zzq.gulimall.auth.feign.MemberFeignService;
import com.zzq.gulimall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.zzq.common.constant.AuthServerConstant.LOGIN_USER;

@Slf4j
@Controller
public class Oauth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;


    @GetMapping(value = "/oauth2.0/github/success")
    public String github(@RequestParam("code") String code, HttpSession session) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("client_id", "47ac420d2ea7af6f5409");
        map.put("client_secret", "d751aca558b6a05ac83c4b75fee924c2aed26c0e");
        map.put("code", code);

        Map<String, String> heads = new HashMap<>();
        heads.put("Accept", "application/json");

        //1、根据用户授权返回的code换取access_token
        HttpResponse response = HttpUtils.doPost("https://github.com", "/login/oauth/access_token", "post", heads, map, new HashMap<>());

        //2、处理
        if (response.getStatusLine().getStatusCode() == 200) {
            //xxx获取到了access_token,转为通用社交登录对象
            String json = EntityUtils.toString(response.getEntity());
            //String json = JSON.toJSONString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);


            //3、查询当前社交用户的社交账号信息（昵称、性别等）
            Map<String, String> heads2 = new HashMap<>();
            heads.put("Authorization", socialUser.getToken_type() + " " + socialUser.getAccess_token());
            HttpResponse response2 = HttpUtils.doGet("https://api.github.com", "/user", "get", heads, new HashMap<String, String>());
            String user = EntityUtils.toString((response2.getEntity()));

            // 创建 ObjectMapper 对象
            ObjectMapper mapper = new ObjectMapper();
            // 将 JSON 字符串转换为 JsonNode 对象
            JsonNode jsonNode = mapper.readTree(user);
            // 获取指定 key 的 value
            String id = jsonNode.get("id").asText();
            socialUser.setSocial_uid(id);

            //知道了哪个社交用户
            //1）、当前用户如果是第一次进网站，自动注册进来（为当前社交用户生成一个会员信息，以后这个社交账号就对应指定的会员）
            //登录或者注册这个社交用户
            System.out.println(socialUser.getAccess_token());
            //调用远程服务
            R oauthLogin = memberFeignService.oauthLogin(socialUser);
            if (oauthLogin.getCode() == 0) {
                MemberResponseVo data = oauthLogin.getData("data", new TypeReference<MemberResponseVo>() {
                });
                log.info("登录成功：用户信息：{}", data.toString());

                //1、第一次使用session，命令浏览器保存卡号，JSESSIONID这个cookie
                //以后浏览器访问哪个网站就会带上这个网站的cookie
                //TODO 1、默认发的令牌。当前域（解决子域session共享问题）
                //TODO 2、使用JSON的序列化方式来序列化对象到Redis中
                session.setAttribute(LOGIN_USER, data);

                //2、登录成功跳回首页
                return "redirect:http://gulimall.com";
            } else {

                return "redirect:http://auth.gulimall.com/login.html";
            }

        } else {
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }


}
