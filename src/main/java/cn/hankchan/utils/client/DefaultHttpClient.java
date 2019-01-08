package cn.hankchan.utils.client;

import cn.hankchan.utils.HttpClientApi;
import cn.hankchan.utils.Strings;
import cn.hankchan.utils.consts.ContentTypeConst;
import cn.hankchan.utils.consts.EncodeConst;
import cn.hankchan.utils.exception.HttpClientApiException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 默认的Http客户端抽象实现
 * @author hankChan
 *
 */
public abstract class DefaultHttpClient extends BaseHttpClient implements HttpClientApi {

    @Override
    public abstract void init();

    @Override
    public void shutdown() {
        if(null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final String getByUrlEncoded(String url, Map<String, String> params) throws HttpClientApiException {
        return getByUrlEncoded(url, params, null);
    }

    @Override
    public final String getByUrlEncoded(String url, Map<String, String> params, Map<String, String> header) throws HttpClientApiException {
        // 拼接URL和请求参数
        String fullUrl = appendUrlWithParams(url, params);
        HttpGet httpGet = new HttpGet(fullUrl);
        try {
            return doGet(httpGet);
        } catch (IOException e) {
            throw new HttpClientApiException("Http Request Exception", e.getMessage());
        }
    }

    @Override
    public final String postByUrlEncoded(String url, Map<String, String> params) throws HttpClientApiException {
        return postByUrlEncoded(url, params, null);
    }

    @Override
    public final String postByUrlEncoded(String url, Map<String, String> params, Map<String, String> header) throws HttpClientApiException {
        HttpPost httpPost = new HttpPost(url);
        // 拼接请求参数
        String keyValuePart = appendKeyAndValues(params);
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(keyValuePart);
            stringEntity.setContentEncoding(EncodeConst.UTF8);
            stringEntity.setContentType(ContentTypeConst.APPLICATION_X_WWW_FORM_URLENCODED);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(stringEntity);
        httpPost.setHeader(ContentTypeConst.CONTENT_TYPE, ContentTypeConst.APPLICATION_X_WWW_FORM_URLENCODED);
        try {
            return doPost(httpPost, header);
        } catch (IOException e) {
            throw new HttpClientApiException("Http Request Exception", e.getMessage());
        }
    }

    @Override
    public final String postByFormData(String url, Map<String, String> params) throws HttpClientApiException {
        return postByFormData(url, params, null);
    }

    @Override
    public final String postByFormData(String url, Map<String, String> params, Map<String, String> header) throws HttpClientApiException {
        return postByFormData(url, params, header, null, null);
    }

    @Override
    public final String postByFormData(String url, Map<String, String> params, String bytesKey, byte[] bytes) throws HttpClientApiException {
        return postByFormData(url, params, null, bytesKey, bytes);
    }

    @Override
    public final String postByFormData(String url, Map<String, String> params, Map<String, String> header, String bytesKey, byte[] bytes) throws HttpClientApiException {
        // 构建表单请求
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        params = Optional.ofNullable(params).orElse(new HashMap<>());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multipartEntityBuilder.addPart(entry.getKey(),
                    new StringBody(entry.getValue(), ContentType.create(ContentTypeConst.MULTIPART_FORM_DATA, EncodeConst.UTF8)));
        }
        // 加入二进制流
        if(null != bytesKey) {
            multipartEntityBuilder.addPart(bytesKey,
                    new ByteArrayBody(bytes, ContentType.create(ContentTypeConst.MULTIPART_FORM_DATA, EncodeConst.UTF8), ""));
        }
        // 构造请求实体
        HttpEntity httpEntity = multipartEntityBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        try {
            return doPost(httpPost, header);
        } catch (IOException e) {
            throw new HttpClientApiException("Http Request Exception", e.getMessage());
        }
    }

    @Override
    public final String postByRequestBody(String url, String requestJsonString) throws HttpClientApiException {
        return postByRequestBody(url, requestJsonString, null);
    }

    @Override
    public final String postByRequestBody(String url, String requestJsonString, Map<String, String> header) throws HttpClientApiException {
        // 构造HttpPost
        HttpPost httpPost = new HttpPost(url);
        // 构造json格式请求体
        StringEntity stringEntity = new StringEntity(requestJsonString, EncodeConst.UTF8);
        stringEntity.setContentEncoding(EncodeConst.UTF8);
        stringEntity.setContentType(ContentTypeConst.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        try {
            return doPost(httpPost, header);
        } catch (IOException e) {
            throw new HttpClientApiException("Http Request Exception", e.getMessage());
        }
    }

    /**
     * 发送Post请求
     * @param httpPost http post请求
     * @param header 请求头内容
     * @return 响应结果
     * @throws IOException io exception
     */
    private final String doPost(HttpPost httpPost, Map<String, String> header) throws IOException {
        // 加入请求头
        if(!Optional.ofNullable(header).orElse(new HashMap<>()).isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return doPost(httpPost);
    }

    /**
     * 将url与请求参数拼接为完整的get请求的url
     * @param url url
     * @param params 请求参数
     * @return 完整url
     */
    private final String appendUrlWithParams(String url, Map<String, String> params) {
        if(null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder builder = new StringBuilder();
        if(url.endsWith("/")) {
            // 如果以/结果,去掉/
            builder.append(url.substring(0, url.length() - 1)).append("?");
        } else {
            builder.append(url).append("?");
        }
        return builder.append(appendKeyAndValues(params)).toString();
    }

    /**
     *
     * @param params
     * @return
     */
    private final String appendKeyAndValues(Map<String, String> params) {
        if(null == params || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            // 只有key不得为空,value可以为空
            if(!Strings.isNullOrEmpty(entry.getKey())) {
                try {
                    // 拼接成k1=v1&k2=v2&k3=v3的结果字符串
                    builder.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue(), EncodeConst.UTF8)).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

}
