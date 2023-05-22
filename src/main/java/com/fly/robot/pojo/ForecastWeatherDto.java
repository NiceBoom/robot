package com.fly.robot.pojo;

import java.util.List;

public class ForecastWeatherDto {
    private String status;

    private String count;

    private String info;

    private String infocode;

    private List<Forecasts> forecasts ;

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setCount(String count){
        this.count = count;
    }
    public String getCount(){
        return this.count;
    }
    public void setInfo(String info){
        this.info = info;
    }
    public String getInfo(){
        return this.info;
    }
    public void setInfocode(String infocode){
        this.infocode = infocode;
    }
    public String getInfocode(){
        return this.infocode;
    }
    public void setForecasts(List<Forecasts> forecasts){
        this.forecasts = forecasts;
    }
    public List<Forecasts> getForecasts(){
        return this.forecasts;
    }
}
