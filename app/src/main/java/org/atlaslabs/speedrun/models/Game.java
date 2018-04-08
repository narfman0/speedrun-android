package org.atlaslabs.speedrun.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.atlaslabs.speedrun.network.RestUtil;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class Game extends RealmObject {
    private static final String TAG = Game.class.getSimpleName();
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

    @Nullable
    public static Game get(Realm realm, String id) {
        if (realm == null)
            return null;
        return realm.where(Game.class).equalTo("id", id).findFirst();
    }

    @Nullable
    public static List<Game> get(Realm realm) {
        if (realm == null)
            return null;
        return realm.where(Game.class)
                .findAllSorted("names.international", Sort.ASCENDING);
    }

    @Nullable
    public static List<Game> getFiltered(Realm realm, String filter) {
        if (realm == null)
            return null;
        return realm.where(Game.class)
                .contains("names.international", filter, Case.INSENSITIVE)
                .findAllSorted("names.international", Sort.ASCENDING);
    }

    public static Single<Game> getOrFetch(Realm realm, @NonNull String id) {
        Game game = get(realm, id);
        if (game == null)
            return fetch(id);
        return Single.just(game);
    }

    public static Single<Game> fetch(@NonNull String id) {
        return RestUtil.createAPI().getGame(id)
                .subscribeOn(Schedulers.newThread())
                .doOnError((e) ->
                        Log.e(TAG, "fetch error: " + e.toString())
                )
                .flatMap((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getGame());
                        realm.commitTransaction();
                    } finally {
                        realm.close();
                    }
                    return Single.just(item.getGame());
                });
    }
}
