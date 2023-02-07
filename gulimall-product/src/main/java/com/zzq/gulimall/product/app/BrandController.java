package com.zzq.gulimall.product.app;

import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.R;
import com.zzq.common.valid.AddGroup;
import com.zzq.gulimall.product.entity.BrandEntity;
import com.zzq.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 品牌
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-30 17:00:49
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);
        //TODO data or page
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand /*, BindingResult result*/) {
//        HashMap<String, String> map = new HashMap<>();
//
//        if (result.hasErrors()) {
//            result.getFieldErrors().forEach((item) ->{
//                String field = item.getField();
//                String message = item.getDefaultMessage();
//                map.put(field,message);
//            });
//
//            return R.error(400,"提交的数据不合法").put("data",map);
//        } else {
//            brandService.save(brand);
//        }
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     * 注意 SQL报错是因为mybatis-plus不支持更新逻辑删除的字段
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand) {
        brandService.updateDetail(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
