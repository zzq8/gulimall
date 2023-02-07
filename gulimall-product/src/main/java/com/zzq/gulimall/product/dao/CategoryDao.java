package com.zzq.gulimall.product.dao;

import com.zzq.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-29 16:34:28
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
