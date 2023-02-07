package com.zzq.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzq.common.utils.PageUtils;
import com.zzq.gulimall.member.entity.MemberEntity;
import com.zzq.gulimall.member.exception.PhoneException;
import com.zzq.gulimall.member.exception.UsernameException;
import com.zzq.gulimall.member.vo.MemberUserLoginVo;
import com.zzq.gulimall.member.vo.MemberUserRegisterVo;
import com.zzq.gulimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:13:48
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberUserRegisterVo vo);

    void checkPhoneUnique(String phone) throws PhoneException;

    void checkUserNameUnique(String userName) throws UsernameException;

    MemberEntity login(MemberUserLoginVo loginVo);

    MemberEntity login(SocialUser socialUser) throws Exception;
}

