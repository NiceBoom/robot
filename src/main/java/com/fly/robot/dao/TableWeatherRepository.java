package com.fly.robot.dao;

import com.fly.robot.entity.TableWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableWeatherRepository extends JpaRepository<TableWeather, Long> {
    //根据cityId与天气类型查询最新的一条天气数据
    List<TableWeather> findFirstByCityIdAndWeatherTypeOrderByCreateAtDesc(String cityId, String weatherType);
}
