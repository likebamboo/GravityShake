
package com.likebamboo.gravityshake.app;

import android.app.Application;
import android.content.Context;

/**
 * GSApp.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述：程序主入口
 */
public class GSApp extends Application {
    /**
     * 上下文
     */
    public static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

}
