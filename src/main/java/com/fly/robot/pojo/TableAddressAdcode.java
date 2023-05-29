package com.fly.robot.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_address_adcode")
public class TableAddressAdcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //自增主键weather_id
    @Column(name = "address_id")
    private Integer addressId;

    //中文地址address
    @Column(name = "address")
    private String address;

    //地区代码adcode
    @Column(name = "adcode")
    private String adcode;

    //创建时间
    @Column(name = "create_at")
    private LocalDateTime createAt;

    public TableAddressAdcode() {
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
