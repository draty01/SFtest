package com.jiaming.sftest.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jiaming.sftest.R;
import com.jiaming.sftest.controls.GameCtrl;
import com.jiaming.sftest.utils.DensityUtil;

/**
 * Created by jiaming.liang on 2017/1/17.
 */

public class InitActivity extends Activity {
    private TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        DensityUtil.init(this);
        tips = (TextView) findViewById(R.id.init_tips);
        GameCtrl mGameCtrl = GameCtrl.getInstance();
        mGameCtrl.init(getApplicationContext(),new GameCtrl.InitCallback() {
            @Override
            public void onChang(final int percentage) {
                if (percentage != GameCtrl.INIT_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tips.setText(getString(R.string.initting) + percentage + "%");
                        }
                    });
                } else {
                    startActivity(new Intent(InitActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}
