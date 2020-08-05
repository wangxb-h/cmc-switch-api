package com.feihu.service.impl;

import com.feihu.dao.TypeMapper;
import com.feihu.entity.TypeInfo;
import com.feihu.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeMapper typeMapper;

    @Override
    public List<TypeInfo> findType() {
        return typeMapper.selectList(null);
    }

    @Override
    public TypeInfo toUpdate(Integer id) {
        return typeMapper.selectById(id);
    }

    @Override
    public void update(TypeInfo typeInfo) {
        typeMapper.updateById(typeInfo);
    }

    @Override
    public void delete(Integer id) {
        typeMapper.deleteById(id);
    }
}
