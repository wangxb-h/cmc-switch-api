package com.feihu.service.impl;

import com.feihu.dao.LoginMapper;
import com.feihu.entity.UserVipInfo;
import com.feihu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserVipInfo findUser(String telephone) {
        return loginMapper.findUser(telephone);
    }
    //通过用户手机号查询该用户信息
    @Override
    public UserVipInfo findUserVipByIphone(String iphone) {
        return loginMapper.findUserVipByIphone(iphone);
    }
}
