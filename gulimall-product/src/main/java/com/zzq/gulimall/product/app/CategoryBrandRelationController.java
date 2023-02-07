package com.zzq.gulimall.product.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.R;
import com.zzq.gulimall.product.entity.BrandEntity;
import com.zzq.gulimall.product.entity.CategoryBrandRelationEntity;
import com.zzq.gulimall.product.service.CategoryBrandRelationService;
import com.zzq.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 品牌分类关联
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-30 17:00:49
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    /**
     * 获取分类关联的品牌
     * /product/categorybrandrelation/brands/list
     * 1、Controller：处理请求，接收和效验数据
     * 2、Service接收Controller传来的数据，进行业务处理
     * 3、Controller接收Service处理完的数据，封装页面指定的vo
     */
    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam("catId") Long catId) {
        /**
         * 虽然可以一步到位，但是雷神推荐解耦。
         * 搞一个查出 brand 下面所有信息的方法（这样其他想调用时候也可以用）
         */
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVo> collect = vos.stream().map((item) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());


//        List<BrandVo> collect = categoryBrandRelationService.list(new QueryWrapper<CategoryBrandRelationEntity>()
//                .eq("catelog_id", catId)).stream().map((item) -> {
//            BrandVo brandVo = new BrandVo();
//            BeanUtils.copyProperties(item,brandVo);
//            return brandVo;
//        }).collect(Collectors.toList());

        return R.ok().put("data", collect);
    }


    @GetMapping("catelog/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R catelogList(@RequestParam("brandId") Long brandId) {

        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId)
        );

        return R.ok().put("data", data);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
