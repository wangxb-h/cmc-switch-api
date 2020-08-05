package com.feihu.config;

import com.feihu.interceptor.KuaYuInterceptor;
import com.feihu.interceptor.LoginInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//配置拦截器类
@Configuration
@AllArgsConstructor
public class LoginConfig implements WebMvcConfigurer {
    private KuaYuInterceptor kuaYuInterceptor;
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        registry.addInterceptor(kuaYuInterceptor).addPathPatterns("/**");

        registry.addInterceptor(loginInterceptor).addPathPatterns("/car/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/address/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/order/**");
    }
}
