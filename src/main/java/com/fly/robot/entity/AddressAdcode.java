package com.fly.robot.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_address_adcode")
@Data
public class AddressAdcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //自增主键id
    @Column(name = "id")
    private Integer addressId;

    //中文地址address
    @Column(name = "address")
    private String address;

    //地区代码adcode
    @Column(name = "adcode")
    private String adcode;

    @Column(name = "gaode_address_info")
    private String gaodeAddressInfo;
    //创建时间
    @Column(name = "create_at")
    private LocalDateTime createAt;

}
