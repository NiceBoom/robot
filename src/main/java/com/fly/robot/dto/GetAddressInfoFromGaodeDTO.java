package com.fly.robot.dto;

import lombok.Data;

import java.util.List;

//这是根据地址从高德API接口获取到的详细地理数据的DTO
//一些参数在官方文档中未标注解释，所以此DTO一些数据没有解释说明
@Data
public class GetAddressInfoFromGaodeDTO {
    private String status;//返回结果状态值，0 代表请求失败，1 代表请求成功
    private String info;//当status为0时，info会返回具体错误原因，否则返回OK，info状态说明表 https://lbs.amap.com/api/webservice/guide/tools/info
    private String infocode;
    private String count;//返回结果的个数
    private List<Geocode> geocodes;//地理编码信息列表

    @Data
    public static class Geocode {
        private String formatted_address;
        private String country;//国家
        private String province;//地址所在的省份名。注：四大直辖市也算省级单位
        private String citycode;//城市编码
        private String city;//地址所在城市名
        private Object district;//地址所在的区
        private Object township;
        private Neighborhood neighborhood;
        private Building building;
        private String adcode;//区域编码
        private Object street;//街道
        private Object number;//门牌号
        private String location;//坐标点 经纬度
        private String level;//匹配级别。参见地理编码匹配级别列表

        @Data
        public static class Neighborhood {
            private Object name;
            private Object type;
        }

        @Data
        public static class Building {
            private Object name;
            private Object type;
        }
    }
}

