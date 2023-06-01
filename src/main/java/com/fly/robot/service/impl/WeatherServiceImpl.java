package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableAddressAdcodeRepository;
import com.fly.robot.entity.*;
import com.fly.robot.pojo.*;
import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
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
    public TableLiveWeatherRepository tableLiveWeatherRepository;
    @Autowired
    public TableForecastWeatherRepository tableForecastWeatherRepository;
    @Autowired
    public TableAddressAdcodeRepository tableAddressAdcodeRepository;

    /**
     * 获取天气
     * @param APIkey     请求地址链接
     * @param cityCode   城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
    public WeatherDTO getWeather(String APIkey, String cityCode, String extensions) throws Exception {

        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime eightHoursAgo = nowTime.minus(8, ChronoUnit.HOURS);
        LocalDateTime twoHoursAgo = nowTime.minus(2, ChronoUnit.HOURS);

        if (GaodeConfig.GET_FORECAST_WEATHER_CODE.equals(extensions)) {
            List<TableForecastWeather> tableForecastWeatherList =
                    tableForecastWeatherRepository.findFirstByCityIdOrderByCreateAtDesc(cityCode);

            if (tableForecastWeatherList.isEmpty() ||
                    tableForecastWeatherList.get(0).getCreateAt().isBefore(eightHoursAgo)) {
                HashMap<String, String> reqParam = new HashMap<>();
                ObjectMapper dtoToJsonMapper = new ObjectMapper();
                reqParam.put("key", APIkey);
                reqParam.put("city", cityCode);
                reqParam.put("extensions", extensions);
                JSONObject reqJson = HttpClient.doGet(GaodeConfig.GET_WEATHER_API_URL, reqParam);
                WeatherDTO forecastWeatherDTO = FastJSONObjectToDto.conversion(reqJson, WeatherDTO.class);

                TableForecastWeather tableForecastWeather = new TableForecastWeather();
                tableForecastWeather.setForecastWeather(dtoToJsonMapper.writeValueAsString(forecastWeatherDTO));
                tableForecastWeather.setCreateAt(LocalDateTime.now());
                tableForecastWeather.setCityId(forecastWeatherDTO.getForecasts().get(0).getAdcode());
                tableForecastWeather.setCityName(forecastWeatherDTO.getForecasts().get(0).getCity());
                tableForecastWeatherRepository.save(tableForecastWeather);
                return forecastWeatherDTO;
            }
            ObjectMapper jsonToDtoMapper = new ObjectMapper();
            return jsonToDtoMapper.readValue(tableForecastWeatherList.get(0).getForecastWeather(), WeatherDTO.class);
        }

        if (GaodeConfig.GET_LIVE_WEATHER_CODE.equals(extensions)) {
            List<TableLiveWeather> tableLiveWeatherList =
                    tableLiveWeatherRepository.findFirstByCityIdOrderByCreateAtDesc(cityCode);

            if (tableLiveWeatherList.isEmpty() || tableLiveWeatherList.get(0).getCreateAt().isBefore(twoHoursAgo)) {
                HashMap<String, String> reqParam = new HashMap<>();
                reqParam.put("key", APIkey);
                reqParam.put("city", cityCode);
                reqParam.put("extensions", extensions);
                JSONObject reqJson = HttpClient.doGet(GaodeConfig.GET_WEATHER_API_URL, reqParam);
                WeatherDTO liveWeatherDTO = FastJSONObjectToDto.conversion(reqJson, WeatherDTO.class);

                ObjectMapper dtoToJsonMapper = new ObjectMapper();
                TableLiveWeather tableLiveWeather = new TableLiveWeather();
                tableLiveWeather.setLiveWeather(dtoToJsonMapper.writeValueAsString(liveWeatherDTO));
                tableLiveWeather.setCreateAt(LocalDateTime.now());
                tableLiveWeather.setCityId(liveWeatherDTO.getLives().get(0).getAdcode());
                tableLiveWeather.setCityName(liveWeatherDTO.getLives().get(0).getCity());
                tableLiveWeatherRepository.save(tableLiveWeather);
                return liveWeatherDTO;
            }
            ObjectMapper jsonToDtoMapper = new ObjectMapper();
            return jsonToDtoMapper
                    .readValue(tableLiveWeatherList.get(0).getLiveWeather(), WeatherDTO.class);
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

        if (findAdcodeByAddress.isEmpty()) {
            FastJSONObjectToDto fastJSONObjectToDto = new FastJSONObjectToDto();
            ObjectMapper dtoToStringMapper = new ObjectMapper();

            Map<String, String> sendGetRequestParamMap = new HashMap();
            sendGetRequestParamMap.put("key", gaodeWebApiKey);
            sendGetRequestParamMap.put("address", findAddressInfoMsg);
            JSONObject getAddressInfoResponseJson = HttpClient.doGet(getAddressInfoUrl, sendGetRequestParamMap);

            GetAddressInfoFromGaodeDTO getAddressInfoFromGaodeDTO = fastJSONObjectToDto.conversion(getAddressInfoResponseJson, GetAddressInfoFromGaodeDTO.class);
            TableAddressAdcode tableAddressAdcode = new TableAddressAdcode();
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
