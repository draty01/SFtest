package com.jiaming.sftest.element;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.jiaming.sftest.model.Coordinate;
import com.jiaming.sftest.setting.Contains;
import com.jiaming.sftest.utils.DensityUtil;

import java.util.LinkedList;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：22:49
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class Snake extends Element {

    private long score;
    private int  jointCount;
    private int  level;
    private int  sizeRad;
    private Path path;
    //这里决定初始位置
    private int                    initLeftAtStageDp = 100;
    private int                    initTopAtStageDp  = 100;
    private LinkedList<Coordinate> joints            = new LinkedList<>();
    private String                 TAG               = "Snake";

    public Snake() {
        this(Contains.SNAKE_INIT_SCORE);
    }

    public Snake(long initScore) {
        setScore(initScore);
        lastTime = nowMillis();
        reflash(lastTime);
        //        path.lineTo();
    }

    private void reflash(long timeMill) {
        if (timeMill == lastTime) {
            //初始化时
            joints.clear();
            int leftAtStage = DensityUtil.dip2px(initLeftAtStageDp);
            int topAtStage = DensityUtil.dip2px(initTopAtStageDp);
            setLeftPosition(leftAtStage);
            setTopPosition(topAtStage);
            Coordinate initCoor = new Coordinate(getLeftPosition(), getTopPosition());
            joints.addLast(initCoor);
            Coordinate fristCoor=null;
            Coordinate secondCoor=initCoor;
            for (int i = 1; i < jointCount; i++) {
                Coordinate nextCoor = nextCoor(fristCoor, secondCoor);
                fristCoor=secondCoor;
                secondCoor=nextCoor;
                Log.d(TAG, "reflash: init nextCoor.left"+nextCoor.mLeft+" nextCoor.top:"+nextCoor.mTop);
                joints.addLast(nextCoor);
            }
        } else {
        }
    }

    private Coordinate nextCoor(Coordinate fristCoor, Coordinate SecondCoor) {
        if (SecondCoor == null) {
            throw new RuntimeException("secondCoor can not be null");
        }
        //l1=kt1+b l2=kt2+b ->(l1-l2)/(t1-t2)=k b=l2-kt2 
        float left1;
        float left2 = SecondCoor.mLeft;
        float top1;
        float top2 = SecondCoor.mTop;
        float k = 0;//这里决定初始方向
        float b = left2;
        if (fristCoor != null) {
            left1 = fristCoor.mLeft;
            top1 = fristCoor.mTop;
            k =  (top1 - top2)/(left1 - left2);
            b =  top2- k *left2 ;
        }
        float distance = sizeRad * 2 * 0.8F;
        //l3=kt3+b  (l3-l2)^2+(t3-t2)^2=dis^2
        int left3;
        int top3;
        if(k!=0) {
             left3 = (int) (left2 + distance / (Math.sqrt(k * k - 1)) + 0.5f);
             top3 = (int) (k * left3 + b + 0.5f);
        }else {
            left3= (int) (left2-distance+0.5f);
            top3= (int) (top2+0.5f);
        }
        return new Coordinate(left3, top3);
    }

    @Override
    public void draw(int ScreenLeftAtStage, int ScreenTopAtStage, Canvas canvas, Paint p) {
//        Log.d(TAG, "draw");
        int screenMaxWidth = getScreenMaxWidth();
        int screenMaxHeight = getScreenMaxHeight();
        for (Coordinate joint : joints) {
            int leftPositionAtStage = joint.mLeft;
            int topPositionAtStage = joint.mTop;
            int leftAtSc = leftPositionAtStage - ScreenLeftAtStage;
            int topAtSc = topPositionAtStage - ScreenTopAtStage;
//            Log.d(TAG, "draw: leftPositionAtStage:" + leftPositionAtStage
//                    + "topPositionAtStage:" + topPositionAtStage
//                    + "leftAtSc:" + leftAtSc + "topAtSc:" + topAtSc + "screenMaxWidth:"+screenMaxWidth+"screenMaxHeight:"+screenMaxHeight);
            if (leftAtSc > screenMaxWidth + sizeRad || leftAtSc < 0 - sizeRad || topAtSc > screenMaxHeight + sizeRad || topAtSc < 0 - sizeRad) {
//                Log.d(TAG, "draw: not at Screen");
                continue;
            }
//            Log.d(TAG, "draw: sizeRad:"+sizeRad);
            drawJoint(canvas, p, leftAtSc, topAtSc);
        }
    }

    //画每一个关节
    private void drawJoint(Canvas canvas, Paint p, int leftAtSc, int topAtSc) {
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        canvas.drawCircle(leftAtSc, topAtSc, sizeRad, p);
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
        int levelupJointCount = Contains.SNAKE_LEVEL_UP_JOINT_COUNT;
        int scoreBase = Contains.SNAKE_LEVEL_SCORE_BASE;
        level = (int) (score / levelupJointCount / scoreBase);
        int jointScore = (level + 1) * scoreBase;
        jointCount = (int) (score / jointScore);
        sizeRad = Contains.SNAKE_JOINT_RAD_BASE + level;
    }
}
