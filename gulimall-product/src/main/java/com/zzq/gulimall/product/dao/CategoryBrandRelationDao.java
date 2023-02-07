package com.zzq.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzq.gulimall.product.entity.CategoryBrandRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-29 16:34:28
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updateCategory(@Param(value = "catId")Long catId, @Param(value = "name")String name);
}
