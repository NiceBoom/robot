package com.fly.robot.dto;

import lombok.Data;

import java.time.LocalDateTime;

//查询个人信息dto
@Data
public class UserInfoResultDTO {

    private String username;
    private String email;
    private String phone;
    private LocalDateTime created;//创建时间
    private LocalDateTime updated;//更新时间
    private String nikeName;//昵称
    private String name;//真实姓名
    private Integer sex;//性别 0未知，1男2女，默认0
    private LocalDateTime birthday;
    private String identity;//身份证号
    private Integer permission;//用户权限
    private LocalDateTime lastLoginTime;//最后一次登陆时间
}
