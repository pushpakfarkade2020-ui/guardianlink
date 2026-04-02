package com.guardianlink;

import android.service.quicksettings.TileService;

public class SOSTileService extends TileService {

    @Override
    public void onClick() {
        super.onClick();

        // 🔥 Direct SOS trigger
        SOSHelper.triggerSOS(this);
    }
}