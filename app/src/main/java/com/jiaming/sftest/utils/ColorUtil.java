package com.jiaming.sftest.utils;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：12:35
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ColorUtil {
    static int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN};

    public static int getRanColor() {
        int colorIndex = MathUtil.getRanInt(0, colors.length);
        return colors[colorIndex];
    }
}
