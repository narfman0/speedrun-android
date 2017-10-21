package org.atlaslabs.speedrun.models;

public class Run {
    private String id, user, game, category;
    private Times times;
    private System system;

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

    public Times getTimes() {
        return times;
    }

    public System getSystem() {
        return system;
    }
}
