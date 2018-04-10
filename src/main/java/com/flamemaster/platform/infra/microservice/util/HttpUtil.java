package com.flamemaster.platform.infra.microservice.util;

import com.flamemaster.platform.infra.microservice.base.Entity;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
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
import sun.net.www.http.HttpClient;

import java.io.IOException;

public class HttpUtil {

    public static String postByJson(String value, String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(value, ContentType.create("application/json", "UTF-8"));
        httpPost.setEntity(entity);
        CloseableHttpResponse response2 = httpClient.execute(httpPost);

        try {
            HttpEntity responseEntity = response2.getEntity();
            return EntityUtils.toString(responseEntity);
        } finally {
            response2.close();
        }
    }

    public static String get(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response1 = httpClient.execute(httpGet);

        try {
            HttpEntity responseEntity = response1.getEntity();
            return EntityUtils.toString(responseEntity);
        } finally {
            response1.close();
        }

    }
}
