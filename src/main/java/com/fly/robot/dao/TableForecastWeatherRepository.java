package com.fly.robot.dao;

import com.fly.robot.pojo.TableForecastWeather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableForecastWeatherRepository extends JpaRepository<TableForecastWeather, Long> {
    //查找最新的一条天气预报数据
    TableForecastWeather findFirstByOrderByCreateAtDesc();
}
