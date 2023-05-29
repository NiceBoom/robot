package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
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
     * 获取实时天气
     * @return 实时天气JSON
     */
    @Override
    public Result findLiveWeather(String weatherApiKey, String cityCode, String weatherCode) {

        return sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
    }

    /**
     * 获取实时天气并保存到mysql中
     * @param weatherApiKey 天气apiKey
     * @param cityCode      城市代码
     * @param weatherCode   天气代码（实时天气或者未来天气预报）
     * @return
     */
    @Override
    public Result findLiveWeatherSaveToMysql(String weatherApiKey, String cityCode, String weatherCode) {
        //获取实时天气
        Result liveWeatherResult =
                sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
        String liveWeather = (String) liveWeatherResult.getData();
        try {
            //格式化实时天气
            ObjectMapper mapper = new ObjectMapper();
            LiveWeatherDTO liveWeatherDto = mapper.readValue(liveWeather, LiveWeatherDTO.class);
            //组装mysql格式的实时天气
            TableLiveWeather tableLiveWeather = new TableLiveWeather();
            tableLiveWeather.setCityId(liveWeatherDto.getLives().get(0).getAdcode());
            tableLiveWeather.setCityName(liveWeatherDto.getLives().get(0).getCity());
            tableLiveWeather.setLiveWeather(liveWeatherDto.getLives().get(0).toString());
            tableLiveWeather.setCreateAt(LocalDateTime.now());

            //保存实时天气到mysql中
            tableLiveWeatherRepository.save(tableLiveWeather);
            //返回实时天气
            Result<Object> successSaveLiveWeatherResult = new Result<>();
            successSaveLiveWeatherResult.setData(tableLiveWeather);
            return successSaveLiveWeatherResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存天气预报失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("save forecast weather fail");
        failResult.setCode(StatusCode.ERROR);

        return failResult;

    }

    /**
     * 获取天气预报
     * @return 天气预报JSON
     */
    @Override
    public Result findForecastWeather(String weatherApiKey, String cityCode, String weatherCode) {

        return sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);

    }

    /**
     * 获取天气预报并保存到mysql中
     *
     * @param weatherApiKey 天气apiKey
     * @param cityCode      城市代码
     * @param weatherCode   天气代码（实时天气或者未来天气预报）
     * @return
     */
    @Override
    public Result findForecastWeatherToMysql(String weatherApiKey, String cityCode, String weatherCode) {
        try {
            //获取天气预报
            Result forecastWeatherResult =
                    sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
            String forecastWeather = (String) forecastWeatherResult.getData();
            //格式化天气预报数据，转换成DTO
                ObjectMapper mapper = new ObjectMapper();
                ForecastWeatherDTO forecastWeatherDto = mapper.readValue(forecastWeather, ForecastWeatherDTO.class);
                List<ForecastWeatherDTO.CityForecast> forecasts = forecastWeatherDto.getForecasts();
                //组装mysql格式的天气预报数据
                TableForecastWeather tableForecastWeather = new TableForecastWeather();
                tableForecastWeather.setCityId(forecasts.get(0).getAdcode());
                tableForecastWeather.setCityName(forecasts.get(0).getCity());
                tableForecastWeather.setForecastWeather(forecastWeather);
                tableForecastWeather.setCreateAt(LocalDateTime.now());
                //保存天气预报到mysql中
                tableForecastWeatherRepository.save(tableForecastWeather);
            Result<Object> getForecastWeatherSuccessResult = new Result<>();
            getForecastWeatherSuccessResult.setData(tableForecastWeather);
            return getForecastWeatherSuccessResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存天气预报失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("save live weather fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }

    /**
     * 发送请求获取天气
     * @param APIkey     请求地址链接
     * @param cityCode   城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
    private Result sendRequestGetWeather(String APIkey, String cityCode, String extensions) {

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
        //组装返回结果
        Result<Object> getWeatherResult = new Result<>();
        getWeatherResult.setData(forecastWeatherDTO);
        return getWeatherResult;
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
