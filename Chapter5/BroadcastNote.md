# Android 广播学习总结

## Android广播类型
### 标准广播
1. 完全异步执行
2. 在广播发出之后，所有的广播接收器几乎都会在同一时刻接收到这条广播
3. 广播效率高，无法被截断
### 有序广播
1. 同步执行
2. 在广播发出之后，同一时刻只有一个广播接收器能够收到这条广播
3. 广播接收器有先后顺序，优先级高的广播接收器可以先收到广播信息，并且可以截断正在传播的广播

## 广播的使用
### 接收系统广播
#### 动态注册 在代码中注册
- 注册步骤
1. 声明广播接收器(继承BroadcastReceiver的类)
2. 重写onReceive方法，处理接收到信息后的具体逻辑
3. 通过IntentFilter指定接收广播类型ACTION
4. 实例化广播接收器
5. 在onCreate方法中，注册广播接收器registerReceiver(BroadcastReceiver, IntentFilter)
6. 在onDestroy方法中，解除广播接收器的注册unregisterReceiver(BroadcastReceiver)

- demo 监听网络变化

#### 静态注册 在AndroidManifest.xml中注册


## 吐槽
1. API变化太频繁了，ConntectivityManager.getNetworkInfo/getActiveNetworkInfo 都惨遭遗弃，连NetworInfo也被遗弃了。。。
有些甚至API 21添加， API 23移除