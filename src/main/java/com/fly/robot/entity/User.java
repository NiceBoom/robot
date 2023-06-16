package com.fly.robot.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user")
@Data
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "created")
    private LocalDateTime created;//创建时间
    @Column(name = "updated")
    private LocalDateTime updated;//更新时间
    @Column(name = "nike_name")
    private String nikeName;//昵称
    @Column(name = "name")
    private String name;//真实姓名
    @Column(name = "status")
    private Integer status;//账户状态，0正常，1被禁用 默认0
    @Column(name = "head_pic")
    private String headPic;//头像存储路径
    @Column(name = "sex")
    private Integer sex;//性别 0未知，1男2女，默认0
    @Column(name = "birthday")
    private LocalDateTime birthday;
    @Column(name = "identity")
    private String identity;//身份证号
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;//最后一次登陆时间
    @Column(name = "permission")
    private Integer permission;//用户权限
}
