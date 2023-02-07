package com.zzq.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:23:40
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

