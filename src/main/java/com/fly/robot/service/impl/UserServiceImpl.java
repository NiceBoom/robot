package com.fly.robot.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.UserRepository;
import com.fly.robot.dto.PhoneRegisterParam;
import com.fly.robot.dto.UpdateUserInfoParam;
import com.fly.robot.dto.UserInfoResultDTO;
import com.fly.robot.entity.User;
import com.fly.robot.pojo.AliCode;
import com.fly.robot.pojo.UserCode;
import com.fly.robot.service.RedisService;
import com.fly.robot.service.UserService;
import com.fly.robot.util.JwtTokenUtil;
import com.fly.robot.util.SnowflakeIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * 使用阿里云短信发送注册账户短信验证码
     *
     * @param phoneNumber 手机号
     */
    @Override
    public void sendRegisterMsgAuthCode(String phoneNumber) throws Exception {
        User userByPhone = userRepository.findByPhone(phoneNumber);
        //如果账户已存在并且已被禁用
        if (userByPhone != null && UserCode.USER_STATUS_DISABLE.equals(userByPhone.getStatus()))
            throw new RuntimeException("您的手机号已被禁用，请联系管理员");
        if (userByPhone != null)
            throw new RuntimeException("您的手机号已被注册，请登录");
        sendAuthCodeMsg(phoneNumber, AliCode.SEND_MSG_REGISTER_MODEL_CODE, getAuthCode(phoneNumber, UserCode.REGISTER_AUTH, registerAuthCodeExpiration));
    }

    /**
     * 使用阿里云短信发送登录短信验证码
     *
     * @param phoneNumber 手机号
     */
    @Override
    public void sendLoginMsgAuthCode(String phoneNumber) throws Exception {
        User userByPhone = userRepository.findByPhone(phoneNumber);
        if (userByPhone == null) {
            throw new RuntimeException("您的手机号还没有注册，请先注册再登录");
        }
        if (userByPhone.getStatus().equals(UserCode.USER_STATUS_DISABLE))
            throw new RuntimeException("您的帐户已被禁用，请联系管理员");
        sendAuthCodeMsg(phoneNumber, AliCode.SEND_MSG_LOGIN_MODEL_CODE, getAuthCode(phoneNumber, UserCode.LOGIN_AUTH, loginAuthCodeExpiration));
    }

    //手机号注册
    @Override
    public void phoneRegister(PhoneRegisterParam phoneRegisterParam) {
        if (!verifyCode(phoneRegisterParam.getPhone(), UserCode.REGISTER_AUTH, phoneRegisterParam.getAuthCode()))
            throw new RuntimeException("您的验证码不正确，请重新输入");
        User userByPhone = userRepository.findByPhone(phoneRegisterParam.getPhone());
        if (userByPhone != null)
            throw new RuntimeException("您的手机号已被注册，请登录");
        User user = new User();
        BeanUtils.copyProperties(phoneRegisterParam, user, "authCode");
        user.setUserId(String.valueOf(new SnowflakeIdUtils(10, 10).nextId()));
        LocalDateTime nowTime = LocalDateTime.now();
        user.setCreated(nowTime);
        user.setUpdated(nowTime);
        String userName = String.valueOf(new SnowflakeIdUtils(2, 3).nextId());
        //校验数据库中是否存在该用户名，若存在则重新获取
        while (userRepository.findByUsername(userName) != null) {
            userName = String.valueOf(new SnowflakeIdUtils(2, 3).nextId());
        }
        user.setUsername(userName);
        user.setNikeName(userName);
        user.setStatus(UserCode.USER_STATUS_ENABLE);
        user.setPermission(UserCode.REGULAR_USER_AUTH);
        user.setSex(UserCode.USER_SEX_NON);
        //TODO 添加密码加密算法
        userRepository.save(user);
        redisService.remove(phoneRegisterParam.getPhone());
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
            throw new RuntimeException("用户名不存在，请重试。");
        if (UserCode.USER_STATUS_DISABLE.equals(user.getStatus()))
            throw new RuntimeException("该账户已被禁用，请联系管理员。");
        String password1 = user.getPassword();
        //TODO 加密密码与校验密码
        if (!password.equals(password1))
            throw new RuntimeException("密码错误，请重试。");
        token = jwtTokenUtil.generateToken(user);
        userRepository.updateLastLoginTime(user.getUserId(), LocalDateTime.now());
        return token;
    }

    /**
     * 验证码登录
     *
     * @param phone 手机号
     * @return
     */
    @Override
    public String authCodeLogin(String phone) {
        String token = null;
        User user = userRepository.findByPhone(phone);
        if (user == null)
            throw new RuntimeException("该用户不存在，请检查手机号");
        LOGGER.info("从数据库根据用户名查找的用户信息为：" + user.toString());
        if (UserCode.USER_STATUS_DISABLE.equals(user.getStatus()))
            throw new RuntimeException("您的账户已被封禁");
        token = jwtTokenUtil.generateToken(user);
        userRepository.updateLastLoginTime(user.getUserId(), LocalDateTime.now());
        redisService.remove(UserCode.LOGIN_AUTH + phone);
        return token;
    }

    //根据token获取用户信息
    @Override
    public UserInfoResultDTO findUserInfoByToken(String token) {
        String username = jwtTokenUtil.getUserNameFromToken(token);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (jwtTokenUtil.validateToken(token, user)) {
                UserInfoResultDTO userInfoResultDTO = new UserInfoResultDTO();
                BeanUtils.copyProperties(user, userInfoResultDTO);
                return userInfoResultDTO;
            }
            throw new RuntimeException("您的登录已过期，请重新登陆");
        }
        throw new RuntimeException("您的登录已过期，请重新登陆");
    }

    @Override
    public void updateUserInfo(UpdateUserInfoParam updateUserInfoParam, String token) {
        String username = jwtTokenUtil.getUserNameFromToken(token);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (jwtTokenUtil.validateToken(token, user)) {
                BeanUtils.copyProperties(updateUserInfoParam, user);
                userRepository.save(user);
            }
            throw new RuntimeException("您的登录已过期，请重新登陆");
        }
        throw new RuntimeException("您的登录已过期，请重新登陆");
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber 手机号
     * @param modelCode   短信模板代码
     * @return
     * @throws Exception
     */

    private void sendAuthCodeMsg(String phoneNumber, String modelCode, String authCode) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> code = new HashMap<>();
        code.put("code", authCode);

        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(sendMsgCodeAddress);
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(AliCode.SEND_MSG_VERIFICATION_CODE_SIGNATURE)
                .setTemplateCode(modelCode)
                .setTemplateParam(objectMapper.writeValueAsString(code));
        SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
        if (sendSmsResponse.getStatusCode() != 200 || !"OK".equals(sendSmsResponse.getBody().getMessage()))
            throw new RuntimeException("发送短信失败，请检查第三方短信服务接口:" + sendSmsResponse.getBody().getMessage());
        LOGGER.info("发送阿里云短信三方接口返回的消息提示：" + sendSmsResponse.getBody().getMessage());
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber  手机号
     * @param authCodeType 验证码类型
     * @param expire       验证码过期时间
     * @return
     */
    private String getAuthCode(String phoneNumber, String authCodeType, Long expire) {
        String authCodeKey = authCodeType + phoneNumber;
        String authCode = redisService.get(authCodeKey);
        if (authCode != null) {
            return authCode;
        }
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        redisService.set(authCodeKey, String.valueOf(code));
        redisService.expire(authCodeKey, expire);
        return String.valueOf(code);
    }

    /**
     * 校验验证码
     *
     * @param phoneNumber  手机号
     * @param authCodeType 验证码类型
     * @param code         验证码
     * @return
     */
    @Override
    public boolean verifyCode(String phoneNumber, String authCodeType, String code) {
        String authCodeKey = authCodeType + phoneNumber;
        if (redisService.get(authCodeKey) == null)
            return false;
        return code.equals(redisService.get(authCodeKey));
    }

}
