package com.jiaming.sftest.element;
import com.jiaming.sftest.model.Coordinate;

import org.junit.Test;
/**
 * Created by jiaming.liang on 2017/1/20.
 */
public class SnakeTest {
    String TAG      = "SnakeTest";
    int    oldLeft  = 100;
    int    oldTop   = 100;
    float  distance = 100f;

    @Test
    public void doTest1() {
        test_nextCoot(90, oldLeft, (int) (oldTop + distance + 0.5f));
    }

    @Test
    public void doTest2() {
        test_nextCoot(180, (int) (oldLeft - distance + 0.5f), (int) (oldTop));
    }

    @Test
    public void doTest3() {
        test_nextCoot(270, oldLeft, (int) (oldTop - distance + 0.5f));
    }

    @Test
    public void doTest4() {
        test_nextCoot(30, oldLeft + 87, (int) (oldTop + distance / 2 + 0.5f));
    }

    @Test
    public void doTest5() {
        test_nextCoot(150, oldLeft - 87, (int) (oldTop + distance / 2 + 0.5f));
    }

    @Test
    public void doTest6() {
        test_nextCoot(210, oldLeft - 87, (int) (oldTop - distance / 2 + 0.5f));
    }

    @Test
    public void doTest7() {
        test_nextCoot(330, oldLeft + 87, (int) (oldTop - distance / 2 + 0.5f));
    }

    public void test_nextCoot(float direction, int excepLeft, int exceptop) {
        Coordinate coordinate = new Coordinate(oldLeft, oldTop);
        Coordinate newCoor;
        newCoor = nextCoor(direction, distance, coordinate);
        int left = newCoor.mLeft;
        int top = newCoor.mTop;
        System.out.println("direction" + direction + " left" + left + "  top" + top);
        System.out.println("direction" + direction + " excepLeft" + excepLeft + "  exceptop" + exceptop);
        assert left == excepLeft && top == exceptop;
    }

    public Coordinate nextCoor(float direction, float distance, Coordinate oldCoor) {
        int newLeft = 0;
        int newTop = 0;
        int oldLeft = oldCoor.mLeft;
        int oldTop = oldCoor.mTop;
        while (direction >= 360) {
            direction -= 360;
        }
       double angle = direction * Math.PI / 180;
        newLeft = doubleToInt(distance * Math.cos(angle));
        newTop = doubleToInt(distance * Math.sin(angle));
        
//        if (direction == 0) {
//            newLeft = floatToInt(distance);
//            newTop = 0;
//        } else if (direction > 0 && direction < 90) {
//            direction = (float) (direction * Math.PI / 180);
//            newLeft = doubleToInt(distance * Math.cos(direction));
//            newTop = doubleToInt(distance * Math.sin(direction));
//        } else if (direction == 90) {
//            newLeft = 0;
//            newTop = floatToInt(distance);
//        } else if (direction > 90 && direction < 180) {
//            direction = 180 - direction;
//            direction = (float) (direction * Math.PI / 180);
//            newLeft = -doubleToInt(distance * Math.cos(direction));
//            newTop = doubleToInt(distance * Math.sin(direction));
//        } else if (direction == 180) {
//            newTop = 0;
//            newLeft = -floatToInt(distance);
//        } else if (direction > 180 && direction < 270) {
//            direction = direction - 180;
//            direction = (float) (direction * Math.PI / 180);
//            newLeft = -doubleToInt(distance * Math.cos(direction));
//            newTop = -doubleToInt(distance * Math.sin(direction));
//        } else if (direction == 270) {
//            newLeft = 0;
//            newTop = -floatToInt(distance);
//        } else if (direction > 270) {
//            direction = 360 - direction;
//            direction = (float) (direction * Math.PI / 180);
//            newLeft = doubleToInt(distance * Math.cos(direction));
//            newTop = -doubleToInt(distance * Math.sin(direction));
//        }
        newLeft = oldLeft + newLeft;
        newTop = oldTop + newTop;
        return new Coordinate(newLeft, newTop);
    }

    private int floatToInt(float v) {
        return (int) (v + 0.5f);
    }

    private int doubleToInt(double v) {
        return (int) (v+0.5f);
    }
}