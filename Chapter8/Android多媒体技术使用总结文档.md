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
   3. 额外：文件需要调用createNewFile()方法新建，在此之前需要判断文件是否已存在，已存在就先删除
2. 判断：
   1. Android 7.0- 调用Uri的fromFile()方法将File对象转换为Uri对象
   2. Android 7.0+ 调用FileProvider的getUriForFile()方法将File对象转换成一个封装过的Uri对象
3. 构建一个Intent对象 将该Intent对象的action置为android.media.action.IMAGE_CAPTURE 再将图片地址置入putExtra() 最后调用startActivityForResult()启动Activity
4. 在onActivityResult()方法中，调用BitmapFactory的decodeStream()方法将拍下的图片解析成Bitmap对象
5. 使用ImageView的setImageBitmap()方法 显示图片

### 从相册中选择图片
1. 判断是否获取读写存储卡的权限 WRITE_EXTERNAL_STORAGE
2. 使用Intent发送action="android.intent.action.GET_CONTENT" 设置需要返回类型
3. 处理intent返回数据 
   1. Android 4.0+ 选取图片不再返回图片真实的Uri，而是封装过的
   2. Android 4.0- 选取图片返回图片真实的Uri
4. Android 4.0+ 解析返回的被封装的Uri
   1. document类型 取document id
   2. content类型 普通方式 通过Uri和selection来获取真实的图片路径
   3. file类型 直接获取图片路径
5. 根据图片路径显示图片

## 多媒体文件
### 播放音频
1. 创建MediaPlayer对象， 通过setDataSource()方法设置音频文件的路径
2. 直接使用MediaPlayer类的控制方法 对音频进行操作
   1. prepare() 进入准备状态
   2. start() 开始播放音频
   3. pause() 暂停播放
   4. reset() 停止播放

#### 工作流程
1. 创建MediaPlayer实例
2. 在onCreate() 进行运行时权限处理 动态申请WRITE_EXTRANAL_STORAGE权限
3. 判断用户是否授权
   1. 拒绝，直接退出finish() 
   2. 同意，初始化MediaPlayer对象
4. 创建File对象指定音频文件的路径
5. 调用MediaPlayer对象的setDataSource()和prepare()方法，为播放前做好准备
6. 判断是否正在播放 调用MediaPlayer对象的isPlaying()方法
   1. 开始播放 在没有播放时
   2. 暂停播放 在播放时
   3. 停止播放并初始化 在播放时
7. 在onDestroy() 停止播放并释放MediaPlayer对象

### 播放视频
1. 创建VideoView对象， 通过setVideoPath()方法设置视频文件的路径
2. 直接使用VideoView类的控制方法 对视频进行操作
   1. start() 开始播放
   2. pause() 暂停播放
   3. resume() 重新播放