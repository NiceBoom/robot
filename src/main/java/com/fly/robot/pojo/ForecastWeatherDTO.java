package com.fly.robot.pojo;

import java.util.List;
//这是从高德API获取到的天气预报数据DTO
public class ForecastWeatherDTO {
    private String status;//返回状态
    private String count;//返回结果总目数
    private String info;//返回的状态信息
    private String infocode;//返回状态说明，10000 代表正确
    private List<CityForecast> forecasts;//预报天气信息数据

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

    public List<CityForecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<CityForecast> forecasts) {
        this.forecasts = forecasts;
    }
    //预报天气信息数据
    public static class CityForecast {
        private String city;//城市名称
        private String adcode;//城市编码
        private String province;//省份名称
        private String reporttime;//预报发布时间
        private List<WeatherForecast> casts;//预报数据list结构，元素cast，按顺序为当天、第二天、第三天的预报数据

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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }

        public List<WeatherForecast> getCasts() {
            return casts;
        }

        public void setCasts(List<WeatherForecast> casts) {
            this.casts = casts;
        }

        //具体预报天气数据
        public static class WeatherForecast {
            private String date;//日期
            private String week;//星期几
            private String dayweather;//白天天气现象
            private String nightweather;//晚上天气现象
            private String daytemp;//白天温度
            private String nighttemp;//晚上温度
            private String daywind;//白天风向
            private String nightwind;//晚上风向
            private String daypower;//白天风力
            private String nightpower;//晚上风力
            private String daytemp_float;
            private String nighttemp_float;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getDayweather() {
                return dayweather;
            }

            public void setDayweather(String dayweather) {
                this.dayweather = dayweather;
            }

            public String getNightweather() {
                return nightweather;
            }

            public void setNightweather(String nightweather) {
                this.nightweather = nightweather;
            }

            public String getDaytemp() {
                return daytemp;
            }

            public void setDaytemp(String daytemp) {
                this.daytemp = daytemp;
            }

            public String getNighttemp() {
                return nighttemp;
            }

            public void setNighttemp(String nighttemp) {
                this.nighttemp = nighttemp;
            }

            public String getDaywind() {
                return daywind;
            }

            public void setDaywind(String daywind) {
                this.daywind = daywind;
            }

            public String getNightwind() {
                return nightwind;
            }

            public void setNightwind(String nightwind) {
                this.nightwind = nightwind;
            }

            public String getDaypower() {
                return daypower;
            }

            public void setDaypower(String daypower) {
                this.daypower = daypower;
            }

            public String getNightpower() {
                return nightpower;
            }

            public void setNightpower(String nightpower) {
                this.nightpower = nightpower;
            }

            public String getDaytemp_float() {
                return daytemp_float;
            }

            public void setDaytemp_float(String daytemp_float) {
                this.daytemp_float = daytemp_float;
            }

            public String getNighttemp_float() {
                return nighttemp_float;
            }

            public void setNighttemp_float(String nighttemp_float) {
                this.nighttemp_float = nighttemp_float;
            }

            // Getters and setters

            @Override
            public String toString() {
                return "WeatherForecast{" +
                        "date='" + date + '\'' +
                        ", week='" + week + '\'' +
                        ", dayweather='" + dayweather + '\'' +
                        ", nightweather='" + nightweather + '\'' +
                        ", daytemp='" + daytemp + '\'' +
                        ", nighttemp='" + nighttemp + '\'' +
                        ", daywind='" + daywind + '\'' +
                        ", nightwind='" + nightwind + '\'' +
                        ", daypower='" + daypower + '\'' +
                        ", nightpower='" + nightpower + '\'' +
                        ", daytemp_float='" + daytemp_float + '\'' +
                        ", nighttemp_float='" + nighttemp_float + '\'' +
                        '}';
            }
        }

        // Getters and setters

        @Override
        public String toString() {
            return "CityForecast{" +
                    "city='" + city + '\'' +
                    ", adcode='" + adcode + '\'' +
                    ", province='" + province + '\'' +
                    ", reporttime='" + reporttime + '\'' +
                    ", casts=" + casts +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ForecastWeatherDTONew{" +
                "status='" + status + '\'' +
                ", count='" + count + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", forecasts=" + forecasts +
                '}';
    }
}

