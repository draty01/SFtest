package com.jiaming.sftest.model;

import java.util.ArrayList;
/**
 * Created by jiaming.liang on 2017/1/23.
 */

public class CoordinatePool {
    ArrayList<Coordinate> freeCoordinate = new ArrayList<>();

    Thread clearThread;
    
    public CoordinatePool(){
        if (clearThread==null){
            clearThread=new ClearThread("clearThread");
            clearThread.start();
        }
    }
    public Coordinate obtain(int mleft, int mTop) {
        
        if (freeCoordinate.size() > 0) {
            return freeCoordinate.remove(0);
        } else {
            return new Coordinate(mleft, mTop);
        }
    }

    public void restore(Coordinate coordinate) {
        coordinate.mLeft = 0;
        coordinate.mTop = 0;
        freeCoordinate.add(coordinate);
    }

    private class ClearThread extends Thread {
        public ClearThread(String clearThread) {
            super(clearThread);
        }

        @Override
        public void run() {
            while (true){
                int size = freeCoordinate.size();
                if (size >10) {
                    for (int i = 10; i < size; i++) {
                        freeCoordinate.remove(i);
                    }
                }
                
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
