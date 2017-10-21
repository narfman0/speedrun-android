package org.atlaslabs.speedrun.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Run extends RealmObject{
    @PrimaryKey
    private String id;
    private String user, game, category;
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
