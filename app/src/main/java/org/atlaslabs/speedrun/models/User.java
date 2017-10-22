package org.atlaslabs.speedrun.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject{
    @PrimaryKey
    private String id;
    private String weblink;
    private String twitch;
    private String youtube;
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

    public String getTwitch() {
        return twitch;
    }

    public String getYoutube() {
        return youtube;
    }

    public static User getByID(String id, Realm realm){
        return realm.where(User.class).equalTo("id", id).findFirst();
    }
}
