# Android 位置服务使用

## 基于位置的服务 LBS
利用无线电通讯网络或GPS等定位方式来确定出移动设备所在的位置

### 技术实现：GPS定位
1. 工作原理：基于手机内置的GPS硬件直接和卫星交互来获取当前的经纬度信息
2. 优点：定位精准度非常高
3. 缺点：只能在室外使用，室内基本无法接收到卫星的信号
### 技术实现：网络定位
1. 工作原理：根据手机当前网络附近的三个基站进行测速，以此计算出手机和每个基站之间的距离，再通过三角定位确定出一个大概的位置
2. 优点：室内外都可以使用
3. 缺点：定位精准度一般
## 高德地图 LBS API
### 确定设备的经纬度
#### API 文档
https://lbs.amap.com/dev/demo/once-continue-location#Android
#### 步骤
1. 检查运行时权限 `ACCESS_FINE_LOCATION`
2. 创建定位客户端 `AmapLocationClient`
3. 创建定位结果监听器  `AMapLocationListener`
   1. 重写`onLocationChanged()`方法
   2. 传入的`AMapLocation`对象 存储着定位信息
4. 设置定位行为 `AMapLocationClientOption`
   1. 是否只获取一次定位结果 `setOnceLocation()` 默认为false
   2. 设置定位模式 `setLocationMode()` 
   3. 设置定位间隔 `setInterval()` 默认2000ms 最低1000ms
5. 启动定位 `startLocation()`
6. 在关闭应用时 关闭定位 `stopLocation()`
### 切换定位模式(精度更高的GPS定位)
### 转换经纬度为位置信息
### 显示地图
### 移动到定位设备位置
### 地图上显示设备具体位置

### 资源
#### 错误码
- https://lbs.amap.com/api/android-sdk/guide/map-tools/error-code
- 个人遇到了#1003 没有在程序中进行运行时权限检测 没有打开手机权限
#### 定位类型表
- https://lbs.amap.com/api/android-location-sdk/guide/utilities/location-type/
- 个人遇到 5:Wifi定位结果 之后一直是2:前次定位结果

## 遭遇问题
### LBSTest 刚配置完SDK 编译
> More than one file was found with OS independent path 'assets/location_map_gps_locked.png'
#### 问题解决 排除掉出问题的文件
```xml
packagingOptions {
    exclude 'assets/location_map_gps_locked.png'
    exclude 'assets/location_map_gps_3d.png'
}
```