package com.likebamboo.gravityshake.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.likebamboo.gravityshake.R;
import com.likebamboo.gravityshake.entities.ImageTag;

public class MyImageView extends ImageView {

    private PointF mDisPoint = new PointF(0, 0);

    private PointF mCenterPoint = null;

    /**
     * tag标签资源图片
     */
    private Bitmap mTagBitmap = null;

    /**
     * 标签数组
     */
    protected ImageTag[] mTags = null;

    /**
     * tag标签高度
     */
    private int mTagHeight = 0;

    /**
     * tag标签宽度
     */
    private int mTagWidth = 0;

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 
     */
    private void init() {
        if (!isInEditMode()) {
            mTagBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.exit_dialog_pptv);
            mTagWidth = mTagBitmap.getWidth();
            mTagHeight = mTagBitmap.getHeight();

            float r = mTagHeight * 1.0F / mTagWidth;
            mDefaultRotate = (float) Math.toDegrees(Math.atan(r));

            mDefaultDis = (float) distance(new PointF(0, 0), new PointF(mTagWidth / 2, mTagHeight / 2));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        drawImageTags(canvas);
    }

    /**
     * 设置tags
     * 
     * @param tags
     */
    public void setTags(ImageTag... tags) {
        mTags = tags;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMode == 0 && event.getAction()!= MotionEvent.ACTION_MOVE) {
            PointF[] imageP = toPoint(mImagePoints);
            PointF[] touchP = toPoint(mTouchPoints);
            if (isContain(touchP[0], touchP[1], touchP[2], touchP[3], new PointF(event.getX(), event.getY()))) {
                System.out.println(event.getX() + "==缩放==" + event.getY());
                mMode = 1;
            } else if (isContain(imageP[0], imageP[1], imageP[2], imageP[3], new PointF(event.getX(), event.getY()))) {
                System.out.println(event.getX() + "==拖动==" + event.getY());
                mMode = 2;
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mMode == 1) {// 如果是转动
                    mDisPoint.x = event.getX();
                    mDisPoint.y = event.getY();
                } else if (mMode == 2) {// 如果是拖动，记录距离差值
                    mDisPoint.x = event.getX() - mCenterPoint.x;
                    mDisPoint.y = event.getY() - mCenterPoint.y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == 1) {// 如果是转动
                    // 计算旋转的角度和缩放的角度
                    double dis1 = distance(mCenterPoint, new PointF(event.getX(), event.getY()));
                    mScale = (float) (dis1 / mDefaultDis);

                    mRotate = angle(new PointF(event.getX(), event.getY())) + mDefaultRotate;
                    invalidate();
                } else if (mMode == 2) {// 如果是拖动
                    mCenterPoint.x = event.getX() - mDisPoint.x;
                    mCenterPoint.y = event.getY() - mDisPoint.y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mMode = 0;
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 绘制Tag
     * 
     * @param canvas
     */
    protected void drawImageTags(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (mTags == null || mTags.length < 1) {
            return;
        }
        for (int i = 0; i < mTags.length; i++) {
            canvas.save();
            if (mCenterPoint == null) {
                RectF r = new RectF(0, 0, getWidth(), getHeight());
                mCenterPoint = new PointF();
                mCenterPoint.x = r.centerX();
                mCenterPoint.y = r.centerY();
            }

            // 将画布原点移动到要绘制的点
            canvas.translate(mCenterPoint.x, mCenterPoint.y);
            canvas.scale(mScale, mScale);

            if (mRotate == 0) {
                canvas.rotate(0);
            } else {
                canvas.rotate(mRotate);
            }
            // 绘制图片
            canvas.drawBitmap(mTagBitmap, 0 - mTagWidth / 2, 0 - mTagHeight / 2, new Paint());
            mPaint.setColor(Color.RED);
            canvas.drawCircle(mTagWidth / 2, 0 - mTagHeight / 2, RADIUS / mScale, mPaint);

            canvas.restore();

            float left = mCenterPoint.x - mTagWidth / 2;
            float right = mCenterPoint.x + mTagWidth / 2;
            float top = mCenterPoint.y - mTagHeight / 2;
            float bottom = mCenterPoint.y + mTagHeight / 2;

            // 左上
            mImagePoints[0] = left;
            mImagePoints[1] = top;

            // 右上
            mImagePoints[2] = right;
            mImagePoints[3] = top;

            // 右下
            mImagePoints[4] = right;
            mImagePoints[5] = bottom;

            // 左下
            mImagePoints[6] = left;
            mImagePoints[7] = bottom;

            Matrix m = new Matrix();
            m.setScale(mScale, mScale, mCenterPoint.x, mCenterPoint.y);
            if (mRotate != 0) {
                m.postRotate(mRotate, mCenterPoint.x, mCenterPoint.y);
            }

            m.mapPoints(mImagePoints);
            
            Paint pa = new Paint();
            pa.setColor(Color.GREEN);
            pa.setStrokeWidth(10);
            canvas.drawPoints(mImagePoints, pa);

            // 左上
            mTouchPoints[0] = mImagePoints[2] - RADIUS;
            mTouchPoints[1] = mImagePoints[3] - RADIUS;

            // 右上
            mTouchPoints[2] = mImagePoints[2] + RADIUS;
            mTouchPoints[3] = mImagePoints[3] - RADIUS;

            // 右下
            mTouchPoints[4] = mImagePoints[2] + RADIUS;
            mTouchPoints[5] = mImagePoints[3] + RADIUS;

            // 左下
            mTouchPoints[6] = mImagePoints[2] - RADIUS;
            mTouchPoints[7] = mImagePoints[3] + RADIUS;

            canvas.drawPoints(mTouchPoints, pa);
        }
    }

    /**
     * @param f
     * @return
     * @see [类、类#方法、类#成员]
     */
    private PointF[] toPoint(float[] f) {
        if (f != null && f.length > 1) {
            PointF[] result = new PointF[f.length / 2];
            for (int i = 0; i < f.length; i += 2) {
                result[i / 2] = new PointF();
                result[i / 2].x = f[i];
                result[i / 2].y = f[i + 1];
            }
            return result;
        }
        return null;
    }

    /**
     * 判断点是否在矩形内部
     * 
     * @param mp1
     * @param mp2
     * @param mp3
     * @param mp4
     * @param mp
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isContain(PointF p1, PointF p2, PointF p3, PointF p4, PointF p) {
        double len51 = distance(p, p1);
        double len52 = distance(p, p2);
        double len53 = distance(p, p3);
        double len54 = distance(p, p4);
        double r1 = Math.acos(cal(len51, len52, distance(p1, p2)));
        double r2 = Math.acos(cal(len52, len53, distance(p2, p3)));
        double r3 = Math.acos(cal(len53, len54, distance(p3, p4)));
        double r4 = Math.acos(cal(len54, len51, distance(p1, p4)));

        Double d = Math.toDegrees(r1 + r2 + r3 + r4);
        if (d.isNaN() || Math.abs(d.intValue() - 360) < 3) {
            return true;
        }
        return false;
    }

    public static double cal(double b, double c, double a) {
        return (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2)) / (2 * b * c);
    }

    /**
     * 求夹角
     * 
     * @param p1
     * @return
     * @see [类、类#方法、类#成员]
     */
    public float angle(PointF p1) {
        double disC = distance(p1, mCenterPoint);
        double r = (p1.y - mCenterPoint.y) / disC;
        float degrees = (float) Math.toDegrees(Math.asin(r));
        if (p1.x < mCenterPoint.x) {
            degrees = 180 - degrees;
        }
        System.out.println(degrees + " 度-----");
        return degrees;
    }

    /**
     * 两点间的距离
     * 
     * @param p1
     * @param p2
     * @return
     * @see [类、类#方法、类#成员]
     */
    public double distance(PointF p1, PointF p2) {
        float diffx = p1.x - p2.x;
        float diffy = p1.y - p2.y;
        return Math.sqrt(diffx * diffx + diffy * diffy);
    }

    /**
     * 模式。0：无，1缩放，2拖动
     */
    private int mMode = 0;

    private Paint mPaint = new Paint();

    /**
     *
     */
    private static final int RADIUS = 50;

    /**
     *
     */
    private float mScale = 1.0F;

    /**
     *
     */
    private float mRotate = 0.0F;

    /**
     *
     */
    private float[] mImagePoints = new float[8];

    /**
     *
     */
    private float[] mTouchPoints = new float[8];

    private float mDefaultRotate = 0.0F;

    private float mDefaultDis = 1.0F;

}
