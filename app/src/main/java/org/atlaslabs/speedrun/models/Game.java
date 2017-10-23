package org.atlaslabs.speedrun.models;

import android.support.annotation.Nullable;

import org.atlaslabs.speedrun.network.IFetchComplete;
import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Game extends RealmObject{
    @PrimaryKey
    private String id;
    private String abbreviation;
    private String weblink;
    private Names names;
    private Assets assets;

    public String getId() {
        return id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getWeblink() {
        return weblink;
    }

    public Names getNames() {
        return names;
    }

    public Assets getAssets() {
        return assets;
    }

    public static Game get(Realm realm, String id){
        return realm.where(Game.class).equalTo("id", id).findFirst();
    }

    public static Game getOrFetch(Realm realm, String id,
                                      @Nullable IFetchComplete<Game> completeHandler){
        Game game = get(realm, id);
        if(game == null)
            fetch(id, completeHandler);
        else
            completeHandler.fetchComplete(game);
        return game;
    }

    public static void fetch(String id, @Nullable IFetchComplete<Game> completeHandler){
        RestUtil.createAPI().getGame(id)
                .subscribeOn(Schedulers.newThread())
                .subscribe((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getGame());
                        realm.commitTransaction();
                    }finally{
                        realm.close();
                    }
                    completeHandler.fetchComplete(item.getGame());
                });
    }
}
