package com.fly.robot.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_flybook_token")
public class TableFlybookToken {

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

    public TableFlybookToken() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Integer tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
