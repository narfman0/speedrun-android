package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class System {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String platformID;
    private boolean emulated;
    private String region;

    public int getId() {
        return id;
    }

    public String getPlatformID() {
        return platformID;
    }

    public boolean isEmulated() {
        return emulated;
    }

    public String getRegion() {
        return region;
    }
}
