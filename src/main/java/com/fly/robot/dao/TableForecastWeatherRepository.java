package com.fly.robot.dao;

import com.fly.robot.pojo.TableForecastWeather;
import com.fly.robot.pojo.TableLiveWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableForecastWeatherRepository extends JpaRepository<TableForecastWeather, Long> {
    //查找最新的一条天气预报数据
    TableForecastWeather findFirstByOrderByCreateAtDesc();
}
