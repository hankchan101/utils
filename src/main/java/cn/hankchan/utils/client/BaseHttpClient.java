package cn.hankchan.utils.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 基础的Http请求客户端
 * @author hankChan
 *
 */
public abstract class BaseHttpClient {

    protected CloseableHttpClient httpClient;

    /**
     * 发送Get请求
     * @param httpGet Http Get请求
     * @return 响应结果
     * @throws IOException io exception
     */
    public final String doGet(HttpGet httpGet) throws IOException {
        try(CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        }
    }

    /**
     * 发送Post请求
     * @param httpPost Http Post请求
     * @return 响应结果
     * @throws IOException io exception
     */
    public final String doPost(HttpPost httpPost) throws IOException {
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        }
    }
}
