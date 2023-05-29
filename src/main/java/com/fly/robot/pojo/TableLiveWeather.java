package com.fly.robot.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * mysql中的tb_live_weather表格
 */
@Entity
@Table(name = "tb_live_weather")
public class TableLiveWeather {

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
    @Column(name = "live_weather")
    private String liveWeather;

    //创建时间
    @Column(name = "create_at")
    private LocalDateTime createAt;

    public TableLiveWeather() {
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
