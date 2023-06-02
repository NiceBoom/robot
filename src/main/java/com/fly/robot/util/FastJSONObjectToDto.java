package com.fly.robot.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * FastJson中的JSONObject转换为dto的工具类
 */
public class FastJSONObjectToDto {

    public static <T> T conversion(JSONObject fastJSONObject, Class<T> dtoClass) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = fastJSONObject.toJSONString();
        JsonNode jsonNode = null;
            jsonNode = objectMapper.readTree(jsonString);
            ObjectNode jsonObject = objectMapper.createObjectNode();
            jsonObject.setAll((ObjectNode) jsonNode);

            return objectMapper.readValue(jsonObject.toString(), dtoClass);
    }

}
