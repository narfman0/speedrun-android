package org.atlaslabs.speedrun.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class RecentRunReceiver extends BroadcastReceiver {
    private Context context;
    private IRecentRunsLoadedHandler handler;

    public RecentRunReceiver(Context context, IRecentRunsLoadedHandler handler) {
        this.context = context;
        this.handler = handler;
        LocalBroadcastManager.getInstance(context).registerReceiver(
                this, new IntentFilter(
                        RecentRunService.INTENT_RECENT_RUNS_COMPLETE));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        handler.recentRunsLoaded();
    }

    public void clean() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        context = null;
        handler = null;
    }

    public interface IRecentRunsLoadedHandler {
        void recentRunsLoaded();
    }
}
