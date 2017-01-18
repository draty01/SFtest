package com.jiaming.sftest.element;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jiaming.sftest.setting.Contains;
import com.jiaming.sftest.utils.ColorUtil;
import com.jiaming.sftest.utils.MathUtil;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：23:58
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class Food extends Element {

    private int color;
    private int sizeL   = 0;
    private int sizeDip = Contains.foodSizesDip[sizeL];
    private int sizeRad = Contains.foodSizesPx[sizeL];
    private String TAG="Food";

    public Food(int sizeL) {
        if (sizeL >= Contains.foodSizesDip.length || sizeL < 0)
            throw new RuntimeException("wrong size of food");
        setSize(sizeL);
        int left = MathUtil.getRanInt(sizeRad, getStageWidthPx()-sizeRad);
        int top = MathUtil.getRanInt(sizeRad, getStageHeightPx()-sizeRad);
        setLeftPosition(left);
        setTopPosition(top);
        color = ColorUtil.getRanColor();
    }

    private void setSize(int i) {
        sizeL=i;
        sizeDip=Contains.foodSizesDip[sizeL];
        sizeRad=Contains.foodSizesPx[sizeL];
    }

    @Override
    public void draw(int ScreenLeftAtStage, int ScreenTopAtStage, Canvas canvas, Paint p) {;
        int leftPositionAtStage = getLeftPosition();
        int topPositionAtStage = getTopPosition();
        int leftAtSc=leftPositionAtStage-ScreenLeftAtStage;
        int topAtSc=topPositionAtStage-ScreenTopAtStage;
        int screenMaxWidth = getScreenMaxWidth();
        int screenMaxHeight = getScreenMaxHeight();
        //判断在不在屏幕内
//        Log.d(TAG, "draw: leftPositionAtStage:" + leftPositionAtStage 
//                + "topPositionAtStage:" + topPositionAtStage 
//                + "leftAtSc:" + leftAtSc + "topAtSc:" + topAtSc + "screenMaxWidth:"+screenMaxWidth+"screenMaxHeight:"+screenMaxHeight);
        if (leftAtSc>screenMaxWidth+sizeRad||leftAtSc<0-sizeRad||topAtSc> screenMaxHeight+sizeRad ||topAtSc<0-sizeRad){
//            Log.d(TAG, "draw: not at Screen");
            return;
        }
        //画出来
//        Log.d(TAG, "draw: color"+color+" sizeRad:"+sizeRad);
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        canvas.drawCircle(leftAtSc, topAtSc, sizeRad, p);
    }
}
