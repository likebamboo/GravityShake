
package com.likebamboo.gravityshake.entities;

/**
 * ImageTag.java
 * 
 * @author likebamboo@163.com
 * @date 2014年7月28日
 * @desc <pre>描述：标签
 */
public class ImageTag {

    private int id = 0;

    private float X = 0F;

    private float Y = 0F;

    public ImageTag(int i, float x, float y) {
        super();
        id = i;
        X = x;
        Y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    @Override
    public String toString() {
        return "ImageTag [id=" + id + ", X=" + X + ", Y=" + Y + "]";
    }

}
