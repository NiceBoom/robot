package com.fly.robot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 短信验证码登录DTO
 */
@Getter
@Setter
@ApiModel("手机号验证码登录")
public class PhoneLoginParam {

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确，请重试。")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @NotEmpty(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确，请重试。")
    @ApiModelProperty(value = "验证码，6位数字", required = true)
    private String authCode;

}
