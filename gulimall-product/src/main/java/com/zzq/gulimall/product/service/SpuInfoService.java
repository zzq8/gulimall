package com.zzq.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.product.entity.SpuInfoEntity;
import com.zzq.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-29 16:34:28
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    //商品上架
    void up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

