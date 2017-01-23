package com.jiaming.sftest.model;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：20:47
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class Coordinate extends LinkObject {
    public int mLeft;
    public int mTop;

    public Coordinate(int left, int top) {
        mLeft = left;
        mTop = top;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "mLeft=" + mLeft +
                ", mTop=" + mTop +
                '}';
    }
}
