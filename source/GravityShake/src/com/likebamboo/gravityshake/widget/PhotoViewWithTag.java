
package com.likebamboo.gravityshake.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.likebamboo.gravityshake.R;
import com.likebamboo.gravityshake.app.GSApp;
import com.likebamboo.gravityshake.entities.ImageTag;
import com.likebamboo.gravityshake.listener.OnTagAngleChangedLitener;
import com.likebamboo.gravityshake.listener.OnTagClickedListener;
import com.likebamboo.gravityshake.utils.AccelHelper;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * PhotoViewWithTag.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述： 带Tag并支持缩放的ImageView
 */
public class PhotoViewWithTag extends PhotoView implements OnTagAngleChangedLitener {
    /**
     * tag标签资源图片
     */
    private Bitmap mTagBitmap = null;

    /**
     * tag标签高度
     */
    private int mTagHeight = 0;

    /**
     * tag标签宽度
     */
    private int mTagWidth = 0;

    /**
     * 临时变量，用于记录标签相对于图片的位置
     */
    private float[] mTempPoint = new float[2];

    /**
     * 标签数组
     */
    protected ImageTag[] mTags = null;

    /**
     * 标签touch区域Rect
     */
    private RectF[] mTouchRects = null;

    /**
     * 临时变量，用于计算touch区域Rect的值
     */
    private Matrix mTempMatrix = new Matrix();

    /**
     * tag点击事件监听
     */
    private OnTagClickedListener mClickListener = null;

    public PhotoViewWithTag(Context context) {
        this(context, null);
    }

    public PhotoViewWithTag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoViewWithTag(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 
     */
    private void init() {
        if (!isInEditMode()) {
            mTagBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tag);
            mTagWidth = mTagBitmap.getWidth();
            mTagHeight = mTagBitmap.getHeight();
        }

        setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mClickListener == null) {
                    return;
                }
                RectF rectfF = getDisplayRect();
                float pointX = x * rectfF.width();
                float pointY = y * rectfF.height();
                //
                for (int i = 0; i < mTouchRects.length; i++) {
                    if (mTouchRects[i].contains(pointX, pointY)) {
                        mClickListener.onTagClicked(i, mTags[i], mTouchRects[i]);
                    }
                }
            }
        });
    }

    /**
     * @return
     */
    private AccelHelper getAccelHelper() {
        if (GSApp.mContext != null) {
            AccelHelper.getInstance(GSApp.mContext);
        }
        return AccelHelper.getInstance(getContext().getApplicationContext());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawImageTags(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getAccelHelper() != null) {
            getAccelHelper().addTagAngleListener(this);
        }
    }

    /**
     * 绘制Tag
     * 
     * @param canvas
     */
    protected void drawImageTags(Canvas canvas) {
        if (mTags == null || mTags.length < 1) {
            return;
        }
        for (int i = 0; i < mTags.length; i++) {
            RectF r = getDisplayRect();
            if (r == null) {
                return;
            }
            // 计算tag标签在图片上的位置
            float pointX = mTags[i].getX() * r.width();
            float pointY = mTags[i].getY() * r.height();

            // 计算tags的位置(转换成相对于ImageView的位置)
            mTempPoint[0] = pointX + r.left;
            mTempPoint[1] = pointY + r.top;

            canvas.save();
            // 将画布原点移动到要绘制的点
            canvas.translate(mTempPoint[0], mTempPoint[1]);
            // 获取旋转角度
            if (getAccelHelper() != null) {
                canvas.rotate(getAccelHelper().getCurrentAngleDegrees());
            }

            // 绘制图片
            canvas.drawBitmap(mTagBitmap, 0 - (mTagWidth >> 1), 0, new Paint());
            canvas.restore();

            // 计算Tag的可touch区域
            calculateTouchRectF(i, pointX, pointY);
        }
    }

    /**
     * 计算相应Tag的可touch区域
     * 
     * @param index
     * @param pointX
     * @param pointY
     */
    private void calculateTouchRectF(int index, float pointX, float pointY) {
        mTouchRects[index] = new RectF(pointX - (mTagWidth >> 1), pointY, pointX - (mTagWidth >> 1)
                + mTagWidth, pointY + mTagHeight);
        mTempMatrix.reset();
        // 获取旋转角度
        if (getAccelHelper() != null) {
            mTempMatrix.setRotate(getAccelHelper().getCurrentAngleDegrees(), pointX, pointY);
        }
        // 此处其实是一个简单的矩形旋转，借助Matrix类
        mTempMatrix.mapRect(mTouchRects[index], mTouchRects[index]);
    }

    @Override
    public void onTagAngleChanged() {
        invalidate();
    }

    /**
     * 设置图标单击事件回调
     * 
     * @return
     */
    public void setOnTagClickedListener(OnTagClickedListener onTagClickedListener) {
        this.mClickListener = onTagClickedListener;
    }

    /**
     * 设置tags
     * 
     * @param tags
     */
    public void setTags(ImageTag... tags) {
        mTags = tags;
        if (tags != null) {
            mTouchRects = new RectF[tags.length];
        }
    }

}
