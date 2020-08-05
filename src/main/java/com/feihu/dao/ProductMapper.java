package com.feihu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feihu.entity.ProductCarInfo;
import com.feihu.entity.ProductInfo;

import java.util.List;

public interface ProductMapper extends BaseMapper<ProductInfo> {
    List<ProductInfo> findHotAll();

    List<ProductInfo> findProductByTypeId(ProductInfo productInfo);

    List<ProductInfo> findProductAllByProductId(Integer id);

    String findAreaNameByAreaIds(Integer areaId);

}
