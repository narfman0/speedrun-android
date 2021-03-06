package org.atlaslabs.speedrun.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class GamesLoadReceiver extends BroadcastReceiver {
    private Context context;
    private IGamesLoadedHandler handler;

    public GamesLoadReceiver(Context context, IGamesLoadedHandler handler) {
        this.context = context;
        this.handler = handler;
        LocalBroadcastManager.getInstance(context).registerReceiver(
                this, new IntentFilter(
                        GamesLoadService.INTENT_GAMES_LOAD_COMPLETE));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        handler.gamesLoaded();
    }

    public void clean() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        context = null;
        handler = null;
    }

    public interface IGamesLoadedHandler {
        void gamesLoaded();
    }
}
