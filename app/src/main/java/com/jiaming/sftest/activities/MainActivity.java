package com.jiaming.sftest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jiaming.sftest.R;
import com.jiaming.sftest.controls.GameCtrl;
import com.jiaming.sftest.element.Snake2;
import com.jiaming.sftest.views.Screen;

import kong.qingwei.rockerlibrary.RockerView;

public class MainActivity extends Activity {

    private GameCtrl mGameCtrl;
    private TextView tips;
    private String TAG = "MainActivity";
    private Screen     mScreen;
    private RockerView mRocker;
    private Snake2 mSnake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGameCtrl=GameCtrl.getInstance();
        mSnake = mGameCtrl.getSnake();
        mRocker = (RockerView) findViewById(R.id.rockerView_center);
        mRocker.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        mRocker.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void angle(double angle) {
                mSnake.setNewMoveDirec((float) angle);
//                Log.d(TAG, "angle: "+angle);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
