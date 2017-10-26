package org.atlaslabs.speedrun.models;

import java.util.LinkedList;
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
    private String comment;
    private Times times;
    private System system;
    private RealmList<User> players;
    private Videos videos;

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

    public String getComment() {
        return comment;
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

    /**
     * @return List of ids of players. Each id guaranteed to be non-null.
     */
    public List<String> getPlayersIDs() {
        LinkedList<String> ids = new LinkedList<>();
        for(User user : players)
            if(user.getId() != null)
                ids.add(user.getId());
        return ids;
    }

    public Videos getVideos(){
        return videos;
    }

    public static List<Run> getByDate(Realm realm, int count){
        return realm.where(Run.class).findAllSorted("submitted", Sort.DESCENDING).subList(0, count);
    }
}
