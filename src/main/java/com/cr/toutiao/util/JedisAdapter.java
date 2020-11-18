package com.cr.toutiao.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author cr
 * @date 2020-11-18 15:59
 */
@Slf4j
@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    public long sadd(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sadd(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return 0;
        }
    }

    public long srem(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return 0;
        }
    }

    public boolean sismember(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return false;
        }
    }

    public long scard(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.scard(key);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return 0;
        }
    }
}
