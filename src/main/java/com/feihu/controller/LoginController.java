package com.feihu.controller;

import com.feihu.common.JsonData;
import com.feihu.entity.UserVipInfo;
import com.feihu.service.LoginService;
import com.feihu.utils.JWT;
import com.feihu.utils.RedisUse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("login")
@Api(description = "登录Controller")
public class LoginController {
    @Autowired
    private LoginService loginService;

    //发送验证码
    @RequestMapping("verificationCode")
    @ApiOperation("发送验证码")//API中说明的该类的作用 6666666
    @ApiImplicitParams({
            @ApiImplicitParam(name = "telephone",//参数名字
                    value = "用户手机号",//参数的描述
                    required = true,//是否必须传参数，true是
                    paramType = "path",//参数类型 path代表路径参数
                    dataType = "string")//参数类型 int
    })
    public JsonData verificationCode(String telephone){
        UserVipInfo userVipInfo=loginService.findUser(telephone);
        if (userVipInfo!=null){
            String code = "111";
            String userPhone = userVipInfo.getUserPhone();
            //发短信
            /*SendSmsResponse response = AliyunSmsUtils.sendSms(userPhone, code);*/
            //将code验证码存入redis
            RedisUse.set(telephone+"_wxb",code);
            return JsonData.getJsonSuccess("成功");
        }
        return JsonData.getJsonSuccess("发送验证码成功");
    }

    //登录
    @RequestMapping("login")
    public JsonData login(String codeMage,String iphone, HttpServletRequest req){
        Map logMap=new HashMap();
        String redis_code = RedisUse.get(iphone + "_wxb");
        if (codeMage!=null && redis_code.equals(codeMage)){
            UserVipInfo userVipInfo=loginService.findUserVipByIphone(iphone);
            //JWT加密
            String sign = JWT.sign(userVipInfo, 1000 * 60 * 60 * 24);
            //base64进行加签 防止篡改数据
            String token = Base64.getEncoder().encodeToString((iphone + "," + sign).getBytes());
            //将最新的密钥放入redis
            RedisUse.set("token_" + iphone,sign);
            logMap.put("code",200);
            logMap.put("message","登陆成功");
            logMap.put("token",token);
        }else {
            logMap.put("code",300);
            logMap.put("message","用户不存在 或者 验证码错误");
        }
        return JsonData.getJsonSuccess(logMap);
    }
}
