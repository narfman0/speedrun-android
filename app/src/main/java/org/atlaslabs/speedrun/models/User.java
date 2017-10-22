package org.atlaslabs.speedrun.models;

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

    public static User getByID(Realm realm, String id){
        return realm.where(User.class).equalTo("id", id).findFirst();
    }
}
