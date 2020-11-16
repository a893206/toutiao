package com.cr.toutiao.service;

import com.cr.toutiao.entity.User;

/**
 * @author cr
 * @date 2020-11-16 23:40
 */
public interface UserService {
    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return 用户对象
     */
    User getUser(Integer userId);
}
