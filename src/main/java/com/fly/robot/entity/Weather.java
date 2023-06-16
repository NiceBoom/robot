package com.fly.robot.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_weather")
@Data
public class Weather {
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
}
