package com.likebamboo.gravityshake;

import android.graphics.RectF;
import android.os.Bundle;

import com.likebamboo.gravityshake.entities.ImageTag;
import com.likebamboo.gravityshake.listener.OnTagClickedListener;
import com.likebamboo.gravityshake.widget.MyImageView;

/**
 * MainActivity.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述： 主界面
 */
public class TestActivity extends BaseActivity implements OnTagClickedListener {

    /**
     * 
     */
    private MyImageView mImageView1 = null;

    private static int index = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        setDatas();
    }

    private void initView() {
        mImageView1 = (MyImageView) findViewById(R.id.image_iv);
    }

    private void setDatas() {
        mImageView1.setTags(buildTags());
        mImageView1.postInvalidate();
    }

    private ImageTag[] buildTags() {
        ImageTag[] tags = {new ImageTag(++index, 0.5F, 0.5F)};
        return tags;
    }

    @Override
    public void onTagClicked(int i, ImageTag tag, RectF rectF) {

    }
}
