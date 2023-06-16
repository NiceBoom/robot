package com.fly.robot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetTenantAccessTokenResDTO {
    @JsonProperty("code")
    private Integer code;//错误码 非0取值表示失败
    @JsonProperty("msg")
    private String msg;//错误描述
    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;//访问凭证
    @JsonProperty("expire")
    private Integer expire;//过期时间 单位为秒
}
