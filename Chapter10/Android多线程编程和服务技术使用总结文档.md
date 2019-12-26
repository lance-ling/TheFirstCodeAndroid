# Android 多线程编程和服务技术使用

## 多线程编程
### 线程的基本用法
1. 继承Thread类 实例化之后调用.start()方法
2. 实现Runnable接口 实例化后作为参数实例化Thread对象 再调用.start()方法
### 在子线程中更新UI
1. 定义一个Handler类对象 重写它的handleMessage(Message)方法
2. 开启子线程
   1. 新建Message对象 
   2. 设置Message对象的what属性
   3. 调用Handler对象的sendMessage(Message)方法 将Message对象发送出去
### 解析异步消息处理机制
1. Message
   1. 在线程之间传递的消息
   2. 内部携带少量信息 用于不同线程之间交换数据
2. Handler
   1. 用于发送和处理消息
   2. sendMessage --> handleMessage
3. MessageQueue
   1. 消息队列 用于存放所有通过Handler发送的消息
   2. 这部分消息会一直存在于消息队列中 等待被处理
   3. 每个线程只会有一个MessageQueue对象
4. Looper
   1. 每个线程中MessageQueue的管家
   2. 调用Looper的loop()方法后 就会进入一个无限循环中 然后每当发现MessageQueue中存在一条消息 就会把它取出 并传递到Handler的handleMessage()方法中
   3. 每个线程中只会有一个Looper对象
### 使用AsyncTask
1. AsyncTask基本用法
   1. 继承指定泛型 <Params, Progress, Result>
      1. Params 在执行AsyncTask时需要传入的参数 可用于在后台任务中使用
      2. Progress 后台任务执行时 如果需要在界面上显示当前的进度 使用此次指定的泛型作为进度单位
      3. Result 当任务执行完毕后 如果需要对结果进行返回 使用此处指定的泛型作为返回值类型
   2. 常用重写方法
      1. onPreExecute() 该方法在后台任务开始执行之前调用 用于进行界面的初始化操作
      2. doInBackground(Params...) 在子线程运行的主体 此处处理所有的耗时任务 任务完成可以通过return将任务执行结果返回 `此处不能进行UI操作`
      3. onProgressUpdate(Progress...) 该方法在pulishProgress(Progress...)方法之后执行 `此处可以对UI进行操作`
      4. onPostExecute(Result) 该方法在任务执行完毕后调用 执行结果传入该方法 可以利用返回结果`进行UI操作`
2. 实例化继承了AsyncTask的类 调用execute()方法启动


## 服务
### 概念
1. 服务是Android中实现程序后台运行的解决方案
2. 非常适合去执行不需要和用户交互且需要长期运行的任务
3. 服务并不是运行再一个独立的进程中，而是依赖于创建服务时所在的应用进程

### 基本用法
#### 定义一个服务
1. 继承Service类
2. 重写onBind() onCreate() onStartCommand() onDestory()方法
3. 去AndroidManifest.xml清单文件注册服务
#### 启动和停止服务
1. startService() 启动服务
2. stopService() 停止服务
#### 活动和服务进行通信
```java
Context:bindService() --> new ServiceConnection() {} --> Binder(Service:onBind()) --> Service
```
1. 调用Context的bindService()方法进行服务绑定
2. 需要创建ServiceConnection实例 可以重写onServiceConnected()/onServiceDisconnected()方法对服务的连接和断开进行监听
3. 在Service创建自定义Binder的继承类 可以在外部(Activity)的onServiceConnected()参数中获取到它 从而调用它的方法
### 服务的生命周期
1. (Context)startService() -> (Service)onCreate()
2. (Service)onStartCommand()
3. (Context)stopService()/(Service)stopSelf() -> (Service)onDestory()

1. (Context)bindService() -> (Service)onCreate() -> (Service)onBind()
2. (Context)unbindService() -> (Service)onDestory()

### 使用服务的技巧
#### 前台服务
1. 可以一直保存运行，不会由于系统内存不足的原因导致被回收掉
2. 会有一个正在运行的图标在系统状态栏显示，可以通过下拉状态栏看到更加详细的消息
##### 创建方式
在Service:onCreate()方法 创建通知

#### 使用IntentService
异步的 会自动停止的服务
1. 自动调用多线程处理逻辑
2. 完成任务后自动停止