package cn.edu.jlu.webcrawler.sms.autoeasy.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class Client {
    /**
     * @param path
     * @param map
     * @param encode
     * @return
     */
    public static String sendHttpClientPost(String path,
                                            Map<String, String> map, String encode) {
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            //实现将请求中的参数封装到请求参数中，请求体中
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encode);
            //使用post方式提交
            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(entity);
            //指定post方式提交数据
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return changeInputStream(httpResponse.getEntity().getContent(), encode);
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    /**
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputStream(InputStream inputStream,
                                            String encode) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] date = new byte[1024];
        String result = "";
        try {
            while ((len = inputStream.read(date)) != -1) {
                outputStream.write(date, 0, len);
            }
            result = new String(outputStream.toByteArray(), encode);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void simpleGet(String host, String path, Map<String, String> map, HttpRequestInterceptor interceptor) {
        try {
            URIBuilder builder = new URIBuilder().setScheme("http")
                    .setHost(host)
                    .setPath(path);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder = builder.setParameter(entry.getKey(), entry.getValue());
            }
            URI uri = builder.build();
            HttpGet httpget = new HttpGet(uri);
            System.out.println(httpget.getURI());
            CloseableHttpClient httpclient = HttpClients.custom()
                    .addInterceptorLast(interceptor)
                    .build();
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println(EntityUtils.toString(entity));
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
