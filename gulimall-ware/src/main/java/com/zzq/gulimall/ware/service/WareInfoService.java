package com.zzq.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.ware.entity.WareInfoEntity;
import com.zzq.gulimall.ware.vo.FareVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:32:23
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    FareVo getFare(Long addrId);
}

