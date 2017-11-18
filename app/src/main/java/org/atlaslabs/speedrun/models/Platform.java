package org.atlaslabs.speedrun.models;

import android.support.annotation.NonNull;
import android.util.Log;

import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Platform extends RealmObject {
    private static final String TAG = Platform.class.getSimpleName();
    @PrimaryKey
    private String id;
    private String name;
    /**
     * Year platform was released
     */
    private int released;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getReleased() {
        return released;
    }

    public static Platform get(Realm realm, String id) {
        return realm.where(Platform.class).equalTo("id", id).findFirst();
    }

    public static Single<Platform> getOrFetch(Realm realm, @NonNull String id) {
        Platform platform = get(realm, id);
        if (platform == null)
            return fetch(id);
        return Single.just(platform);
    }

    public static Single<Platform> fetch(@NonNull String id) {
        return RestUtil.createAPI().getPlatform(id)
                .subscribeOn(Schedulers.newThread())
                .doOnError((e) ->
                        Log.e(TAG, "fetch error: " + e.toString())
                )
                .flatMap((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getPlatform());
                        realm.commitTransaction();
                    } finally {
                        realm.close();
                    }
                    return Single.just(item.getPlatform());
                });
    }
}
