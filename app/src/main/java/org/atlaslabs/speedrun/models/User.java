package org.atlaslabs.speedrun.models;

public class User {
    String id;
    String weblink;
    String twitch;
    String youtube;
    Names names;

    public Names getNames() {
        return names;
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
}
