
package com.likebamboo.gravityshake.utils;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.view.Surface;
import android.view.WindowManager;

import com.likebamboo.gravityshake.listener.OnTagAngleChangedLitener;

import java.util.ArrayList;
import java.util.List;

/**
 * AccelHelper.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述：加速度传感器工具类
 */
public class AccelHelper implements SensorEventListener {
    /**
     * 是否是模拟器
     */
    public static boolean IS_EMULATOR = false;

    /**
     * 2π
     */
    private static final double PI_TIMES_2 = 2 * Math.PI;

    /**
     * 过滤因子
     */
    private static final float kFilteringFactor = 0.6F;

    /**
     * x方向加速度 （转换后）
     */
    private double mAccelerationX = 0D;

    /**
     * y方向加速度 （转换后）
     */
    private double mAccelerationY = 0D;

    /**
     * z向加速度 （转换后）
     */
    private double mAccelerationZ = 0D;

    /**
     * context
     */
    private Context mContext = null;

    /**
     * 临界值
     */
    private final double angularTolerance = Math.toRadians(0.5D);

    /**
     * 角速度
     */
    private double angularVelocity = 0D;

    /**
     * 监听器
     */
    private final List<OnTagAngleChangedLitener> listners = new ArrayList<OnTagAngleChangedLitener>();

    /**
     * 是否是中止状态
     */
    private boolean isPaused = true;

    /**
     * 旋转角度
     */
    private float rotation = 0F;

    /**
     * 加速度传感器
     */
    private final Sensor accelerometer;

    /**
     * 传感器管理对象
     */
    private final SensorManager sensorManager;

    /**
     * 角度
     */
    private float tagAngleDegreen = 0F;

    /**
     * 弧度
     */
    private double tagAngleRadians = 0F;

    /**
     * 更新角度信息线程
     */
    private Runnable updater = new Runnable() {
        public void run() {
            if (isPaused) {
                return;
            }
            try {
                // 计算角度
                recalculateAngle();
            } catch (Exception e) {
                //
            }
            // 每50毫秒更新一次角度信息(每秒更新20次)
            handler.postDelayed(updater, 50L);
        }
    };

    /**
     * handler
     */
    private Handler handler = null;

    /**
     * 当前x方向加速度
     */
    private float x = 0.0F;

    /**
     * 当前Y方向加速度
     */
    private float y = 0.0F;

    /**
     * 当前Z方向加速度
     */
    private float z = 0.0F;

    static {
        if (Build.MODEL.equals("sdk")) {
            // IS_EMULATOR = true;
        } else {
            IS_EMULATOR = false;
        }
    }

    /**
     * 
     */
    private static AccelHelper mInstance = null;

    private AccelHelper(Context context) {
        this.mContext = context;
        if (!IS_EMULATOR) {
            sensorManager = (SensorManager)context.getSystemService(Service.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            handler = new Handler();
        } else {
            sensorManager = null;
            accelerometer = null;
        }
    }

    /**
     * 获取单例
     * 
     * @param context
     * @return
     */
    public static AccelHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AccelHelper.class) {
                if (mInstance == null) {
                    mInstance = new AccelHelper(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加监听器
     * 
     * @param ontaganglechangedlitener
     */
    public void addTagAngleListener(OnTagAngleChangedLitener ontaganglechangedlitener) {
        listners.add(ontaganglechangedlitener);
    }

    /**
     * 获取当前手机转动角度值
     * 
     * @return
     */
    public float getCurrentAngleDegrees() {
        return tagAngleDegreen;
    }

    /**
     * 角度变化回调
     */
    protected void notifyAngleChanged() {
        for (int i = 0; i < listners.size(); i++) {
            ((OnTagAngleChangedLitener)listners.get(i)).onTagAngleChanged();
        }
    }

    /**
     * 
     */
    public void destroy() {
        mContext = null;
        mInstance = null;
        isPaused = true;
        listners.clear();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[SensorManager.DATA_X];
        y = event.values[SensorManager.DATA_Y];
        z = event.values[SensorManager.DATA_Z];
    }

    /**
     * 请在
     */
    public void pause() {
        if (!IS_EMULATOR && sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        isPaused = true;
    }

    /**
     * 计算角度
     */
    public void recalculateAngle() {
        WindowManager wm = (WindowManager)mContext.getSystemService(Service.WINDOW_SERVICE);
        int i = wm.getDefaultDisplay().getRotation();
        mAccelerationX = kFilteringFactor * x + (1 - kFilteringFactor) * mAccelerationX;
        mAccelerationY = kFilteringFactor * y + (1 - kFilteringFactor) * mAccelerationY;
        mAccelerationZ = kFilteringFactor * z + (1 - kFilteringFactor) * mAccelerationZ;
        double d1 = 0.0D;
        switch (i) {
            case Surface.ROTATION_0:// 手机处于正常状态
                d1 = Math.PI;
                rotation = 180.0F;
                break;
            case Surface.ROTATION_90:// 手机旋转90度
                d1 = Math.PI / 2;
                rotation = 90.0F;
                break;
            case Surface.ROTATION_180:
                d1 = 0D;
                rotation = 0.0F;
                break;
            case Surface.ROTATION_270:
                d1 = 0 - Math.PI / 2;
                rotation = 270.0F;
                break;
            default:
                break;
        }

        double d2 = 0.0D;
        if ((Math.abs(mAccelerationZ) > 3.5D * Math.abs(mAccelerationX))
                && (Math.abs(mAccelerationZ) > 3.5D * Math.abs(mAccelerationY))) {
            d2 = d1 + tagAngleRadians;
        } else {
            d2 = tagAngleRadians - Math.atan2(-mAccelerationX, -mAccelerationY);
        }
        if (d2 > Math.PI) {
            d2 -= PI_TIMES_2;
        } else if (d2 < 0 - Math.PI) {
            d2 += PI_TIMES_2;
        }
        angularVelocity = -0.1D * d2 - 0.1D * angularVelocity + angularVelocity;
        tagAngleRadians += angularVelocity;
        tagAngleDegreen = (float)Math.toDegrees(tagAngleRadians) + rotation;
        if (Math.abs(angularVelocity) > angularTolerance) {
            notifyAngleChanged();
        }
    }

    public void removeTagAngleListener(OnTagAngleChangedLitener ontaganglechangedlitener) {
        listners.remove(ontaganglechangedlitener);
    }

    /**
     * 请在activity的onResume方法中调用
     */
    public void resume() {
        if (!IS_EMULATOR) {
            sensorManager.unregisterListener(this);
            sensorManager.registerListener(this, accelerometer, 0);
            mAccelerationX = 0.0D;
            mAccelerationY = 0.0D;
            mAccelerationZ = 0.0D;
            isPaused = false;
            handler.postDelayed(updater, 50L);
        }
    }
}
