package com.guardianlink;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class SOSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔓 Show over lock screen
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        // 🔥 Trigger SOS immediately
        SOSHelper.triggerSOS(this);

        finish();
    }
}