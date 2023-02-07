package com.zzq.gulimall.product.web;

import com.zzq.gulimall.product.config.MyBindProperties;
import com.zzq.gulimall.product.entity.CategoryEntity;
import com.zzq.gulimall.product.service.CategoryService;
import com.zzq.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;


    @Autowired
    MyBindProperties diy;

    @ResponseBody
    @RequestMapping("/test")
    public String testProperties(){
        return diy.toString();
    }

    /**
     * 跳首页
     * 显示一级菜单
     */
    @RequestMapping({"/","index.html"})
    public String indexPage(Model model){
        //拿一级标题
        List<CategoryEntity> level1Category = categoryService.getLevel1Category();
        model.addAttribute("categories",level1Category);
        //XXX 这里由于 Thymeleaf 可以自动拼接：可以到配置里看有提醒，例如输入一个  prefix  xx suffix
        return "index";
    }

    //index/json/catalog.json
    @GetMapping(value = "/index/catalog.json")
    @ResponseBody
    public Map<String,List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }
}
