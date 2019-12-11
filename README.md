# TheFirstCodeAndroid
《第一行代码 （Android）》 练习

## Chapter 1 创建第一个Android项目
- 详解项目中的资源
> module(app) AndroidManifest.xml build.gradle res(layout/drawable/values) 
- 日志的五种级别
> verbose < debug < info < warn < error

## Chapter 2 探究Activity
- Activity的基本用法
> 1. 在AndroidManifest.xml注册
> 2. 使用Toast
> 3. 使用Menu
- Activity的跳转(Intent的两种用法)
> 1. 显式Intent 直接指明来源和去路
> 2. 隐式Intent 通过AndroidManifest.xml设定的intent-filter圈定启动的Activity
- Activity的生命周期
> Create --> Start --> Resume --> Pause --> Stop --> Destroy  
> Stop --> start (restart)  
- Activity的启动模式
> 1. standard 默认标准模式 每次启动activity都是全新的 完全不顾栈中已经创建过启动的activity且在栈顶(当前)
> 2. singleTop 当栈顶已有所需创建的activity 则直接复用 (很好地解决重复创建栈顶activity的问题)
> 3. singleTask 当栈内已有所需创建的activity 则将其置入栈顶(即其上的activity全部弹出) 没有则自建
> 4. singleInstance 给被设置该模式的activity单独设置一个返回栈 解决共享activity实例问题
