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
1. 定义广播接收器(继承BroadcastReceiver的类)
2. 重写onReceive方法，处理接收到信息后的具体逻辑
3. 通过IntentFilter指定接收广播类型ACTION
4. 实例化广播接收器
5. 在onCreate方法中，注册广播接收器registerReceiver(BroadcastReceiver, IntentFilter)
6. 在onDestroy方法中，解除广播接收器的注册unregisterReceiver(BroadcastReceiver)

- demo 监听网络变化

#### 静态注册 在AndroidManifest.xml中注册
- 注册步骤
1. 定义广播接收器(继承BroadcastReceiver的类)
2. 重写onReceive方法，处理接收到信息后的具体逻辑
3. 在AndroidManifest.xml中注册 节点：`<recevier>`
4. 在节点内使用 `<intent-filter>` 设置指定接收的广播类型ACTION
5. 如果需要权限的话，还需要加入权限

- demo 实现开机启动(监听系统开启信息)

#### 广播接收器 动态和静态注册的区别
1. 动态注册的广播接收器可以自由地控制注册与注销
2. 动态注册必须在程序启动之后才能接收到广播，因为逻辑是写在onCreate方法内
3. 静态注册的广播接收器必须提前在AndroidManifest.xml中配置
4. 静态注册在获取到权限后，可以在程序未启动的情况下就能接收到广播
### 发送自定义广播
#### 发送标准广播
- 发送广播步骤 (使用静态注册)
1. 定义广播接收器接收自己发送的广播
2. 在AndroidManifest.xml中设置接收广播类型ACTION
3. 使用Intent(ACTION)和sendBroadcast(Intent)发送指定ACTION广播

#### 发送有序广播
- 发送广播步骤 (使用静态注册)
1. 定义广播接收器接收所发送的广播
2. 在AndroidManifest.xml中设置接收广播类型ACTION
3. 使用Intent(ACTION)和sendOrderedBroadcast(Intent, null)发送指定有序ACTION广播
4. 可以在AndroidManifest.xml的`<intent-filter android:priority=?>`设置优先级
5. 可以在onReceive()方法中调用abortBroadcast方法截断当前广播

#### 发送本地广播
- 发送广播步骤 (只能使用动态注册)
1. 定义广播接收器(继承BroadcastReceiver)
2. 通过LocalBroadcastManager.getInstance(this)获取本地广播管理实例
3. 接收：设置指定接收广播类型，再通过LocalBroadcastManager注册
localBroadcastManager.registerReceiver(BroadcastReceiver, IntentFilter)
4. 发送：设置指定发送广播类型，再通过LocalBroadcastManager发送
localBroadcastManager.sendBroadcast(Intent)
5. 在onDestroy方法中解除注册 
localBroadcastManager.unregisterReceiver(BroadcastReceiver)

- 本地广播的优势
1. 可以明确地知道正在发送的广播不会离开当前程序，没有机密数据泄露问题
2. 不会接收其他程序的广播，没有安全漏洞隐患
3. 发送本地广播比发送系统全局广播更加高效

## 最佳实践 --强制下线功能
- 基本思路
1. 强制下线需要先关闭所有的活动，然后回到登录界面
2. 在某一页面收到账号在他处登录时，启动强制下线
3. 强制下线将弹出一个对话框，使得用户无法进行其他任何操作，必须点击确认后，返回登录界面

- 实现
1. ActivityCollector 管理所有活动 使用List实现
2. BaseActivity 作为所有Activity的父类 与ActivityCollector组合，添加删除
3. LoginActivity 登录界面 强制下线后返回页面
4. MainActivity 内含强制下线按钮 发送强制下线广播
5. ForceOfflineReceiver 强制下线的广播接收器 处理强制下线逻辑
