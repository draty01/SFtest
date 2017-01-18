package com.jiaming.sftest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jiaming.sftest.R;
import com.jiaming.sftest.controls.GameCtrl;
import com.jiaming.sftest.views.Screen;

public class MainActivity extends Activity {

    private GameCtrl mGameCtrl;
    private TextView tips;
    private String TAG = "MainActivity";
    private Screen mScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
