package org.atlaslabs.speedrun.models;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class Run extends RealmObject{
    @PrimaryKey
    private String id;
    private String game;
    private String category;
    private String submitted;
    private Times times;
    private System system;
    private RealmList<User> players;

    public String getID(){
        return id;
    }

    public String getGame() {
        return game;
    }

    public String getCategory() {
        return category;
    }

    public String getSubmitted() {
        return submitted;
    }

    public Times getTimes() {
        return times;
    }

    public System getSystem() {
        return system;
    }

    public List<User> getPlayers() {
        return players;
    }

    public static List<Run> getByDate(Realm realm, int count){
        return realm.where(Run.class).findAllSorted("submitted", Sort.DESCENDING).subList(0, count);
    }
}
