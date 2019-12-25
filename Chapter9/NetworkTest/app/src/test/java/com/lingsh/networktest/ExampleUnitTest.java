package com.lingsh.networktest;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testHttpUrlConnection() {
        try {
            URL url = new URL("https://cn.bing.com/");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.connect();

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            if (reader != null) {
                reader.close();
            }

            if (connection != null) {
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        String TEST_NET_STATION = "http://www.baidu.com";
        String REQUEST_METHOD_GET = "GET";

        HttpURLConnection connection = null;
        try {
            // 1. 实例化Url 传入目标网络地址
            URL url = new URL(TEST_NET_STATION);
            // 2. 通过Url对象的openConnection()方法得到HttpURLConnection实例
            connection = (HttpURLConnection) url.openConnection();
            // 3. 通过HttpURLConnection实例的setRequestMethod()方法传入HTTP请求所使用的方法 GET/POST
            connection.setRequestMethod(REQUEST_METHOD_GET);
            // 4. 自由定制
            //  1. setConnectTimeout() // 设置连接超时的毫秒数
            connection.setConnectTimeout(8000);
            //  2. setReadTimeout() // 设置读取超时的毫秒数
            connection.setReadTimeout(8000);
            connection.connect();
            // 5. 调用getInputStream()方法获取服务器返回的输入流 然后就是对输入流的读取
            InputStream stream = connection.getInputStream();
            fromStream(stream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 调用disconnect()方法将HTTP连接关闭
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void fromStream(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("---------------------------------");
        System.out.println(sb.toString());
    }
}