package com.fly.robot.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * mysql中的tb_forecast_weather表格
 */
@Entity
@Table(name = "tb_forecast_weather")
public class TableForecastWeather {

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

    //天气报告数据
    @Column(name = "forecast_weather")
    private String forecastWeather;

    //创建时间
    @Column(name = "create_at")
    private LocalDateTime createAt;

    public TableForecastWeather() {
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

    public String getForecastWeather() {
        return forecastWeather;
    }

    public void setForecastWeather(String forecastWeather) {
        this.forecastWeather = forecastWeather;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
