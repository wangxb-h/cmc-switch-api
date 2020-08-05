package com.feihu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feihu.dao.ProductCarMapper;
import com.feihu.dao.ProductMapper;
import com.feihu.entity.ProductCarInfo;
import com.feihu.entity.ProductInfo;
import com.feihu.entity.UserVipInfo;
import com.feihu.service.ProductCarService;
import com.feihu.utils.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Service
public class ProductCarServiceImpl implements ProductCarService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductCarMapper productCarMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public Integer addCart(Integer id, Integer count) {
        //获取用户信息
        UserVipInfo login_user = (UserVipInfo) request.getAttribute("login_user");
        String iphone = login_user.getUserPhone();
        ProductInfo productInfo = productMapper.selectById(id);
        if (count > 0) {
            //判断是否有足够库存
            if (count > productInfo.getStoryCount()) {
                return productInfo.getStoryCount() - count;
            }
        }

        //获取购物车中指定商品的信息
        String hget = RedisUse.hget("car_" + iphone + "_wxb", id + "");
        //并且判断商品是否存在购物车
        if (StringUtils.isEmpty(hget)) {
            //根据商品id查询对应商品
            ProductCarInfo carInfo = productCarMapper.findProductById(id);
            //设置默认被选中
            carInfo.setCheck(true);
            //设置购买个数
            carInfo.setCount(count);
            //计算小计
            BigDecimal countMoney = carInfo.getProductPrice().multiply(new BigDecimal(count));
            carInfo.setCountMoney(countMoney);
            //将对象转成json字符串放入redis
            String productCarString = JSONObject.toJSONString(carInfo);
            RedisUse.hset("car_" + iphone + "_wxb", id + "", productCarString);
            //获取一共多少个商品
            long hlen = RedisUse.hlen("car_" + iphone + "_wxb");
            return (int) hlen;
        } else {//在购物车中已经存在
            //将json字符串转为java对象
            ProductCarInfo carInfo = JSONArray.parseObject(hget, ProductCarInfo.class);
            //修改个数
            carInfo.setCount(carInfo.getCount() + count);
            //验证库存
            //判断库存是否够
            ProductInfo product = productMapper.selectById(id);
            if (carInfo.getCount() > product.getStoryCount()) {
                return product.getStoryCount() - carInfo.getCount();
            }
            //计算小计
            BigDecimal mony = carInfo.getProductPrice().multiply(new BigDecimal(carInfo.getCount()));
            carInfo.setCountMoney(mony);
            String string = JSONObject.toJSONString(carInfo);
            RedisUse.hset("car_" + iphone + "_wxb", id + "", string);
            //怎么获取商品种类的个数
            long hlen = RedisUse.hlen("car_" + iphone + "_wxb");
            return (int) hlen;
        }
    }
}
