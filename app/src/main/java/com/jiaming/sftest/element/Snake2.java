package com.jiaming.sftest.element;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jiaming.sftest.model.Coordinate;
import com.jiaming.sftest.setting.Contains;
import com.jiaming.sftest.utils.DensityUtil;

import java.util.HashMap;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：22:49
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class Snake2 extends Element {

    private long score;
    private int  jointCount;
    private int  level;
    private int  sizeRad;
    private Path path;
    //这里决定初始位置
    private int initLeftAtStageDp = 100;
    private int initTopAtStageDp  = 100;
    //    private LinkedList<Coordinate> turnPoint            = new LinkedList<>();//转折点
    Coordinate firstTurnPoint;           //第一个转折点
    private String TAG           = "Snake";
    private float  LastMoveDirec = 0;//移动方向,正右边为0度,顺时针加由0-360,取值范围[0,360)
    private float  newMoveDirec = 0;//移动方向,正右边为0度,顺时针加由0-360,取值范围[0,360)
    
    public Snake2() {
        this(Contains.SNAKE_INIT_SCORE);
    }

    public Snake2(long initScore) {
        setScore(initScore);
        lastTime = nowMillis();
        reflash(lastTime, LastMoveDirec);
        //        path.lineTo();
    }

    private void reflash(long timeMill, float newDirec) {
        if (timeMill == lastTime) {
            //初始化时
            //设置蛇头位置
            int leftAtStage = DensityUtil.dip2px(initLeftAtStageDp);
            int topAtStage = DensityUtil.dip2px(initTopAtStageDp);
            setLeftPosition(leftAtStage);
            setTopPosition(topAtStage);
            //设点第一个折点位置,默认为最后一个点后面
            firstTurnPoint = new Coordinate(getLeftPosition() - jointCount * Contains.SNAKE_JOINT_DISTANCE_PX, getTopPosition());
            Log.d(TAG, "reflash: firstTurnPoint:"+firstTurnPoint);
        } else {
            if (newDirec!= LastMoveDirec){
                //添加一个转折头
                Coordinate newTurn = new Coordinate(getLeftPosition(), getTopPosition());
                newTurn.setNext(firstTurnPoint);
                firstTurnPoint=newTurn;
            }
            //根据方向刷新头位置
            Coordinate oldCoor = new Coordinate(getLeftPosition(), getTopPosition());
            float distance=(timeMill-lastTime)*getSnakeSpeed();
            Coordinate newHead = nextCoor(LastMoveDirec, distance, oldCoor);
            setLeftPosition(newHead.mLeft);
            setTopPosition(newHead.mTop);
            LastMoveDirec=newDirec;
        }
        lastTime=timeMill;
    }
    
    private float getSnakeSpeed() {
        return Contains.SNAKE_MOVE_SPEED_NORMAL_PX;
    }

    /**
     * @param direction 方向[0,360) //正右边为0,顺时针
     * @param distance  距离
     * @param oldCoor   旧坐标
     * @return 新坐标
     */
    private Coordinate nextCoor(float direction, float distance, Coordinate oldCoor) {
        if (direction<0){
            throw  new RuntimeException("direction must not less than  0");
        }
        int newLeft;
        int newTop ;
        int oldLeft = oldCoor.mLeft;
        int oldTop = oldCoor.mTop;
        while (direction >= 360) {
            direction -= 360;
        }
        double angle = direction * Math.PI / 180;
        newLeft = doubleToInt(distance * Math.cos(angle));
        newTop = doubleToInt(distance * Math.sin(angle));
        //        if (direction == 0) {
        //            newLeft = floatToInt(distance);
        //            newTop = 0;
        //        } else if (direction > 0 && direction < 90) {
        //            direction = (float) (direction * Math.PI / 180);
        //            newLeft = doubleToInt(distance * Math.cos(direction));
        //            newTop = doubleToInt(distance * Math.sin(direction));
        //        } else if (direction == 90) {
        //            newLeft = 0;
        //            newTop = floatToInt(distance);
        //        } else if (direction > 90 && direction < 180) {
        //            direction = 180 - direction;
        //            direction = (float) (direction * Math.PI / 180);
        //            newLeft = -doubleToInt(distance * Math.cos(direction));
        //            newTop = doubleToInt(distance * Math.sin(direction));
        //        } else if (direction == 180) {
        //            newTop = 0;
        //            newLeft = -floatToInt(distance);
        //        } else if (direction > 180 && direction < 270) {
        //            direction = direction - 180;
        //            direction = (float) (direction * Math.PI / 180);
        //            newLeft = -doubleToInt(distance * Math.cos(direction));
        //            newTop = -doubleToInt(distance * Math.sin(direction));
        //        } else if (direction == 270) {
        //            newLeft = 0;
        //            newTop = -floatToInt(distance);
        //        } else if (direction > 270) {
        //            direction = 360 - direction;
        //            direction = (float) (direction * Math.PI / 180);
        //            newLeft = doubleToInt(distance * Math.cos(direction));
        //            newTop = -doubleToInt(distance * Math.sin(direction));
        //        }
        newLeft=oldLeft+newLeft;
        newTop=oldTop+newTop;
        return new Coordinate(newLeft, newTop);
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
            k = (top1 - top2) / (left1 - left2);
            b = top2 - k * left2;
        }
        float distance = sizeRad * 2 * 0.8f;
        //l3=kt3+b  (l3-l2)^2+(t3-t2)^2=dis^2
        int left3;
        int top3;
        if (k != 0) {
            left3 = (int) (left2 + distance / (Math.sqrt(k * k - 1)) + 0.5f);
            top3 = (int) (k * left3 + b + 0.5f);
        } else {
            left3 = (int) (left2 - distance + 0.5f);
            top3 = (int) (top2 + 0.5f);
        }
        return new Coordinate(left3, top3);
    }

    @Override
    public void draw(int ScreenLeftAtStage, int ScreenTopAtStage, Canvas canvas, Paint p) {
        //        Log.d(TAG, "draw");
        reflash(nowMillis(),newMoveDirec);
        int screenMaxWidth = getScreenMaxWidth();
        int screenMaxHeight = getScreenMaxHeight();
        //获取第一个点:
        Coordinate FirstJoint = new Coordinate(getLeftPosition(), getTopPosition());
//        Log.d(TAG, "draw: "+FirstJoint);
        Coordinate lastJoint=FirstJoint;//最后一个Joint
        //从初始位置计算,根据方向,关节间距计算下一个点
        Coordinate beforeTrun = FirstJoint;//这个是临时的
        Coordinate afterTrun = firstTurnPoint;
        float trunDist = getDistance(beforeTrun, afterTrun);//两个转折点之间的距离
        float lastJointToBefore = 0;//上一个关节到上一个转折点的距离
        for (int i = 0; i < jointCount; i++) {
            float jointToBefore = Contains.SNAKE_JOINT_DISTANCE_PX + lastJointToBefore;//本关节到上一个转折点的距离
//            Log.d(TAG, "draw: jointToBefore:"+jointToBefore+" trunDist:"+trunDist);
            while (jointToBefore > trunDist) {//如果本关节到上一个转接点的距离少于这两个关节点的距离,就把本关节移动到下一节
                jointToBefore = jointToBefore - trunDist;
                beforeTrun = afterTrun;
                afterTrun = (Coordinate) beforeTrun.getNext();
                trunDist = getDistance(beforeTrun, afterTrun);
            }
            int mLeft ;
            int mTop;
            if (jointToBefore == trunDist) {
                mLeft = afterTrun.mLeft;
                mTop = afterTrun.mTop;
            } else {
                float v = jointToBefore / trunDist;
                mLeft= (int) ((afterTrun.mLeft-beforeTrun.mLeft)*v+beforeTrun.mLeft+0.5f);
                mTop= (int) ((afterTrun.mTop-beforeTrun.mTop)*v+beforeTrun.mTop+0.5f);
            }
            Coordinate newCoor = new Coordinate(mLeft, mTop);
//            Log.d(TAG, "draw: newCoor:"+newCoor+" lastJoint:"+lastJoint);
            newCoor.setNext(lastJoint);
            lastJoint=newCoor;
            lastJointToBefore = jointToBefore;
        }
        //去掉最后没用的转折点
        if (afterTrun.getNext()!=null) {
            afterTrun.getNext().setNext(null);
        }
        //根据点的位置绘制点:
        Coordinate drowJoint =lastJoint;
        int drawCount=0;
        while (drowJoint!=null&&drawCount<10){
//            Log.d(TAG, "draw: drawCount:"+ ++drawCount);
//            Log.d(TAG, "draw: drowJoint:"+drowJoint);
            int leftPositionAtStage = drowJoint.mLeft;
            int topPositionAtStage = drowJoint.mTop;
            int leftAtSc = leftPositionAtStage - ScreenLeftAtStage;
            int topAtSc = topPositionAtStage - ScreenTopAtStage;
            //            Log.d(TAG, "draw: leftPositionAtStage:" + leftPositionAtStage
            //                    + "topPositionAtStage:" + topPositionAtStage
            //                    + "leftAtSc:" + leftAtSc + "topAtSc:" + topAtSc + "screenMaxWidth:"+screenMaxWidth+"screenMaxHeight:"+screenMaxHeight);
            if (leftAtSc > screenMaxWidth + sizeRad || leftAtSc < 0 - sizeRad || topAtSc > screenMaxHeight + sizeRad || topAtSc < 0 - sizeRad) {
                //                Log.d(TAG, "draw: not at Screen");
            }else {
                drawJoint(canvas, p, leftAtSc, topAtSc);
            }
            //            Log.d(TAG, "draw: sizeRad:"+sizeRad);
            drowJoint= (Coordinate) drowJoint.getNext();
        }
    }

    HashMap<Integer, Float> distances = new HashMap<>();

    private float getDistance(@NonNull Coordinate beforeTrun,@NonNull  Coordinate afterTrun) {
        int beHash = beforeTrun.hashCode();
        int afHash = afterTrun.hashCode();
        int key = afHash - beHash;
        Float distance = distances.get(key);
//        Log.d(TAG, "getDistance: beforeTrun:"+beforeTrun+" afterTrun:"+afterTrun);
        if (distance == null) {
            int l = (beforeTrun.mLeft - afterTrun.mLeft) * (beforeTrun.mLeft - afterTrun.mLeft);
            int t = (beforeTrun.mTop - afterTrun.mTop) * (beforeTrun.mTop - afterTrun.mTop);
            distance = (float) Math.sqrt(l + t);
            distances.put(key, distance);
        }
        return distance;
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
        Log.d(TAG, "setScore: jointCount:"+jointCount);
        sizeRad = Contains.SNAKE_JOINT_RAD_BASE + level;
    }

    public void setNewMoveDirec(float newMoveDirec) {
        this.newMoveDirec = newMoveDirec;
    }
    private  int doubleToInt(double v) {
        return (int) (v + 0.5f);
    }
}
