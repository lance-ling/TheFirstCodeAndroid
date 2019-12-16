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

### 数据库存储
