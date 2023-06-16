package com.fly.robot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户名密码登录Dto
 */
@Getter
@Setter
@ApiModel("账户密码登录")
public class UserLoginParam {

    @NotEmpty(message = "用户名不能为空")
    @Size(min = 10, max = 30, message = "用户名长度应在10-30个字符之间")
    @ApiModelProperty(value = "用户名长度应在10-30个字符之间", required = true)
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$", message = "密码应以字母开头，长度在6-18之间，只能包含字符、数字和下划线")
    @ApiModelProperty(value = "密码以字母开头，长度在6-18之间，只能包含字符、数字和下划线", required = true)
    private String password;
}
