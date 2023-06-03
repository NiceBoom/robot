package com.fly.robot.dao;

import com.fly.robot.entity.TableAddressAdcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableAddressAdcodeRepository extends JpaRepository<TableAddressAdcode, Long> {
    //根据地址查找adcode数据
    TableAddressAdcode findByAddress(String address);
}
