package com.zzq.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzq.gulimall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:32:23
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(Long skuId, Long wareId, Integer skuNum);

    Long getSkuStock(Long item);

    List<Long> listWareIdHasSkuStock(Long skuId);

    Long lockSkuStock(Long skuId, Long wareId, Integer num);

    /**
     * 解锁库存
     * @param skuId
     * @param wareId
     * @param num
     */
    void unLockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);
}
