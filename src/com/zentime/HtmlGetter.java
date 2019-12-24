package com.zentime;

import java.io.*;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

public class HtmlGetter {
    // 将html塞入List中，每一行为list的一个元素
    public List<String> getHtml(String url) throws Exception {
        List<String> htmlList = new ArrayList<String>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Constants.socketTimeOut).setConnectTimeout(Constants.connectionTimeOut).build();// 设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        // try
        // {
        CloseableHttpResponse response = httpClient.execute(httpGet);
        // System.out.println(response.getProtocolVersion());
        // System.out.println(response.getStatusLine().getStatusCode());
        // System.out.println(response.getStatusLine().getReasonPhrase());
        // System.out.println(response.getStatusLine().toString());
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String htmlLine = "";
            if (entity != null) {
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((htmlLine = br.readLine()) != null) {
                    htmlList.add(htmlLine);
                }
            }
        }
        response.close();
        httpClient.close();
        // }
        // catch (Exception e)
        // {
        // e.printStackTrace();
        // }
        return htmlList;
    }
}
