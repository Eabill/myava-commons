package com.myava.base.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.collections.MapUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP客户端实用类，使用okhttp3实现
 *
 * @author biao
 */
@Slf4j
public class OkHttpUtil {

    private static final OkHttpClient httpClient;

    /**
     * 默认的 user-agent
     */
    public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";

    /**
     * 实现默认的编码
     */
    public static final String DEFAULT_CHARSET = "utf-8";

    private static final Map<String, String> DEFAULT_HEADERS = new HashMap<>(4);

    static {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .connectionPool(new ConnectionPool()).build();
        DEFAULT_HEADERS.put("User-agent", USER_AGENT);
        DEFAULT_HEADERS.put("Connection", "close");
    }

    /**
     * 发送GET请求
     * @param url
     * @return
     */
    public static Response get(String url) throws IOException {
        return get(url, DEFAULT_CHARSET, null, DEFAULT_HEADERS);
    }

    /**
     * 发送GET请求
     * @param url
     * @param params
     * @return
     */
    public static Response get(String url, Map<String, Object> params) throws IOException {
        return get(url, DEFAULT_CHARSET, params, DEFAULT_HEADERS);
    }

    /**
     * 发送GET请求
     * @param url
     * @return
     */
    public static Response getWithHeaders(String url, Map<String, String> headers) throws IOException {
        return get(url, DEFAULT_CHARSET, null, headers);
    }

    /**
     * 发送普通内容post请求
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static Response post(String url, Map<String, Object> params) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("url不能为空");
        }
        Builder builder = new Builder();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> builder.add(k, String.valueOf(v)));
        }
        log.info("url: {}, params: {}", url, JSON.toJSONString(params));
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return httpClient.newCall(request).execute();
    }

    /**
     * 发送json内容格式post请求
     * @param url
     * @param params
     * @return
     */
    public static Response postJson(String url, Object params) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("url不能为空");
        }
        // 请求内容转化为json字符串
        String requestJson = JSON.toJSONString(params);
        log.info("url: {}, params: {}", url, requestJson);
        RequestBody requestBody = RequestBody.create(requestJson, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return httpClient.newCall(request).execute();
    }

    /**
     * 发送GET请求
     * @param url
     * @param charset
     * @param params
     * @param headers
     * @return
     */
    private static Response get(String url, String charset, Map<String, Object> params, Map<String, String> headers) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("url不能为空");
        }
        String fullUrl = concatUrl(url, charset, params);
        log.info("url: {}", fullUrl);
        Request.Builder builder = new Request.Builder();
        builder.get().url(fullUrl);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpClient.newCall(builder.build()).execute();
    }

    /**
     * 拼接请求URL地址
     * @param url
     * @param charset
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String concatUrl(String url, String charset, Map<String, Object> params) throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder result = new StringBuilder(128);
        result.append(url);
        if (url.indexOf("?") < 0) {
            result.append("?");
        }
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            result.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), charset)).append("&");
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }

}
