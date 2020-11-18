package com.cr.toutiao.service.impl;

import com.cr.toutiao.service.LikeService;
import com.cr.toutiao.util.JedisAdapter;
import com.cr.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-18 16:09
 */
@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    @Override
    public long like(int userId, int entityType, int entityId) {
        //修改当前点赞状态
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String value = String.valueOf(userId);
        if (jedisAdapter.sismember(likeKey, value)) {
            jedisAdapter.srem(likeKey, value);
        } else {
            jedisAdapter.sadd(likeKey, value);
        }
        //从点踩里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, value);
        return jedisAdapter.scard(likeKey) - jedisAdapter.scard(disLikeKey);
    }

    @Override
    public long disLike(int userId, int entityType, int entityId) {
        //修改当前点踩状态
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        String value = String.valueOf(userId);
        if (jedisAdapter.sismember(disLikeKey, value)) {
            jedisAdapter.srem(disLikeKey, value);
        } else {
            jedisAdapter.sadd(disLikeKey, value);
        }
        //从点赞里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, value);
        return jedisAdapter.scard(likeKey) - jedisAdapter.scard(disLikeKey);
    }
}
