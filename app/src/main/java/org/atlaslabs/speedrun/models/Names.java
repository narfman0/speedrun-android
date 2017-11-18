package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;

public class Names extends RealmObject {
    private String international, japanese, twitch;

    public String getInternational() {
        return international;
    }

    public String getJapanese() {
        return japanese;
    }

    public String getTwitch() {
        return twitch;
    }
}
