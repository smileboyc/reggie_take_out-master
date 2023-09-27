package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 龚正宇
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-12-30 15:01:56
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




