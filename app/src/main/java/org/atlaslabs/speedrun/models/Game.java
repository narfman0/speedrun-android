package org.atlaslabs.speedrun.models;

public class Game {
    private String id, abbreviation, weblink;
    private Names names;
    private Assets assets;

    class Asset {
        String uri;
    }

    class Assets {
        Asset logo, background, foreground;
    }
}
