package org.atlaslabs.speedrun.models;

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
}
