package org.atlaslabs.speedrun.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

public class Favorite extends RealmObject {
    private String id, id2; // ponder inheritance scheme..?
    private int type;

    public Favorite() {
    }

    public Favorite(String id, FavoriteType type) {
        this.id = id;
        this.type = type.ordinal();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId2() {
        return id2;
    }

    public Favorite setId2(String id2) {
        this.id2 = id2;
        return this;
    }

    public FavoriteType getType() {
        return FavoriteType.values()[type];
    }

    public void setType(FavoriteType type) {
        this.type = type.ordinal();
    }

    @Nullable
    public static List<Favorite> get(@NonNull Realm realm) {
        return realm.where(Favorite.class).findAll();
    }

    public static void insert(@NonNull Realm realm, @NonNull String id, @NonNull FavoriteType type) {
        realm.beginTransaction();
        realm.insert(new Favorite(id, type));
        realm.commitTransaction();
    }

    public static void insert(@NonNull Realm realm, @NonNull String id, @NonNull String id2, @NonNull FavoriteType type) {
        realm.beginTransaction();
        realm.insert(new Favorite(id, type).setId2(id2));
        realm.commitTransaction();
    }

    @Nullable
    public static Favorite get(@NonNull Realm realm, @NonNull String id, @NonNull FavoriteType type) {
        return realm.where(Favorite.class)
                .equalTo("id", id)
                .equalTo("type", type.ordinal())
                .findFirst();
    }

    @Nullable
    public static Favorite get(@NonNull Realm realm, @NonNull String id, @NonNull String id2,
                               @NonNull FavoriteType type) {
        return realm.where(Favorite.class)
                .equalTo("id", id)
                .equalTo("id2", id2)
                .equalTo("type", type.ordinal())
                .findFirst();
    }

    public static void remove(@NonNull Realm realm, Favorite favorite) {
        realm.beginTransaction();
        realm.where(Favorite.class)
                .equalTo("id", favorite.id)
                .equalTo("id2", favorite.id2)
                .equalTo("type", favorite.type)
                .findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    public enum FavoriteType {
        CATEGORY, GAME, USER
    }
}
