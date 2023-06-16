package com.fly.robot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * 用户注册DTO
 */
@Getter
@Setter
public class PhoneRegisterParam {
    @NotEmpty
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    @NotEmpty
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @Email
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    @NotEmpty
    @ApiModelProperty(value = "用户手机号", required = true)
    private String phone;
    @ApiModelProperty(value = "用户昵称")
    private String nikeName;
    @ApiModelProperty(value = "用户性别", required = true, notes = "默认0未知，1男，2女")
    private Integer sex;
    @ApiModelProperty(value = "用户生日")
    private LocalDateTime birthday;
    @NotEmpty
    @ApiModelProperty(value = "真实姓名", required = true)
    private String name;
    @NotEmpty
    @ApiModelProperty(value = "身份证号", required = true)
    private String identity;
    @ApiModelProperty(value = "用户权限", required = true, notes = "0普通账户，1管理员，2超级管理员")
    private Integer permission;
    @NotEmpty
    @ApiModelProperty(value = "验证码", required = true)
    private String authCode;
}
