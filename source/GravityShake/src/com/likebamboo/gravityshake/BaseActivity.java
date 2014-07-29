
package com.likebamboo.gravityshake;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.likebamboo.gravityshake.utils.AccelHelper;

/**
 * BaseActivity.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述：Activity基类
 */
public class BaseActivity extends Activity {
    /**
     * 上下文对象
     */
    protected Context mContext = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AccelHelper.getInstance(mContext).resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccelHelper.getInstance(mContext).destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AccelHelper.getInstance(mContext).pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
