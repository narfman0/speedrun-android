package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;

public class Link extends RealmObject{
    private String uri, rel;

    public String getUri() {
        return uri;
    }

    public String getRel() {
        return rel;
    }
}
