package com.fly.robot.dao;

import com.fly.robot.pojo.TableLiveWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TableLiveWeatherRepository extends JpaRepository<TableLiveWeather, Long> {

    //查询最新的一条实时天气数据
    TableLiveWeather findFirstByOrderByCreateAtDesc();

    //查找某city_id中最新的一条实时天气数据
    List<TableLiveWeather> findFirstByCityIdOrderByCreateAtDesc(String cityId);
}
