package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Game {
    @PrimaryKey
    private String id;
    private String abbreviation, weblink, backgroundURL, name;

    public String getId() {
        return id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getWeblink() {
        return weblink;
    }

    public String getBackgroundURL() {
        return backgroundURL;
    }

    public String getName() {
        return name;
    }
}
