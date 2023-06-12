package com.fly.robot.service.impl;

import com.fly.robot.dao.UserRepository;
import com.fly.robot.entity.User;
import com.fly.robot.pojo.UserCode;
import com.fly.robot.service.UserService;
import com.fly.robot.util.JwtTokenUtil;
import com.fly.robot.util.SnowflakeIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    public String register(User user) {
        if (user.getUsername() != null && userRepository.findByUsername(user.getUsername()) != null)
            return UserCode.CREATE_USER_ERROR_USER_NAME_REPETITION;
        if (user.getPhone() != null && userRepository.findByPhone(user.getPhone()) != null)
            return UserCode.CREATE_USER_ERROR_PHONE_NUMBER_REPETITION;
        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()) != null)
            return UserCode.CREATE_USER_ERROR_EMAIL_REPETITION;

        user.setUserId(String.valueOf(new SnowflakeIdUtils(10, 10).nextId()));
        userRepository.save(user);
        return UserCode.CREATE_USER_SUCCESS;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return jwtToken
     */
    @Override
    public String login(String username, String password) {
        String token = null;
        User user = userRepository.findByUsername(username);
        if (user == null)
            return UserCode.LOGIN_USER_ERROR_PASSWORD_FAIL;
        if (UserCode.ACCOUNT_FAIL.equals(user.getStatus()))
            return UserCode.LOGIN_USER_ERROR_ACCOUNT_FAIL;
        if (password.equals(user.getPassword()))
            return UserCode.LOGIN_USER_ERROR_PASSWORD_FAIL;
        token = jwtTokenUtil.generateToken(user);
        return token;
    }

}
