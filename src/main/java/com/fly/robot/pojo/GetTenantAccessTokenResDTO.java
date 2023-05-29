package com.fly.robot.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetTenantAccessTokenResDTO {
    @JsonProperty("code")
    private Integer code;//错误码 非0取值表示失败
    @JsonProperty("msg")
    private String msg;//错误描述
    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;//访问凭证
    @JsonProperty("expire")
    private Integer expire;//过期时间 单位为秒

    // 默认构造函数
    public GetTenantAccessTokenResDTO() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTenantAccessToken() {
        return tenantAccessToken;
    }

    public void setTenantAccessToken(String tenantAccessToken) {
        this.tenantAccessToken = tenantAccessToken;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
