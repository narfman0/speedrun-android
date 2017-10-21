package org.atlaslabs.speedrun.models;

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
}
