package com.fly.robot.dao;

import com.fly.robot.pojo.TableForecastWeather;
import com.fly.robot.pojo.TableLiveWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableForecastWeatherRepository extends JpaRepository<TableForecastWeather, Long> {

    //查找所有天气预报
    List<TableForecastWeather> findAll();
}
