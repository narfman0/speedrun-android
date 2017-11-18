package org.atlaslabs.speedrun.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import static org.atlaslabs.speedrun.services.PlatformsService.INTENT_PLATFORMS_COMPLETE;

public class PlatformsReceiver extends BroadcastReceiver {
    private Context context;
    private IPlatformsHandler handler;

    public PlatformsReceiver(Context context, IPlatformsHandler handler) {
        this.context = context;
        this.handler = handler;
        LocalBroadcastManager.getInstance(context).registerReceiver(
                this, new IntentFilter(INTENT_PLATFORMS_COMPLETE));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        handler.platformsLoaded();
    }

    public void clean() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        context = null;
        handler = null;
    }

    public interface IPlatformsHandler {
        void platformsLoaded();
    }
}
