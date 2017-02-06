package com.jiaming.sftest.element;
import com.jiaming.sftest.setting.Contains;
import com.jiaming.sftest.utils.DensityUtil;
/**
 * 项目名称：SFtest
 * 类描述：
 * 创建人：JiaMing
 * 创建时间：23:56
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public abstract class Element implements IElement {
    public static int mStageWidthPx;
    public static int mStageHeightPx;
    public static int mStageWidthdip;
    public static int mStageHeightdip;
    public static int ScreenMaxWidthPx;
    public static int ScreenMaxHeightPx;
    //自己的位置
    public        int mPositionLeftAtStage;
    public        int mPositionTopAtStage;

    long lastTime;
    static {
        mStageWidthPx = Contains.STAGE_WIDTH_PX;
        mStageHeightPx = Contains.STAGE_HEIGHT_PX;
        mStageWidthdip = Contains.STAGE_WIDTH;
        mStageHeightdip = Contains.STAGE_HEIGHT;
        ScreenMaxWidthPx = DensityUtil.getScreenMaxWidth();
        ScreenMaxHeightPx = DensityUtil.getScreenMaxHeight();
    }
    /**
     * 单位是px
     * @return
     */
    public int getLeftPosition() {
        return mPositionLeftAtStage;
    }

    public void setLeftPosition(int LeftPosition) {
        mPositionLeftAtStage = LeftPosition;
    }

    public int getTopPosition() {
        return mPositionTopAtStage;
    }

    public void setTopPosition(int positionTop) {
        mPositionTopAtStage = positionTop;
    }

    public static int getScreenMaxWidthDip() {
        return DensityUtil.px2dip(ScreenMaxWidthPx);
    }

    public static int getScreenMaxHeightDip() {
        return DensityUtil.px2dip(ScreenMaxHeightPx);
    }

    public static int getScreenMaxWidth() {
        return ScreenMaxWidthPx;
    }

    public static int getScreenMaxHeight() {
        return ScreenMaxHeightPx;
    }

    public int getStageWidthPx() {
        return mStageWidthPx;
    }

    public int getStageHeightPx() {
        return mStageHeightPx;
    }

    public static int getmStageWidthdip() {
        return mStageWidthdip;
    }

    public static int getmStageHeightdip() {
        return mStageHeightdip;
    }

    public long nowMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public void reflash(long time) {
    }

    @Override
    public boolean isShow() {
        return true;
    }
}
