package com.zzq.gulimall.coupon.dao;

import com.zzq.gulimall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:03:31
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
