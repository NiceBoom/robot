package com.fly.robot.dao;

import com.fly.robot.entity.AddressAdcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressAdcodeRepository extends JpaRepository<AddressAdcode, Long> {
    //根据地址查找adcode数据
    AddressAdcode findByAddress(String address);
}
