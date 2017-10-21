package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;

public class System extends RealmObject{
    private String platform;
    private boolean emulated;
    private String region;

    public String getPlatform() {
        return platform;
    }

    public boolean isEmulated() {
        return emulated;
    }

    public String getRegion() {
        return region;
    }
}
