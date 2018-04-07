package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Run {
    @PrimaryKey
    private String id, game, category, submitted, comment;
    private long time;
    private System system;
    private List<User> players;
    @Embedded
    private Videos videos;

    public String getID() {
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

    public long getTime() {
        return time;
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
        for (User user : players)
            if (user.getId() != null)
                ids.add(user.getId());
        return ids;
    }

    public Videos getVideos() {
        return videos;
    }
}
