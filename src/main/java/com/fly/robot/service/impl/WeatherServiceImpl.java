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
    public Result getWeather(String APIkey, String cityCode, String extensions) {
        //获取当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        //获取当前时间减去八小时后的时间,校验天气预报的过期时间
        LocalDateTime eightHoursAgo = nowTime.minus(8, ChronoUnit.HOURS);
        //获取当前时间减去两小时后的时间，校验实时天气的过期时间
        LocalDateTime twoHoursAgo = nowTime.minus(2, ChronoUnit.HOURS);

        //获取未来预报天气
        if(GaodeConfig.GET_FORECAST_WEATHER_CODE.equals(extensions)){
            //从数据库查询最新一条天气
            List<TableForecastWeather> tableForecastWeatherList =
                    tableForecastWeatherRepository.findFirstByCityIdOrderByCreateAtDesc(cityCode);
            //如果mysql中没有或者查询结果在8小时之前，则查询新天气
            if(tableForecastWeatherList.isEmpty() || tableForecastWeatherList.get(0).getCreateAt().isBefore(eightHoursAgo)){
                //组装请求参数
                HashMap<String, String> reqParam = new HashMap<>();
                reqParam.put("key", APIkey);
                reqParam.put("city", cityCode);
                reqParam.put("extensions", extensions);
                //发送GET请求
                JSONObject reqJson = HttpClient.doGet(GaodeConfig.GET_WEATHER_API_URL, reqParam);
                System.out.println(reqJson);
                //把返回结果转换为dto
                ForecastWeatherDTO forecastWeatherDTO = FastJSONObjectToDto.conversion(reqJson, ForecastWeatherDTO.class);
                try {
                    //dto转换为json字符串
                    ObjectMapper objectMapper = new ObjectMapper();
                    String forecastWeatherDTOString = objectMapper.writeValueAsString(forecastWeatherDTO);
                    System.out.println(forecastWeatherDTOString);
                    //组装数据并存入到mysql中
                    TableForecastWeather tableForecastWeather = new TableForecastWeather();
                    tableForecastWeather.setForecastWeather(forecastWeatherDTOString);
                    tableForecastWeather.setCreateAt(LocalDateTime.now());
                    tableForecastWeather.setCityId(forecastWeatherDTO.getForecasts().get(0).getAdcode());
                    tableForecastWeather.setCityName(forecastWeatherDTO.getForecasts().get(0).getCity());
                    tableForecastWeatherRepository.save(tableForecastWeather);
                    //组装返回结果
                    Result<Object> getForecastDtoResult = new Result<>();
                    getForecastDtoResult.setData(forecastWeatherDTO);
                    return getForecastDtoResult;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }
            //数据库中存在的话企且没有过期的话
            Result<Object> getForecastWeatherResult = new Result<>();
            //把tableForecastWeather转换为forecastDto
            String forecastWeather = tableForecastWeatherList.get(0).getForecastWeather();
            try {
                // 创建ObjectMapper对象
                ObjectMapper objectMapper = new ObjectMapper();
                // 将字符串转换为DTO对象
                ForecastWeatherDTO forecastWeatherDTO = objectMapper.readValue(forecastWeather, ForecastWeatherDTO.class);
                getForecastWeatherResult.setData(forecastWeatherDTO);
                return getForecastWeatherResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //获取实时天气消息
        if(GaodeConfig.GET_LIVE_WEATHER_CODE.equals(extensions)){
            //从数据库查询最新一条天气
            List<TableLiveWeather> tableLiveWeatherList =
                    tableLiveWeatherRepository.findFirstByCityIdOrderByCreateAtDesc(cityCode);
            //数据库中不存在或者创建时间超过两小时的话
            if(tableLiveWeatherList.isEmpty() || tableLiveWeatherList.get(0).getCreateAt().isBefore(twoHoursAgo)){
                //组装请求参数
                HashMap<String, String> reqParam = new HashMap<>();
                reqParam.put("key", APIkey);
                reqParam.put("city", cityCode);
                reqParam.put("extensions", extensions);
                //发送GET请求
                JSONObject reqJson = HttpClient.doGet(GaodeConfig.GET_WEATHER_API_URL, reqParam);
                System.out.println(reqJson);
                //把返回结果转换为dto
                LiveWeatherDTO liveWeatherDTO = FastJSONObjectToDto.conversion(reqJson, LiveWeatherDTO.class);
                //dto转换为json字符串
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String liveWeatherDTOString = objectMapper.writeValueAsString(liveWeatherDTO);
                    System.out.println(liveWeatherDTOString);
                //组装数据并存入到mysql中
                TableLiveWeather tableLiveWeather = new TableLiveWeather();
                tableLiveWeather.setLiveWeather(liveWeatherDTOString);
                tableLiveWeather.setCreateAt(LocalDateTime.now());
                tableLiveWeather.setCityId(liveWeatherDTO.getLives().get(0).getAdcode());
                tableLiveWeather.setCityName(liveWeatherDTO.getLives().get(0).getCity());
                tableLiveWeatherRepository.save(tableLiveWeather);
                //组装返回结果
                Result<Object> getWeatherResult = new Result<>();
                getWeatherResult.setData(liveWeatherDTO);
                return getWeatherResult;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            }
            //数据库中存在的话
            Result<Object> getLiveWeatherDtoResult = new Result<>();
            //把tableForecastWeather转换为forecastDto
            String liveWeather = tableLiveWeatherList.get(0).getLiveWeather();
            try {
                // 创建ObjectMapper对象
                ObjectMapper objectMapper = new ObjectMapper();
                // 将字符串转换为DTO对象
                LiveWeatherDTO liveWeatherDTO = objectMapper.readValue(liveWeather, LiveWeatherDTO.class);
                getLiveWeatherDtoResult.setData(liveWeatherDTO);
                return getLiveWeatherDtoResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //天气类型代码不存在
        Result<Object> failResult = new Result<>();
        failResult.setMessage("get weather extension is fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }

    /**
     *  从高德地图api查找消息地址的具体数据
     * @param getAddressInfoUrl 获取地址具体信息的URL
     * @param gaodeWebApiKey 高德ApiKey
     * @param findAddressInfoMsg 需要查找具体信息的地址
     * @return
     */
    @Override
    public Result findAddressInfoByMsg(String getAddressInfoUrl, String gaodeWebApiKey, String findAddressInfoMsg) {
        //查询该地址在mysql中是否存在
        List<TableAddressAdcode> findAdcodeByAddress = tableAddressAdcodeRepository.findByAddress(findAddressInfoMsg);
        //如果数据库中不存在，则发送请求查询并将结果保存到mysql，返回信息
        if(findAdcodeByAddress.isEmpty()){
            //创建请求体map信息，携带AppId与AppSecret
            Map<String, String> sendGetRequestParamMap = new HashMap();
            sendGetRequestParamMap.put("key", gaodeWebApiKey);
            sendGetRequestParamMap.put("address", findAddressInfoMsg);
            //发送请求
            JSONObject getAddressInfoResponseJson = HttpClient.doGet(getAddressInfoUrl, sendGetRequestParamMap);
            //把fastjson的JSONObject 转换为jackson中的JSONObject
            FastJSONObjectToDto fastJSONObjectToDto = new FastJSONObjectToDto();
            GetAddressInfoFromGaodeDTO conversion = fastJSONObjectToDto.conversion(getAddressInfoResponseJson, GetAddressInfoFromGaodeDTO.class);
            //提取出其中的adcode代码
            String addressAdcode = conversion.getGeocodes().get(0).getAdcode();
            //组装存到mysql中的数据
            TableAddressAdcode tableAddressAdcode = new TableAddressAdcode();
            tableAddressAdcode.setAddress(findAddressInfoMsg);
            tableAddressAdcode.setAdcode(addressAdcode);
            tableAddressAdcode.setCreateAt(LocalDateTime.now());
            //保存结果到数据库
            tableAddressAdcodeRepository.save(tableAddressAdcode);
            //返回查询结果
            Result<Object> getAddressInfoResponseResult = new Result<>();
            getAddressInfoResponseResult.setData(tableAddressAdcode);
            return getAddressInfoResponseResult;
        }
        //若数据库中存在，则返回查询结果
        Result<Object> getAddressInfoResponseResult = new Result<>();
        getAddressInfoResponseResult.setData(findAdcodeByAddress.get(0));
        return getAddressInfoResponseResult;

    }

}
