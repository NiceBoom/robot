package com.fly.robot.dao;

import com.fly.robot.pojo.TableLiveWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableLiveWeatherRepository extends JpaRepository<TableLiveWeather, Long> {

    //查找所有实时天气
    List<TableLiveWeather> findAll();
}
