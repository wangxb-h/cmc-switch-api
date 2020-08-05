package com.feihu.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.feihu.common.JsonData;
import com.feihu.entity.ProductInfo;
import com.feihu.service.ProductService;
import com.feihu.utils.RedisUse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("product")
@Api(description = "商品Controller")
public class ProductController {
    @Autowired
    private ProductService productService;
    //查询热销数据
    @RequestMapping("findHotAll")
    public JsonData findHotAll(){
        String hot_json = RedisUse.get("hot_json");
        if (StringUtils.isEmpty(hot_json) == true) {
            List<ProductInfo> list = productService.findHotAll();
            String string = JSONObject.toJSONString(list);
            RedisUse.set("hot_json", string);
            return JsonData.getJsonSuccess(string);
        } else {
            return JsonData.getJsonSuccess(hot_json);
        }
    }

    //根据类型id查询对应商品
    @RequestMapping("findProductByTypeId")
    public JsonData findProductByTypeId(ProductInfo productInfo){
        List<ProductInfo> list=productService.findProductByTypeId(productInfo);
        return JsonData.getJsonSuccess(list);
    }

    //根据商品id查询商品详情
    @RequestMapping("findProductAllByProductId")
    public JsonData findProductAllByProductId(Integer id){
        List<ProductInfo> list=productService.findProductAllByProductId(id);
        return JsonData.getJsonSuccess(list);
    }
}
