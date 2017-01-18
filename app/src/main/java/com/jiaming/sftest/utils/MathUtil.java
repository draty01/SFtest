package com.jiaming.sftest.utils;
import java.util.Random;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：14:43
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class MathUtil {
    /**
     * 获得一个随机整数
     *
     * @param min 最小值 包含
     * @param max 最大值 不包含
     * @return 一个随机整数
     */

    public static int getRanInt(int min, int max) {
        int v = (int) (Math.random() * max);
        int ret = v + min;
        return ret >= 100 ? v : ret;
    }
}
