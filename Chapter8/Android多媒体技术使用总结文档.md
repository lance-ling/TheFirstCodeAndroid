# Android多媒体技术使用总结

## 通知
可以让某个不在前台运行的程序向用户发出一些提示信息
### 基本用法
1. 通过Context的getSystemService(Context.NOTIFICATION_SERVICE)获取NotificationManager对象
2. 使用扩展包的NotificationCompat工具类创建Notification对象
3. 通过NotificationCompat.Builder对象填入配置(setContentTitle/setContentText/setWhen)
4. 调用NotificationManager的notify()方法显示通知
- 注意：API 29运行不响应通知， API 21可以响应

- PendingIntent 响应通知的点击事件 延迟响应
1. 创建PendingIntent对象 
2. 调用NotificationCompat.Builder对象的setContentIntent()方法置入PendingIntent对象 

- 取消通知
1. 配置NotificationCompat.Builder对象的setAutoCancel()方法
2. 调用NotificationManager对象的cancel()方法 传入需要被取消的id
3. 
## 摄像头和相册


## 多媒体文件


### 播放音频


### 播放视频