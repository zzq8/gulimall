package com.zzq.gulimall.order.dao;

import com.zzq.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:23:40
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
