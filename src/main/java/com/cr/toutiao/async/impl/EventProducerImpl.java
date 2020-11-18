package com.cr.toutiao.async.impl;

import com.alibaba.fastjson.JSONObject;
import com.cr.toutiao.async.EventModel;
import com.cr.toutiao.async.EventProducer;
import com.cr.toutiao.util.JedisAdapter;
import com.cr.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-18 23:06
 */
@Service
public class EventProducerImpl implements EventProducer {
    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
