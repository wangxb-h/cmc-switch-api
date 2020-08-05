package com.feihu.service;

import com.feihu.entity.UserVipInfo;

public interface LoginService {
    UserVipInfo findUser(String telephone);

    UserVipInfo findUserVipByIphone(String iphone);
}
