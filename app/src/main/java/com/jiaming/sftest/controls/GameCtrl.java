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
    private static      Lock lock               = new ReentrantLock();
    public final static int  INIT_START         = 10;
    public final static int  INIT_SCREEN_INITED = 20;
    public final static int  INIT_OK            = 100;
    public final static String  TAG            = "GameCtrl";
    private Screen mScreen;
    private int CtrlDirec=0;
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

    public void init(Context context,@NonNull InitCallback callback) {
        if (isInited) {
            Log.e(TAG, "isInited: "+isInited);
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
        final int ScreenHaftWidthPx = DensityUtil.getScreenMaxWidth()/2;
        final int ScreenHaftHeightPx = DensityUtil.getScreenMaxHeight()/2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int foodCount = Contains.FOOD_COUNT;
                int snakeCount = Contains.SNAKE_COUNT;
                for (int i = 0; i < foodCount; i++) {
                    foods.add(new Food(0));
                }
                mSnake = new Snake2();
                mSnake.setMoveListen(new Snake2.onMoveListen() {
                    @Override
                    public void onMove(int leftAtStage, int topAtStage) {
                        setScreenLeft(leftAtStage-ScreenHaftWidthPx);
                        setScreenTop(topAtStage-ScreenHaftHeightPx);
                    }
                });
                snakes.add(mSnake);
                initCallback.onChang(INIT_OK);
            }
        }).start();
    }

    IElement       bg     = new StageBg();
    List<IElement> foods  = new ArrayList<>();
    List<IElement> snakes = new ArrayList<>();
    Snake2 mSnake;
    
    public int screenLeft;
    public int screenTop;

    public Snake2 getSnake() {
        return mSnake;
    }

    public void setCtrlDirec(int ctrlDirec) {
        CtrlDirec = ctrlDirec;
        if (mSnake!=null){
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
        return foods;
    }

    public List<IElement> getSnakes(boolean isShow) {
        return snakes;
    }

    public interface InitCallback {
        void onChang(int percentage);
    }
}
