# Android网络技术(HTTP协议在安卓的使用)
## WebView 以此在应用中嵌入游览器 展示网页
1. 在布局layout中添加WebView控件
2. 在Activity中实例化WebView
3. 调用WebView对象的
   1. .getSettings().setJavaScriptEnabled(true) // 设置游览器属性(此次只设置游览器支持JavaScript功能)
   2. .setWebViewClient(new WebViewClient()) // 传入一个WebViewClient实例 设置跳转的页面仍在WebView中显示
   3. .loadUrl(uri:String) // 打开网址
4. 注意：需要声明访问网络的权限

## 使用HTTP协议访问网络
### HttpURLConnection
#### 基本用法
1. 实例化Url 传入目标网络地址
2. 通过Url对象的openConnection()方法得到HttpURLConnection实例
3. 通过HttpURLConnection实例的setRequestMethod()方法传入HTTP请求所使用的方法 GET/POST
4. 自由定制
   1. setConnectTimeout() // 设置连接超时的毫秒数
   2. setReadTimeout() // 设置读取超时的毫秒数
5. 调用getInputStream()方法获取服务器返回的输入流 然后就是对输入流的读取
6. 调用disconnect()方法将HTTP连接关闭

### OkHttp
1. 创建OkHttpClient实例 new OkHttpClient()
2. 创建Request对象 new Request.Builder().build() 空的 需要在build()之前连缀方法来丰富这个Request对象
   1. .url() // 设置目标网址
3. 创建Call对象 并调用其execute()方法来发送请求并获取服务器返回数据 (OkHttpClient对象.newCall(Request对象) --> Call对象).execute() --> Response对象
4. Respose对象 服务器返回的数据
   1. 调用Respose对象.body().string()获取具体内容

## XML、JSON数据解析方式

