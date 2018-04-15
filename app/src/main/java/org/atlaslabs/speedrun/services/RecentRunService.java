package org.atlaslabs.speedrun.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.models.User;
import org.atlaslabs.speedrun.network.RestUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class RecentRunService extends IntentService {
    private static final String TAG = RecentRunService.class.getSimpleName();
    static String INTENT_RECENT_RUNS_COMPLETE = "INTENT_RECENT_RUNS_COMPLETE";

    public RecentRunService() {
        super(RecentRunService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        RestUtil.createAPI().getRunsRecent()
                .subscribeOn(Schedulers.newThread())
                .subscribe((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(Arrays.asList(item.getRuns()));
                        realm.commitTransaction();
                    } finally {
                        realm.close();
                    }
                    LinkedList<String> userIds = new LinkedList<>();
                    for (Run run : item.getRuns())
                        for (User player : run.getPlayers())
                            if (player.getId() != null)
                                userIds.add(player.getId());
                    loadUsers(userIds);
                }, e -> Log.w(TAG, "Error getting recent runs: " + e));
    }

    private void loadUsers(List<String> userIds) {
        ConcurrentLinkedQueue<String> work = new ConcurrentLinkedQueue<>(userIds);
        for (String userID : work)
            RestUtil.createAPI().getUser(userID)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe((item) -> {
                        Realm realm = Realm.getDefaultInstance();
                        try {
                            realm.beginTransaction();
                            realm.insertOrUpdate(item.getUser());
                            realm.commitTransaction();
                        } finally {
                            realm.close();
                        }
                        work.remove(userID);
                    });
        // wait for work list to be completed
        while (!work.isEmpty()) ;

        // Broadcast that we are done populating game data
        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(INTENT_RECENT_RUNS_COMPLETE));
    }
}
