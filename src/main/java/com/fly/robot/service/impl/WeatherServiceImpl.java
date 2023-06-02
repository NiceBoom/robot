package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableAddressAdcodeRepository;
import com.fly.robot.dao.TableWeatherRepository;
import com.fly.robot.entity.*;
import com.fly.robot.pojo.*;
import com.fly.robot.service.WeatherService;
import com.fly.robot.util.FastJSONObjectToDto;
import com.fly.robot.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Autowired
    public TableAddressAdcodeRepository tableAddressAdcodeRepository;

    @Autowired
    public TableWeatherRepository tableWeatherRepository;

    /**
     * 获取天气
     *
     * @param APIkey     请求地址链接
     * @param cityId     城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
    public WeatherDTO getWeather(String APIkey, String cityId, String extensions) throws Exception {

        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime eightHoursAgo = nowTime.minus(8, ChronoUnit.HOURS);
        LocalDateTime twoHoursAgo = nowTime.minus(2, ChronoUnit.HOURS);
        //获取未来天气预报
        if (GaodeConfig.GET_FORECAST_WEATHER_CODE.equals(extensions)) {

            List<TableWeather> tableWeatherList =
                    tableWeatherRepository.findFirstByCityIdAndWeatherTypeOrderByCreateAtDesc(cityId, extensions);
            //mysql中无数据或者已过期，查询新数据并保存返回
            if (tableWeatherList.isEmpty() ||
                    tableWeatherList.get(0).getCreateAt().isBefore(eightHoursAgo)) {
                HashMap<String, String> reqParam = new HashMap<>();
                ObjectMapper dtoToJsonMapper = new ObjectMapper();
                reqParam.put("key", APIkey);
                reqParam.put("city", cityId);
                reqParam.put("extensions", extensions);
                JSONObject reqJson = HttpClient.doGet(GaodeConfig.GET_WEATHER_API_URL, reqParam);
                WeatherDTO forecastWeatherDTO = FastJSONObjectToDto.conversion(reqJson, WeatherDTO.class);

                TableWeather tableWeather = new TableWeather();
                tableWeather.setForecastWeather(dtoToJsonMapper.writeValueAsString(forecastWeatherDTO));
                tableWeather.setCreateAt(LocalDateTime.now());
                tableWeather.setCityId(forecastWeatherDTO.getForecasts().get(0).getAdcode());
                tableWeather.setCityName(forecastWeatherDTO.getForecasts().get(0).getCity());
                tableWeather.setWeatherType(extensions);
                tableWeatherRepository.save(tableWeather);
                return forecastWeatherDTO;
            }
            ObjectMapper jsonToDtoMapper = new ObjectMapper();
            return jsonToDtoMapper.readValue(tableWeatherList.get(0).getForecastWeather(), WeatherDTO.class);
        }
        //获取实时天气预报
        if (GaodeConfig.GET_LIVE_WEATHER_CODE.equals(extensions)) {
            List<TableWeather> tableWeatherList =
                    tableWeatherRepository.findFirstByCityIdAndWeatherTypeOrderByCreateAtDesc(cityId, extensions);
            //mysql无数据或数据已过期
            if (tableWeatherList.isEmpty() || tableWeatherList.get(0).getCreateAt().isBefore(twoHoursAgo)) {
                HashMap<String, String> reqParam = new HashMap<>();
                reqParam.put("key", APIkey);
                reqParam.put("city", cityId);
                reqParam.put("extensions", extensions);
                JSONObject reqJson = HttpClient.doGet(GaodeConfig.GET_WEATHER_API_URL, reqParam);
                WeatherDTO liveWeatherDTO = FastJSONObjectToDto.conversion(reqJson, WeatherDTO.class);

                ObjectMapper dtoToJsonMapper = new ObjectMapper();
                TableWeather tableWeather = new TableWeather();
                tableWeather.setLiveWeather(dtoToJsonMapper.writeValueAsString(liveWeatherDTO));
                tableWeather.setCreateAt(LocalDateTime.now());
                tableWeather.setCityId(liveWeatherDTO.getLives().get(0).getAdcode());
                tableWeather.setCityName(liveWeatherDTO.getLives().get(0).getCity());
                tableWeather.setWeatherType(extensions);
                tableWeatherRepository.save(tableWeather);
                return liveWeatherDTO;
            }
            ObjectMapper jsonToDtoMapper = new ObjectMapper();
            return jsonToDtoMapper
                    .readValue(tableWeatherList.get(0).getLiveWeather(), WeatherDTO.class);
        }
        return null;
    }

    /**
     * 查找消息地址的具体数据
     *
     * @param getAddressInfoUrl  获取地址具体信息的URL
     * @param gaodeWebApiKey     高德ApiKey
     * @param findAddressInfoMsg 需要查找具体信息的地址
     * @return
     */
    @Override
    public TableAddressAdcode findAddressInfoByMsg(String getAddressInfoUrl, String gaodeWebApiKey, String findAddressInfoMsg) throws Exception {
        List<TableAddressAdcode> findAdcodeByAddress = tableAddressAdcodeRepository.findByAddress(findAddressInfoMsg);
        //数据库中不存在则查询高德api，保存到mysql并返回
        if (findAdcodeByAddress.isEmpty()) {
            FastJSONObjectToDto fastJSONObjectToDto = new FastJSONObjectToDto();
            ObjectMapper dtoToStringMapper = new ObjectMapper();
            Map<String, String> sendGetRequestParamMap = new HashMap();
            TableAddressAdcode tableAddressAdcode = new TableAddressAdcode();

            sendGetRequestParamMap.put("key", gaodeWebApiKey);
            sendGetRequestParamMap.put("address", findAddressInfoMsg);
            GetAddressInfoFromGaodeDTO getAddressInfoFromGaodeDTO =
                    fastJSONObjectToDto.conversion(HttpClient.doGet(getAddressInfoUrl, sendGetRequestParamMap), GetAddressInfoFromGaodeDTO.class);

            tableAddressAdcode.setAddress(findAddressInfoMsg);
            tableAddressAdcode.setAdcode(getAddressInfoFromGaodeDTO.getGeocodes().get(0).getAdcode());
            tableAddressAdcode.setCreateAt(LocalDateTime.now());
            tableAddressAdcode.setGaodeAddressInfo(dtoToStringMapper.writeValueAsString(getAddressInfoFromGaodeDTO));
            tableAddressAdcodeRepository.save(tableAddressAdcode);
            return tableAddressAdcode;
        }
        return findAdcodeByAddress.get(0);
    }
}
