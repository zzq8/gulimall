package com.zzq.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzq.common.utils.HttpUtils;
import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.Query;
import com.zzq.gulimall.member.dao.MemberDao;
import com.zzq.gulimall.member.dao.MemberLevelDao;
import com.zzq.gulimall.member.entity.MemberEntity;
import com.zzq.gulimall.member.entity.MemberLevelEntity;
import com.zzq.gulimall.member.exception.PhoneException;
import com.zzq.gulimall.member.exception.UsernameException;
import com.zzq.gulimall.member.service.MemberService;
import com.zzq.gulimall.member.vo.MemberUserLoginVo;
import com.zzq.gulimall.member.vo.MemberUserRegisterVo;
import com.zzq.gulimall.member.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberUserRegisterVo vo) {

        MemberEntity memberEntity = new MemberEntity();

        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.selectOne(new QueryWrapper<MemberLevelEntity>().eq("default_status", 1));
        memberEntity.setLevelId(levelEntity.getId());

        //设置其它的默认信息
        //检查用户名和手机号是否唯一。感知异常，异常机制
        //XX 讲道理这里应该获取验证码之前做的
        checkPhoneUnique(vo.getPhone());
        checkUserNameUnique(vo.getUserName());

        memberEntity.setNickname(vo.getUserName());
        memberEntity.setUsername(vo.getUserName());
        //密码进行MD5加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(vo.getPassword());
        memberEntity.setPassword(encode);
        memberEntity.setMobile(vo.getPhone());
        memberEntity.setGender(0);
        memberEntity.setCreateTime(new Date());

        //保存数据
        this.baseMapper.insert(memberEntity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneException {

        Integer phoneCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));

        if (phoneCount > 0) {
            throw new PhoneException();
        }

    }

    @Override
    public void checkUserNameUnique(String userName) throws UsernameException {

        Integer usernameCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));

        if (usernameCount > 0) {
            throw new UsernameException();
        }
    }

    @Override // service
    public MemberEntity login(MemberUserLoginVo loginVo) {
        String loginAccount = loginVo.getLoginacct();
        //以用户名或电话号登录的进行查询
        MemberEntity entity = this.getOne(new QueryWrapper<MemberEntity>().eq("username", loginAccount).or().eq("mobile", loginAccount));
        if (entity!=null){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matches = bCryptPasswordEncoder.matches(loginVo.getPassword(), entity.getPassword());
            if (matches){
                //TODO why
                entity.setPassword("");
                return entity;
            }
        }
        return null;
    }

    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {

        //具有登录和注册逻辑
        String uid = socialUser.getSocial_uid();

        //1、判断当前社交用户是否已经登录过系统
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));

        if (memberEntity != null) {
            //这个用户已经注册过
            //更新用户的访问令牌的时间和access_token
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(String.valueOf(socialUser.getExpires_in()));
            this.baseMapper.updateById(update);

            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(String.valueOf(socialUser.getExpires_in()));
            return memberEntity;
        } else {
            //2、没有查到当前社交用户对应的记录我们就需要注册一个
            MemberEntity register = new MemberEntity();
            //3、查询当前社交用户的社交账号信息（昵称、性别等）
            Map<String,String> heads = new HashMap<>();
            heads.put("Authorization",socialUser.getToken_type()+" "+socialUser.getAccess_token());

            HttpResponse response = HttpUtils.doGet("https://api.github.com", "/user", "get", heads, new HashMap<String, String>());

            if (response.getStatusLine().getStatusCode() == 200) {
                //查询成功
                String json = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSON.parseObject(json);
                String name = jsonObject.getString("name");
//                String gender = jsonObject.getString("gender");
                String profileImageUrl = jsonObject.getString("avatar_url");
                String id = jsonObject.getString("id");

                register.setNickname(name);
//                register.setGender("m".equals(gender)?1:0);
                register.setHeader(profileImageUrl);
                register.setCreateTime(new Date());
                register.setSocialUid(id);
                register.setAccessToken(socialUser.getAccess_token());
                register.setExpiresIn(String.valueOf(socialUser.getExpires_in()));

                //把用户信息插入到数据库中
                this.baseMapper.insert(register);

            }
            return register;
        }

    }

}