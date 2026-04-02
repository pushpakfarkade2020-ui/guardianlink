package com.guardianlink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SOSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SOSHelper.triggerSOS(context);
    }
}