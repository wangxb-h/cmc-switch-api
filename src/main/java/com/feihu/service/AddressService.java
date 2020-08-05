package com.feihu.service;

import com.feihu.entity.AddressInfo;

import java.util.List;

public interface AddressService {
    List<AddressInfo> findAddress(String iphone);

    void addRess(AddressInfo addressInfo);

    void updateIscheck(Integer id);
}
