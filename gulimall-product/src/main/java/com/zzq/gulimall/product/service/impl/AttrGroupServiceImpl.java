package com.zzq.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.Query;
import com.zzq.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zzq.gulimall.product.dao.AttrGroupDao;
import com.zzq.gulimall.product.entity.AttrEntity;
import com.zzq.gulimall.product.entity.AttrGroupEntity;
import com.zzq.gulimall.product.service.AttrGroupService;
import com.zzq.gulimall.product.service.AttrService;
import com.zzq.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.zzq.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * Query   PageUtils   都是gulimall-common的自定义类封装
     * 去公司也是直接封装好的
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        /**
         * 记得先把语句罗列好再写！！！
         */
        //select * from pms_attr_group where catelog_id=? and (attr_group_id=key or attr_group_name like %key%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }

        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        } else {
            wrapper.eq("catelog_id", catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }

    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        // 1、获取分组
        List<AttrGroupEntity> attGroups = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 1.1 设置分组的attr属性   stream map 也可以
        List<AttrGroupWithAttrsVo> vos = new ArrayList<>();
        attGroups.forEach((item) -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item, attrGroupWithAttrsVo);
            List<AttrEntity> attr_id = attrService.getRelationAttr(item.getAttrGroupId());

//            List<AttrAttrgroupRelationEntity> attr_group_id = attrAttrgroupRelationDao.selectList(
//                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", item.getAttrGroupId()));
//
//            List<Long> collect = attr_group_id.stream().map(idss -> idss.getAttrId()).collect(Collectors.toList());
//            List<AttrEntity> attr_id = attrService.list(new QueryWrapper<AttrEntity>().in("attr_id", collect));

            attrGroupWithAttrsVo.setAttrs(attr_id);
            vos.add(attrGroupWithAttrsVo);
        });
        return vos;
    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {

        //1、查出当前spu对应的所有属性的分组信息以及当前分组下的所有属性对应的值
        AttrGroupDao baseMapper = this.getBaseMapper();
        List<SpuItemAttrGroupVo> vos = baseMapper.getAttrGroupWithAttrsBySpuId(spuId,catalogId);

        return vos;
    }

}