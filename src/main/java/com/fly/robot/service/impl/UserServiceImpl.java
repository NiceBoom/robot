package com.fly.robot.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
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
    private String accessKeyId; //阿里短信accessKeyId
    @Value("${ali.sms.access-key-secret}")
    private String accessKeySecret; //阿里短信accessKeySecret
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
     * 发送验证码
     *
     * @param phoneNumber 手机号
     * @throws Exception
     */
    @Override
    public void sendAuthCode(String phoneNumber) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> code = new HashMap<>();
        code.put("code", getCode(phoneNumber));
        Client client = createClient(accessKeyId, accessKeySecret);
        SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(AliCode.SEND_MSG_VERIFICATION_CODE_SIGNATURE)//签名代码
                .setTemplateCode(AliCode.SEND_MSG_VERIFICATION_CODE_MODEL)//模板代码
                .setTemplateParam(objectMapper.writeValueAsString(code));//手机号
        LOGGER.info("发送短信的请求数据为:" + sendSmsRequest.toString());
        try {
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            LOGGER.info("发送短信的请求返回的响应为为:" + sendSmsResponse.toString());
            LOGGER.info(sendSmsResponse.toString());
        } catch (TeaException error) {
            LOGGER.info(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            LOGGER.info(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }

    //使用AK&SK初始化账号client
    private Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber 手机号
     * @return
     */
    private String getCode(String phoneNumber) {
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
