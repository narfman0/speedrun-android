package org.atlaslabs.speedrun.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Platform extends RealmObject{
    @PrimaryKey
    private String id;
    private String name;
    /**
     * Year platform was released
     */
    private int released;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getReleased() {
        return released;
    }

    public static Platform getByID(Realm realm, String id){
        return realm.where(Platform.class).equalTo("id", id).findFirst();
    }
}
