package com.fly.robot.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * FastJson中的JSONObject转换为dto的工具类
 */
public class FastJSONObjectToDto {

    public static <T> T conversion(JSONObject fastJSONObject, Class<T> dtoClass){

        // 将Fastjson的JSONObject转换为JSON字符串
        String jsonString = fastJSONObject.toJSONString();

        // 使用Jackson的ObjectMapper解析JSON字符串为JsonNode对象
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
            // 将JsonNode对象转换为Jackson中的JSONObject对象
            ObjectNode jsonObject = objectMapper.createObjectNode();
            jsonObject.setAll((ObjectNode) jsonNode);

            // 打印Jackson中的JSONObject对象
            System.out.println(jsonObject);

            return objectMapper.readValue(jsonObject.toString(), dtoClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
