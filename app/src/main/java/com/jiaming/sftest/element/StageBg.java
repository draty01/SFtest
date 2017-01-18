package com.jiaming.sftest.element;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jiaming.sftest.setting.Contains;
import com.jiaming.sftest.utils.DensityUtil;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：22:51
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class StageBg extends Element {
    
    public StageBg(){
        setLeftPosition(DensityUtil.dip2px(Contains.INIT_POSITION_LEFT));
        setTopPosition(DensityUtil.dip2px(Contains.INIT_POSITION_TOP));
    }
    
    @Override
    public void draw(int ScreenLeftAtStage, int ScreenTopAtStage, Canvas canvas, Paint p) {
        //根据舞台位置与屏幕位置计算应画的位置
        
        int stageHeight = getStageHeightPx();
        int stageWidth = getStageWidthPx();
        int ScreenMaxWidth = getScreenMaxWidth();//屏幕最大宽度 注意是横屏后的宽高
        int ScreenMaxHeight = getScreenMaxHeight();//屏幕最大高度
        if (ScreenLeftAtStage<=-ScreenMaxWidth||ScreenTopAtStage<=-ScreenMaxHeight||ScreenLeftAtStage>=stageWidth||ScreenTopAtStage>=stageHeight){
            return;
        }
        int left = -ScreenLeftAtStage;
        int top = -ScreenTopAtStage;
        if (left < 0)
            left = 0;
        if (top < 0)
            top = 0;
        int right = stageWidth - ScreenLeftAtStage;
        if (right > ScreenMaxWidth)
            right = ScreenMaxWidth;
        int bottom = stageHeight - ScreenTopAtStage;
        if (bottom > ScreenMaxHeight)
            bottom = ScreenMaxHeight;
        
        p.setColor(Color.WHITE);
        Rect r = new Rect(left, top, right, bottom);
        canvas.drawRect(r, p);
    }
}
