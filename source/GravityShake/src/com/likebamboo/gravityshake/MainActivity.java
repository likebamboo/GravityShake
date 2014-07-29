
package com.likebamboo.gravityshake;

import android.graphics.RectF;
import android.os.Bundle;
import android.widget.Toast;

import com.likebamboo.gravityshake.entities.ImageTag;
import com.likebamboo.gravityshake.listener.OnTagClickedListener;
import com.likebamboo.gravityshake.widget.PhotoViewWithTag;

/**
 * MainActivity.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述： 主界面
 */
public class MainActivity extends BaseActivity implements OnTagClickedListener {

    /**
     * 
     */
    private PhotoViewWithTag mImageView1 = null;

    private PhotoViewWithTag mImageView2 = null;

    private PhotoViewWithTag mImageView3 = null;

    private PhotoViewWithTag mImageView4 = null;

    private static int index = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setDatas();
    }

    private void initView() {
        mImageView1 = (PhotoViewWithTag)findViewById(R.id.image_iv1);
        mImageView2 = (PhotoViewWithTag)findViewById(R.id.image_iv2);
        mImageView3 = (PhotoViewWithTag)findViewById(R.id.image_iv3);
        mImageView4 = (PhotoViewWithTag)findViewById(R.id.image_iv4);
    }

    private void setDatas() {
        mImageView1.setImageResource(R.drawable.guide_01);
        mImageView1.setZoomable(false);
        mImageView1.setTags(buildTags());
        mImageView1.setOnTagClickedListener(this);
        mImageView2.setImageResource(R.drawable.guide_01);
        mImageView2.setTags(buildTags());
        mImageView3.setImageResource(R.drawable.guide_01);
        mImageView4.setImageResource(R.drawable.guide_01);
        mImageView4.setTags(buildTags());
        mImageView4.setOnTagClickedListener(this);
    }

    private ImageTag[] buildTags() {
        ImageTag[] tags = {
                new ImageTag(++index, 0.2F, 0.4F), new ImageTag(++index, 0.5F, 0.5F)
        };
        return tags;
    }

    @Override
    public void onTagClicked(int i, ImageTag tag, RectF rectF) {
        Toast.makeText(this, tag + "", Toast.LENGTH_SHORT).show();
    }
}
