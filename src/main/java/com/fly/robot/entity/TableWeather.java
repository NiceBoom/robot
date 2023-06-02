package com.fly.robot.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_weather")
public class TableWeather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //自增主键weather_id
    @Column(name = "id")
    private Integer weatherId;
    //城市代码
    @Column(name = "city_id")
    private String cityId;
    //城市名称
    @Column(name = "city_name")
    private String cityName;
    //天气类型
    @Column(name = "weather_type")
    private String weatherType;
    //未来天气报告数据
    @Column(name = "forecast_weather")
    private String forecastWeather;
    //实时天气报告数据
    @Column(name = "live_weather")
    private String liveWeather;
    //创建时间
    @Column(name = "create_at")
    private LocalDateTime createAt;

    public TableWeather() {
    }

    public Integer getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Integer weatherId) {
        this.weatherId = weatherId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getForecastWeather() {
        return forecastWeather;
    }

    public void setForecastWeather(String forecastWeather) {
        this.forecastWeather = forecastWeather;
    }

    public String getLiveWeather() {
        return liveWeather;
    }

    public void setLiveWeather(String liveWeather) {
        this.liveWeather = liveWeather;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
