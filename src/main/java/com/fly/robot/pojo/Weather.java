package com.fly.robot.pojo;

import javax.persistence.*;

@Entity
@Table(name = "weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "weather_id")
    private Integer weatherId;

    @Column(name = "weather_repo")
    private String weatherRepo;

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
