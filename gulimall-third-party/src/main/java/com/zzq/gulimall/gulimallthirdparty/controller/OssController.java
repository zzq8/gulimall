package com.zzq.gulimall.gulimallthirdparty.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.zzq.common.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OssController {

    @RequestMapping("/oss/policy")
    public R policy(){
        //...生成上传凭证，然后准备上传
        String accessKey = "_gHWJBizTe98WqI9wxMXMHjNQmzcVOZYssOLwq1I";
        String secretKey = "f2iesdbbxk_ul6sEtOQr6aGnkPg_rF66kQn1-6R2";
        String bucket = "gulimall-mytest";
        Map<String, String> respMap = new LinkedHashMap<String, String>();;
//        key: "", //图片名字处理

        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String host = "http://rgqxp85gm.hn-bkt.clouddn.com/";

        respMap.put("host",host);
        respMap.put("token",upToken);
        respMap.put("key",format);
        return R.ok().put("data",respMap);
    }
}
