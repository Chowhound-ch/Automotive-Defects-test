package per.chowhound.hfut.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>不依赖于web服务，在容器启动之前即可使用(静态)
 *
 * @author : Chowhound
 * @since : 2025/3/22 - 22:49
 */
@Slf4j
@SuppressWarnings("unused")
public class HttpUtil {
    private final static HttpClient webClient;
    private final static String BASE_URL = "";
    public final static Header[] EMPTY_HEADERS = new Header[0];
    static {
        // 配置基础url
        webClient = HttpClients.custom()
                .addInterceptorFirst((HttpRequest request, HttpContext context) -> {
                    // 记录请求的详细信息
                    RequestLine requestLine = request.getRequestLine();
                    log.info("Request:  {}  {}", requestLine.getMethod(), requestLine.getUri());
                    // 添加请求头
                    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                }).build();

    }
    // Entity
    public static HttpEntity getStringEntity(String str) {
        return new StringEntity(str, StandardCharsets.UTF_8);
    }


    // region get
    public static String doGetStr(String url, Header[] headers, Map<String, String> params) {
        HttpGet httpGet = new HttpGet(getUri(url, params));
        httpGet.setHeaders(headers);
        HttpResponse execute;
        try {
            execute = webClient.execute(httpGet);
            return EntityUtils.toString(execute.getEntity());
        } catch (IOException e) {
            log.error("请求失败： ", e);
        }
        return null;
    }
    public static URI getUri(String uri, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(BASE_URL + uri);
            return uriBuilder.addParameters(getParams(params)).build();
        } catch (URISyntaxException e) {
            log.error("uri错误: {}", BASE_URL + uri, e);
            throw new IllegalArgumentException(e);
        }
    }

    public static List<NameValuePair> getParams(Map<String, String> params) {
        return params.entrySet()
                .stream()
                .map(entry -> (NameValuePair) new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .toList();
    }


    // 重载
    public static String doGetStr(String url) {
        return doGetStr(url, EMPTY_HEADERS, Collections.emptyMap());
    }

    public static String doGetStr(String url, Header[] headers) {
        return doGetStr(url, headers, Collections.emptyMap());
    }
    public static String doGetStr(String url, Map<String, String> params) {
        return doGetStr(url, EMPTY_HEADERS, params);
    }


    public static byte[] doGetBytes(String url, Header[] headers, Map<String, String> params) {
        HttpGet httpGet = new HttpGet(getUri(url, params));
        httpGet.setHeaders(headers);
        HttpResponse execute;
        try {
            execute = webClient.execute(httpGet);
            return EntityUtils.toByteArray(execute.getEntity());
        } catch (IOException e) {
            log.error("请求失败： ", e);
        }
        return null;
    }
    // 重载
    public static byte[] doGetBytes(String url, Header[] headers) {
        return doGetBytes(url, headers, Collections.emptyMap());
    }
    public static byte[] doGetBytes(String url, Map<String, String> params) {
        return doGetBytes(url, EMPTY_HEADERS, params);
    }

    public static byte[] doGetBytes(String url) {
        return doGetBytes(url, EMPTY_HEADERS, Collections.emptyMap());
    }

    public static JsonNode doGetJson(String url, Header[] headers, Map<String, String> params) {
        return JacksonUtil.readTree(doGetStr(url, headers, params));
    }

    // 重载
    public static JsonNode doGetJson(String url) {
        return doGetJson(url, EMPTY_HEADERS, Collections.emptyMap());
    }

    public static JsonNode doGetJson(String url, Map<String, String> params) {
        return doGetJson(url, EMPTY_HEADERS, params);
    }
    public static JsonNode doGetJson(String url, Header[] headers) {
        return doGetJson(url, headers, Collections.emptyMap());
    }
    // endregion

    // region post
    public static String doPostStr(String url, Header[] headers, Map<String, String> params,HttpEntity body) {
        HttpPost httpPost = new HttpPost(getUri(url, params));
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);
        HttpResponse execute;
        try {
            execute = webClient.execute(httpPost);
            return EntityUtils.toString(execute.getEntity());
        } catch (IOException e) {
            log.error("请求失败： ", e.getMessage());
        }
        return null;

    }
    // 重载
    public static String doPostStr(String url) {
        return doPostStr(url, EMPTY_HEADERS, Collections.emptyMap(), null);
    }

    public static String doPostStr(String url, Map<String, String> params) {
        return doPostStr(url, EMPTY_HEADERS, params, null);
    }

    public static String doPostStr(String url, Header[] headers) {
        return doPostStr(url, headers, Collections.emptyMap(), null);
    }

    public static String doPostStr(String url, Header[] headers, HttpEntity body) {
        return doPostStr(url, headers, Collections.emptyMap(), body);
    }

    public static String doPostStr(String url, HttpEntity body) {
        return doPostStr(url, EMPTY_HEADERS, Collections.emptyMap(), body);
    }

    public static JsonNode doPostJson(String url, Header[] headers, Map<String, String> params, HttpEntity body) {
        return JacksonUtil.readTree(doPostStr(url, headers, params, body));
    }
    // 重载
    public static JsonNode doPostJson(String url) {
        return doPostJson(url, EMPTY_HEADERS, Collections.emptyMap(), null);
    }

    public static JsonNode doPostJson(String url, Header[] headers) {
        return doPostJson(url, headers, Collections.emptyMap(), null);
    }

    public static JsonNode doPostJson(String url, Map<String, String> params) {
        return doPostJson(url, EMPTY_HEADERS, params, null);
    }
    public static JsonNode doPostJson(String url, Map<String, String> params, HttpEntity body) {
        return doPostJson(url, EMPTY_HEADERS, params, body);
    }


    public static JsonNode doPostJson(String url, HttpEntity body) {
        return doPostJson(url, EMPTY_HEADERS, Collections.emptyMap(), body);
    }
    // Class
    public static <T> T doPostObject(String url, Header[] headers, Map<String, String> params, HttpEntity body, Class<T> clazz) {
        return JacksonUtil.readValue(doPostStr(url, headers, params, body), clazz);
    }

    public static <T> T doPostObject(String url, Class<T> clazz) {
        return doPostObject(url, EMPTY_HEADERS, Collections.emptyMap(), null, clazz);
    }

    public static <T> T doPostObject(String url, Header[] headers, Class<T> clazz) {
        return doPostObject(url, headers, Collections.emptyMap(), null, clazz);
    }

    public static <T> T doPostObject(String url, Map<String, String> params, Class<T> clazz) {
        return doPostObject(url, EMPTY_HEADERS, params, null, clazz);

    }
    public static <T> T doPostObject(String url, Map<String, String> params, HttpEntity body, Class<T> clazz) {
        return doPostObject(url, EMPTY_HEADERS, params, body, clazz);
    }


    public static <T> T doPostObject(String url, HttpEntity body, Class<T> clazz) {
        return doPostObject(url, EMPTY_HEADERS, Collections.emptyMap(), body, clazz);
    }
    // JavaType
    public static <T> T doPostObject(String url, Header[] headers, Map<String, String> params, HttpEntity body, JavaType javaType) {
        return JacksonUtil.readValue(doPostJson(url, headers, params, body), javaType);
    }

    // 重载
    public static <T> T doPostObject(String url, JavaType javaType) {
        return doPostObject(url, EMPTY_HEADERS, Collections.emptyMap(), null, javaType);
    }

    public static <T> T doPostObject(String url, Header[] headers, JavaType javaType) {
        return doPostObject(url, headers, Collections.emptyMap(), null, javaType);
    }

    public static <T> T doPostObject(String url, Map<String, String> params, JavaType javaType) {
        return doPostObject(url, EMPTY_HEADERS, params, null, javaType);
    }
    public static <T> T doPostObject(String url, Map<String, String> params, HttpEntity body, JavaType javaType) {
        return doPostObject(url, EMPTY_HEADERS, params, body, javaType);
    }

    public static <T> T doPostObject(String url, HttpEntity body, JavaType javaType) {
        return doPostObject(url, EMPTY_HEADERS, Collections.emptyMap(), body, javaType);
    }
    // endregion
}
