package com.cr.toutiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.User;
import com.cr.toutiao.mapper.UserMapper;
import com.cr.toutiao.service.UserService;
import com.cr.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

    @Override
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>(3);

        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name", username));

        if (user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        // 密码强度
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        userMapper.insert(user);

        return map;
    }
}
