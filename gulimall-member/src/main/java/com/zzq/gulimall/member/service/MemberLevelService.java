package com.zzq.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:13:48
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

