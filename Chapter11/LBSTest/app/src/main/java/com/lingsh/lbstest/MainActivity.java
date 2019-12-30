package com.lingsh.lbstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    /**
     * 声明AMapLocationClient类对象
     */
    public AMapLocationClient mLocationClient = null;

    /**
     * 声明定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        /**
         * 定位SDK API文档
         * http://a.amap.com/lbs/static/unzip/Android_Location_Doc/index.html?com/amap/api/location/AMapLocation.html
         *
         * @param location 定位信息类。定位完成后的位置信息。
         */
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null) {
                if (location.getErrorCode() == 0) {
                    // 定位成功 解析amplocation的相应内容
                    // 获取当前定位结果来源 如网络定位结果
                    int locationType = location.getLocationType();

                    // 获取经度
                    double longitude = location.getLongitude();
                    // 获取纬度
                    double latitude = location.getLatitude();
                    String format = String.format("定位类型:%d 经度-纬度:[%s-%s]",
                            locationType, longitude, latitude);
                    Log.d("Amp==经度：纬度",
                            format);
                    mPosition.setText(format);
                } else {
                    // 定位失败
                    // 通过错误码ErrCode信息判断失败原因
                    // errInfo是错误信息
                    Log.e("AmapError", "location Error, ErrCode:"
                            + location.getErrorCode() + " errInfo: "
                            + location.getErrorInfo());
                }
            }
        }
    };

    /**
     * 声明AMapLocationClientOption对象
     */
    public AMapLocationClientOption mLocationClientOption = null;
    private TextView mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPosition = (TextView) findViewById(R.id.position_text_view);

        // 运行时检测定位权限是否获取
        if (checkLocationPermissionGrant()) {
            // 去进行定位
            requestLocation();
        }
    }

    /**
     * 获取定位信息
     */
    private void requestLocation() {
        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());

        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        // 初始化AMapLocationClientOption对象
        mLocationClientOption = new AMapLocationClientOption();
        // 设置定位模式为AmapLocationMode.Battery_Saving 低能耗模式
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 给客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationClientOption);

        // 启动定位
        mLocationClient.startLocation();
        // 获取一次定位 默认为false 置为true表示只定位一次
        mLocationClientOption.setOnceLocation(true);
    }

    private boolean checkLocationPermissionGrant() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {
                Toast.makeText(this, "抱歉！必须赋予位置权限，才能使用本服务", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
