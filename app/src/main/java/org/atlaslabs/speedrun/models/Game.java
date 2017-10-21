package org.atlaslabs.speedrun.models;

public class Game {
    private String id;
    private String abbreviation;
    private String weblink;
    private Names names;
    private Assets assets;

    public String getId() {
        return id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getWeblink() {
        return weblink;
    }

    public Names getNames() {
        return names;
    }

    public Assets getAssets() {
        return assets;
    }

    class Asset {
        String uri;
    }

    class Assets {
        Asset logo, background, foreground;
    }
}
