package com.fly.robot.service;

import com.fly.robot.entity.User;

public interface UserService {

    /**
     * 注册
     *
     * @param user
     * @return 创建状态返回代码 UserCode
     */
    String register(User user);

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return jwtToken
     */
    String login(String username, String password);
}
