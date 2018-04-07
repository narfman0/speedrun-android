package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    private String id;
    private String rel, role, twitch, youtube, name, weblink;

    public String getName() {
        return name;
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

    public String getRel() {
        return rel;
    }
}
