package com.sll.common.utils;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: showroommanageservice
 * @description: redis工具类
 * @author: shill12
 * @create: 2019-12-12 16:57
 **/
@Configuration
public class RedisUtil {

    public static String msgKey = "msg";
    public static String codeKey = "code";
    public static String codeOk = "200";
    public static String codeError = "201";

    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public static boolean expire(RedisTemplate redisTemplate, String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(RedisTemplate redisTemplate, String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(RedisTemplate redisTemplate, String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static void del(RedisTemplate redisTemplate, String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    // ============================String=============================


    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(RedisTemplate redisTemplate, String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(RedisTemplate redisTemplate, String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(RedisTemplate redisTemplate, String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public static long incr(RedisTemplate redisTemplate, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static long decr(RedisTemplate redisTemplate, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 检查用户是否符合当前限制条件（同一用户，同一个上传路径，半小时之内上传次数不能超过五次）
     *
     * @param redisTemplate
     * @param userName
     * @return
     */
    public static Map<String, String> checkUser(RedisTemplate redisTemplate, String userName) {

        Integer checkCount = 5;//允许上传次数上限

        Map<String, String> map = new HashMap<>(3);

        //首先判断 缓存是否有该用户，有 进行次数判断  没有 添加缓冲
        Boolean aBoolean = redisTemplate.hasKey(userName);
        if (aBoolean == true) {
            Object o = redisTemplate.opsForValue().get(userName);
            int count = Integer.parseInt(o.toString());

            long expire = RedisUtil.getExpire(redisTemplate, userName);
            long minute = (expire % (60 * 60)) / 60;

            if (count < checkCount) {
                RedisUtil.incr(redisTemplate, userName, 1);
                map.put(codeKey, codeOk);
                int residuecount = checkCount-count-1;
                map.put(msgKey, "在"+minute+"分钟内，剩余允许上传"+residuecount+"次文件");
            } else {
                map.put(codeKey, codeError);
                map.put(msgKey, "你在30分钟内已经上传5次,请" + minute + "分钟后再上传");
                return map;
            }
        } else {
            redisTemplate.opsForValue().set(userName, 1, 900, TimeUnit.SECONDS);
            map.put(codeKey, codeOk);
            map.put(msgKey, "允许上传文件");
        }
        return map;
    }

}
