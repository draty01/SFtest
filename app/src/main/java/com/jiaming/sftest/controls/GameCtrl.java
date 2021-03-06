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

    public void init(Context context, boolean forceInit, @NonNull InitCallback callback) {
        if (isInited && !forceInit) {
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
                mSnake.setEventListen(new Snake2.onEventListen() {
                    @Override
                    public void onMove(int leftAtStage, int topAtStage, Snake2 snake) {
                        setScreenLeft(leftAtStage - ScreenHaftWidthPx);
                        setScreenTop(topAtStage - ScreenHaftHeightPx);
                        //获取对应块的食物以及碰撞判定
                        int left, top, right, bottom;
                        int sizeRad = mSnake.getSizeRad();
                        left = leftAtStage - sizeRad;
                        left = left < 0 ? 0 : left;
                        top = topAtStage - sizeRad;
                        top = top < 0 ? 0 : top;
                        right = leftAtStage + sizeRad;
                        bottom = topAtStage + sizeRad;
                        //                        Log.d(TAG, "onMove2: "
                        //                                +"left   :"+left
                        //                                +"top    :"+top
                        //                                +"right  :"+right
                        //                                +"bottom :"+bottom
                        //                        );
                        int attract_Rad = sizeRad+ Contains.SNAKE_ATTRACT_RAD_PX;
                        int left1 = leftAtStage - attract_Rad;
                        left1 = left1 < 0 ? 0 : left1;
                        int top1 = topAtStage - attract_Rad;
                        top1 = top1 < 0 ? 0 : top1;
                        int right1 = leftAtStage + attract_Rad;
                        int bottom1 = topAtStage + attract_Rad;
                        
                        List<Food>[] foodlists = new List[4];
                        foodlists[0] = mFoodStore.getList(left, top);
                        foodlists[1] = mFoodStore.getList(left, bottom);
                        foodlists[2] = mFoodStore.getList(right, top);
                        foodlists[3] = mFoodStore.getList(right, bottom);
                        for (int i = 0; i < foodlists.length; i++) {
                            List<Food> list = foodlists[i];
                            if (list.size() > 0) {
                                //遍历所有,如果遇到的在范围内就吃掉,蛇加分
                                for (int i1 = 0; i1 < list.size(); i1++) {
                                    Food food = list.get(i1);
                                    int leftPosit = food.getLeftPosition();
                                    int topPosit = food.getTopPosition();
                                    if (leftPosit < left || leftPosit > right || topPosit < top || topPosit > bottom) {
                                        //                                        Log.d(TAG, "onMove: leftPosit:"+leftPosit
                                        //                                                +" topPosit:"+topPosit
                                        //                                                +" left:"+left
                                        //                                                +" top:"+top
                                        //                                                +" right:"+right
                                        //                                                +" bottom:"+bottom
                                        //                                        );
                                        //非吃到的区域,检查是否在吸引区
                                        if (leftPosit < left1 || leftPosit > right1 || topPosit < top1 || topPosit > bottom1) {
                                            continue;
                                        }
                                        //吸引区:
                                        int l = (leftAtStage - leftPosit) * (leftAtStage - leftPosit);
                                        int t = (topAtStage - topPosit) * (topAtStage - topPosit);
                                        float distance = (float) Math.sqrt(l + t);
                                        
                                        float v = 4 / distance;
                                        int mLeft = (int) ((leftAtStage - leftPosit) * v + leftPosit + 0.5f);
                                        int mTop = (int) ((topAtStage - topPosit) * v + topPosit + 0.5f);
                                        food.setLeftPosition(mLeft);
                                        food.setTopPosition(mTop);
                                        continue;
                                    }
                                    int score = food.eated();
                                    snake.addScore(score);
                                    list.remove(i1);
                                    if (food.isReuse()) {
                                        mFoodStore.addChunk(food);
                                    }
                                }
                            }
                        }
                       
                    }

                    @Override
                    public void onDead(int[] lefts, int[] tops) {
                        for (int i = 0; i < lefts.length; i++) {
                            if (eventCallback != null) {
                                eventCallback.onGameOver();
                            }
                            if (i % 2 == 0) {
                                return;
                            }
                            Food food = new Food(2);
                            food.setLeftPosition(lefts[i]);
                            food.setTopPosition(tops[i]);
                            mFoodStore.addToAll(food);
                            mFoodStore.addChunk(food);
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

    EventCallback eventCallback;

    public interface EventCallback {
        void onGameOver();
    }

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }
}
