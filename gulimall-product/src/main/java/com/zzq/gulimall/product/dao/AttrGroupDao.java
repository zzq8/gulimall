package com.zzq.gulimall.product.dao;

import com.zzq.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzq.gulimall.product.vo.SpuItemAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 属性分组
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-29 16:34:28
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}
