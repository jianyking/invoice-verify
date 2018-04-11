package com.flamemaster.platform.infra.microservice.common.infrastruct.utils;

import com.flamemaster.platform.infra.microservice.base.Entity;
import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;
import lombok.extern.log4j.Log4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j
public class HttpUtil {

    private static final int MAX_CONNECTION_TIME = 5000;

    private static RequestConfig getDefaultConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(MAX_CONNECTION_TIME)
                .setConnectionRequestTimeout(MAX_CONNECTION_TIME)
                .setSocketTimeout(MAX_CONNECTION_TIME).build();
    }

    public static Entity postByJson(String value, String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getDefaultConfig());
        StringEntity entity = new StringEntity(value, ContentType.create("application/json", Consts.UTF_8));
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            int status = response.getStatusLine().getStatusCode();
            if (status < 300) {
                HttpEntity responseEntity = response.getEntity();
                return new Entity(InvoiceConstants.SUCCESS_STATUS, "调用成功",
                        EntityUtils.toString(responseEntity));
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return new Entity(InvoiceConstants.NET_WORK_ERROR, "服务调用失败", null);
    }

    public static String postByForm(Map<String, Object> paramsMap, String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<>();
        for (String key : paramsMap.keySet()) {
            String value = (String)(paramsMap.get(key));
            params.add(new BasicNameValuePair(key, value));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } finally {
            response.close();
        }
    }

    public static String get(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } finally {
            response.close();
        }
    }

    public static InputStream getStream(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            HttpEntity responseEntity = response.getEntity();
            return responseEntity.getContent();
        } finally {
            response.close();
        }
    }

}
