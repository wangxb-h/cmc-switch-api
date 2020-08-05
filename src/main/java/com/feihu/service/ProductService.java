package com.feihu.service;

import com.feihu.entity.ProductInfo;

import java.util.List;

public interface ProductService {
    List<ProductInfo> findHotAll();

    List<ProductInfo> findProductByTypeId(ProductInfo productInfo);

    //根据商品id查询商品详情
    List<ProductInfo> findProductAllByProductId(Integer id);
}
