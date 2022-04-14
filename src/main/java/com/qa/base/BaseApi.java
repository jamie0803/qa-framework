package com.qa.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Zhang Huijuan
 * @Date: 2022/4/12
 */
public class BaseApi {

    @BeforeTest
    public void beforeTest() {

    }


    //get请求(带请求头)
    public static CloseableHttpResponse get(String url, Map<String, String> headers) throws IOException {
        return getCloseableHttpResponse(url, headers);
    }

    private static CloseableHttpResponse getCloseableHttpResponse(String url, Map<String, String> headers) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (headers.size() > 0 && headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        return execute;
    }

    //发送post请求(以form-data格式)
    public static CloseableHttpResponse postForm(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        //添加请求头格式
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return postCloseableHttpResponse(url, null, params, headers);
    }

    //发送post请求(以json格式)
    public static CloseableHttpResponse postJson(String url, String jsonString, Map<String, String> headers) throws IOException {
        //添加请求头格式
        headers.put("Content-Type", "application/json");
        return postCloseableHttpResponse(url, jsonString, null, headers);
    }

    private static CloseableHttpResponse postCloseableHttpResponse(String url, String jsonString, Map<String, String> params, Map<String, String> headers) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //json字符串参数空，此时发来请求方式是form-data类型
        if (jsonString != null && !"".equals(jsonString)) {
            httpPost.setEntity(new StringEntity(jsonString));
        } else { //json字符串非空，此时发来请求方式是json类型
            List<NameValuePair> nvp = new ArrayList<>();
            //组合参数
            if (params.size() > 0 && params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvp.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvp));
        }

        //添加headers到请求头对象中
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        // 发送post请求
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        return httpResponse;
    }

    public static CloseableHttpResponse put(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut httpput = new HttpPut(url);
        // 构造请求体，创建参数队列
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvp.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpput.setEntity(new UrlEncodedFormEntity(nvp));

        // 加载请求头到httpput对象
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 发送put请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpput);
        return httpResponse;
    }

    public static int responseCode(CloseableHttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode;
    }

    public static JSONObject getJSONObject(CloseableHttpResponse response) throws IOException {
        //得到响应格式的String格式
        String jsonString = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject responseJsonObject = JSONObject.parseObject(jsonString);
        return responseJsonObject;
    }

    //把json字符串转换成指定类型的实体bean
    public static <T>T getBeanFromJson(CloseableHttpResponse response) throws IOException {
        String jsonString = EntityUtils.toString(response.getEntity(), "utf-8");
        //此处也可以使用gson: T t= gson.fromJson("jsonString", Class<T> clazz);
        T t = JSON.parseObject(jsonString, new TypeReference<T>() {});
        return t;
    }
}
