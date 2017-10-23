package org.atlaslabs.speedrun.models;

import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.Single;
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

    public static Single<Game> getOrFetch(Realm realm, String id){
        Game game = get(realm, id);
        if(game == null)
            return fetch(id);
        return Single.just(game);
    }

    public static Single<Game> fetch(String id){
        return RestUtil.createAPI().getGame(id)
                .subscribeOn(Schedulers.newThread())
                .flatMap((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getGame());
                        realm.commitTransaction();
                    }finally{
                        realm.close();
                    }
                    return Single.just(item.getGame());
                });
    }
}
