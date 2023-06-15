package com.fly.robot.service;

/**
 * redis操作service
 * 对象与数组都以json形式存储
 */
public interface RedisService {

    //存储数据
    void set(String key, String value);

    //获取数据
    String get(String key);

    //设置过期时间
    boolean expire(String key, long expire);

    //获取过期时间
    Long getExpire(String key);

    //删除数据
    void remove(String key);

    /**
     * 自增操作
     *
     * @param delta 自增步长
     */
    Long increment(String key, long delta);
}
