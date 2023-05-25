package com.fly.robot.pojo;

public class GetTenantAccessTokenResponseDto {
        private int code;
        private String msg;
        private String tenantAccessToken;
        private int expire;

        // 构造函数
        public GetTenantAccessTokenResponseDto() {}

        // Getters and Setters
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
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

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

}
