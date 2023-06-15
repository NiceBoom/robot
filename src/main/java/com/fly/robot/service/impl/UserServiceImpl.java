package com.fly.robot.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.UserRepository;
import com.fly.robot.entity.User;
import com.fly.robot.pojo.AliCode;
import com.fly.robot.pojo.UserCode;
import com.fly.robot.service.RedisService;
import com.fly.robot.service.UserService;
import com.fly.robot.util.JwtTokenUtil;
import com.fly.robot.util.SnowflakeIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;
    @Value("${ali.sms.access-key-id}")
    private String accessKeyId;
    @Value("${ali.sms.access-key-secret}")
    private String accessKeySecret;

    @Value("${ali.msg-auth-code-expire.login}")
    private Long loginAuthCodeExpiration;
    @Value("${ali.msg-auth-code-expire.register}")
    private Long registerAuthCodeExpiration;
    @Value("${ali.sms.send-msg-code-address}")
    private String sendMsgCodeAddress;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

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
        //TODO 添加密码加密算法
        LOGGER.info("用户信息为" + user.toString());
        userRepository.save(user);
        return UserCode.CREATE_USER_SUCCESS;
    }

    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return jwtToken
     */
    @Override
    public String userLogin(String username, String password) {
        String token = null;
        User user = userRepository.findByUsername(username);
        if (user == null)
            return UserCode.LOGIN_USER_ERROR_PASSWORD_FAIL;
        LOGGER.info("从数据库根据用户名查找的用户信息为：" + user.toString());
        if (UserCode.ACCOUNT_FAIL.equals(user.getStatus()))
            return UserCode.LOGIN_USER_ERROR_ACCOUNT_FAIL;
        String password1 = user.getPassword();
        if (!password.equals(password1))
            return UserCode.LOGIN_USER_ERROR_PASSWORD_FAIL;
        token = jwtTokenUtil.generateToken(user);
        return token;
    }

    /**
     * 验证码登录
     * @param phoneNumber 手机号
     * @return
     */
    @Override
    public String authCodeLogin(String phoneNumber) {
        String token = null;
        User user = userRepository.findByPhone(phoneNumber);
        if (user == null)
            throw new RuntimeException("该用户不存在，检验手机号");
        LOGGER.info("从数据库根据用户名查找的用户信息为：" + user.toString());
        if (UserCode.ACCOUNT_FAIL.equals(user.getStatus()))
            throw new RuntimeException("您的账户已被封禁");
        token = jwtTokenUtil.generateToken(user);
        return token;
    }

    /**
     * 使用阿里云短信发送注册账户短信验证码
     *
     * @param phoneNumber 手机号
     */
    @Override
    public String sendRegisterMsgAuthCode(String phoneNumber) throws Exception {
        if (userRepository.findByPhone(phoneNumber) != null)
            return UserCode.SEND_REGISTER_AUTH_CODE_FAIL_ACCOUNT_READY;
        return sendAuthCodeMsg(phoneNumber, AliCode.SEND_MSG_REGISTER_MODEL_CODE);
    }

    /**
     * 使用阿里云短信发送登录短信验证码
     *
     * @param phoneNumber 手机号
     */
    @Override
    public String sendLoginMsgAuthCode(String phoneNumber) throws Exception {
        if (userRepository.findByPhone(phoneNumber) == null) {
            return UserCode.SEND_LOGIN_AUTH_CODE_FAIL_ACCOUNT_NOT_READY;
        }
        return sendAuthCodeMsg(phoneNumber, AliCode.SEND_MSG_LOGIN_MODEL_CODE);
    }

    /**
     * 发送验证码
     * @param phoneNumber 手机号
     * @param modelCode 短信模板代码
     * @return
     * @throws Exception
     */

    private String sendAuthCodeMsg(String phoneNumber, String modelCode) throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(sendMsgCodeAddress);
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(AliCode.SEND_MSG_VERIFICATION_CODE_SIGNATURE)
                .setTemplateCode(modelCode)
                .setTemplateParam(getAuthCode(phoneNumber));
        LOGGER.info("发送短信的请求数据为:" + sendSmsRequest.toString());
        try {
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            LOGGER.info(sendSmsResponse.toString());
        } catch (TeaException error) {
            LOGGER.info(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            LOGGER.info(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
        return UserCode.SEND_AUTH_CODE_MSG_SUCCESS;
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber 手机号
     * @return
     */
    private String getAuthCode(String phoneNumber) {
        String authCode = redisService.get(phoneNumber);
        if (authCode != null) {
            return authCode;
        }
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        redisService.set(phoneNumber, String.valueOf(code));
        redisService.expire(phoneNumber, 15 * 60);
        return String.valueOf(code);
    }

    /**
     * 校验验证码
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     * @return
     */
    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        if (redisService.get(phoneNumber) == null)
            return false;
        return code.equals(redisService.get(phoneNumber));
    }

}
