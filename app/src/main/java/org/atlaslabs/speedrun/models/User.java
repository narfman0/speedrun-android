package org.atlaslabs.speedrun.models;

import android.support.annotation.Nullable;

import org.atlaslabs.speedrun.network.IFetchComplete;
import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject{
    @PrimaryKey
    private String id;
    private String rel, role;
    private Link twitch;
    private Link youtube;
    private String weblink;
    private Names names;

    public Names getNames() {
        return names;
    }

    public String getId() {
        return id;
    }

    public String getWeblink() {
        return weblink;
    }

    public Link getTwitch() {
        return twitch;
    }

    public Link getYoutube() {
        return youtube;
    }

    public String getRel() {
        return rel;
    }

    public static User get(Realm realm, String id){
        return realm.where(User.class).equalTo("id", id).findFirst();
    }

    public static User getOrFetch(Realm realm, String id,
                                  @Nullable IFetchComplete<User> completeHandler){
        User user = get(realm, id);
        if(user == null)
            fetch(id, completeHandler);
        else
            completeHandler.fetchComplete(user);
        return user;
    }

    public static void fetch(String id, @Nullable IFetchComplete<User> completeHandler){
        RestUtil.createAPI().getUser(id)
                .subscribeOn(Schedulers.newThread())
                .subscribe((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getUser());
                        realm.commitTransaction();
                    }finally{
                        realm.close();
                    }
                    completeHandler.fetchComplete(item.getUser());
                });
    }
}
