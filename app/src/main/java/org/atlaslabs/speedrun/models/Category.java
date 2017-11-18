package org.atlaslabs.speedrun.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {
    private static final String TAG = Category.class.getSimpleName();
    @PrimaryKey
    private String id;
    private String name;
    private String weblink;
    private String type;
    private String rules;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWeblink() {
        return weblink;
    }

    public String getType() {
        return type;
    }

    public String getRules() {
        return rules;
    }

    @Nullable
    public static Category get(Realm realm, String id) {
        if (realm == null)
            return null;
        return realm.where(Category.class).equalTo("id", id).findFirst();
    }

    public static Single<Category> getOrFetch(Realm realm, @NonNull String id) {
        Category category = get(realm, id);
        if (category == null)
            return fetch(id);
        return Single.just(category);
    }

    public static Single<Category> fetch(@NonNull String id) {
        return RestUtil.createAPI().getCategory(id)
                .subscribeOn(Schedulers.newThread())
                .doOnError((e) ->
                        Log.e(TAG, "fetch error: " + e.toString())
                )
                .flatMap((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getCategory());
                        realm.commitTransaction();
                    } finally {
                        realm.close();
                    }
                    return Single.just(item.getCategory());
                });
    }
}
