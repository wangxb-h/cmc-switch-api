package com.feihu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feihu.common.JsonData;
import com.feihu.common.RedisUtil;
import com.feihu.entity.ProductCarInfo;
import com.feihu.entity.UserVipInfo;
import com.feihu.service.ProductCarService;
import com.feihu.utils.RedisUse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("car")
@Api(description = "购物车Controller")
public class ProductCarController {
    @Autowired
    private ProductCarService productCarService;


    //加入购物车
    @RequestMapping("addCart")
    public JsonData addCart(Integer id,Integer count){
        Integer count_y=productCarService.addCart(id,count);
        return JsonData.getJsonSuccess(count_y);
    }

    //查询购物车
    @RequestMapping("findShopCart")
    public JsonData findShopCart(HttpServletRequest request){
        UserVipInfo login_user = (UserVipInfo) request.getAttribute("login_user");
        String iphone = login_user.getUserPhone();
        Jedis jedis = RedisUtil.getJedis();
        Map<String, String> map = jedis.hgetAll("car_" + iphone + "_wxb");
        Set<String> keys = map.keySet();
        List list=new ArrayList();
        for (String key: keys){
            ProductCarInfo carInfo = JSONObject.parseObject(map.get(key),ProductCarInfo.class);
            list.add(carInfo);
        }
        RedisUtil.returnJedis(jedis);
        return JsonData.getJsonSuccess(list);
    }


    //修改购物车商品选中状态 true/false
    @RequestMapping("updateIsCheck")
    public JsonData updateIsCheck(Boolean isCheck,Integer id,HttpServletRequest request){
        if (id==null){
            return JsonData.getJsonError("id为空");
        }
        UserVipInfo login_user = (UserVipInfo)  request.getAttribute("login_user");
        String iphone = login_user.getUserPhone();

        String hget = RedisUse.hget("car_" + iphone + "_wxb", String.valueOf(id));
        ProductCarInfo carInfo = JSONArray.parseObject(hget, ProductCarInfo.class);
        carInfo.setCheck(isCheck);
        RedisUse.hset("car_" + iphone + "_wxb",String.valueOf(id),JSONObject.toJSONString(carInfo));
        return JsonData.getJsonSuccess("修改状态成功");
    }

    //删除购物车商品
    @RequestMapping("deleteShopCar")
    public JsonData deleteShopCar(Integer id,HttpServletRequest request){
        String Sid = String.valueOf(id);
        UserVipInfo login_user = (UserVipInfo) request.getAttribute("login_user");
        String iphone = login_user.getUserPhone();
        RedisUse.hdel("car_"+iphone+"_wxb",Sid);
        return JsonData.getJsonSuccess("success");
    }

    //查询选中的订单
    @RequestMapping("findCheckProduct")
    public JsonData findCheckProduct(HttpServletRequest request){
        UserVipInfo userVipInfo = (UserVipInfo) request.getAttribute("login_user");
        String iphone = userVipInfo.getUserPhone();
        Jedis jedis = RedisUtil.getJedis();
        Map<String, String> map = jedis.hgetAll("car_" + iphone + "_wxb");
        Set<String> keys = map.keySet();
        List<ProductCarInfo> list=new ArrayList<>();
        for (String key : keys) {
            ProductCarInfo carInfo = JSONArray.parseObject(map.get(key), ProductCarInfo.class);
            if (carInfo.isCheck()==true){
                list.add(carInfo);
            }
        }
        RedisUtil.returnJedis(jedis);
        return JsonData.getJsonSuccess(list);
    }
}
