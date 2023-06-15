package com.fly.robot.dto;

import java.util.List;

//这是根据地址从高德API接口获取到的详细地理数据的DTO
//一些参数在官方文档中未标注解释，所以此DTO一些数据没有解释说明
public class GetAddressInfoFromGaodeDTO {
    private String status;//返回结果状态值，0 代表请求失败，1 代表请求成功
    private String info;//当status为0时，info会返回具体错误原因，否则返回OK，info状态说明表 https://lbs.amap.com/api/webservice/guide/tools/info
    private String infocode;
    private String count;//返回结果的个数
    private List<Geocode> geocodes;//地理编码信息列表

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Geocode> getGeocodes() {
        return geocodes;
    }

    public void setGeocodes(List<Geocode> geocodes) {
        this.geocodes = geocodes;
    }

    public static class Geocode {
        private String formatted_address;
        private String country;//国家
        private String province;//地址所在的省份名。注：四大直辖市也算省级单位
        private String citycode;//城市编码
        private String city;//地址所在城市名
        private Object district;//地址所在的区
        private Object township;
        private Neighborhood neighborhood;
        private Building building;
        private String adcode;//区域编码
        private Object street;//街道
        private Object number;//门牌号
        private String location;//坐标点 经纬度
        private String level;//匹配级别。参见地理编码匹配级别列表

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Object getDistrict() {
            return district;
        }

        public void setDistrict(Object district) {
            this.district = district;
        }

        public Object getTownship() {
            return township;
        }

        public void setTownship(Object township) {
            this.township = township;
        }

        public Neighborhood getNeighborhood() {
            return neighborhood;
        }

        public void setNeighborhood(Neighborhood neighborhood) {
            this.neighborhood = neighborhood;
        }

        public Building getBuilding() {
            return building;
        }

        public void setBuilding(Building building) {
            this.building = building;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public Object getStreet() {
            return street;
        }

        public void setStreet(Object street) {
            this.street = street;
        }

        public Object getNumber() {
            return number;
        }

        public void setNumber(Object number) {
            this.number = number;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public static class Neighborhood {
            private Object name;
            private Object type;

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }
        }

        public static class Building {
            private Object name;
            private Object type;

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }
        }

        @Override
        public String toString() {
            return "Geocode{" +
                    "formatted_address='" + formatted_address + '\'' +
                    ", country='" + country + '\'' +
                    ", province='" + province + '\'' +
                    ", citycode='" + citycode + '\'' +
                    ", city='" + city + '\'' +
                    ", district=" + district +
                    ", township=" + township +
                    ", neighborhood=" + neighborhood +
                    ", building=" + building +
                    ", adcode='" + adcode + '\'' +
                    ", street=" + street +
                    ", number=" + number +
                    ", location='" + location + '\'' +
                    ", level='" + level + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetAddressInfoFromGaode{" +
                "status='" + status + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", count='" + count + '\'' +
                ", geocodes=" + geocodes +
                '}';
    }
}

