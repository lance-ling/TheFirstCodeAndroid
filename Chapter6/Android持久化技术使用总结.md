# Android 持久化技术使用总结

## 持久化技术
1. 将内存中的瞬时数据保存到存储设备中，保证即使设备断电关机的情况下，数据仍然不会丢失
2. 保存在内存中的数据是瞬时状态的，保存在存储设备中的数据是处于持久状态的
3. 持久化技术提供了一种机制可以让数据在瞬时状态和持久状态之间进行转换

## 数据持久化方式
### 文件存储
#### 将数据存储到文件中
- 存储步骤
    1. 通过Context的openFileOutput方法获取FileOutputStream对象
    2. 可以使用BufferedWriter将数据写入文件 Java IO流

```java
String data = "Data to Save";
FileOutputStream out = null;
BufferedWriter writer = null;
try {
    // openFileOutput(String, MODE) Context提供 
    // 第一个参数为文件名 不可以包含路径 因为所有文件都是默认存储到/data/data/<packagename>/files/目录下的
    // 第二个参数是文件的操作模式 MODE_PRIVATE(默认)和MODE_APPEND
    out = openFileOutputStream("data", Context.MODE_PRIVATE);
    writer = new BufferedWriter(new OutputStreamWriter(out));
    writer.write(data);
} ...
```

#### 从文件中读取数据
- 读取步骤
    1. 通过Context提供的openFileInput方法获取FileInputStream
    2. 可以使用BufferedReader将数据从文件中读取 Java IO流

### SharedPreferences存储
使用键值对的方式存储数据
#### 将数据存储到SharedPreferences中
- 获取SharedPreferences对象
    1. Context类 getSharedPreferences(filename, MODE_PRIVATE) 文件名和指定操作模式 Android 6.0+只有MODE_PRIVATE一种模式
    2. Activity类 getPreferences(MODE_PRIVATE) 指定操作模式 文件名为当前活动类名
    3. PreferenceManager类 getDefaultSharedPreferences(Context):static 自动使用当前程序包名作为前缀来命名SharedPreferences文件
- 通过SharedPreferences对象向文件中存储数据
  1. 调用SharedPreferences对象的edit方法来获取一个SharedPreferences.Edit对象
  2. 向SharedPreferences.Edit对象中添加数据，使用putXXX方法(putBoolean, putString, putInt)
  3. 调用apply方法将添加的数据提交，完成数据存储操作
#### 从SharedPreferences中读取数据
通过上面方法获取SharedPreferences对象，使用getXXX方法获取数据 (getBoolean, getString, getInt)

### 数据库存储
#### SQLite数据库存储
- 操作SQLite数据库
  1. 创建数据库
     1. 继承SQLiteOpenHelper 在onCreate中使用db.execSQL(SQL语句)创建数据库
     2. 通过构造器创建对象 调用对象的getWritableDatabase()方法 自动创建数据库
  2. 升级数据库
     1. 通过修改新的版本号version 处理onUpgrade()方法的逻辑
  3. 添加数据
     1. 通过getWritableDatabase()方法获取SQLiteDatabase对象
     2. 新建ContentValues对象来组装数据
     3. 通过SQLiteDatabase对象的insert()方法插入ContentValues对象
  4. 更新数据
     1. 通过getWritableDatabase()方法获取SQLiteDatabase对象
     2. 新建ContentValues对象来存储更新的数据
     3. 通过SQLiteDatabase对象的update()方法置入ContentValues对象 更新数据
  5. 删除数据
     1. 通过getWritableDatabase()方法获取SQLiteDatabase对象
     2. 通过SQLiteDatabase对象的delete()方法 删除参数指定数据
  6. 查询数据
     1. 通过getWritableDatabase()方法获取SQLiteDatabase对象
     2. 通过SQLiteDatabase对象的query()方法获得指针Cursor对象
     3. 遍历Cursor对象 取出数据
     4. 关闭Cursor对象
- 注意：Android Studio虚拟机高版本没有root权限 也没有sqlite。。 本例是在Android 5.0中运行测试的

#### 使用LitePal操作数据库