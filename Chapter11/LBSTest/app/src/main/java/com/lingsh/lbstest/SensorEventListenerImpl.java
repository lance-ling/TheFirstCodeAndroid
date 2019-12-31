package com.lingsh.lbstest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.amap.api.maps.model.Marker;

/**
 * 实现传感器事件监听
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/31 10:13
 **/


public class SensorEventListenerImpl implements SensorEventListener {

    public static final float CIRCLE_ANGLE = 360.0F;
    private final int TIME_SENSOR = 100;
    private final Sensor mSensor;

    private SensorManager mSensorManager;
    private long lastTime = 0;
    private Context mContext;
    private Marker mMarker;
    private float mAngle;

    public SensorEventListenerImpl(Context context) {
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    public void registerSensorListener() {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegisterSensorListener() {
        mSensorManager.unregisterListener(this, mSensor);
    }

    public void setCurrentMarker(Marker marker) {
        mMarker = marker;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[0];
            x += getScreenRotationOnPhone(mContext);
            x %= CIRCLE_ANGLE;
            if (x > CIRCLE_ANGLE / 2) {
                x -= CIRCLE_ANGLE;
            } else if (x < -CIRCLE_ANGLE / 2) {
                x += CIRCLE_ANGLE;
            }

            if (Math.abs(mAngle - x) < 3.0f) {
                return;
            }
            mAngle = Float.isNaN(x) ? 0 : x;
            if (mMarker != null) {
                mMarker.setRotateAngle(CIRCLE_ANGLE - mAngle);
            }
            lastTime = System.currentTimeMillis();
        }
    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @param context
     * @return 0：竖屏 90：左横屏 180：反向竖屏 270：右横屏
     */
    private static int getScreenRotationOnPhone(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return -90;
        }

        return 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
