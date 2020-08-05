package com.feihu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feihu.entity.UserVipInfo;

public interface LoginMapper extends BaseMapper {
    UserVipInfo findUser(String telephone);

    UserVipInfo findUserVipByIphone(String iphone);
}
