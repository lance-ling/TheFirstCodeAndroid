# Android运行时权限和内容提供器使用总结
跨程序共享数据

## 内容提供器
1. 内容提供器主要用于在不同的应用程序之间实现数据共享的功能
2. 提供了一套完整的机制，允许一个程序访问另外一个程序中的数据，同时还能保证被访数据的安全性
3. Android实现跨程序共享数据的标准方式

## 运行时权限
### 危险权限
|权限组名|权限名|
|-|-|
|CALENDAR|READ_CALENDAR/WRITE_CALENDAR |
|CAMERA|CAMERA |
|CONTACTS|READ_CONTACTS/WRITE_CONTACTS/GET_CONTACTS |
|LOCATION|ACCESS_FINE_LOCATION/ACCESS_COARSE_LOCATION |
|MICROPHONE|RECORD_AUDIO |
|PHONE|READ_PHONE_STATE/CALL_PHONE/READ_CALL_LOG/WRITE_CALL_LOG/ADD_VOICEMAIL/USE_SIP/PROCESS_OUTGOING_CALLS |
|SENSORS|BODY_SENSORS |
|SMS|SEND_SMS/RECEIVE_SMS/READ_SMS/RECEIVE_WAP_PUSH/RECEIVE_MMS |
|STORAGE|READ_EXTERNAL_STORAGE/WRITE_EXTERNAL_STORAGE |

- 注意：
> 1. 普通权限只需在AndroidManifest.xml中使用<uses-permission android:name="">配置即可，系统会自动帮我们授权， 但危险权限需要运行时权限处理，必须由用户手动赋权
> 2. 当用户授权了某一个权限，该权限所属权限组的其他权限也会被授权

### 在运行时申请权限
- 申请步骤
  1. 在AndroidManifest.xml文件中声明权限
  2. 在程序需要权限的之处，通过ContextCompat.checkSelfPermission()方法进行判断是否授权
  3. 如果没有授权，则通过ActivityCompat.requestPermissions()方法进行申请
  4. 同时在当前Activity中override重写onRequestPermissionsResult()方法 处理之前权限申请的返回结果

## 访问其他程序中的数据
### ContentResolver的基本用法
1. 创建实例 Context.getContentResolver()
2. 查询数据 getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder):Cursor
3. 添加数据 getContentResolver().insert(uri, ContentValues)
4. 更新数据 getContentResolver().update(uri, ContentValues, selection, selectionArgs)
5. 删除数据 getContentResolver().delete(uri, selection, selectionArgs)

###  创建内容提供器
1. 继承ContentProvider
2. 实现所继承的方法 onCreate()/query()/insert()/update()/delete()/getType()
   1. onCreate() 初始化内容提供器 进行对数据库的创建和升级
   2. query() 从内容提供器中查询数据 
   3. insert() 向内容提供器中添加一条数据
   4. update() 更新内容提供器中已有的数据
   5. delete() 从内容提供器中删除数据
   6. getType() 根据传入的内容URI来返回相应的MIME类型
3. Uri格式 
   1. 表格 content://<包名>.provider/<表名>
   2. 数据 content://<包名>.provider/<表名>/<数据id>
4. MIME格式
   1. 表格 vnd.android.cursor.dir/vnd.<包名>.provider.<表名>
   2. 数据 vnd.android.cursor.item/vnd.<包名>.provider.<表名>
