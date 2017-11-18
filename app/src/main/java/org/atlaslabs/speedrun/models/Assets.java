package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;

public class Assets extends RealmObject {
    private Asset logo, background, foreground;

    public Asset getLogo() {
        return logo;
    }

    public Asset getBackground() {
        return background;
    }

    public Asset getForeground() {
        return foreground;
    }
}
