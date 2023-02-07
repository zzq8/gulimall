package com.zzq.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zzq.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public String app_id;

    // 商户私钥，您的PKCS8格式RSA2私钥
    public String merchant_private_key="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQChRohud5nXGoxATaRRocicEKHFkB8FIncYjQlRibZORB3j0LK0O9bh+hqnRnzYHtQM4rzryLDV51qusn42552nQdpDq7JKP0Ttb7jE5MLsbsYfPt/uUlRbn4roBugPeRZGpWjJvtgErP6pn14p0MFlJu28vhFB6UI4lwXnhvnbmgSwn4DcSF19/sOka1mXLelvC1aV4rs1mxovWc2Gy/MDK+Ljr9DqKg/KG4drji2auPSi9eApFKxYtH9Q+ha7AvYtcXNhoAsYQXjPA45ik9rZh3Ld18O8MqnfOoOEz1W8rRrIyNFHrHJ9ziC1yD32r4htdnRkYsRRzWc1ah449nvRAgMBAAECggEAYLw77XbT0hBce92WaFiQSfw37JbL5HqN0S4Xns7piPyWqSVva2hRNe6cuiUvkTq8mpYOF6ejWkqRPCRYTHqVN9PqJZkBMkPNBlZzD7bfrI2tTOSb0QmVC/ggi5hI7a8Fy44/0nMkg3l4rymmg5JxzkiRZRrkoxJU5DjCE+QETtGY0NdsYw4bY4U6D9RZt+eAxbMgWRgVRDGz9jmXErYsPr/9+1s2kjj8crRBoWfoW/UYt6DTqE5J15r9uEcI1UTQP0dn80U0i/ukxlvOsTRTIdidQWYvTa5ogVSG3Y6fozyScFMtSGKQNNyYA5aOzgWbo36w3eomUQzhp6odnEDzeQKBgQDcz5FV/k/fYBcvKPbO23k9QvDivSWN02UPlSXcMYK9taXCB5poeQghTg9Zg1gaQ47bEjKcdIl+OtkWPFlOD2rNoWIZksERnVoZ64/Alw/AA3IYi9ZxgS+65OwK7Kv7jYsGOafvQPKrMBS0r22RcdgJ/mWFHnsmJYmWORPbQs3nxwKBgQC6+hmpBV36X+jfRq0zxAvu989Wwtd4xMaJQTii24XvoLlZgh1J0xgRSdvhs6+LkngnAJHNKQlmvE9SSsrzVk+j4I2koWTykbwcW8HrRrkMRgg605q1T2JJM45obhxc/5QjtscvPUl9K8AwaOHUEjIl2ImvH2JZDmDlVvbSS7NvpwKBgAeInXDNqHP6l0/omeMc4doROp7BzVI1cDFD6XLFzawjUnB4SfBekMRyIVvwe0UKIQmgaVGlODuyTk3P6UwcgoxZgTtWIngzYiuYOeNcRBFNDMuBmeNtgO9jeBo3UPbYMXIY7X3gPWdR0KMJiTd5JuHQaC5Oh+0ZQsR5VhAZC2VpAoGBAIuY4sOgsDOYm+Lnf7Ugb9Bw0Cp0ui7+q0Qan9Bnma35D6lWTcuqgVOXE0WFxFCugGqOME0+kDhV69sC4FehX0B5DQeXQCkiyZFaqIOif9I8DzTse5tWs4j3pyEs5JxCbTXk5v3MAiwkGA+Mnt6Y3zwGWXWYNvNKmulfblRAFK4NAoGAUCLYA12hbNc8SPRMeDWX2YvZ68E5bIcnwYK12m57AAEnBeCUf3r7agKjnyWhofrxGmDcwfEsPAGfTyG+SMxi7pNoRqyHIa/xsBu/7zbg+akwu5QRk+s6IqMJZrDg01Dwr5hKZ2Hc+cmbzNgQDPpSScp47MYWiIeutE9EUn+kxJQ=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public String alipay_public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgTG3coHhx9JNLCNM4vm/n5JAJc3woU4VQhooK0DSOiPa2p7HFLT/BAz79SDBK5jnGkLy++riL1uNrbHl0wYlDO9qWlF4PGmA+DgfXgds1AltdTEeqMO+wJDNn4GgmBxbyz962nZVQKpoLU2MgbOPWepPPQWhOfMBBqFM87/wJ2lmdSevokYcGebjNOZgwPPRyb9pPuW/NiSARrZ8TE/kgbybW6f8V2Kf7yxz3FhhLNFKrJ4h0o9AqCrlQD5PBPc7Tz5rFrLlJQ6evaziRILRpWmZqZ1hq/IO1JiovOg2ZSIAH32pONDYWYpxlhSMExUvRuLhAoGUJh+PNl2H6I2+bwIDAQAB";

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    public String notify_url="http://p9magt.natappfree.cc/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    public String return_url="http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type="RSA2";

    // 字符编码格式
    private  String charset="utf-8";

    //订单超时时间
    private String timeout = "1m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    public String gatewayUrl="https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
