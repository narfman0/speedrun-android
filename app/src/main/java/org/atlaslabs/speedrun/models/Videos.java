package org.atlaslabs.speedrun.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Videos extends RealmObject {
    private String text;
    private RealmList<Link> links;

    public String getText() {
        return text;
    }

    public List<Link> getLinks() {
        return links;
    }
}
