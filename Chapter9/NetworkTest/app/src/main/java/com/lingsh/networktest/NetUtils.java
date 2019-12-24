package com.lingsh.networktest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 处理网络连接 及信息获取
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/24 14:40
 **/


public class NetUtils {
    private static final String TAG = "NetUtils";

    static final String TEST_NET_STATION = "http://www.baidu.com";
    private static final String REQUEST_METHOD_GET = "GET";

    static String netByHttpUrlConnection() {
        Log.d(TAG, "netByHttpUrlConnection: ");
        String content = null;
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
            content = fromStream(stream);
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

        Log.d(TAG, "netByHttpUrlConnection: " + content);
        return content;
    }

    static String netByOkHttp() {
        Log.d(TAG, "netByOkHttp: ");
        String content = null;

        // 1. 创建OkHttpClient实例 new OkHttpClient()
        OkHttpClient okHttpClient = new OkHttpClient();
        // 2. 创建Request对象 new Request.Builder().build() 空的 需要在build()之前连缀方法来丰富这个Request对象
        //  1. .url() // 设置目标网址
        Request request = new Request.Builder().url(TEST_NET_STATION).build();
        // 3. 创建Call对象 并调用其execute()方法来发送请求并获取服务器返回数据 (OkHttpClient对象.newCall(Request对象) --> Call对象).execute() --> Response对象
        try {
            Response response = okHttpClient.newCall(request).execute();
            // 4. Respose对象 服务器返回的数据
            //  1. 调用Respose对象.body().string()获取具体内容
            content = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "netByOkHttp: " + content);
        return content;
    }

    private static String fromStream(InputStream stream) {
        Log.d(TAG, "fromStream: ");
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
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

        return sb.toString();
    }
}
