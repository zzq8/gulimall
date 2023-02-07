package com.zzq.gulimall.product.dao;

import com.zzq.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-29 16:34:28
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
