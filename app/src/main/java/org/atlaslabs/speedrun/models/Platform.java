package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Platform {
    @PrimaryKey
    private String id;
    private String name;
    /**
     * Year platform was released
     */
    private int released;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getReleased() {
        return released;
    }
}
