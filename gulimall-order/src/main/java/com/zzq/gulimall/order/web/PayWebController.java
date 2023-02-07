package com.zzq.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import com.zzq.gulimall.order.config.AlipayTemplate;
import com.zzq.gulimall.order.service.OrderService;
import com.zzq.gulimall.order.vo.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class PayWebController {
    @Autowired
    private AlipayTemplate alipayTemplate;

    @Autowired
    private OrderService orderService;

//    @Autowired
//    private BestPayService bestPayService;
//
//    @Resource
//    private WxPayConfig wxPayConfig;

    /**
     * 用户下单:支付宝支付
     * 1、让支付页让浏览器展示
     * 2、支付成功以后，跳转到用户的订单列表页
     */
    @ResponseBody
    @GetMapping(value = "/aliPayOrder",produces = "text/html")
    public String aliPayOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {

        PayVo payVo = orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(payVo);
        System.out.println(pay);
        return pay;
    }
}
