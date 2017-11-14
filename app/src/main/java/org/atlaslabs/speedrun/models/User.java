package org.atlaslabs.speedrun.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject{
    private static final String TAG = User.class.getSimpleName();
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

    @Nullable
    public String getNamePretty(){
        if(names != null){
            if(!TextUtils.isEmpty(names.getInternational()))
                return names.getInternational();
            else if(!TextUtils.isEmpty(names.getJapanese()))
                return names.getJapanese();
        }
        return null;
    }

    @Nullable
    public String getYoutubePretty(){
        return youtube == null ? null : youtube.getUri();
    }

    @Nullable
    public String getTwitchPretty(){
        return twitch == null ? null : twitch.getUri();
    }

    public static User get(Realm realm, String id){
        return realm.where(User.class).equalTo("id", id).findFirst();
    }

    public static Single<User> getOrFetch(Realm realm, @NonNull String id){
        User user = get(realm, id);
        if(user == null)
            return fetch(id);
        return Single.just(user);
    }

    public static Single<User> fetch(@NonNull String id){
        return RestUtil.createAPI().getUser(id)
                .subscribeOn(Schedulers.newThread())
                .doOnError((e) ->
                    Log.e(TAG, "fetch error: " + e.toString())
                )
                .flatMap((item) -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        realm.insertOrUpdate(item.getUser());
                        realm.commitTransaction();
                    }finally{
                        realm.close();
                    }
                    return Single.just(item.getUser());
                });
    }
}
