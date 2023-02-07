package com.zzq.gulimall.cart.intercept;

import com.zzq.common.constant.AuthServerConstant;
import com.zzq.common.vo.MemberResponseVo;
import com.zzq.gulimall.cart.to.UserInfoTo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.zzq.common.constant.CartConstant.TEMP_USER_COOKIE_NAME;
import static com.zzq.common.constant.CartConstant.TEMP_USER_COOKIE_TIMEOUT;

/**
 * 在执行目标方法之前，判断用户的登录状态.并封装传递给controller目标请求
 */
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> toThreadLocal = new ThreadLocal<>();
    /**
     * 该模块每个 API 执行前都要过这里，统一处理是否登录逻辑
     * session 判断用户登录状态，封装成TO对象（ThreadLocal修饰方便后面拿）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo info = new UserInfoTo();
        MemberResponseVo member = (MemberResponseVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TEMP_USER_COOKIE_NAME.equals(cookie.getName())) {
                    info.setUserKey(cookie.getValue());
                }
            }
        }

        if (member != null) {
            info.setUserId(member.getId());
            info.setTempUser(true);
        }else {
            //临时用户，每次刷新都会 +if
            if (info.getUserKey() == null) {
                info.setUserKey(UUID.randomUUID().toString());
                Cookie cookie = new Cookie(TEMP_USER_COOKIE_NAME, info.getUserKey());
                //扩大作用域
                cookie.setDomain("gulimall.com");
                //设置过期时间
                cookie.setMaxAge(TEMP_USER_COOKIE_TIMEOUT);
                response.addCookie(cookie);
            }
        }

        toThreadLocal.set(info);
        return true;
    }


    /**
     * 没有登录：方法执行完后需要保存一个 cookie 到浏览器
     */
}
