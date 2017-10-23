package org.atlaslabs.speedrun.models;

import android.support.annotation.Nullable;

import org.atlaslabs.speedrun.network.IFetchComplete;
import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Platform extends RealmObject{
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

    public static Platform get(Realm realm, String id){
        return realm.where(Platform.class).equalTo("id", id).findFirst();
    }

    public static Platform getOrFetch(Realm realm, String id,
                                      @Nullable IFetchComplete<Platform> completeHandler){
        Platform platform = get(realm, id);
        if(platform == null)
            fetch(id, completeHandler);
        else
            completeHandler.fetchComplete(platform);
        return platform;
    }

    public static void fetch(String id, @Nullable IFetchComplete<Platform> completeHandler){
        RestUtil.createAPI().getPlatform(id)
                .subscribeOn(Schedulers.newThread())
                .subscribe((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getPlatform());
                        realm.commitTransaction();
                    }finally{
                        realm.close();
                    }
                    completeHandler.fetchComplete(item.getPlatform());
                });
    }
}
