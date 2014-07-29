
package com.likebamboo.gravityshake.listener;

import android.graphics.RectF;

import com.likebamboo.gravityshake.entities.ImageTag;

/**
 * OnTagClickedListener.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述：标签点击事件回调
 */
public abstract interface OnTagClickedListener {
    public abstract void onTagClicked(int i, ImageTag tag, RectF rectF);
}
