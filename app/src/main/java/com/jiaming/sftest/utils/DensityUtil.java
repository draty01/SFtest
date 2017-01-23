package com.jiaming.sftest.utils;
import android.content.Context;
import android.util.Log;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：21:24
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class DensityUtil {
    public static final String TAG="DensityUtil";

    private static float scale = 0;
    private static Context context;

    public static void init(Context context) {
        DensityUtil.context = context;
        scale = context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * 返回float
     */
    public static float dip2px_f(float dpValue) {
        return dpValue * scale;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 横屏,所有宽高相反
     * @return
     */
    public static int getScreenMaxWidth() {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        Log.v(TAG, "getScreenMaxWidthDip: "+widthPixels);
        return widthPixels;
    }

    public static int getScreenMaxWidthDip() {
        return px2dip(getScreenMaxWidth());
    }

    public static int getScreenMaxHeight() {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        Log.v(TAG, "getScreenMaxHeightDip: "+heightPixels);
        return heightPixels;
    }

    public static int getScreenMaxHeightDip() {
        return px2dip(getScreenMaxHeight());
    }
}
