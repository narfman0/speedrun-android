package org.atlaslabs.speedrun.models;

import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject{
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

    public static Category get(Realm realm, String id){
        return realm.where(Category.class).equalTo("id", id).findFirst();
    }

    public static Single<Category> getOrFetch(Realm realm, String id){
        Category category = get(realm, id);
        if(category == null)
            return fetch(id);
        return Single.just(category);
    }

    public static Single<Category> fetch(String id){
        return RestUtil.createAPI().getCategory(id)
                .subscribeOn(Schedulers.newThread())
                .flatMap((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getCategory());
                        realm.commitTransaction();
                    }finally{
                        realm.close();
                    }
                    return Single.just(item.getCategory());
                });
    }
}
