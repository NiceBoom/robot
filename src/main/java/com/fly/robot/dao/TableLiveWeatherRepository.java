package com.fly.robot.dao;

import com.fly.robot.entity.TableLiveWeather;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TableLiveWeatherRepository extends JpaRepository<TableLiveWeather, Long> {

    //查询最新的一条实时天气数据
    TableLiveWeather findFirstByOrderByCreateAtDesc();
}
