package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;

public class Times extends RealmObject{
    private float primary_t;
    private float realtime_t;
    private float ingame_t;

    public float getIngameTime() {
        return ingame_t;
    }

    public float getPrimaryTime() {
        return primary_t;
    }

    public float getRealTime() {
        return realtime_t;
    }
}
