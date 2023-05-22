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

    //天气报告数据
    @Column(name = "weather")
    private String weatherRepo;

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

    public String getWeatherRepo() {
        return weatherRepo;
    }

    public void setWeatherRepo(String weatherRepo) {
        this.weatherRepo = weatherRepo;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
