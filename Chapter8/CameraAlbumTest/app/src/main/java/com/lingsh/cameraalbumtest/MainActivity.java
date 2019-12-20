package com.lingsh.cameraalbumtest;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private static final int REQUEST_CODE_WRITE = 3;
    private Uri mUri;
    private ImageView mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button camera = (Button) findViewById(R.id.use_camera);
        Button album = (Button) findViewById(R.id.use_album);
        mPicture = (ImageView) findViewById(R.id.show_picture);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 判断是否获取读写存储卡的权限 WRITE_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE);
                    return;
                }
                choosePhoto();
            }
        });

    }

    private void choosePhoto() {
        // 2. 使用Intent发送action="android.intent.action.GET_CONTENT" 设置需要返回类型
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void takePhoto() {
        // 1. 创建File对象，用于存放拍下的图片 存放位置设为应用关联缓存目录getExternalCacheDir()
        //  1. 具体路径："/sdcard/Android/data/<package name>/cache"
        //  2. 从Android 6.0之后 读写SD卡被列为危险权限 如果将图片存放在SD卡的任何其他位置，都需要运行时权限处理才行，而使用应用关联目录则可以跳过这一步
        File file = new File(getExternalCacheDir(), "output_image.jpg");
        //  3. 额外：文件需要调用createNewFile()方法新建，在此之前需要判断文件是否已存在，已存在就先删除
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. 判断：
        mUri = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //  1. Android 7.0- 调用Uri的fromFile()方法将File对象转换为Uri对象
            mUri = Uri.fromFile(file);
        } else {
            //  2. Android 7.0+ 调用FileProvider的getUriForFile()方法将File对象转换成一个封装过的Uri对象
            mUri = FileProvider.getUriForFile(MainActivity.this, "com.lingsh.cameraalbumtest.fileprovider", file);
        }
        // 3. 构建一个Intent对象 将该Intent对象的action置为android.media.action.IMAGE_CAPTURE
        // 再将图片地址置入putExtra() name=MediaStore.EXTRA_OUTPUT
        // 最后调用startActivityForResult()启动Activity
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: " +
                "requestCode = [" + requestCode + "], " +
                "permissions = [" + Arrays.toString(permissions) + "], " +
                "grantResults = [" + Arrays.toString(grantResults) + "]");

        if (requestCode == REQUEST_CODE_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thank for you grant", Toast.LENGTH_SHORT).show();
                choosePhoto();
            } else {
                Toast.makeText(this, "Sorry, you deny the grant", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 4. 在onActivityResult()方法中，调用BitmapFactory的decodeStream()方法将拍下的图片解析成Bitmap对象
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUri));
                        // 5. 使用ImageView的setImageBitmap()方法 显示图片
                        mPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                // 3. 处理intent返回数据
                //  1. Android 4.0+ 选取图片不再返回图片真实的Uri，而是封装过的
                //  2. Android 4.0- 选取图片返回图片真实的Uri
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        // 4. Android 4.0+ 解析返回的被封装的Uri
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //  1. document类型 取document id
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(uri, selection);
            } else if ("com.android.providers.downloads.document".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //  2. content类型 普通方式
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //  3. file类型 直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;

        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }

    private void displayImage(String imagePath) {
        // 5. 根据图片路径显示图片
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPicture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
