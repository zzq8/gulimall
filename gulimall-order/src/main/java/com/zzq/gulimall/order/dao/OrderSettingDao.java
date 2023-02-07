package com.zzq.gulimall.order.dao;

import com.zzq.gulimall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:23:41
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
