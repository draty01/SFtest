package com.jiaming.sftest.element;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * Created by Administrator on 2017/1/14.
 * 要画的元素
 */
public interface IElement {
    void draw(int ScreenLeftAtStage, int ScreenTopAtStage, Canvas canvas, Paint p);
    void reflash(long time);
}
