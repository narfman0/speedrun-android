package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Entity;

import java.util.List;

@Entity
public class Videos {
    private String text;
    private List<String> links;

    public String getText() {
        return text;
    }

    public List<String> getLinks() {
        return links;
    }
}
