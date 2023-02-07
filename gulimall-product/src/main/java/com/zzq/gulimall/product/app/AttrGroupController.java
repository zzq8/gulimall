package com.zzq.gulimall.product.app;

import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.R;
import com.zzq.gulimall.product.entity.AttrEntity;
import com.zzq.gulimall.product.entity.AttrGroupEntity;
import com.zzq.gulimall.product.service.AttrAttrgroupRelationService;
import com.zzq.gulimall.product.service.AttrGroupService;
import com.zzq.gulimall.product.service.AttrService;
import com.zzq.gulimall.product.service.CategoryService;
import com.zzq.gulimall.product.vo.AttrGroupRelationVo;
import com.zzq.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-30 17:00:49
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    ///product/attrgroup/{catelogId}/withattr
    // 获取分类下所有分组&关联属性
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){

        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data",vos);
    }



    ///product/attrgroup/attr/relation
    @PostMapping(value = "/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos) {

        attrAttrgroupRelationService.saveBatch(vos);

        return R.ok();

    }

    /**
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping(value = "/{attrgroupId}/noattr/relation")
    public R attrNoattrRelation(@RequestParam Map<String, Object> params,
                                @PathVariable("attrgroupId") Long attrgroupId) {

        // List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);

        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);

        return R.ok().put("page", page);
    }


    //    /product/attrgroup/attr/relation/delete
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos) {
        attrService.deleteRelation(vos);

        return R.ok();
    }

    //    /product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrgroupId) {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);

        return R.ok().put("data", entities);
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable Long catelogId) {

//        PageUtils page = attrGroupService.queryPage(params);

        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
