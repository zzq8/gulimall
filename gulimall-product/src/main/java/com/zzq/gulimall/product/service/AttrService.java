package com.zzq.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.product.entity.AttrEntity;
import com.zzq.gulimall.product.vo.AttrGroupRelationVo;
import com.zzq.gulimall.product.vo.AttrRespVo;
import com.zzq.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-29 16:34:29
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttrById(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    List<Long> selectSearchAttrs(List<Long> attrIds);
}

