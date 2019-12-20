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

### 进阶用法 
1. 带铃声 .setSound()
2. 带震动 .setVibrate()
3. 带LED灯 .setLights()
4. 默认效果 .setDefault()

### 高级用法
1. 富文本 NotificationCompat.BigTextStyle().bigText()
2. 带图片 NotificationCompat.BigPictureStyle().bigPicture()
3. 优先级 .setPriority()

## 摄像头和相册
### 调用摄像头拍照
1. 创建File对象，用于存放拍下的图片 存放位置设为应用关联缓存目录getExternalCacheDir() 
   1. 具体路径："/sdcard/Android/data/<package name>/cache"
   2. 从Android 6.0之后 读写SD卡被列为危险权限 如果将图片存放在SD卡的任何其他位置，都需要运行时权限处理才行，而使用应用关联目录则可以跳过这一步
2. 判断：
   1. Android 7.0- 调用Uri的fromFile()方法将File对象转换为Uri对象
   2. Android 7.0+ 调用FileProvider的getUriForFile()方法将File对象转换成一个封装过的Uri对象
3. 构建一个Intent对象 将该Intent对象的action置为android.media.action.IMAGE_CAPTURE 再将图片地址置入putExtra() 最后调用startActivityForResult()启动Activity
4. 在onActivityResult()方法中，调用BitmapFactory的decodeStream()方法将拍下的图片解析成Bitmap对象

### 从相册中选择图片

## 多媒体文件


### 播放音频


### 播放视频