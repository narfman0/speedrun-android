package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Category {
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
