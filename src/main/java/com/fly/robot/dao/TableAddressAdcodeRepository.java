package com.fly.robot.dao;

import com.fly.robot.pojo.TableAddressAdcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableAddressAdcodeRepository extends JpaRepository<TableAddressAdcode, Long> {
    //根据地址查找adcode数据
    List<TableAddressAdcode> findByAddress(String address);
}