package com.cr.toutiao.service.impl;

import com.cr.toutiao.entity.User;
import com.cr.toutiao.mapper.UserMapper;
import com.cr.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-16 23:41
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Integer userId) {
        return userMapper.selectById(userId);
    }
}
