package com.fly.robot.pojo;

import java.util.List;

/**
 * 实时天气结果DTO
 */
public class LiveWeatherDTO {
    private String status;//返回状态 1：成功 2：失败
    private String count;//返回结果总数目
    private String info;//返回的状态信息
    private String infocode;//返回状态说明  10000 代表正确
    private List<LiveWeather> lives;//实况天气数据信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<LiveWeather> getLives() {
        return lives;
    }

    public void setLives(List<LiveWeather> lives) {
        this.lives = lives;
    }

    public static class LiveWeather {
        private String province;//省份名
        private String city;//城市名
        private String adcode;//区域编码
        private String weather;//天气现象（汉字描述）
        private String temperature;//实时气温，单位：摄氏度
        private String winddirection;//风向描述
        private String windpower;//风力级别，单位：级
        private String humidity;//空气湿度
        private String reporttime;//数据发布的时间
        private String temperature_float;
        private String humidity_float;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWinddirection() {
            return winddirection;
        }

        public void setWinddirection(String winddirection) {
            this.winddirection = winddirection;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }

        public String getTemperature_float() {
            return temperature_float;
        }

        public void setTemperature_float(String temperature_float) {
            this.temperature_float = temperature_float;
        }

        public String getHumidity_float() {
            return humidity_float;
        }

        public void setHumidity_float(String humidity_float) {
            this.humidity_float = humidity_float;
        }

        @Override
        public String toString() {
            return "LiveWeather{" +
                    "province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", adcode='" + adcode + '\'' +
                    ", weather='" + weather + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", winddirection='" + winddirection + '\'' +
                    ", windpower='" + windpower + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", reporttime='" + reporttime + '\'' +
                    ", temperature_float='" + temperature_float + '\'' +
                    ", humidity_float='" + humidity_float + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LiveWeatherDTONew{" +
                "status='" + status + '\'' +
                ", count='" + count + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", lives=" + lives +
                '}';
    }
}
