package org.atlaslabs.speedrun.models;

public class Game {
    String id, abbreviation, weblink;
    Names names;
    Assets assets;

    class Asset {
        String uri;
    }

    class Assets {
        Asset logo, background, foreground;
    }
}
