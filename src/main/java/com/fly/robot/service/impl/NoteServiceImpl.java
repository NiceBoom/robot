package com.fly.robot.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.controller.FlyBookController;
import com.fly.robot.pojo.AliCode;
import com.fly.robot.service.NoteService;
import com.fly.robot.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private RedisService redisService;
    @Value("${ali.sms.access-key-id}")
    private String accessKeyId; //阿里短信accessKeyId
    @Value("${ali.sms.access-key-secret}")
    private String accessKeySecret; //阿里短信accessKeySecret
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private final static Logger LOGGER = LoggerFactory.getLogger(FlyBookController.class);

    /**
     * 发送验证码
     * @param phoneNumber 手机号
     * @throws Exception
     */
    @Override
    public void sendAuthCode(String phoneNumber) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> code = new HashMap<>();
        code.put("code",getCode(phoneNumber));
        Client client = createClient(accessKeyId, accessKeySecret);
        SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(AliCode.SEND_MSG_VERIFICATION_CODE_SIGNATURE)//签名代码
                .setTemplateCode(AliCode.SEND_MSG_VERIFICATION_CODE_MODEL)//模板代码
                .setTemplateParam(objectMapper.writeValueAsString(code));//手机号
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
     * @param phoneNumber 手机号
     * @return
     */
    private String getCode(String phoneNumber) {
        String authCode = redisService.get(phoneNumber);
        if(authCode != null){
            return authCode;
        }
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        redisService.set(phoneNumber, String.valueOf(code));
        redisService.expire(phoneNumber, 5 * 60);
        return String.valueOf(code);
    }

    /**
     * 校验验证码
     * @param phoneNumber 手机号
     * @param code 验证码
     * @return
     */
    @Override
    public boolean verifyCode(String phoneNumber, String code){
        if (redisService.get(phoneNumber) == null)
            return false;
        return code.equals(redisService.get(phoneNumber));
    }
}
