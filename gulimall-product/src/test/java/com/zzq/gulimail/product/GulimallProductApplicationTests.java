package com.zzq.gulimail.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzq.common.valid.AddGroup;
import com.zzq.gulimall.product.GulimallProductApplication;
import com.zzq.gulimall.product.config.MyBindProperties;
import com.zzq.gulimall.product.entity.BrandEntity;
import com.zzq.gulimall.product.service.BrandService;
import com.zzq.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
// 下面这两个注解是因为这是 SpringBoot 2.2.x 之前
@RunWith(SpringRunner.class)  //需要从容器中获取实例是需要加上该注解，否则空指针，管你是啥IDE
@SpringBootTest(classes = GulimallProductApplication.class)
public class GulimallProductApplicationTests {

    @Resource
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    MyBindProperties diy; //将person自动注入进来

    @Test
    public void testProcessor() {
        System.out.println(diy); //打印person信息
    }


    @Test
    public void testRedis(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", UUID.randomUUID().toString());

        String hello = ops.get("hello");
        System.out.println(hello);
    }


    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    /**
     * list.toArray(new String[0]))
     * 测试数组长度有什么限制，具体看 MainTest
     */
    @Test
    public void testAslistMethod(){
        List<String> list = Arrays.asList("a", "b", "c");
        System.out.println(list.size());
        log.info("arr: {}",list.toArray(new String[0]));
        log.info("arr111: {}",Arrays.asList(list.toArray(new String[list.size()])));
    }

    /**
     * 测试 增加、修改、查
     */
    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功...");

//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("华为品牌");
//        brandService.updateById(brandEntity);

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));
        list.forEach((item) -> System.out.println(item));
    }


    @Test
    //如果数组属性只有一个值，这时候属性值部分可以省略大括号
//    @AnnotationTest({AddGroup.class})
    @AnnotationTest(AddGroup.class)
    public void myTest(){
        Class[] value = {AddGroup.class};
    }
}
