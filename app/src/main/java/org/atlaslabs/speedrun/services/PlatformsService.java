package org.atlaslabs.speedrun.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.atlaslabs.speedrun.models.Platform;
import org.atlaslabs.speedrun.network.RestUtil;

import java.util.Arrays;

import io.realm.Realm;

public class PlatformsService extends IntentService {
    static final String INTENT_PLATFORMS_COMPLETE = "INTENT_PLATFORMS_COMPLETE";

    public PlatformsService(){
        super(PlatformsService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Realm realm = Realm.getDefaultInstance();
        try {
            long totalPlatforms = realm.where(Platform.class).count();
            if(totalPlatforms == 0)
                RestUtil.createAPI().getPlatformsBulk().subscribe((item) -> {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(Arrays.asList(item.getPlatforms()));
                    realm.commitTransaction();
                });
        } finally {
            realm.close();
        }

        // Broadcast that we are done populating game data
        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(INTENT_PLATFORMS_COMPLETE));
    }
}