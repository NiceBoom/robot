package com.fly.robot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateUserInfoParam {

    @NotEmpty(message = "用户名不能为空")
    @Pattern(regexp = "[A-Za-z\\d_\\-\\u4e00-\\u9fa5]+", message = "用户名只能含有数字与字母")
    @Size(min = 8, max = 20, message = "用户名长度应在10-30个字符之间")
    @ApiModelProperty(value = "用户名长度应在10-30个字符之间", required = true)
    private String username;

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱地址", required = true)
    private String email;

    @NotEmpty(message = "昵称不能为空")
    @Size(min = 3, max = 13, message = "用户名长度应在10-30个字符之间")
    @ApiModelProperty(value = "用户名长度应在10-30个字符之间", required = true)
    private String nikeName;//昵称

    private String name;//真实姓名
    @NotEmpty(message = "性别不能为空")
    @Pattern(regexp = "[0-2]\\d*", message = "性别设置错误，请重试")
    private Integer sex;//性别 0未知，1男2女，默认0

    private LocalDateTime birthday;
    private String identity;//身份证号
}
