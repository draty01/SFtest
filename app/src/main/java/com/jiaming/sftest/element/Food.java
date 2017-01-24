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
    private int sizeDip = Contains.FOOD_SIZES_DP[sizeL];
    private int sizeRad = Contains.FOOD_SIZES_PX[sizeL];
    private String TAG="Food";
    Paint mPaint=new Paint();

    public Food(int sizeL) {
        if (sizeL >= Contains.FOOD_SIZES_DP.length || sizeL < 0)
            throw new RuntimeException("wrong size of food");
        setSize(sizeL);
        RandPosiAndColor();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    /**
     * 重新随机重置位置和颜色
     */
    private void RandPosiAndColor() {
        int left = MathUtil.getRanInt(sizeRad, getStageWidthPx()-sizeRad);
        int top = MathUtil.getRanInt(sizeRad, getStageHeightPx()-sizeRad);
        setLeftPosition(left);
        setTopPosition(top);
        color = ColorUtil.getRanColor();
        mPaint.setColor(color);
    }

    /**
     * @see Contains#FOOD_SIZES_DP 注意长度不能超出foodSizesDip的长度
     * @param i
     */
    private void setSize(int i) {
        sizeL=i;
        sizeDip=Contains.FOOD_SIZES_DP[sizeL];
        sizeRad=Contains.FOOD_SIZES_PX[sizeL];
    }
    
    @Override
    public void draw(int ScreenLeftAtStage, int ScreenTopAtStage, Canvas canvas, Paint p) {;
        
        int leftAtSc=mPositionLeftAtStage-ScreenLeftAtStage;
        int topAtSc=mPositionTopAtStage-ScreenTopAtStage;
//        int screenMaxWidth = getScreenMaxWidth();
//        int screenMaxHeight = getScreenMaxHeight();
        //判断在不在屏幕内
//        Log.d(TAG, "draw: leftPositionAtStage:" + leftPositionAtStage 
//                + "topPositionAtStage:" + topPositionAtStage 
//                + "leftAtSc:" + leftAtSc + "topAtSc:" + topAtSc + "screenMaxWidth:"+screenMaxWidth+"screenMaxHeight:"+screenMaxHeight);
        if (leftAtSc>ScreenMaxWidthPx+sizeRad||leftAtSc<0-sizeRad||topAtSc> ScreenMaxHeightPx+sizeRad ||topAtSc<0-sizeRad){
//            Log.d(TAG, "draw: not at Screen");
            return;
        }
        //画出来
//        Log.d(TAG, "draw: color"+color+" sizeRad:"+sizeRad);
     
        p.set(mPaint);
//        canvas.drawPoint(leftAtSc, topAtSc,p);
        canvas.drawCircle(leftAtSc, topAtSc, sizeRad, p);
    }

    /**
     * 返回分数
     * @return
     */
    public int eated() {
        //被吃,返回自己分值 重新分配位置
        RandPosiAndColor();
        return Contains.FOOD_SORCES[sizeL];
    }
}
