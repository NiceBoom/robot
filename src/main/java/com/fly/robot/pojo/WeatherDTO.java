package com.fly.robot.pojo;

import java.util.ArrayList;

//天气DTO
public class WeatherDTO {
    private String status;//返回状态
    private String count;//返回结果总目数
    private String info;//返回的状态信息
    private String infocode;//返回状态说明，10000 代表正确
    private ArrayList<Forecast> forecasts;//预报天气信息数据
    private ArrayList<Live> lives;//实况天气数据信息

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

    public ArrayList<Live> getLives() {
        return lives;
    }

    public void setLives(ArrayList<Live> lives) {
        this.lives = lives;
    }

    public ArrayList<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(ArrayList<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public static class Forecast {
        private String city;//城市名称
        private String adcode;//城市编码
        private String province;//省份名称
        private String reporttime;//预报发布时间
        private ArrayList<Cast> casts;//预报数据天气数据信息，按顺序为当天、第二天、第三天的预报数据


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

        public ArrayList<Cast> getCasts() {
            return casts;
        }

        public void setCasts(ArrayList<Cast> casts) {
            this.casts = casts;
        }

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

            public float getDaytemp_float() {
                return daytemp_float;
            }

            public void setDaytemp_float(float daytemp_float) {
                this.daytemp_float = daytemp_float;
            }

            public float getNighttemp_float() {
                return nighttemp_float;
            }

            public void setNighttemp_float(float nighttemp_float) {
                this.nighttemp_float = nighttemp_float;
            }
        }

    }

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

        public float getTemperature_float() {
            return temperature_float;
        }

        public void setTemperature_float(float temperature_float) {
            this.temperature_float = temperature_float;
        }

        public float getHumidity_float() {
            return humidity_float;
        }

        public void setHumidity_float(float humidity_float) {
            this.humidity_float = humidity_float;
        }

    }

}

