package com.fly.robot.dao;

import com.fly.robot.pojo.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    List<Weather> findAll();

}
