package com.fly.robot.pojo;

import javax.persistence.*;

/**
 *  mysql中的weather表格
 *  此表格仅供链接到mysql测试用
 *  不是正式的存储位置
 */
@Entity
@Table(name = "weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //自增主键weather_id
    @Column(name = "weather_id")
    private Integer weatherId;

    //天气报告数据
    @Column(name = "weather_repo")
    private String weatherRepo;

    //创建时间
    @Column(name = "create_at")
    private String createAt;

    public Weather(Integer weatherId, String weatherRepo, String createAt) {
        this.weatherId = weatherId;
        this.weatherRepo = weatherRepo;
        this.createAt = createAt;
    }
    public Weather(){
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
