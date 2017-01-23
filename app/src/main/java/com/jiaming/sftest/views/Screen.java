package com.jiaming.sftest.views;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jiaming.sftest.controls.GameCtrl;
import com.jiaming.sftest.element.IElement;
import com.jiaming.sftest.setting.Contains;

import java.util.List;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：11:23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class Screen extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private ScreenThread  myThread;
    private GameCtrl      mGameCtrl;
    
    private String TAG = "Screen";

    public Screen(Context context) {
        this(context, null);
    }

    public Screen(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Screen(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Screen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mGameCtrl = GameCtrl.getInstance();
        holder = this.getHolder();
        holder.addCallback(this);
        myThread = new ScreenThread(holder);//创建一个绘图线程
        setLongClickable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated: ");
        myThread.isRun = true;
        myThread.start();
    }

    

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        myThread.isRun = false;
    }

    private float downX;
    private float downY;
    private int   tempScX;
    private int   tempScY;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        float moveX = 0;
//        float moveY = 0;
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getX();
//                downY = event.getY();
//                tempScX = mGameCtrl.getScreenLeft();
//                tempScY = mGameCtrl.getScreenTop();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveX = event.getX();
//                moveY = event.getY();
//                break;
//        }
//        if (moveX == 0 && moveY == 0) {
//            return super.onTouchEvent(event);
//        }
//        int distanceX = (int) (moveX - downX + 0.5f);
//        int distanceY = (int) (moveY - downY + 0.5f);
//        int screenLeft = tempScX - distanceX;
//        mGameCtrl.setScreenLeft(screenLeft);
//        int screenTop = tempScY - distanceY;
//        mGameCtrl.setScreenTop(screenTop);
//        //        Log.d(TAG, "onTouchEvent: downX:" + downX + "moveX:" + moveX + "moveY:" + moveY + "distanceX:" + distanceX + "screenLeft:" + screenLeft);
//        return super.onTouchEvent(event);
//    }

    //线程内部类
    class ScreenThread extends Thread {
        private SurfaceHolder holder;
        public  boolean       isRun;

        public ScreenThread(SurfaceHolder holder) {
            this.holder = holder;
            isRun = true;
        }

        long startTime = 0;

        @Override
        public void run() {
            int count = 0;
            Paint p = new Paint(); //创建画笔
            Log.d(TAG, "run: start run");
            while (isRun) {
                startTime = System.currentTimeMillis();
                Canvas c = null;
                try {
                    if (!holder.getSurface().isValid()) {
                        isRun = false;
                    } else {
                        c = holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                        if (c != null) {
                            c.drawColor(0xFFD6FEE7);//设置画布背景颜色
                            drawScreen(c, p);
                        }
                    }
                } catch (IllegalStateException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        if (!holder.getSurface().isValid()) {
                            isRun = false;
                        } else {
                            holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。
                        }
                        //                        Log.d(TAG, "run: start finally");
                    }
                }
                long endTime = System.currentTimeMillis();
                long usedTime = endTime-startTime;
                long sleepTime = Contains.DRAW_SLEEP_TIME - usedTime;
                Log.d(TAG, "run: usedTime:"+usedTime);
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void drawScreen(Canvas c, Paint p) {
            IElement bg = mGameCtrl.getBg(true);
            //刷新数据
            List<IElement> foods = mGameCtrl.getFoods(true);
            List<IElement> snakes = mGameCtrl.getSnakes(true);
            long now = System.currentTimeMillis();
            for (int i = 0; i < foods.size(); i++) {
                foods.get(i).reflash(now);
            }
            for (int i = 0; i < snakes.size(); i++) {
                snakes.get(i).reflash(now);
            }
            
            int currentLeft = mGameCtrl.getScreenLeft();
            int currentTop = mGameCtrl.getScreenTop();
            
            //画背景
            p.reset();
            bg.draw(currentLeft, currentTop, c, p);
            //画食物
            p.reset();
            for (int i = 0; i < foods.size(); i++) {
                //                Log.d(TAG, "drawFoods: "+i);
                foods.get(i).draw(currentLeft, currentTop, c, p);
            }
            //画蛇
            p.reset();
            for (int i = 0; i < snakes.size(); i++) {
                snakes.get(i).draw(currentLeft, currentTop, c, p);
            }
        }
    }
}
