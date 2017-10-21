package org.atlaslabs.speedrun.models;

public class User {
    private String id;
    private String weblink;
    private String twitch;
    private String youtube;
    private Names names;

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
