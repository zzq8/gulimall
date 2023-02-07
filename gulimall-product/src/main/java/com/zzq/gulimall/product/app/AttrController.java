package com.zzq.gulimall.product.app;

import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.R;
import com.zzq.gulimall.product.entity.ProductAttrValueEntity;
import com.zzq.gulimall.product.service.AttrService;
import com.zzq.gulimall.product.service.ProductAttrValueService;
import com.zzq.gulimall.product.vo.AttrRespVo;
import com.zzq.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品属性
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-30 17:00:49
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    ///product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R infoBySpu(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> data = productAttrValueService.getAttrInfoBySpu(spuId);
        return R.ok().put("data", data);
    }


    ///product/attr/base/list/{catelogId}
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String type) {

        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, type);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
//		AttrEntity attr = attrService.getById(attrId);
        AttrRespVo respVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr) {
        attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr) {
//		attrService.updateById(attr);

        attrService.updateAttrById(attr);
        return R.ok();
    }

    //    /product/attr/update/{spuId}
    //FIXME 请求参数是多个 Entity 怎么批量修改
    //answer 用 List 包起来
    @PostMapping("/update/{spuId}")
    public R updateBySpu(@PathVariable("spuId") Long spuId,
                         @RequestBody List<ProductAttrValueEntity>  entities) {
        productAttrValueService.updateBySpu(spuId,entities);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
