package com.jiaming.sftest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiaming.sftest.R;
import com.jiaming.sftest.controls.GameCtrl;
import com.jiaming.sftest.views.Screen;

import kong.qingwei.rockerlibrary.RockerView;

public class MainActivity extends Activity implements View.OnClickListener {

    private GameCtrl mGameCtrl;
    private String TAG = "MainActivity";
    private RockerView mRocker;
    private Button mRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRestart = (Button) findViewById(R.id.restart);
        mRestart.setOnClickListener(this);
        mGameCtrl = GameCtrl.getInstance();
        mGameCtrl.setEventCallback(new GameCtrl.EventCallback() {
            @Override
            public void onGameOver() {
                Log.d(TAG, "onGameOver: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRestart.setVisibility(View.VISIBLE);
                    }
                });
                
            }
        });
        mRocker = (RockerView) findViewById(R.id.rockerView_center);
        mRocker.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        mRocker.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void angle(double angle) {
//                Log.d(TAG, "angle: "+angle);
                mGameCtrl.setCtrlDirec((int) (angle+0.5f));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart:
                mGameCtrl.init(this, true, new GameCtrl.InitCallback() {
                    @Override
                    public void onChang(int percentage) {
                        if (percentage==GameCtrl.INIT_OK){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRestart.setVisibility(View.GONE);
                                }
                            });
                            
                        }
                    }
                });
                break;
        }
    }
}
