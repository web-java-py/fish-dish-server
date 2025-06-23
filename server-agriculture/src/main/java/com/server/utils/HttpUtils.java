package com.server.utils;

import com.server.properties.AiLocalProperties;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

/**
 * 通用http发送方法
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(AiLocalProperties.getTimeout()).build();

    /**
     * 发送POST方式请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static CloseableHttpResponse doPostStream(String url, Map<String, String> paramMap, String file) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 构建multipart请求体
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            // 添加表单参数
            for (Map.Entry<String, String> param : paramMap.entrySet()) {
                builder.addTextBody(param.getKey(), param.getValue(),
                        ContentType.create("text/plain", "UTF-8"));
            }

            if (!ObjectUtils.isEmpty(file)) {
                // 添加文件
                builder.addBinaryBody("prompt_image", StringBase64ToBytes(file),
                        ContentType.create("application/octet-stream"), "prompt_image");
            }

            HttpEntity multipart = builder.build();

            httpPost.setEntity(multipart);
            httpPost.setConfig(config);

            // 执行http请求
            return httpClient.execute(httpPost);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 发送POST方式请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost4Json(String url, Map<String, String> paramMap, String file) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = null;

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 构建multipart请求体
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            // 添加表单参数
            for (Map.Entry<String, String> param : paramMap.entrySet()) {
                builder.addTextBody(param.getKey(), param.getValue(),
                        ContentType.create("text/plain", "UTF-8"));
            }

            if (!ObjectUtils.isEmpty(file)) {
                // 添加文件
                builder.addBinaryBody("prompt_image", StringBase64ToBytes(file),
                        ContentType.create("application/octet-stream"), "prompt_image");
            }

            HttpEntity multipart = builder.build();

            httpPost.setEntity(multipart);
            httpPost.setConfig(config);

            // 执行http请求
            response = httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }


    public static byte[] StringBase64ToBytes(String base64Str){
        try {
            base64Str = base64Str.split("base64,")[1];
            byte[] bytes = Base64.getDecoder().decode(base64Str);
            byte[] result = Arrays.copyOf(bytes, bytes.length);
            for (int i = 0; i < result.length; ++i) {
                if (result[i] < 0) {
                    result[i] += 256;
                }
            }
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
