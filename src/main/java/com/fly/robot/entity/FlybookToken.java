package com.fly.robot.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_flybook_token")
@Data
public class FlybookToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //自增主键id
    @Column(name = "id")
    private Integer id;

    //应用的唯一标识appId
    @Column(name = "app_id")
    private String appId;

    //应用的秘钥
    @Column(name = "app_secret")
    private String appSecret;

    //token的类型
    @Column(name = "token_type")
    private String tokenType;

    //token
    @Column(name = "token")
    private String token;

    //token过期时间 单位：秒
    @Column(name = "token_expire")
    private Integer tokenExpire;

    //创建时间
    @Column(name = "create_at")
    private LocalDateTime createAt;

}
