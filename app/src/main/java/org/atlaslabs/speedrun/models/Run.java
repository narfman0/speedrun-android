package org.atlaslabs.speedrun.models;

public class Run {
    String id, user, game, category, platform, time;

    public String getID(){
        return id;
    }

    public String getUser(){
        return user;
    }

    public String getGame() {
        return game;
    }

    public String getCategory() {
        return category;
    }

    public String getPlatform() {
        return platform;
    }

    public String getTime() {
        return time;
    }
}
