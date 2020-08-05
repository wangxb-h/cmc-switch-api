package com.feihu.service;

import com.feihu.entity.TypeInfo;

import java.util.List;

public interface TypeService {
    List<TypeInfo> findType();

    TypeInfo toUpdate(Integer id);

    void update(TypeInfo typeInfo);

    void delete(Integer id);
}
