package com.jiaming.sftest.setting;
import android.util.Log;

import com.jiaming.sftest.utils.DensityUtil;
/**
 * Created by Administrator on 2017/1/14.
 */
public class Contains {
    public static int   STAGE_WIDTH  = 500; //这里单位为dp
    public static int   STAGE_HEIGHT = 300; //这里单位为dp
    public static int[] foodSizesDip = {3, 5, 7};
    public static int[] foodSizesPx  = new int[foodSizesDip.length];

    public static int STAGE_WIDTH_PX; //这里单位为PX
    public static int STAGE_HEIGHT_PX; //这里单位为PX

    public static int  INIT_POSITION_LEFT         = 0; //这里单位为dp
    public static int  INIT_POSITION_TOP          = 0; //这里单位为dp
    public static int  FOOD_COUNT                 = 100;
    public static int  SNAKE_COUNT                = 10;
    public static long DRAW_SLEEP_TIME            = 16;//每帧时间
    public static int  SNAKE_INIT_SCORE           = 90;//L0 0 3 ;L1 1*3*20=60 6 ;L2 2*3*20=120; L=score/SNAKE_LEVEL_UP_JOINT_COUNT/SNAKE_LEVEL_SCORE_BASE
    public static int  SNAKE_LEVEL_UP_JOINT_COUNT = 20;//每多少节关节生一次级
    public static int  SNAKE_LEVEL_SCORE_BASE     = 3;
    public static int  SNAKE_JOINT_RAD_BASE       = 30;//LEVEL+BASE关节半径 单位px

    public static int[] snakeJointScores = new int[]{3, 6, 11, 18};
//    public static int SNAKE_JOINT_DISTANCE_DP;//关节间的距离 单位dp
    public static int SNAKE_JOINT_DISTANCE_PX;//关节间的距离
    public static int SNAKE_JOINT_DISTANCE;//关节间的距离
    public static float SNAKE_MOVE_SPEED_NORMAL_DP = 0.1f;//dp/每毫秒 蛇的移动速度
    public static float SNAKE_MOVE_SPEED_NORMAL_PX;//px/每毫秒 蛇的移动速度

    private static final String TAG="Contains";
    static {
        STAGE_WIDTH_PX = DensityUtil.dip2px(STAGE_WIDTH);
        STAGE_HEIGHT_PX = DensityUtil.dip2px(STAGE_HEIGHT);
        for (int i = 0; i < foodSizesDip.length; i++) {
            foodSizesPx[i] = DensityUtil.dip2px(foodSizesDip[i]);
        }
        SNAKE_JOINT_DISTANCE_PX= (int) (SNAKE_JOINT_RAD_BASE*2*0.8F+0.5f);
        
        SNAKE_MOVE_SPEED_NORMAL_PX = DensityUtil.dip2px_f(SNAKE_MOVE_SPEED_NORMAL_DP);
        Log.d(TAG, "static initializer: SNAKE_JOINT_DISTANCE_PX:"+SNAKE_JOINT_DISTANCE_PX);
    }
}
