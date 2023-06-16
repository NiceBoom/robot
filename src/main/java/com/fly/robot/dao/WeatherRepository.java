package com.fly.robot.dao;

import com.fly.robot.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    //根据cityId与天气类型查询最新的一条天气数据
    List<Weather> findFirstByCityIdAndWeatherTypeOrderByCreateAtDesc(String cityId, String weatherType);
}
