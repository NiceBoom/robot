package com.fly.robot.pojo;

public class GetTenantAccessTokenResponseDto {
    public class ResponseDTO {
        private int code;
        private String msg;
        private String tenant_access_token;
        private int expire;

        // 构造函数
        public ResponseDTO() {}

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

        public String getTenant_access_token() {
            return tenant_access_token;
        }

        public void setTenant_access_token(String tenant_access_token) {
            this.tenant_access_token = tenant_access_token;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }
    }

}
