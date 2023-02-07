package com.zzq.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.order.entity.MqMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:23:41
 */
public interface MqMessageService extends IService<MqMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

