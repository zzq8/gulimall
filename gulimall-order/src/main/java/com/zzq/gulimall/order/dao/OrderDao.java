package com.zzq.gulimall.order.dao;

import com.zzq.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:23:40
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    void updateOrderStatus(String orderSn, Integer code, Integer payType);
}
