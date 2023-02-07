package com.zzq.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.to.OrderTo;
import com.zzq.common.to.SkuHasStockVo;
import com.zzq.common.to.mq.StockLockedTo;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.ware.entity.WareSkuEntity;
import com.zzq.gulimall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:32:23
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    boolean orderLockStock(WareSkuLockVo vo);

    /**
     * 解锁库存
     * @param to
     */
    void unlockStock(StockLockedTo to);

    /**
     * 解锁订单
     * @param orderTo
     */
    void unlockStock(OrderTo orderTo);
}

