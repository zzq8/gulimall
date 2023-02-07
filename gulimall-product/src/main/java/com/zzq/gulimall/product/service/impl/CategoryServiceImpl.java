package com.zzq.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.Query;
import com.zzq.gulimall.product.dao.CategoryDao;
import com.zzq.gulimall.product.entity.CategoryEntity;
import com.zzq.gulimall.product.service.CategoryBrandRelationService;
import com.zzq.gulimall.product.service.CategoryService;
import com.zzq.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 以前正常写法
     */
//    @Autowired
//    private CategoryDao categoryDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        /**
         * 现在用了 MyBatis-Plus 后
         * baseMapper 代替了 categoryDao(不用注入)
         */
        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //从一级分类开始给每个递归给一个子list
        List<CategoryEntity> AllLevelMenus = entities.stream()
                .filter(entity -> entity.getParentCid() == 0)
                .map((entity) -> {
                    entity.setChildren(getChildrens(entity, entities));
                    //因为 map 的Lambda表达式必须是 Function 接口的一个实例，也就是需要参数需要返回值，上面这行没有返回值
                    return entity;
                })
                .sorted((menu1, menu2) -> menu1.getSort() - menu2.getSort())
                .collect(Collectors.toList());
        return AllLevelMenus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //物理删除就真没了，所以使用逻辑删除设置一个标志位
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();

        findParentPath(catelogId,paths);
        Collections.reverse(paths);

        return paths.toArray(new Long[0]);
    }

//    @Caching(evict = {
//            @CacheEvict(value = "category",key = "'getLevel1Category'"),
//            @CacheEvict(value = "category",key = "'getCatalogJson'")
//    })//Group annotation for multiple cache annotations
//调用该方法会删除缓存category下的所有cache，如果要删除某个具体，用key="''"
@CacheEvict(value = {"category"},allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    //每一个需要缓存的数据我们都来指定要放到那个名字的缓存。【缓存的分区（按照业务类型分）】
    // sync表示该方法的缓存被读取时会加锁 // value等同于cacheNames // key如果是字符串"''"
    @Cacheable(value = {"category"},key = "#root.methodName",sync = true) //代表当前方法的结果需要缓存，如果缓存中有，方法不用调用。如果缓存中没有，会调用方法，最后将方法的结果放入缓存！
    @Override
    public List<CategoryEntity> getLevel1Category() {
        System.out.println("getLevel1Categorys........");
        List<CategoryEntity> list = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 1));
        return list;
    }


    /**
     * Spring Cache
     */
    @Cacheable(value = {"category"},key = "#root.methodName",sync = true)
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        return getCatalogByDb();
    }

    /**
     * 使用redis作为本地缓存
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonByRedis(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String catalogJson = ops.get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            System.out.println("缓存不命中...查询数据库...");
            Map<String, List<Catelog2Vo>> map = getCatalogJsonDb();
            String json = JSON.toJSONString(map);

            ops.set("catalogJson",json);
            return map;
        }

        /**
         * protected TypeReference()  构造方法是protected所以需要匿名创建
         */
        System.out.println("缓存命中...直接返回...");
        Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>(){});
        return stringListMap;
    }

    /**
     * 问题：Lock.Lock（10,TimeUnit.SECONDS）在锁时间到了以后，不会自动续期。
     * 1、如果我们传递了锁的超时时间，就发送给redis执行脚本，进行占锁，默认超时就是我们指定的时间
     * 2、如果我们未指定锁的超时时间，就使用30*1000【LockWatchdogTimeout看门狗的默认时间】；
     * 只要占锁成功，就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗的默认时间】每隔10s续期续成30s看下行
     * internalLockLeaseTime【看门狗时间】／3,10s  续期  默认30s-19>=10给恢复到30
     * //最佳实战
     * //1）、Lock.Lock（30,TimeUnit.SECONDS）省掉了整个续期操作。手动解锁    注意：有参构造没用看门狗
     */

    public synchronized Map<String, List<Catelog2Vo>> getCatalogJsonDb() {

        /**
         * 光 synchronized 还不行，只是让它们串行了，但是实际上第一个执行完了后面就可以到缓存拿了
         */
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String catalogJson = ops.get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)) {
            Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>(){});
            return stringListMap;
        }
        //TODO 这里查了两次数据库   原因是锁的时序问题具体看资料图，这个方法完了放入redis的操作没在锁里。所以交互的空隙后面排队的乘空隙判断redis里没数据
        //解决办法：把redis set也放到这个synchronized块里
        return getCatalogByDb();
    }

    private Map<String, List<Catelog2Vo>> getCatalogByDb() {
        System.out.println("查询了数据库");

        //将数据库的多次查询变为一次
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);

        //1、查出所有分类
        //1、1）查出所有一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        //封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1、每一个的一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());

            //2、封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());

                    //1、找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());

                    if (level3Catelog != null) {
                        List<Catelog2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                            //2、封装成指定格式
                            Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return category3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(category3Vos);
                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        return parentCid;
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList,Long parentCid) {
        List<CategoryEntity> categoryEntities = selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
        return categoryEntities;
        // return this.baseMapper.selectList(
        //         new QueryWrapper<CategoryEntity>().eq("parent_cid", parentCid));
    }

    private void findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }

    }

    private List<CategoryEntity> getChildrens(CategoryEntity entity, List<CategoryEntity> all) {
        List<CategoryEntity> childrenList = all.stream()
                .filter(menu -> menu.getParentCid() == entity.getCatId())
                .map((menu) -> {
                    menu.setChildren(getChildrens(menu, all));
                    return menu;
                })
                .sorted((menu1, menu2) -> menu1.getSort() == null ? 0 : menu1.getSort() - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .collect(Collectors.toList());
        return childrenList;
    }

}