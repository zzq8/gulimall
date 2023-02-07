package com.zzq.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.Query;
import com.zzq.gulimall.product.dao.ProductAttrValueDao;
import com.zzq.gulimall.product.entity.ProductAttrValueEntity;
import com.zzq.gulimall.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> collect) {
        this.saveBatch(collect);
    }

    @Override
    public List<ProductAttrValueEntity> getAttrInfoBySpu(Long spuId) {

        List<ProductAttrValueEntity> spu_id = this.baseMapper.selectList(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return spu_id;
    }

    @Override
    public void updateBySpu(Long spuId, List<ProductAttrValueEntity> entities) {
        //ISSUE 注意 spuId是修改的所有条目
        entities.forEach((entity) -> {
            this.baseMapper.update(entity,new QueryWrapper<ProductAttrValueEntity>()
                    .eq("spu_id", spuId)
                    .and(e -> e.eq("attr_id",entity.getAttrId())));
        });
    }

    @Override
    public List<ProductAttrValueEntity> baseAttrListforspu(Long spuId) {

        List<ProductAttrValueEntity> attrValueEntityList = this.baseMapper.selectList(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return attrValueEntityList;
    }

}