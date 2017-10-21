package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;

public class Times extends RealmObject{
    private String primary_t;
    private String realtime_t;
    private String ingame_t;

    public String getIngameTime() {
        return ingame_t;
    }

    public String getPrimaryTime() {
        return primary_t;
    }

    public String getRealTime() {
        return realtime_t;
    }
}
