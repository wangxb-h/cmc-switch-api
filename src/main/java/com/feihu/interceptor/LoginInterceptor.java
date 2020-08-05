package com.feihu.interceptor;

import com.feihu.common.exception.NologinException;
import com.feihu.entity.UserVipInfo;
import com.feihu.utils.JWT;
import com.feihu.utils.RedisUse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = request.getHeader("token");
        if (token==null){
            throw new NologinException("没有登录");
        }
        //base64解密
        byte[] decode = Base64.getDecoder().decode(token);
        //转为string数组
        String signToken=new String(decode);
        //判断是否被篡改
        String[] split = signToken.split(",");
        if (split.length!=2){
            throw new NologinException("没有登录");
        }
        String iphone = split[0];
        // jwt的秘钥
        String sign = split[1];
        UserVipInfo user = JWT.unsign(sign, UserVipInfo.class);
        if (user!=null){
            //获取登录时放入的密钥
            String sing_redis = RedisUse.get("token_" + iphone);
            if (!sing_redis.equals(sign)){
                throw new NologinException("验证码已过期  请重新登录");
            }
        }
        //续命
        RedisUse.set("token_" + iphone,sign,60*30);
        if (user==null){
            //返回json字符串
            throw new NologinException("没有登录");
        }
        //将用户信息放入request中  方便后面需求处理
        request.setAttribute("login_user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
