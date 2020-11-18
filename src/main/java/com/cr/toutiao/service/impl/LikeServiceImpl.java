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
        //在点赞集合里增加
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        //从点踩里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey) - jedisAdapter.scard(disLikeKey);
    }

    @Override
    public long disLike(int userId, int entityType, int entityId) {
        //在点踩集合里增加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        //从点赞里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey) - jedisAdapter.scard(disLikeKey);
    }
}
