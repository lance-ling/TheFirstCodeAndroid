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

### 解析XML格式数据
#### Pull解析方式
1. 获取XmlPullParserFactory实例 单例
2. 通过XmlPullParserFactory实例得到XmlPullParser对象
3. 调用XmlPullParser对象的setInput方法 填入XML数据进行解析
4. 通过parser对象的getEventType方法得到当前的解析事件
5. 以XmlPullParser.END_DOCUMENT为解析结尾标识 
   1. 通过getName()方法得到当前节点的名字
   2. 调用next()方法获取下一个解析事件
   3. 调用nextText()方法获取节点内具体内容
   4. 以XmlPullParser.END_TAG为标识表示开始解析某个节点
   5. 以XmlPullParser.END_TAG为标识完成解析某个节点

#### SAX解析方式
1. 新建一个类继承DefaultHandler 重写父类的方法
   1. startDocument() 开始解析XML
   2. startElement() 开始解析XML某个节点
   3. characters() 获取节点内容
   4. endElement() 完成解析某个节点
   5. endDocument() 完成整个XML解析之后
2. 在自建类内部
   1. 节点名称:String 节点内容:StringBuilder
   2. 在startDocumnet() 初始化节点内容实例
   3. 在startElement() 记录当前节点名称
   4. 在characters() 根据当前的节点名判断将内容添加到哪一个StringBuilder对象
   5. 在endElement() 重置StringBuilder对象
3. 先创建SAXParserFactory对象
4. SAXParserFactory对象.newSAXParser().getXMLReader() 获取XMLReader对象
5. 实例化自建类 置入XMLReader对象的setContentHandler()方法
6. 调用XMLReader对象对parse()方法

#### DOM解析方式

### 解析JSON格式数据
#### 使用JSONObject
1. 将数据传入JSONArray对象中
2. 循环遍历JSONArray对象 取出每个JSONObject对象
3. 通过JSONObject对象的getString()方法取出键值
#### 使用GSON
1. 添加gson依赖
2. 实例化Gson对象
   1. Gson对象.fromJson() 可以将JSON数据直接解析成Bean对象
   2. 借助new TypeToken<List<Bean>>(){}.getType() 可以解析JSON数组成Bean对象列表

### 测试数据
#### XML
```xml
<apps>
    <app>
        <id>1</id>
        <name>Google Maps</name>
        <version>1.0</version>
    </app>
    <app>
        <id>2</id>
        <name>Chrome</name>
        <version>2.1</version>
    </app>
    <app>
        <id>3</id>
        <name>Google Play</name>
        <version>2.3</version>
    </app>
</apps>
```
#### JSON
```json
[
  {
    "id": "1",
    "name": "Google Maps",
    "version": "1.0"
  },
  {
    "id": "2",
    "name": "Chrome",
    "version": "2.1"
  },
  {
    "id": "3",
    "name": "Google Play",
    "version": "2.3"
  }
]
```