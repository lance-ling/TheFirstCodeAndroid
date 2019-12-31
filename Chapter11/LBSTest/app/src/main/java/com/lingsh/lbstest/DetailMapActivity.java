package com.lingsh.lbstest;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

/**
 * 可以显示地图 和当前位置标志
 * 定位图标箭头指向手机朝向
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/31 10:35
 **/


public class DetailMapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {

    public static final String LOCATION_MARKER_FLAG = "mylocation";

    public static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    public static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);

    private AMap aMap;
    private MapView mMapView;
    private SensorEventListenerImpl mSensorEventListener;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mClientOption;

    private Circle mCircle;
    private Marker mMarker;

    private TextView mLocationErrText;

    private boolean mFirstFix = false;

    public static void toThisActivity(Context from) {
        Intent intent = new Intent(from, DetailMapActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示应用的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_map);

        mMapView = ((MapView) findViewById(R.id.map_view));
        mMapView.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mSensorEventListener != null) {
            mSensorEventListener.registerSensorListener();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorEventListener != null) {
            mSensorEventListener.unRegisterSensorListener();
            mSensorEventListener.setCurrentMarker(null);
            mSensorEventListener = null;
        }
        mMapView.onPause();
        deactivate();
        mFirstFix = false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMarker != null) {
            mMarker.destroy();
        }
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }

        mSensorEventListener = new SensorEventListenerImpl(this);
        mSensorEventListener.registerSensorListener();

        mLocationErrText = ((TextView) findViewById(R.id.location_errInfo_text));
        mLocationErrText.setVisibility(View.GONE);
    }

    /**
     * 设置aMap属性
     */
    private void setUpMap() {
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位 false表示隐藏定位层并不可触发定位 默认为false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式 可以由定位/跟随或地图根据面向方向旋转几种
        aMap.setMyLocationStyle(new MyLocationStyle().myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
    }

    /**
     * 定位成功后回调函数
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener == null || aMapLocation == null) {
            return;
        }
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mLocationErrText.setVisibility(View.GONE);

            // 获取经纬度 即当前位置
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            if (!mFirstFix) {
                mFirstFix = true;
                // 添加定位精准度圆
                addCircle(latLng, aMapLocation.getAccuracy());
                // 添加定位图标
                addMarker(latLng);
                // 定位图标旋转
                mSensorEventListener.setCurrentMarker(mMarker);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            } else {
                mCircle.setCenter(latLng);
                mCircle.setRadius(aMapLocation.getAccuracy());
                mMarker.setPosition(latLng);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            }
        } else {
            String errText = String.format("定位失败，%d: %s",
                    aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
            Log.e("AmapErr", errText);
            mLocationErrText.setVisibility(View.VISIBLE);
            mLocationErrText.setText(errText);
        }
    }

    /**
     * 激活定位
     *
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient != null) {
            return;
        }
        mLocationClient = new AMapLocationClient(this);
        mClientOption = new AMapLocationClientOption();
        // 设置定位监听
        mLocationClient.setLocationListener(this);
        // 设置为高精度定位模式
        mClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位参数
        mLocationClient.setLocationOption(mClientOption);

        // 此方法为每隔固定时间会发起一次定位请求
        // 为了减少电量消耗或网络流量消耗
        // 注意设置合适的定位时间间隔 (最小间隔支持为2000ms) 并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后 在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下 定位无论成功与否 都无需调用stopLocation()方法移除请求 定位SDK内部会移除
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    private void addCircle(LatLng latLng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latLng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latLng) {
        if (mMarker != null) {
            return;
        }

        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(this.getResources(), R.mipmap.navi_map_gps_locked)
        ));
        options.anchor(0.5f, 0.5f);
        options.position(latLng);
        mMarker = aMap.addMarker(options);
        mMarker.setTitle(LOCATION_MARKER_FLAG);
    }
}
