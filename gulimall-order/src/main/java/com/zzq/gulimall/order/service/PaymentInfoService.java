package com.zzq.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.order.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:23:41
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

