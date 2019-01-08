package cn.hankchan.utils;

import cn.hankchan.utils.exception.HttpClientApiException;

import java.util.Map;

/**
 * Http客户端请求接口
 * @author hankChan
 *
 */
public interface HttpClientApi {

    /**
     * 客户端初始化
     */
    void init();

    /**
     * 客户端资源回收
     */
    void shutdown();

    /**
     * 通过URL Encoded方式发送GET请求
     * @param url 请求url
     * @param params 请求参数集合
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String getByUrlEncoded(String url, Map<String, String> params) throws HttpClientApiException;

    /**
     * 通过URL Encoded方式发送GET请求
     * @param url 请求url
     * @param params 请求参数集合
     * @param header 请求头内容
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String getByUrlEncoded(String url, Map<String, String> params, Map<String, String> header) throws HttpClientApiException;

    /**
     * 通过URL Encoded方式发送POST请求
     * @param url 请求url
     * @param params 请求参数集合
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByUrlEncoded(String url, Map<String, String> params) throws HttpClientApiException;

    /**
     * 通过URL Encoded方法发送POST请求
     * @param url 请求url
     * @param params 请求参数集合
     * @param header 请求头内容
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByUrlEncoded(String url, Map<String, String> params, Map<String, String> header) throws HttpClientApiException;

    /**
     * 通过表单方式发送POST请求
     * @param url 请求url
     * @param params 请求参数集合
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByFormData(String url, Map<String, String> params) throws HttpClientApiException;

    /**
     * 通过表单方式发送POST请求
     * @param url 请求url
     * @param params 请求参数集合
     * @param header 请求头内容
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByFormData(String url, Map<String, String> params, Map<String, String> header) throws HttpClientApiException;

    /**
     * 通过表单请求方式发送POST请求
     * @param url 请求url
     * @param params 请求参数集合
     * @param bytesKey 如果需要上传二进制流,则设置字段名
     * @param bytes 二进制流内容
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByFormData(String url, Map<String, String> params, String bytesKey, byte[] bytes) throws HttpClientApiException;

    /**
     * 通过表单请求方式发送POST请求
     * @param url 请求url
     * @param params 请求参数集合
     * @param header 请求头内容
     * @param bytesKey 如果需要上传二进制流,则设置字段名
     * @param bytes 二进制流内容
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByFormData(String url, Map<String, String> params, Map<String, String> header, String bytesKey, byte[] bytes) throws HttpClientApiException;

    /**
     * 通过JSON格式字符串发送POST请求
     * @param url 请求url
     * @param requestJsonString JSON格式的请求内容
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByRequestBody(String url, String requestJsonString) throws HttpClientApiException;

    /**
     * 通过JSON格式字符串发送POST请求
     * @param url 请求url
     * @param requestJsonString JSON格式的请求内容
     * @param header 请求头内容
     * @return 响应结果字符串
     * @throws HttpClientApiException {@link HttpClientApiException}
     */
    String postByRequestBody(String url, String requestJsonString, Map<String, String> header) throws HttpClientApiException;

}
