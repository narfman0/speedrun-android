package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;

public class Asset extends RealmObject {
    private String uri;

    public String getUri() {
        return uri;
    }
}
