package org.atlaslabs.speedrun.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.atlaslabs.speedrun.models.Platform;
import org.atlaslabs.speedrun.network.RestUtil;

import java.util.Arrays;

import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class PlatformsService extends IntentService {
    static final String INTENT_PLATFORMS_COMPLETE = "INTENT_PLATFORMS_COMPLETE";

    public PlatformsService(){
        super(PlatformsService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        long totalPlatforms = 0;
        Realm realm = Realm.getDefaultInstance();
        try {
            totalPlatforms = realm.where(Platform.class).count();
        } finally {
            realm.close();
        }
        if(totalPlatforms == 0)
            populate();
    }

    private void populate(){
        RestUtil.createAPI().getPlatformsBulk()
                .subscribeOn(Schedulers.newThread())
                .subscribe((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(Arrays.asList(item.getPlatforms()));
                        realm.commitTransaction();
                    } finally {
                        realm.close();
                    }

                    // Broadcast that we are done populating game data
                    LocalBroadcastManager.getInstance(this).sendBroadcast(
                            new Intent(INTENT_PLATFORMS_COMPLETE));
                });
    }
}