package com.fly.robot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 短信验证码登录DTO
 */
@Getter
@Setter
public class PhoneLoginParam {
    @NotEmpty
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    @NotEmpty
    @ApiModelProperty(value = "验证码", required = true)
    private String authCode;

}
