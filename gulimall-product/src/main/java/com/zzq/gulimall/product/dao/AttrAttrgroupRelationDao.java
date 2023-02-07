package com.zzq.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzq.gulimall.product.entity.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-29 16:34:28
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("entities")List<AttrAttrgroupRelationEntity> entities);
}
