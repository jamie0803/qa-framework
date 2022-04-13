package com.qa.base;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: Zhang Huijuan
 * @Date: 2022/4/12
 */
public class BaseApi {
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

}
