package com.jiaming.sftest.controls;
import android.util.Log;

import com.jiaming.sftest.element.Food;
import com.jiaming.sftest.element.IElement;
import com.jiaming.sftest.setting.Contains;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by jiaming.liang on 2017/1/24.
 * 保存点,根据food的位置,分块保存
 * <p/>
 * 初始化时根据地图大小分块
 * 每块一个集合保存食物
 * 集合放进集合二位数组
 */

public class FoodStore {
    public static final String TAG = "FoodStore";
    private int            mapWidth;//单位px
    private int            mapHeight;
    private List<Food>[][] coorLists;//[Width][Height]
    List<IElement> allFoods = new ArrayList<>();
    List<IElement> tempFoods = new ArrayList<>();
    private int chunkSizePx = Contains.FOODSTORE_CHUNK_SIZE_PX;

    /**
     * @param mapWidthPx  stage 的宽度 单位px
     * @param mapHeightPX stage 的高度 单位px
     */
    public FoodStore(int mapWidthPx, int mapHeightPX, int initFoodCount) {
        this.mapWidth = mapWidthPx;
        this.mapHeight = mapHeightPX;
        int widthCount = 1;
        int heightCount = 1;
        while (mapWidthPx > chunkSizePx) {
            widthCount++;
            mapWidthPx -= chunkSizePx;
        }
        while (mapHeightPX > chunkSizePx) {
            heightCount++;
            mapHeightPX -= chunkSizePx;
        }
        Log.d(TAG, "FoodStore: chunkSizePx:" + chunkSizePx);
        coorLists = new List[widthCount][heightCount];
        initFood(initFoodCount);
    }

    private boolean isInit = false;

    public void initFood(int foodCount) {
        if (isInit)
            return;
        isInit = true;
        for (int i = 0; i < foodCount; i++) {
            Food food = new Food(0);
            allFoods.add(food);
            addChunk(food);
        }
    }

    public List<IElement> getFoods() {
        return allFoods;
    }

    public void addChunk(Food food) {
        int leftPosition = food.getLeftPosition();
        int topPosition = food.getTopPosition();
        //根据点位置放到对应的list 内
        List<Food> list = getList(leftPosition, topPosition);
        list.add(food);
    }

    public void addToAll(Food food) {
        allFoods.add(food);
    }
    

    public void removeFromAll(Food food) {
        allFoods.remove(food);
    }

    public List<Food> getList(int left, int top) {
        int width = left / chunkSizePx;
        int hight = top / chunkSizePx;
        if (coorLists[width][hight] == null) {
            coorLists[width][hight] = new ArrayList<>();
        }
        return coorLists[width][hight];
    }
}
