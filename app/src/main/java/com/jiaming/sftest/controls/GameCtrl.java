package com.jiaming.sftest.controls;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jiaming.sftest.element.Food;
import com.jiaming.sftest.element.IElement;
import com.jiaming.sftest.element.Snake2;
import com.jiaming.sftest.element.StageBg;
import com.jiaming.sftest.setting.Contains;
import com.jiaming.sftest.utils.DensityUtil;
import com.jiaming.sftest.views.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：23:11
 * 修改人：
 * 修改时间：
 * 修改备注：
 * <p/>
 * 游戏控制器
 * 屏幕位置
 * 各种元素
 */
public class GameCtrl {
    private static GameCtrl instance;
    private static      Lock   lock               = new ReentrantLock();
    public final static int    INIT_START         = 10;
    public final static int    INIT_SCREEN_INITED = 20;
    public final static int    INIT_OK            = 100;
    public final static String TAG                = "GameCtrl";
    private Screen mScreen;
    private int CtrlDirec = 0;
    private FoodStore mFoodStore;
    //    private        int  mStageWidth  = Contains.STAGE_WIDTH;
    //    private        int  mStageHeight = Contains.STAGE_HEIGHT;

    public static GameCtrl getInstance() {
        if (instance == null) {
            lock.lock();
            if (instance == null) {
                instance = new GameCtrl();
            }
            lock.unlock();
        }
        return instance;
    }

    private InitCallback initCallback;
    private boolean isInited = false;

    public void init(Context context, @NonNull InitCallback callback) {
        if (isInited) {
            Log.e(TAG, "isInited: " + isInited);
            callback.onChang(INIT_OK);
            return;
        }
        isInited = true;
        initCallback = callback;
        initCallback.onChang(INIT_START);
        initScreen(context);
        initElemnts();
    }

    private GameCtrl() {
    }

    private void initScreen(Context context) {
        screenLeft = DensityUtil.dip2px(Contains.INIT_POSITION_LEFT);
        screenTop = DensityUtil.dip2px(Contains.INIT_POSITION_TOP);
        //        mScreen = new Screen(context, this);
        initCallback.onChang(INIT_SCREEN_INITED);
    }

    public Screen getScreen() {
        return mScreen;
    }

    private void initElemnts() {
        //新建一个线程池建场景元素
        final int ScreenHaftWidthPx = DensityUtil.getScreenMaxWidth() / 2;
        final int ScreenHaftHeightPx = DensityUtil.getScreenMaxHeight() / 2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mFoodStore = new FoodStore(Contains.STAGE_WIDTH_PX, Contains.STAGE_HEIGHT_PX, Contains.FOOD_COUNT);
//                int snakeCount = Contains.SNAKE_COUNT;
                mSnake = new Snake2();
                mSnake.setMoveListen(new Snake2.onMoveListen() {
                    @Override
                    public void onMove(int leftAtStage, int topAtStage, Snake2 snake) {
                        setScreenLeft(leftAtStage - ScreenHaftWidthPx);
                        setScreenTop(topAtStage - ScreenHaftHeightPx);
                        //获取对应块的食物以及碰撞判定
                        int left, top, right, bottom;
                        int sizeRad = mSnake.getSizeRad();
                        left    = leftAtStage - sizeRad;
                        left=left<0?0:left;
                        top     = topAtStage - sizeRad;
                        top=top<0?0:top;
                        right   = leftAtStage + sizeRad;
                        bottom  = topAtStage + sizeRad;
//                        Log.d(TAG, "onMove2: "
//                                +"left   :"+left
//                                +"top    :"+top
//                                +"right  :"+right
//                                +"bottom :"+bottom
//                        );
                        List<Food>[] foodlists=new List[4];
                        foodlists[0]=mFoodStore.getList(left,top);
                        foodlists[1]=mFoodStore.getList(left,bottom);
                        foodlists[2]=mFoodStore.getList(right,top);
                        foodlists[3]=mFoodStore.getList(right,bottom);
                        for (int i = 0; i < foodlists.length; i++) {
                            List<Food> list = foodlists[i];
                            if (list.size()>0){
                                //遍历所有,如果遇到的在范围内就吃掉,蛇加分
                                for (Food food : list) {
                                    int leftPosit = food.getLeftPosition();
                                    int topPosit = food.getTopPosition();
                                    if (leftPosit<left||leftPosit>right||topPosit<top||topPosit>bottom){
//                                        Log.d(TAG, "onMove: leftPosit:"+leftPosit
//                                                +" topPosit:"+topPosit
//                                                +" left:"+left
//                                                +" top:"+top
//                                                +" right:"+right
//                                                +" bottom:"+bottom
//                                        );
                                        continue;
                                    }
                                    int score = food.eated();
                                    snake.addScore(score);
                                    list.remove(food);
                                    mFoodStore.addChunk(food);
                                }
                            }
                        }
                        
                    }
                });
                snakes.add(mSnake);
                initCallback.onChang(INIT_OK);
            }
        }).start();
    }

    IElement       bg     = new StageBg();
    List<IElement> snakes = new ArrayList<>();
    Snake2 mSnake;

    public int screenLeft;
    public int screenTop;

    public Snake2 getSnake() {
        return mSnake;
    }

    public void setCtrlDirec(int ctrlDirec) {
        CtrlDirec = ctrlDirec;
        if (mSnake != null) {
            mSnake.setCtrlDirec(CtrlDirec);
        }
    }

    public int getScreenLeft() {
        return screenLeft;
    }

    public void setScreenLeft(int screenLeft) {
        this.screenLeft = screenLeft;
    }

    public int getScreenTop() {
        return screenTop;
    }

    public void setScreenTop(int screenTop) {
        this.screenTop = screenTop;
    }

    public IElement getBg(boolean isShow) {//表示是否去用来显示的那个
        return bg;
    }

    public List<IElement> getFoods(boolean isShow) {
        if (mFoodStore == null)
            throw new RuntimeException("please init FoodStore");
        return mFoodStore.getFoods();
    }

    public List<IElement> getSnakes(boolean isShow) {
        return snakes;
    }

    public interface InitCallback {
        void onChang(int percentage);
    }
}
