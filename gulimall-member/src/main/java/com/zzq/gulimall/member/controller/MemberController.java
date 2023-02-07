package com.zzq.gulimall.member.controller;

import com.zzq.common.exception.BizCodeEnum;
import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.R;
import com.zzq.gulimall.member.entity.MemberEntity;
import com.zzq.gulimall.member.exception.PhoneException;
import com.zzq.gulimall.member.exception.UsernameException;
import com.zzq.gulimall.member.feign.CouponFeignService;
import com.zzq.gulimall.member.service.MemberService;
import com.zzq.gulimall.member.vo.MemberUserLoginVo;
import com.zzq.gulimall.member.vo.MemberUserRegisterVo;
import com.zzq.gulimall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 会员
 *
 * @author zzq
 * @email 1024zzq@gmail.com
 * @date 2022-07-31 13:13:48
 */
@RestController
@RequestMapping("member/member")
public class  MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;


    @PostMapping(value = "/oauth2/login")
    public R oauthLogin(@RequestBody SocialUser socialUser) throws Exception {

        MemberEntity memberEntity = memberService.login(socialUser);

        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
        }
    }

    @RequestMapping("/login") // member
    public R login(@RequestBody MemberUserLoginVo loginVo) {
        MemberEntity entity=memberService.login(loginVo);
        if (entity!=null){
            return R.ok();
        }else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(), BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
        }
    }


    @PostMapping(value = "/register")
    public R register(@RequestBody MemberUserRegisterVo vo) {

        try {
            memberService.register(vo);
        } catch (PhoneException e) {//这个叫分层思想，又不是写学生作业，什么都混一起，能用就行
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnum.PHONE_EXIST_EXCEPTION.getMessage());
        } catch (UsernameException e) {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(),BizCodeEnum.USER_EXIST_EXCEPTION.getMessage());
        }

        return R.ok();
    }


    @RequestMapping("/coupons")
    public R RPCTest(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R r = couponFeignService.memberCoupons();
        return R.ok().put("member",memberEntity).put("coupons",r.get("coupons"));
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
