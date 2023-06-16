package com.fly.robot.dto;

import lombok.Data;

import java.util.ArrayList;

//天气DTO
@Data
public class WeatherDTO {
    private String status;//返回状态
    private String count;//返回结果总目数
    private String info;//返回的状态信息
    private String infocode;//返回状态说明，10000 代表正确
    private ArrayList<Forecast> forecasts;//预报天气信息数据
    private ArrayList<Live> lives;//实况天气数据信息

    @Data
    public static class Forecast {
        private String city;//城市名称
        private String adcode;//城市编码
        private String province;//省份名称
        private String reporttime;//预报发布时间
        private ArrayList<Cast> casts;//预报数据天气数据信息，按顺序为当天、第二天、第三天的预报数据

        @Data
        public static class Cast {
            private String date;
            private String week;
            private String dayweather;
            private String nightweather;
            private String daytemp;
            private String nighttemp;
            private String daywind;
            private String nightwind;
            private String daypower;
            private String nightpower;
            private float daytemp_float;
            private float nighttemp_float;
        }
    }

    @Data
    public static class Live {
        private String province;//省份名
        private String city;//城市名
        private String adcode;//区域编码
        private String weather;//天气现象（汉字描述）
        private String temperature;//实时气温，单位：摄氏度
        private String winddirection;//风向描述
        private String windpower;//风力级别，单位：级
        private String humidity;//空气湿度
        private String reporttime;//数据发布的时间
        private float temperature_float;
        private float humidity_float;
    }

}

