package com.fly.robot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @Description: <p>通用http调用工具</p>
 */
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private static final RequestConfig requestConfig;

    private static final RequestConfig uploadConfig;

    static {
        requestConfig = RequestConfig.custom()
                // 客户端和服务器建立连接的timeout
                .setConnectTimeout(1000 * 60)
                // 指从连接池获取连接的timeout
                .setConnectionRequestTimeout(6000)
                // 客户端从服务器读取数据的timeout
                .setSocketTimeout(1000 * 60 * 3)
                .build();
        uploadConfig = RequestConfig.custom()
                // 客户端和服务器建立连接的timeout
                .setConnectTimeout(1000 * 60 * 20)
                // 指从连接池获取连接的timeout
                .setConnectionRequestTimeout(6000)
                // 客户端从服务器读取数据的timeout
                .setSocketTimeout(1000 * 60 * 20)
                .build();
    }

    /**
     * map转换
     */
    public static Map<String, String> convertMap(JSONObject jsonObject) throws Exception {
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        Map<String, String> map = convertMap(innerMap);
        return map;
    }

    /**
     * map转换
     */
    public static Map<String, String> convertMap(Map<String, Object> objectMap) throws Exception {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> entries : objectMap.entrySet()) {
            map.put(entries.getKey(), null == entries.getValue() ? null : String.valueOf(entries.getValue()));
        }
        return map;
    }

    /**
     * 发送get请求，接收json响应数据
     * GET
     *
     * @param url   访问地址，无query参数
     * @param param query参数
     * @return
     */
    public static JSONObject doGet(String url, Map<String, String> param) throws HttpClientException {

        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            log.debug("-->>Http GET请求地址：" + url);
            if (null != param) {
                log.debug("-->>Http 请求参数：" + param.toString());
            }

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);

            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("<<--Http 响应内容：" + resultString);
            } else {
                log.error("<<--Http 响应状态码：" + response.getStatusLine().getStatusCode());
                throw new HttpClientException("请求失败 状态码：{}", response.getStatusLine().getStatusCode());
            }

        } catch (IOException | URISyntaxException e) {
            log.error("Http 发送请求异常 url:{}", url, e);
            throw new HttpClientException("发送请求失败 url:{}", url);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error("Http 关闭流异常", e);
            }
        }
        return JSONObject.parseObject(resultString);
    }

    /**
     * 发送post请求，上传byte
     * POST binary
     *
     * @param url 请求地址，不拼接
     * @return
     */
    public static JSONObject doPostBinaryBody(String url, byte[] bytes, String fileName) throws HttpClientException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            log.debug("-->>Http POST请求地址：" + url);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(uploadConfig);
            // 创建参数列表

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // TODO:这个video自己填，因为这个方法非常少用，是我用来上传视频的，所以这里写死了
            multipartEntityBuilder.addBinaryBody("video", bytes, ContentType.MULTIPART_FORM_DATA, fileName);

            httpPost.setEntity(multipartEntityBuilder.build());
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("<<--Http 响应内容：" + resultString);
            } else {
                log.error("<<--Http 响应状态码：" + response.getStatusLine().getStatusCode());
                throw new HttpClientException("请求失败 状态码：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("Http 发送请求异常 url:{}", url, e);
            throw new HttpClientException("发送请求失败 url:{}", url);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("Http 关闭流异常", e);
            }
        }
        return JSONObject.parseObject(resultString);
    }

    /**
     * 发送post请求，form-data数据传输
     * POST multipart/form-data
     *
     * @param url 请求地址
     * @return 返回json数据
     */
    public static JSONObject doPostFormData(String url, Map<String, String> formData) throws HttpClientException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            log.debug("-->>Http POST请求地址：" + url);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            // 创建参数列表
//            httpPost.setHeader("Content-Type", "multipart/form-data;charset=utf-8"); // 报错
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (formData != null) {
                log.debug("-->>Http POST form-data内容：" + JSON.toJSONString(formData));
                for (String key : formData.keySet()) {
                    multipartEntityBuilder.addTextBody(key, formData.get(key), ContentType.MULTIPART_FORM_DATA);
                }
            }

            httpPost.setEntity(multipartEntityBuilder.build());
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("<<--Http 响应内容：" + resultString);
            } else {
                log.error("<<--Http 响应状态码：" + response.getStatusLine().getStatusCode());
                throw new HttpClientException("请求失败 状态码：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("Http 发送请求异常", e);
            throw new HttpClientException("发送请求失败 url:{}", url);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("Http 关闭流异常", e);
            }
        }
        return JSONObject.parseObject(resultString);
    }

    /**
     * 发送post请求，接收json响应数据
     * POST application/x-www-form-urlencoded
     *
     * @param url   请求地址，不拼接
     * @param param 表单query参数
     * @return
     */
    public static JSONObject doPost(String url, Map<String, String> param) throws HttpClientException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            log.debug("-->>Http POST请求地址：" + url);
            if (null != param) {
                log.debug("-->>Http 请求参数：" + param.toString());
            }

            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("<<--Http 响应内容：" + resultString);
            } else {
                log.error("<<--Http 响应状态码：" + response.getStatusLine().getStatusCode());
                throw new HttpClientException("请求失败 状态码：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("Http 发送请求异常 url:{}", url, e);
            throw new HttpClientException("发送请求失败 url:{}", url);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("Http 关闭流异常", e);
            }
        }
        return JSONObject.parseObject(resultString);
    }

    /**
     * 发送post请求，接收json响应数据
     * POST application/json
     *
     * @param url  请求地址
     * @param json json入参
     * @return
     */
    public static JSONObject doPostJson(String url, String json) throws HttpClientException {
        if (StringUtils.isBlank(json)) {
            log.error("-->>Http POST发送json数据，json不能为空，url:" + url);
            return null;
        }
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            log.debug("-->>Http POST请求地址：" + url);
            log.debug("-->>Http 请求参数：" + json);
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("<<--Http 响应内容：" + resultString);
            } else {
                log.error("<<--Http 响应状态码：" + response.getStatusLine().getStatusCode());
                throw new HttpClientException("请求失败 状态码：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("Http 发送请求异常 url:{}", url, e);
            throw new HttpClientException("发送请求失败 url:{}", url);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error("Http 关闭流异常", e);
            }
        }
        return JSONObject.parseObject(resultString);
    }


    /**
     * 发送get请求，接收json响应数据 [使用代理]
     *
     * @param url           访问地址，不拼接
     * @param params        query参数
     * @param headers       请求头
     * @param proxyHost     代理服务器地址
     * @param proxyPort     代理服务器端口
     * @param proxyUser     认证用户名
     * @param proxyPassword 认证密码
     * @return
     * @throws HttpClientException
     */
    public static JSONObject doGetProxy(String url,
                                        Map<String, String> params,
                                        Map<String, String> headers,
                                        String proxyHost, Integer proxyPort,
                                        String proxyUser, String proxyPassword) throws HttpClientException {
        String resultString = "";
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            // 设置代理HttpHost
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);

            // 设置代理认证
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(proxyUser, proxyPassword));

            httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();

            // 构建URI
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key));
                }
            }
            URI uri = builder.build();

            // 装配发送Get请求
            HttpGet httpGet = new HttpGet(uri);
            log.info("-->> ProxyHttp 请求内容：" + uri.toString());

            // 请求设置时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectionRequestTimeout(3000)
                    .build();
            httpGet.setConfig(requestConfig);

            // 装配请求头
            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            // 执行请求
            response = httpClient.execute(httpGet);

            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("<<--ProxyHttp 响应内容：" + resultString);
            } else {
                log.error("<<--ProxyHttp 响应状态码：" + response.getStatusLine().getStatusCode());
                throw new HttpClientException("请求失败");
            }
        } catch (IOException | URISyntaxException e) {
            log.error("ProxyHttp 发送请求异常", e);
            throw new HttpClientException("ProxyHttp 发送请求失败 url:{}", url);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error("ProxyHttp 关闭流异常", e);
            }
        }
        return JSONObject.parseObject(resultString);
    }

    /**
     * 发送POST请求
     *
     * @param url      访问地址
     * @param header   请求头
     * @param formData
     * @param params   请求参数
     * @param body     请求体
     * @return
     * @throws HttpClientException
     */
    public static JSONObject doPostAllParam(String url,
                                            Map<String, String> header,
                                            Map<String, String> formData,
                                            Map<String, String> params,
                                            Map<String, String> body) throws HttpClientException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {

            // 创建Http Post请求
            log.debug("-->>Http POST请求地址：" + url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            //获取map集合的迭代器
            Iterator<Map.Entry<String, String>> headerIterator = header.entrySet().iterator();
            //把设置请求头
            while (headerIterator.hasNext()) {
                Map.Entry<String, String> entry = headerIterator.next();
                httpPost.setHeader(entry.getKey(), entry.getValue());
                log.debug("-->>Http POST请求头：" + entry.toString());
            }
            //未完成修改，修改到此
            // 创建参数列表
//            httpPost.setHeader("Content-Type", "multipart/form-data;charset=utf-8"); // 报错
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (formData != null) {
                log.debug("-->>Http POST form-data内容：" + JSON.toJSONString(formData));
                for (String key : formData.keySet()) {
                    multipartEntityBuilder.addTextBody(key, formData.get(key), ContentType.MULTIPART_FORM_DATA);
                }
            }

            httpPost.setEntity(multipartEntityBuilder.build());
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("<<--Http 响应内容：" + resultString);
            } else {
                log.error("<<--Http 响应状态码：" + response.getStatusLine().getStatusCode());
                throw new HttpClientException("请求失败 状态码：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("Http 发送请求异常", e);
            throw new HttpClientException("发送请求失败 url:{}", url);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("Http 关闭流异常", e);
            }
        }
        return JSONObject.parseObject(resultString);
    }
}
